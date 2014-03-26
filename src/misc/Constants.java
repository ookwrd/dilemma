package misc;

import java.util.Properties;

public class Constants {
	
	public static int nbNodes = 10; // max number of nodes in one GRN
	
	public static int internalSteps = 100; // number of time steps done by the GRN during one time step of the "server".
	//Represents the discrepancy between reaction and diffusion.
	
	public static double defaultDecay = 1.0; // decay spead of species (one a.u. per time step)
	
	public static String propertiesFileName = "default.prop";
	
	public static boolean faces = false; // do we take faces into account or not?
	
	public static double threshold = 10.0; // the threshold concentration value to cooperate
	
	//Ranges
	
	public static double minInitConcentration = 0.0; //min initial concentration for species	
	public static double maxInitConcentration = 100.0; // max initial concentration for species
	public static double baseConc = 1.0;//Concentration when creating a new species
	
	public static double minConnecConcentration = 0.0; //min strength for connections
	public static double maxConnecConcentration = 100.0; // max strength for connections
	
	public static double minInhibConcentration = 0.0; //min strength for inhibitions
	public static double maxInhibConcentration = 100.0; // max strength for inhibitions
	
	public static double paramMutateGaussianFactor = 1.0; // size of the gaussian for gaussian mutation
	
	
	
	//TODO: to add to the config file
	//Mutation parameters
	
	public static double mutationInitProba = 0.1; // the probability to mutate a species init conc
	public static double mutationConnecProba = 0.1;
	public static double mutationInhibProba = 0.1;
	
	public static double addConnectionProba = 0.1;
	public static int maxAddedConnectionAtOnce = 3;
	public static double newSpeciesProba = 0.25; // probability to add a new species along with a connection
	public static double connectionCutProba = 0.7; // probability, when adding a new connection,
	//                                                   to do so by cutting an existing connection in two.
	
	public static double addInhibitionProba = 0.1;
	public static int maxAddedInhibitionAtOnce = 2;
	
	
//	public static int numberGenes = nbNodes + nbNodes*nbNodes + nbNodes*nbNodes*nbNodes;
	
	public Properties readProperties(String fileName) {
        propertiesFileName = fileName;
//        if (fileName.equals(""))
//            return properties;
        Properties properties = new Properties();
        
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(fileName);
            properties.load(fis);
            fis.close();
        } 
        catch(java.io.IOException e) { 
            System.err.println("File '" + fileName + "' not found, no options read");
            // e.printStackTrace();
        }
        setFromProperties(properties);
        return properties;
    }
	
	static void setFromProperties(Properties properties){
		if(properties.getProperty("nbNodes") != null){
			nbNodes = Integer.parseInt(properties.getProperty("nbNodes"));
		}
		
		if(properties.getProperty("internalSteps") != null){
			internalSteps = Integer.parseInt(properties.getProperty("internalSteps"));
		}
		
		if(properties.getProperty("defaultDecay") != null){
			defaultDecay = Double.parseDouble(properties.getProperty("defaultDecay"));
		}
		if(properties.getProperty("faces") != null){
			faces = Boolean.parseBoolean(properties.getProperty("faces"));
		}
		if(properties.getProperty("minInitConcentration") != null){
			minInitConcentration = Double.parseDouble(properties.getProperty("minInitConcentration"));
		}
		if(properties.getProperty("maxInitConcentration") != null){
			maxInitConcentration = Double.parseDouble(properties.getProperty("maxInitConcentration"));
		}
		if(properties.getProperty("minConnecConcentration") != null){
			minConnecConcentration = Double.parseDouble(properties.getProperty("minConnecConcentration"));
		}
		if(properties.getProperty("maxConnecConcentration") != null){
			maxConnecConcentration = Double.parseDouble(properties.getProperty("maxConnecConcentration"));
		}
		if(properties.getProperty("minInhibConcentration") != null){
			minInhibConcentration = Double.parseDouble(properties.getProperty("minInhibConcentration"));
		}
		if(properties.getProperty("maxInhibConcentration") != null){
			maxInhibConcentration = Double.parseDouble(properties.getProperty("maxInhibConcentration"));
		}
		if(properties.getProperty("paramMutateGaussianFactor") != null){
			paramMutateGaussianFactor = Double.parseDouble(properties.getProperty("paramMutateGaussianFactor"));
		}
		if(properties.getProperty("baseConc") != null){
			baseConc = Double.parseDouble(properties.getProperty("baseConc"));
		}
		if(properties.getProperty("mutationInitProba") != null){
			mutationInitProba = Double.parseDouble(properties.getProperty("mutationInitProba"));
			if(mutationInitProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				mutationInitProba = 1.0;
			} else if (mutationInitProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				mutationInitProba = 0.0;
			}
		}
		if(properties.getProperty("mutationConnecProba") != null){
			mutationConnecProba = Double.parseDouble(properties.getProperty("mutationConnecProba"));
			if(mutationConnecProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				mutationConnecProba = 1.0;
			} else if (mutationConnecProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				mutationConnecProba = 0.0;
			}
		}
		if(properties.getProperty("mutationInhibProba") != null){
			mutationInhibProba = Double.parseDouble(properties.getProperty("mutationInhibProba"));
			if(mutationInhibProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				mutationInhibProba = 1.0;
			} else if (mutationInhibProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				mutationInhibProba = 0.0;
			}
		}
		if(properties.getProperty("addConnectionProba") != null){
			addConnectionProba = Double.parseDouble(properties.getProperty("addConnectionProba"));
			if(addConnectionProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				addConnectionProba = 1.0;
			} else if (addConnectionProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				addConnectionProba = 0.0;
			}
		}
		if(properties.getProperty("maxAddedConnectionAtOnce") != null){
			maxAddedConnectionAtOnce = Integer.parseInt(properties.getProperty("maxAddedConnectionAtOnce"));
		}
		if(properties.getProperty("newSpeciesProba") != null){
			newSpeciesProba = Double.parseDouble(properties.getProperty("newSpeciesProba"));
			if(newSpeciesProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				newSpeciesProba = 1.0;
			} else if (newSpeciesProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				newSpeciesProba = 0.0;
			}
		}
		if(properties.getProperty("connectionCutProba") != null){
			connectionCutProba = Double.parseDouble(properties.getProperty("connectionCutProba"));
			if(connectionCutProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				connectionCutProba = 1.0;
			} else if (connectionCutProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				connectionCutProba = 0.0;
			}
		}
		if(properties.getProperty("addInhibitionProba") != null){
			addInhibitionProba = Double.parseDouble(properties.getProperty("addInhibitionProba"));
			if(addInhibitionProba>1.0 ){
				System.err.println("[WARNING] probability is out of range, set to 1.0");
				addInhibitionProba = 1.0;
			} else if (addInhibitionProba<0.0 ){
				System.err.println("[WARNING] probability is out of range, set to 0.0");
				addInhibitionProba = 0.0;
			}
		}
		if(properties.getProperty("maxAddedInhibitionAtOnce") != null){
			maxAddedInhibitionAtOnce = Integer.parseInt(properties.getProperty("maxAddedInhibitionAtOnce"));
		}
	}

}
