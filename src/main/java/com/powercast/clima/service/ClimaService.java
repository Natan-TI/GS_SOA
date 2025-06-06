package com.powercast.clima.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.powercast.clima.dto.ClimaDto;
import com.powercast.clima.model.ClimaResponse;

@Service
public class ClimaService {
	
	private final RestTemplate rest;
	@Value("${openweather.api.url}")
	private String apiUrl;
	@Value("${openweather.api.key}")
	private String apiKey;
	
	public ClimaService(RestTemplate rest) {
		this.rest = rest;
	}
	
	public ClimaDto buscarPorCidade(String cidade) {
		String url = String.format("%s/weather?q=%s&units=metric&appid=%s", apiUrl, cidade, apiKey);
		ClimaResponse resp = rest.getForObject(url, ClimaResponse.class);
		
		return ClimaDto.builder()
				.temperatura(resp.getMain().getTemp())
				.umidade(resp.getMain().getHumidity())
				.descricao(resp.getWeather().get(0).getDescription())
				.velocidadeVento(resp.getWind().getSpeed())
				.build();
	}
}
