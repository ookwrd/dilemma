package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import chemistry.Connection;
import chemistry.SimpleGRN;

public class Genotype {
	//Species that are used by the algorithm and should be there. FaceIn not included, since it's conditional, but don't forget it
	public static String[] necessarySpecies = new String[]{"signalIn", "health", "messageIn", "cooperate", "signalOut", "speed"};
	
	public int speciesCount;
	
	public String[] speciesNames; //length should be species count
	public double[] speciesConc; //length should be species count
	
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
		
		
		//TODO
		HashMap<String,ArrayList<Connection>> connections = new HashMap<String,ArrayList<Connection>>();
		HashMap<String, ArrayList<Connection>> inhibitions = new HashMap<String, ArrayList<Connection>>();
	
		return new SimpleGRN(speciesConcentrations,connections,inhibitions);
	}

}
