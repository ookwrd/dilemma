package world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import misc.Constants;

import chemistry.Connection;
import chemistry.SimpleGRN;

public class Genotype implements Cloneable {
	//Species that are used by the algorithm and should be there. FaceIn not included, since it's conditional, but don't forget it
	public static ArrayList<String> necessarySpecies = new ArrayList<String>(Arrays.asList(new String[]{"signalIn", "health", "messageIn", "cooperate", "signalOut", "speed"}));
	
	public static final Random rand = new Random(); // the rand object
	
	public int speciesCount;
	
	public String[] speciesNames; //length should be species count
	public double[] speciesConc; //length should be species count
	
	public ArrayList<ArrayList<Connection>> connections; //connections, indexed by speciesNames index
	public ArrayList<ArrayList<Connection>> inhibitions; //inhibitions, indexed by speciesNames index
	
	public Genotype(){
		speciesCount = necessarySpecies.size();
		speciesNames = new String[speciesCount];
		speciesNames = necessarySpecies.toArray(speciesNames);
		speciesConc = new double[speciesCount];
		Arrays.fill(speciesConc, Constants.baseConc);
		connections = new ArrayList<ArrayList<Connection>>();
		inhibitions = new ArrayList<ArrayList<Connection>>();
		for(int i=0; i<speciesCount; i++){
			connections.add(new ArrayList<Connection>());
			inhibitions.add(new ArrayList<Connection>());
		}
		
		mutate(this); //Generate a random genotype
	}
	
	public static SimpleGRN fromGenotype(Genotype gen){
		LinkedHashMap<String,Double> speciesConcentrations = new LinkedHashMap<String,Double>();
		//Necessary species
		for(int i=0; i<necessarySpecies.size();i++){
		speciesConcentrations.put(necessarySpecies.get(i), 0.0);
		}
		if(misc.Constants.faces)
			speciesConcentrations.put("faceIn", 0.0);
		
		for(int i = 0; i<gen.speciesCount;i++){
			speciesConcentrations.put(gen.speciesNames[i], gen.speciesConc[i]);
		}
		
		
		
		HashMap<String,ArrayList<Connection>> connections = new HashMap<String,ArrayList<Connection>>();
		for(int i =0; i<gen.speciesCount; i++){
			connections.put(gen.speciesNames[i], gen.connections.get(i));
		}
		
		HashMap<String, ArrayList<Connection>> inhibitions = new HashMap<String, ArrayList<Connection>>();
		for(int i =0; i<gen.speciesCount; i++){
			inhibitions.put(gen.speciesNames[i], gen.inhibitions.get(i));
		}
		return new SimpleGRN(speciesConcentrations,connections,inhibitions);
	}
	
	public static Genotype mutate(Genotype gen){
		//With some probability...
		mutateParam(gen);
		addConnection(gen);
		addInhibition(gen);
		cleanUp(gen);
		
		return gen;
	}
	
	private static double gaussianParam(double oldParam, double factor, double min, double max){
		double mutatedParam = oldParam
		   * (factor * rand.nextGaussian() + 1);
		if (mutatedParam < min)
		   mutatedParam = min;
		if (mutatedParam > max)
		   mutatedParam = max;
		
		return mutatedParam;
	}

	//mutate the initial concentrations and connection/inhibition strengths
	private static void mutateParam(Genotype gen){
		for(int i=0; i<gen.speciesCount;i++){
			if(rand.nextDouble()>= misc.Constants.mutationInitProba){
				gen.speciesConc[i] = gaussianParam(gen.speciesConc[i],Constants.paramMutateGaussianFactor,
						Constants.minInitConcentration,Constants.maxInitConcentration);
			}
		}
		
		for(int i=0; i< gen.connections.size(); i++){
			for(Connection c : gen.connections.get(i)){
				if(rand.nextDouble()>= misc.Constants.mutationConnecProba){
					c.setStrength(gaussianParam(c.getStrength(),Constants.paramMutateGaussianFactor,
							Constants.minConnecConcentration,Constants.maxConnecConcentration));
				}
			}
		}
		
		for(int i=0; i< gen.inhibitions.size(); i++){
			for(Connection c : gen.inhibitions.get(i)){
				if(rand.nextDouble()>= misc.Constants.mutationInhibProba){
					c.setStrength(gaussianParam(c.getStrength(),Constants.paramMutateGaussianFactor,
							Constants.minInhibConcentration,Constants.maxInhibConcentration));
				}
			}
		}
		
	}
	
