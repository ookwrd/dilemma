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
