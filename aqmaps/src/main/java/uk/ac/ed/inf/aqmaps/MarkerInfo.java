package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

public class MarkerInfo {
    private ArrayList<String> markerProperties;

	public MarkerInfo(String b, String r) {
		this.markerProperties = new ArrayList<String>(3);
		
		if(b == null || (b != null && Double.parseDouble(b) < 10.0)) {
			markerProperties = new ArrayList<>(List.of("#000000","Black","cross"));
		}
        else {
        	var reading = Double.parseDouble(r);
        	
        	if(reading < 32)
	        	markerProperties = new ArrayList<>(List.of("#00ff00", "Green", "lighthouse"));
	        else if(reading < 64)
	        	markerProperties = new ArrayList<>(List.of("#40ff00", "Medium Green", "lighthouse"));
	        else if(reading < 96)
	        	markerProperties = new ArrayList<>(List.of("#80ff00", "LightGreen", "lighthouse"));
	        else if(reading < 128)
	        	markerProperties = new ArrayList<>(List.of("#c0ff00", "LimeGreen", "lighthouse"));
	        else if(reading < 160)
	        	markerProperties = new ArrayList<>(List.of("#ffc000", "Gold", "danger"));
	        else if(reading < 192)
	        	markerProperties = new ArrayList<>(List.of("#ff8000", "Orange", "danger"));
	        else if(reading < 224)
	        	markerProperties = new ArrayList<>(List.of("#ff4000", "Red/Orange", "danger"));
	        else if(reading < 256)
	        	markerProperties = new ArrayList<>(List.of("#ff0000", "Red", "danger"));
        }
        	
    }
    
    public ArrayList<String> getMarkerProperties() {
		return markerProperties;
	}

}
