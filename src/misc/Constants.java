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
	
	//TODO: to add to the config file
	
	//Ranges
	
	public static double minInitConcentration = 0.0; //min initial concentration for species	
	public static double maxInitConcentration = 100.0; // max initial concentration for species
	
	public static double minConnecConcentration = 0.0; //min strength for connections
	public static double maxConnecConcentration = 100.0; // max strength for connections
	
	public static double minInhibConcentration = 0.0; //min strength for inhibitions
	public static double maxInhibConcentration = 100.0; // max strength for inhibitions
	
	public static double paramMutateGaussianFactor = 1.0; // size of the gaussian for gaussian mutation
	
	public static double baseConc = 0.0;//Concentration when creating a new species
	//Mutation parameters
	
	public static double mutationInitProba = 0.1; // the probability to mutate a species init conc
	public static double mutationConnecProba = 0.1;
	public static double mutationInhibProba = 0.1;
	
	public static double addConnectionProba = 0.1;
	public static double maxAddedConnectionAtOnce = 3;
	public static double newSpeciesProba = 0.05; // probability to add a new species along with a connection
	public static double connectionCutProba = 0.7; // probability, when adding a new connection,
	//                                                   to do so by cutting an existing connection in two.
	
	public static double addInhibitionProba = 0.1;
	public static double maxAddedInhibitionAtOnce = 2;
	
	
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
		
	}

}
