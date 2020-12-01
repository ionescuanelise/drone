package uk.ac.ed.inf.aqmaps;

import java.net.URI;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerConnect {
	
	public static String connect (String urlString) throws IOException, InterruptedException {
		var client = HttpClient.newHttpClient();
		var request = HttpRequest.newBuilder().uri(URI.create(urlString)).build();
		var response = client.send(request, BodyHandlers.ofString());
		return response.body();
	}
	
}
