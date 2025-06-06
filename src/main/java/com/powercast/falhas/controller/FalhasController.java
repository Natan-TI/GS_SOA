package com.powercast.falhas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.powercast.falhas.dto.FalhaDto;
import com.powercast.falhas.service.FalhasService;

@RestController
@RequestMapping("/falhas")
public class FalhasController {
	
	private final FalhasService service;
	
	public FalhasController(FalhasService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<FalhaDto> criar(@RequestBody FalhaDto dto) {
		FalhaDto salvo = service.salvar(dto);
		return ResponseEntity.status(201).body(salvo);
	}
	
	@GetMapping
	public ResponseEntity<List<FalhaDto>> listar(@RequestParam(required = false) String bairro) {
		if (bairro != null) {
			return ResponseEntity.ok(service.buscaPorBairro(bairro));
		} else {
			return ResponseEntity.ok(service.buscaPorBairro("")); 
		}
	}
}
