package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfJoins;

public class ComputeRoute { 
	
	public static Polygon getMapGrid(){
		
		List<List<Point>> gridLimits = new ArrayList<>();
		List<Point> temp = new ArrayList<>();
		
		temp.add(Point.fromLngLat(-3.192473, 55.946233));
		temp.add(Point.fromLngLat(-3.184319, 55.946233));
		temp.add(Point.fromLngLat(-3.184319, 55.942617));
		temp.add(Point.fromLngLat(-3.192473, 55.942617));
		
		gridLimits.add(temp);
		return Polygon.fromLngLats(gridLimits);
		
	}
	
	public static Point getCoordinates(String location) throws IOException, InterruptedException {
		var wordsUrl = new WordsJson(location).getWordsjson();
		String wordsjson = ServerConnect.connect(wordsUrl);
		var listType = new TypeToken<What3Words>() {}.getType();
		What3Words sensorLocation = new Gson().fromJson(wordsjson, listType);
		Double lng = sensorLocation.getLng();
		Double lat = sensorLocation.getLat();
		var point = Point.fromLngLat(lng, lat);
		
		return point;
	}
	
	public static ArrayList<Point> getLocations(ArrayList<Sensor> sensors) throws IOException, InterruptedException {
		var sensorLocations = new ArrayList<Point>();
		for(int i = 0; i < sensors.size(); i++) {
			sensorLocations.add(getCoordinates(sensors.get(i).location));
		}
		return sensorLocations;
	}
	
	public static double distance(Point start, Point end) {
		Double distance = Math.sqrt(Math.pow((start.longitude() - end.longitude()),2) + Math.pow((start.latitude() - end.latitude()),2));
		return distance;
	}
	
	public static int getAngle(Point start, Point end) {
		double a = Math.toDegrees(Math.atan2(end.latitude() - start.latitude(), end.longitude() - start.longitude()));
		a = a + Math.ceil(-a / 360) * 360;
		int angle = (int)a;
		
		if(angle % 10 > 5)
			angle += (10 - angle % 10);
		else
			angle -= angle % 10;
	
	    return angle;
	}
	
	public static Point getPosition(Point currentLocation, int angle) {
		var lng = currentLocation.longitude() + 0.0003 * Math.cos(Math.toRadians(angle));
		var lat = currentLocation.latitude() + 0.0003 *  Math.sin(Math.toRadians(angle));
		var point = Point.fromLngLat(lng, lat);
		
		return point;
	}
	
	public static boolean intersect(Point start, Point end, ArrayList<Feature> noZones) {
		Line2D myLine = new Line2D.Double(start.longitude(), start.latitude(), end.longitude(), end.latitude());
		
		for(int p = 0; p < noZones.size(); p++) {
			var polyPoints = ((Polygon) noZones.get(p).geometry()).coordinates().get(0);
			for (int i = 0; i < polyPoints.size() - 1; i++) 
				if (myLine.intersectsLine(polyPoints.get(i).longitude(), polyPoints.get(i).latitude(), polyPoints.get(i+1).longitude(), polyPoints.get(i+1).latitude())) 
					return true;
		}
		
		return false;
	}
	
	
	public static ArrayList<Point> getValidDirections(Point currentLocation, ArrayList<Feature> noZones) {
		var validDirections = new ArrayList<Point>();
		for(int i = 0; i <= 350; i += 10) { 
			boolean flag = true;
			var mapContour = getMapGrid();
			var lng = currentLocation.longitude() + 0.0003 * Math.cos(Math.toRadians(i));
			var lat = currentLocation.latitude() + 0.0003 *  Math.sin(Math.toRadians(i));
			var point = Point.fromLngLat(lng, lat);
			if(TurfJoins.inside(point, mapContour) == false)
				flag = false;
			for(int p = 0; p < noZones.size(); p++)
				if(TurfJoins.inside(point, (Polygon)noZones.get(p).geometry()))
					flag = false;
			if(flag == true)
				validDirections.add(point);
		}
		return validDirections;
	}
	
	
	public static boolean checkIfValid(Point point, ArrayList<Feature> noZones) {
		var mapContour = getMapGrid();
		if(TurfJoins.inside(point, mapContour) == false)
			return false;
		for(int p = 0; p < noZones.size(); p++)
			if(TurfJoins.inside(point, (Polygon)noZones.get(p).geometry()))
				return false;
		return true;
	}
	    
}