	//Get the list of all connections that could be created among existing sequences
	protected ArrayList<Connection> getPossibleConnections(){
		ArrayList<Connection> connects = new ArrayList<Connection>();
		for(int i = 0; i< speciesCount;i++){
			for(int j = 0; j <speciesCount;j++){
				Connection c = new Connection(speciesNames[i],speciesNames[j],0.0);
				if(!connections.contains(c)){
					connects.add(c);
				}
			}
		}
		return connects;
	}
	
	private static void addConnection(Genotype gen){
		//Either add activation between two species (possibly creating one)
		//or cut an existing connection in two
		int count = 0;
		while(rand.nextDouble()<Constants.addConnectionProba && count < Constants.maxAddedConnectionAtOnce){
			
			if(rand.nextDouble() < Constants.newSpeciesProba){
				//We add a new species in the process
				if(rand.nextDouble() < Constants.connectionCutProba){
					//We take a connection and cut it in two
					
					//First, flatten the list of existing connections
					ArrayList<Connection> allConnections = new ArrayList<Connection>();
					for(int i = 0; i< gen.connections.size(); i++){
						allConnections.addAll(gen.connections.get(i)); //connections.get(i) might be empty
					}
					//Then, pick one at random
					Connection c = allConnections.get(rand.nextInt(allConnections.size()));
					String newS = addSpecies(gen);
					//Take part of the connection strength to give it to the new connection
					//so that the overall strength is about the same
					double separateStrength = c.getStrength()/2.0*(1+rand.nextGaussian());
					
					Connection cPrime = new Connection(newS,c.getTo(),c.getStrength()-separateStrength);
					c.setStrength(separateStrength);
					c.setTo(newS);
					gen.connections.get(gen.speciesCount-1).add(cPrime);
					
				} else {
					//We add a connection to or from a new species
					String newS = addSpecies(gen);
					double value = (Constants.minConnecConcentration+Constants.maxConnecConcentration)/2.0+(Constants.maxConnecConcentration-Constants.minConnecConcentration)/2.0*rand.nextGaussian();
					if(rand.nextBoolean()){ //50/50
						//FROM the new species
						int indexDest = rand.nextInt(gen.speciesCount-1);
						Connection c = new Connection(newS,gen.speciesNames[indexDest],value);
						gen.connections.get(gen.speciesCount-1).add(c);
					} else {
						//TO the new species
						int indexOrigin = rand.nextInt(gen.speciesCount-1);
						Connection c = new Connection(gen.speciesNames[indexOrigin],newS,value);
						gen.connections.get(indexOrigin).add(c);
					}
				}
			} else{
			ArrayList<Connection> possibleConnections = gen.getPossibleConnections();
			Connection c = possibleConnections.get(rand.nextInt(possibleConnections.size()));
			c.setStrength((Constants.minConnecConcentration+Constants.maxConnecConcentration)/2.0+(Constants.maxConnecConcentration-Constants.minConnecConcentration)/2.0*rand.nextGaussian());
			
			gen.connections.get(gen.indexSpecies(c.getFrom())).add(c);
			}
			count++;
		}
		
	}
	
	//Get the list of all connections that could be created among existing sequences
	protected ArrayList<Connection> getNotInhibitedConnections(){
		//First, flatten the list of existing connections
		ArrayList<Connection> connects = new ArrayList<Connection>();
		for(int i = 0; i< connections.size(); i++){
			connects.addAll(connections.get(i)); //connections.get(i) might be empty
		}
		
		//Then, check against all the inhibitions
		for(int i = 0; i< inhibitions.size(); i++){
			for(Connection c : inhibitions.get(i)){
			if(connects.contains(c)){ //already inhibited
				connects.remove(c);
			}
			}
		}
		return connects;
	}
	
	private static void addInhibition(Genotype gen){
		int count = 0;
		while(rand.nextDouble()<Constants.addInhibitionProba && count < Constants.maxAddedInhibitionAtOnce){
			ArrayList<Connection> inhibitable = gen.getNotInhibitedConnections();
			if(inhibitable.isEmpty()){
				return;
			}
			double value = (Constants.minInhibConcentration+Constants.maxInhibConcentration)/2.0+(Constants.maxInhibConcentration-Constants.minInhibConcentration)/2.0*rand.nextGaussian();
			Connection c = inhibitable.get(rand.nextInt(inhibitable.size()));
			Connection cPrime = new Connection(c.getFrom(),c.getTo(),value); //gives data plus inhibition strength
			int inhibIndex = rand.nextInt(gen.speciesCount);
			gen.inhibitions.get(inhibIndex).add(cPrime);
			count++;
		}
	}
	
