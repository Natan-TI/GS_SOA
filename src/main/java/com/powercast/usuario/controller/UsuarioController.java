package com.powercast.usuario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powercast.shared.exception.BadRequestException;
import com.powercast.usuario.dto.UsuarioDto;
import com.powercast.usuario.dto.UsuarioRequest;
import com.powercast.usuario.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	private final UsuarioService service;
	
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> create(@RequestBody UsuarioRequest req) {
		if (req.getUsername().isBlank()) {
		    throw new BadRequestException("O campo username não pode ser vazio");
		}
		UsuarioDto dto = service.createUser(
				req.getUsername(),
				req.getPassword(),
				"ROLE_USER"
				);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> findAll() {
		List<UsuarioDto> users = service.findAll();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> findById(@PathVariable Long id) {
		UsuarioDto user = service.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<UsuarioDto> findByUsername(@PathVariable String username) {
		UsuarioDto user = service.findByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDto> update(@PathVariable Long id, @RequestBody UsuarioRequest req) {
		if (req.getUsername().isBlank()) {
		    throw new BadRequestException("O campo username não pode ser vazio");
		}
		UsuarioDto updated = service.updateUser(
				id,
				req.getUsername(),
				req.getPassword(),
				"ROLE_USER"
				);
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
