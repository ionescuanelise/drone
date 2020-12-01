package uk.ac.ed.inf.aqmaps;

public class What3Words {
	public String country;
	public Square square;
	public String nearestPlace;
	public Coordinates coordinates;
	public String words;
	public String language;
	public String map;
	
	public class Square {
		Coordinates southwest;
		Coordinates northeast;
	}
	
	public class Coordinates {
		String lng;
		String lat;
	}

	public Double getLng() {
		return Double.parseDouble(coordinates.lng);
	}
	
	public Double getLat() {
		return Double.parseDouble(coordinates.lat);
	}
}
