package com.powercast.predicao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powercast.predicao.dto.PredicaoDto;
import com.powercast.predicao.service.PredicaoService;

@RestController
@RequestMapping("/predicao")
public class PredicaoController {

	private final PredicaoService service;
	
	public PredicaoController(PredicaoService service) {
		this.service = service;
	}
	
	@GetMapping("/{bairro}")
	public ResponseEntity<PredicaoDto> getRisco(@PathVariable String bairro) {
		return ResponseEntity.ok(service.preverRisco(bairro));
	}
}
