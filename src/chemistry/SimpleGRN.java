package chemistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator;

public class SimpleGRN implements FirstOrderDifferentialEquations{

	LinkedHashMap<String,Double> speciesConcentrations; //Cannot be empty
	HashMap<String,ArrayList<Connection>> connections; // all connections starting from the species
	HashMap<String,ArrayList<Connection>> reverseConnections; // all connections going TO the species
	HashMap<String, ArrayList<Connection>> inhibitions; // we give the origin and dest of the inhibited connection, 
	//the strength is the inhibition strength
	HashMap<Connection, ArrayList<String>> reverseInhibition; 
	
	double decay; //Can be negative (everyone is created all the time)
	
	public SimpleGRN(LinkedHashMap<String, Double> initConcentrations, HashMap<String,ArrayList<Connection>> connections,HashMap<String, ArrayList<Connection>> inhibitions){
		 init(initConcentrations, connections, inhibitions, misc.Constants.defaultDecay);
	}
	
	public SimpleGRN(LinkedHashMap<String, Double> initConcentrations, HashMap<String,ArrayList<Connection>> connections,HashMap<String, ArrayList<Connection>> inhibitions, double decay){
		 init(initConcentrations, connections, inhibitions, decay);
	}
	
	private void init(LinkedHashMap<String, Double> initConcentrations, HashMap<String,ArrayList<Connection>> connections,HashMap<String, ArrayList<Connection>> inhibitions, double decay){
		this.speciesConcentrations = initConcentrations;
		this.connections = (connections!=null?connections:new HashMap<String,ArrayList<Connection>>());
		this.reverseConnections = new HashMap<String,ArrayList<Connection>>();
		for(String origin : connections.keySet()){
			ArrayList<Connection> connects = connections.get(origin);
			for(Connection conn : connects){
				//We add all the connections to reverseConnections, indexed by their destinations instead
				ArrayList<Connection> rev = reverseConnections.get(conn.getTo());
				if(rev == null){
					rev = new ArrayList<Connection>();
					reverseConnections.put(conn.getTo(), rev);
				}
				rev.add(conn);
			}
		}
		
		this.inhibitions = (inhibitions!=null?inhibitions:new HashMap<String,ArrayList<Connection>>());
		this.reverseInhibition = new HashMap<Connection,ArrayList<String>>();
		for(String inhib : inhibitions.keySet()){
			ArrayList<Connection> inhibited = connections.get(inhib);
			for(Connection conn : inhibited){
				//We add all the inhibitors to reverseInhibition, indexed by their targets instead
				ArrayList<String> rev = reverseInhibition.get(conn);
				if(rev == null){
					rev = new ArrayList<String>();
					reverseInhibition.put(conn, rev);
				}
				rev.add(inhib);
			}
		}
		
		this.decay = decay;
	}
	
	private double partialDerivative(String origin, String dest){
		double deriv = 0.0;
		
		ArrayList<Connection> connects = connections.get(origin);
		if(connects == null)
			return deriv;
		Connection rel = new Connection(origin, dest, 0.0); // we use the surcharged equals function to find the correct one
		double activ = connects.get(connects.indexOf(rel)).getStrength();
		double inh = 0;
		
		if(reverseInhibition.get(rel) != null){
			ArrayList<String> inhibs = reverseInhibition.get(rel);
			if(inhibs != null){
				for(String inhname : inhibs){
					inh += speciesConcentrations.get(inhname); // we add all the inhibitors' concentrations
				}
			}
		}
		
		deriv += activ * speciesConcentrations.get(origin);
		deriv /= 1 + speciesConcentrations.get(origin) + inh; //deriv = activ*origin/(1+origin+inhib)
		
		return deriv;
	}
	
	private double totalDerivative(String species){
		double total = 0.0;
		
		for(Connection generator : reverseConnections.get(species))
			total += partialDerivative(generator.getFrom(), species);
		
		total -= decay * speciesConcentrations.get(species);
		
		return total;
	}

	@Override
	public void computeDerivatives(double time, double[] values, double[] derivatives)
			throws MaxCountExceededException, DimensionMismatchException {
		
		String[] names = speciesConcentrations.keySet().toArray(null);
		
		//Update values
		for(int i=0; i<values.length; i++){
			speciesConcentrations.put(names[i], values[i]);
		}
		
		for(int i=0; i<values.length; i++){
			derivatives[i] = totalDerivative(names[i]);
		}
		
	}

	@Override
	public int getDimension() {
		// TODO Auto-generated method stub
		return speciesConcentrations.size();
	}
	
	private double[] generateInitialConditions(){
		String[] names = speciesConcentrations.keySet().toArray(null);
		double[] initialConditions = new double[names.length];
		for(int i=0; i<names.length; i++){
			initialConditions[i] = speciesConcentrations.get(names[i]);
		}
		return initialConditions;
	}
	
public static double[][] solveEqus(LinkedHashMap<String, Double> initConcentrations, HashMap<String,ArrayList<Connection>> connections,HashMap<String, ArrayList<Connection>> inhibitions){
		
		SimpleGRN odes = new SimpleGRN(initConcentrations, connections, inhibitions);
		GraggBulirschStoerIntegrator myIntegrator = new GraggBulirschStoerIntegrator(
				1e-13, 1, 1e-6, 1e-6);
		misc.MyStepHandler handler = new misc.MyStepHandler(odes.getDimension());
		myIntegrator.addStepHandler(handler);
		myIntegrator.addEventHandler(new misc.CalcErrEventHandler(), 2000, 0.000000001, 100000);
		myIntegrator.addEventHandler(new misc.MyODEEventHandler(), 10, 1, 100);
		double[] initialConditions = odes.generateInitialConditions();
		myIntegrator.integrate(odes, 0, initialConditions, misc.Constants.internalSteps ,
				initialConditions);
		return handler.getTimeSerie();
	}
	
	
	
}
