package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import chemistry.Connection;
import chemistry.SimpleGRN;

public class Genotype implements Cloneable {
	//Species that are used by the algorithm and should be there. FaceIn not included, since it's conditional, but don't forget it
	public static String[] necessarySpecies = new String[]{"signalIn", "health", "messageIn", "cooperate", "signalOut", "speed"};
	
	public int speciesCount;
	
	public String[] speciesNames; //length should be species count
	public double[] speciesConc; //length should be species count
	
	public ArrayList<ArrayList<Connection>> connections; //connections, indexed by speciesNames index
	public ArrayList<ArrayList<Connection>> inhibitions; //inhibitions, indexed by speciesNames index
	
	public static SimpleGRN fromGenotype(Genotype gen){
		LinkedHashMap<String,Double> speciesConcentrations = new LinkedHashMap<String,Double>();
		//Necessary species
		for(int i=0; i<necessarySpecies.length;i++){
		speciesConcentrations.put(necessarySpecies[i], 0.0);
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
		Genotype genprime = gen.clone();
		//With some probability...
		mutateParam(genprime);
		addConnection(genprime);
		addInhibition(genprime);
		cleanUp(genprime);
		
		return genprime;
	}

	//mutate the initial concentrations and connection/inhibition strengths
	public static void mutateParam(Genotype gen){
		
	}
	
	public static void addConnection(Genotype gen){
		
	}
	
	public static void addInhibition(Genotype gen){
		
	}
	
	//If a new connection justifies a new sequence, add one
	public static void addSpecies(Genotype gen){
		
	}
	
	//If a connection/inhibition strength goes to zero, we remove it.
	//If a species is not used anymore, we remove it.
	public static void cleanUp(Genotype gen){
		
	}
	
	//TODO
	
	public Genotype clone(){
		try {
			return (Genotype) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
