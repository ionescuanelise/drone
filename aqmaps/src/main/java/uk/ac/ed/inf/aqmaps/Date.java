package uk.ac.ed.inf.aqmaps;

public class Date {
	private String year;
	private String month;
	private String day;
	
	public Date(String day, String month, String year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public String getUrlAqJson() {
		StringBuilder builder = new StringBuilder();

		builder.append("http://localhost/maps/");
		builder.append(year);
		builder.append("/");
		builder.append(month);
		builder.append("/");
		builder.append(day);
		builder.append("/");
		builder.append("air-quality-data.json");
		
		String aqjson = builder.toString();
		return aqjson;
	}

}
