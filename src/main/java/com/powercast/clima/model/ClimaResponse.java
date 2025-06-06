package com.powercast.clima.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClimaResponse {
	@JsonProperty("main")
	private Main main;
	
	@JsonProperty("weather")
	private List<Weather> weather;
	
	@JsonProperty("wind")
	private Wind wind;
	
	@Data public static class Main {
		private double temp;
		private double humidity;
	}
	
	@Data public static class Weather{
		private String description;
	}
	
	@Data public static class Wind {
		private double speed;
	}
}
