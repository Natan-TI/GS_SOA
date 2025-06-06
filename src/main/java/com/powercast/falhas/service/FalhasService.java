package com.powercast.falhas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.powercast.falhas.dto.FalhaDto;
import com.powercast.falhas.model.Falha;
import com.powercast.falhas.repository.FalhasRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FalhasService {
	
	private final FalhasRepository repo;
	
	@Autowired
	public FalhasService(FalhasRepository repo) {
		this.repo = repo;
	}
	
	public FalhaDto salvar(FalhaDto dto) {
		Falha entity = Falha.builder()
				.bairro(dto.getBairro())
				.descricao(dto.getDescricao())
				.timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
				.build();
		
		entity = repo.save(entity);
		dto.setId(entity.getId());
		dto.setTimestamp(entity.getTimestamp());
		return dto;
	}
	
	public List<FalhaDto> buscaPorBairro(String bairro) { 
		return repo.findByBairro(bairro).stream().map(f -> new FalhaDto(f.getId(), f.getBairro(), f.getDescricao(), f.getTimestamp())).collect(Collectors.toList());
	}
}
