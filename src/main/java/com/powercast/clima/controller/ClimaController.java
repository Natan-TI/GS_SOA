package com.powercast.clima.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powercast.clima.dto.ClimaDto;
import com.powercast.clima.service.ClimaService;

@RestController
@RequestMapping("/clima")
public class ClimaController {
	
	private final ClimaService service;
	
	public ClimaController(ClimaService service) {
		this.service = service;
	}
	
	@GetMapping("/{cidade}")
	public ResponseEntity<ClimaDto> getClima(@PathVariable("cidade") String cidade) {
		return ResponseEntity.ok(service.buscarPorCidade(cidade));
	}
}
