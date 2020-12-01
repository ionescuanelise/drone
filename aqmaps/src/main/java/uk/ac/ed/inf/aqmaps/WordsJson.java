package uk.ac.ed.inf.aqmaps;

public class WordsJson {
	private String wordsjson;
	
	public WordsJson(String location) {
		String[] threeWords = location.split("\\.");
		this.wordsjson = "http://localhost/words/" + threeWords[0] + "/" + threeWords[1] + "/" + threeWords[2] + "/details.json";
	}
	
	public String getWordsjson() {
		return wordsjson;
	}

}