	//If a new connection justifies a new sequence, add one
	private static String addSpecies(Genotype gen){
		String s = gen.speciesCount>necessarySpecies.size()?"S"+(Integer.parseInt(gen.speciesNames[gen.speciesCount-1].substring(1))+1):"S1";
		gen.speciesCount++;
		String[] tempNames = new String[gen.speciesCount];
		double[] tempConc = new double[gen.speciesCount];
 		for(int i=0; i<gen.speciesCount-1;i++){
			tempNames[i] = gen.speciesNames[i];
			tempConc[i] = gen.speciesConc[i];
		}
 		tempNames[gen.speciesCount-1] = s;
 		tempConc[gen.speciesCount-1] = Constants.baseConc;
 		gen.speciesNames = tempNames;
 		gen.speciesConc = tempConc;
 		
 		ArrayList<Connection> connectionsFromNewS = new ArrayList<Connection>();
 		gen.connections.add(connectionsFromNewS); //At the right index!
 		
 		ArrayList<Connection> inhibitionsFromNewS = new ArrayList<Connection>();
 		gen.inhibitions.add(inhibitionsFromNewS); //At the right index!
		return s;
	}
	
	private void removeConnection(Connection c){
		int index = this.indexSpecies(c.getFrom());
		connections.get(index).remove(c);
		
		//remove inhibitions
		for(int i=0; i<inhibitions.size(); i++){
			if(inhibitions.get(i).contains(c)){
				inhibitions.get(i).remove(c);
			}
		}
	}
	
	private void removeSpecies(String s){
		if(necessarySpecies.contains(s)){
			return; //we don't remove it
		}
		int index = indexSpecies(s);
		String[] tempNames = new String[speciesCount-1];
		double[] tempConc = new double[speciesCount-1];
		for(int i = 0; i<index;i++){
			tempNames[i] = speciesNames[i];
			tempConc[i] = speciesConc[i];
		}
		for(int i = index+1; i<speciesCount; i++){
			tempNames[i-1] = speciesNames[i];
			tempConc[i-1] = speciesConc[i];
		}
		connections.remove(index);
		inhibitions.remove(index);
		speciesCount--;
		
	}
	
	//If a connection/inhibition strength goes to zero, we remove it.
	//If a species is not used anymore, we remove it.
	private static void cleanUp(Genotype gen){
		
		//remove unused connections
		for(int i = 0; i<gen.connections.size(); i++){
			for(int j = 0; j<gen.connections.get(i).size(); j++){
				Connection c = gen.connections.get(i).get(j);
				if(c.getStrength() <= 0.0){
					gen.removeConnection(c);
				}
			}
		}
		
		//remove unused inhibitions
		for(int i = 0; i<gen.inhibitions.size(); i++){
			for(int j = 0; j<gen.inhibitions.get(i).size(); j++){
				Connection c = gen.inhibitions.get(i).get(j);
				if(c.getStrength() <= 0.0){
					gen.inhibitions.get(i).remove(c);
				}
			}
		}
		
		//remove unused species (no connection nor inhibition)
		for(int i=0; i<gen.speciesCount; i++){
			if(gen.connections.get(i).isEmpty() && gen.inhibitions.get(i).isEmpty()){
				//also, no one creates it
				boolean generated = false;
				for(int j=0; j<gen.speciesCount;j++){
					Connection test = new Connection(gen.speciesNames[j],gen.speciesNames[i],0.0);
					if(gen.connections.get(j).contains(test)){
						generated = true;
						break;
					}
				}
				if(!generated)
					gen.removeSpecies(gen.speciesNames[i]);
			}
		}
		
	}
	
	//Not super efficient. The data structure could be made better...
	protected int indexSpecies(String s){
		for(int i = 0; i< speciesCount; i++){
			if(speciesNames[i].equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	//TODO
	
	public Genotype clone(){
		try {
			Genotype clone = (Genotype) super.clone();
			
			
			return clone;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString(){
		String result = "Genotype Number of species:"+this.speciesCount;
		return result;
	}
	
	public static void main(String[] args){
		Genotype gen = new Genotype();
		for(int i=0;i<100;i++){
			gen = Genotype.mutate(gen);
			System.out.println(gen);
		}
	}
}
