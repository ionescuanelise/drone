package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

public class GeoJsonMap {
	private ArrayList<Feature> listMarkers;

	public GeoJsonMap(ArrayList<Sensor> sensors, ArrayList<Integer> notVisited) throws IOException, InterruptedException {
		this.listMarkers = new ArrayList<Feature>();
		
		for (Sensor sensor : sensors) {
			var wordsUrl = new WordsJson(sensor.location).getWordsjson();
			String wordsjson = ServerConnect.connect(wordsUrl);
			var listType = new TypeToken<What3Words>() {}.getType();
			What3Words sensorLocation = new Gson().fromJson(wordsjson, listType);
			Feature marker = createMarker(sensorLocation, sensor.battery, sensor.reading, sensor.location);			
			this.listMarkers.add(marker);
		}
	}
	
	public Feature createMarker (What3Words sensorLocation, String battery, String reading, String location) {
		Feature marker = Feature.fromGeometry((Geometry)getPointFromLocation(sensorLocation));
		
		var markerInfoRender = new MarkerInfo(battery, reading);
		var stringProperty = markerInfoRender.getMarkerProperties();
		marker.addStringProperty("location", location);
		marker.addStringProperty("rgb-string", stringProperty.get(0));
		marker.addStringProperty("marker-color", stringProperty.get(0));
		marker.addStringProperty("marker-symbol", stringProperty.get(2));
		
		return marker;
	}
		
	public Point getPointFromLocation(What3Words sensorLocation){
		Double lng = sensorLocation.getLng();
		Double lat = sensorLocation.getLat();
		var point = Point.fromLngLat(lng, lat);
		return point;
	}
	
	public ArrayList<Feature> getListMarkers() {
		return listMarkers;
	}
	
}



