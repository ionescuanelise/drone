package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

public class App 
{
	public static String seed;
	public static String port;
	public static Point startingPoint;

	
    public static void main( String[] args ) throws IOException, InterruptedException {
        var date = new Date(args[0], args[1], args[2]);
        var startingPoint = Point.fromLngLat(Double.parseDouble(args[4]), Double.parseDouble(args[3]));
        
        seed = args[5];
        port = args[6];
        
        String aqJson = ServerConnect.connect(date.getUrlAqJson());
        var parseAqJson = new AirQualityJson(aqJson);
        var sensors = parseAqJson.getSensorsList();
        var sensorLocations = ComputeRoute.getLocations(sensors);
        
        var noZones = new NoFlyZones();
        var restricted = noZones.getNoZones();
        
        var flyPath = new FlyPath(startingPoint, sensorLocations, restricted);
        List<Point> line = flyPath.getPath();
        var path = Feature.fromGeometry((Geometry)(LineString.fromLngLats(line)));
        System.out.println(flyPath.getNoMoves());
        
        var map = new GeoJsonMap(sensors, flyPath.getNotVisited());
        
        var listFeatures = map.getListMarkers();
        listFeatures.add(path);
        for(int i = 0; i < restricted.size(); i++)
        	listFeatures.add(restricted.get(i));
        
        var mapContour = ComputeRoute.getMapGrid();
        listFeatures.add(Feature.fromGeometry((Geometry)mapContour));
        
        var featureCollection = FeatureCollection.fromFeatures(listFeatures);
        var json = featureCollection.toJson();
        ProduceOutputFile.createFile(json);
          
    }
    
}
