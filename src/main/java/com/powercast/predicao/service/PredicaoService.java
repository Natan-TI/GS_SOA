package com.powercast.predicao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.powercast.clima.dto.ClimaDto;
import com.powercast.clima.service.ClimaService;
import com.powercast.falhas.service.FalhasService;
import com.powercast.predicao.dto.PredicaoDto;

@Service
public class PredicaoService {

	private final ClimaService climaService;
	private final FalhasService falhasService;
	
	public PredicaoService(ClimaService climaService, FalhasService falhasService) {
		this.climaService = climaService;
		this.falhasService = falhasService;
	}
	
	public PredicaoDto preverRisco(String bairro) {
		ClimaDto clima = climaService.buscarPorCidade(bairro);
		
		List<?> falhas = falhasService.buscaPorBairro(bairro);
		long falhasRecentes = falhas.stream()
				.filter(f -> {
					return ((com.powercast.falhas.dto.FalhaDto) f)
							.getTimestamp().isAfter(LocalDateTime.now().minusDays(1));
				})
				.count();
		
		double risco = 10
				+ clima.getTemperatura() * 0.5
				+ falhasRecentes * 5;
		
		if (risco > 100) risco = 100;
		
		return PredicaoDto.builder()
				.bairro(bairro)
				.risco(risco)
				.temperatura(clima.getTemperatura())
				.umidade(clima.getUmidade())
				.falhasRecentes((int) falhasRecentes)
				.build();
	}
}
