package chemistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class SimpleGRN implements FirstOrderDifferentialEquations{

	LinkedHashMap<String,Double> speciesConcentrations;
	HashMap<String,ArrayList<Connection>> connections; // all connections starting from the species
	HashMap<String,ArrayList<Connection>> reverseConnections; // all connections going TO the species
	HashMap<String, ArrayList<Connection>> inhibitions; // we give the origin and dest of the inhibited connection, 
	//the strength is the inhibition strength
	HashMap<Connection, ArrayList<String>> reverseInhibition; 
	
	double decay;
	
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
	
	
	
}
