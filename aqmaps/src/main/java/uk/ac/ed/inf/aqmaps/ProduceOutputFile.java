package uk.ac.ed.inf.aqmaps;

import java.io.*;

public class ProduceOutputFile {
	public static void createFile(String outputGeojson) {
        try {
        	var writer = new FileWriter("map.geojson");
            writer.write(outputGeojson);
            writer.close();

        } catch (IOException e) {
        	System.out.println("An error occured while outputting the geojson file");
            e.printStackTrace();
        }
	}
	
}
