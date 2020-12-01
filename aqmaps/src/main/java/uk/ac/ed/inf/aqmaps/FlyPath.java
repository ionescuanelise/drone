package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

public class FlyPath {
	private Point startingPoint;
	private int noMoves;
	private ArrayList<Point> orderedSensors;
	private ArrayList<Feature> noZones;
	private ArrayList<Point> sensors;
	private boolean[] visitedSensors;
	private boolean[] v;
	private ArrayList<Point> visited;
	private ArrayList<Integer> notVisited;
	private List<Point> path;
	
	
	public FlyPath(Point startingPoint, ArrayList<Point> sensors, ArrayList<Feature> noZones) {
		this.startingPoint = startingPoint;
		this.sensors = sensors;
		this.noMoves = 0;
		this.visited = new ArrayList<Point>();
		this.orderedSensors = new ArrayList<Point>();
		this.noZones = noZones;
		this.notVisited = new ArrayList<Integer>();
		this.visitedSensors = new boolean[sensors.size()];
		this.v = new boolean[sensors.size()+1];
		this.path = new ArrayList<Point>();
		orderSensors(startingPoint);
		generatePath();
	}
	
	
	public void generatePath() {
		Point curr = startingPoint, next;
		Point sensor = orderedSensors.get(0);
		int i = 0;
		path.add(startingPoint);
		
		while(!areAllVisited() && noMoves < 150) {
			var close = scanner(curr);
			if(close.size() != 0)
				for(int j = 0; j < close.size(); j++)
					v[close.get(j)] = true;
			
			if(ComputeRoute.distance(curr, sensor) > 0.0002) {
				var angle = ComputeRoute.getAngle(curr, sensor);
				if(ComputeRoute.checkIfValid(ComputeRoute.getPosition(curr,angle), noZones) && !ComputeRoute.intersect(curr, ComputeRoute.getPosition(curr,angle), noZones)) {
					curr = ComputeRoute.getPosition(curr,angle);
					path.add(curr);
					visited.add(curr);
				}
				else {
					var valid = ComputeRoute.getValidDirections(curr, noZones);
					next = findMinimCost(valid, sensor);
					while(ComputeRoute.intersect(curr, next, noZones)) {
						visited.add(next);
						next = findMinimCost(valid, sensor);
					}
					curr = next;
					path.add(curr);
					visited.add(curr);
				}
				noMoves++;
			}
			else {
				v[i] = true;
				while(v[i] == true && i < orderedSensors.size()-1) i++;
				sensor = orderedSensors.get(i);
			}
		}
		
		if(!areAllVisited()) {
			var notVisited = new ArrayList<>();
			for(int s = 0; s < v.length-1; s++)
				if(v[s] == false)
					notVisited.add(s);
		}
		
		flyBackToStart(curr);
		
	}
	
	public void flyBackToStart(Point curr) {
		boolean ok = true;
		Point next;
		this.visited = new ArrayList<Point>();
		
		while(noMoves < 150 && ok == true) {
			if(ComputeRoute.distance(curr, startingPoint) <= 0.0003) {
				path.add(startingPoint);
				ok = false;
			}
			else {
				var valid = ComputeRoute.getValidDirections(curr, noZones);
				next = findMinimCost(valid, startingPoint);
				while(ComputeRoute.intersect(curr, next, noZones)) {
					visited.add(next);
					next = findMinimCost(valid, startingPoint);
				}
				curr = next;
				path.add(curr);
				visited.add(curr);
			}
			noMoves++;
		}
	}
	
	
	public boolean areAllVisited() {
		for(int i = 0; i < v.length-1; i++)
			if(v[i] == false)
				return false;
		return true;
	}
	
	public ArrayList<Integer> scanner(Point curr) {
		var close = new ArrayList<Integer>();
		for(int i = 0; i < orderedSensors.size(); i++)
			if(ComputeRoute.distance(orderedSensors.get(i), curr) <= 0.0002)
				close.add(i);
		return close;
	}
	
	public void orderSensors(Point start){
		orderedSensors.add(findNearestSensor(start));
		for(int i = 1; i < sensors.size(); i++)
			orderedSensors.add(findNearestSensor(orderedSensors.get(orderedSensors.size() - 1)));
	}
	
	public Point findMinimCost(ArrayList<Point> validDirections, Point closestSensor) {
		double minim = Double.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		
		for(int j = 0; j < validDirections.size(); j++) {	
			if(!isVisited(validDirections.get(j))) {
				var d = ComputeRoute.distance(validDirections.get(j), closestSensor);
				if(d < minim) {
					minim = d;
					minIndex = j;
				}
			}
		}
		return validDirections.get(minIndex);
	}
	
	public boolean isVisited(Point point) {
		for(int i = 0; i < visited.size(); i++)
			if(visited.get(i) == point)
				return true;
		return false;
	}

	
	public Point findNearestSensor(Point curr) {
		double minim = Double.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		
		for(int i = 0; i < sensors.size(); i++) {
			if(visitedSensors[i] == false && sensors.get(i) != curr) {
				var d = ComputeRoute.distance(sensors.get(i), curr);
				if(d < minim) {
					minim = d;
					minIndex = i;
				}
			}
		}
		visitedSensors[minIndex] = true;
		return sensors.get(minIndex);
	}
	
	public int getNoMoves() {
		return noMoves;
	}
	
	public ArrayList<Integer> getNotVisited() {
		return notVisited;
	}
	
	public List<Point> getPath(){
		return path;
	}

}
