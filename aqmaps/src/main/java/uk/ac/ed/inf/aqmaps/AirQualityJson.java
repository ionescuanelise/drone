package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AirQualityJson {
	private ArrayList<Sensor> sensorsList;
	
	public AirQualityJson(String aqjson) {
		var listType = new TypeToken<ArrayList<Sensor>>() {}.getType();
		ArrayList<Sensor> droneReading = new Gson().fromJson(aqjson, listType);
		this.sensorsList = droneReading;
	}

	public ArrayList<Sensor> getSensorsList() {
		return sensorsList;
	}	
}