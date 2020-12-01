package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.util.ArrayList;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

public class NoFlyZones {
	public ArrayList<Feature> noZones;

	public NoFlyZones() throws IOException, InterruptedException {
		String buildingsJson = ServerConnect.connect("http://localhost/buildings/no-fly-zones.geojson");
		this.noZones = (ArrayList<Feature>) FeatureCollection.fromJson(buildingsJson).features();
	}
	
	public ArrayList<Feature> getNoZones(){
		return noZones;
	}
	
}
