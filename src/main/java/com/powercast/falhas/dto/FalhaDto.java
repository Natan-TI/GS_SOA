package com.powercast.falhas.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FalhaDto {
	private Long id;
	private String bairro;
	private String descricao;
	private LocalDateTime timestamp;
}
