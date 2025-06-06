package com.powercast.clima.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClimaDto {
	private double temperatura;
	private double umidade;
	private String descricao;
	private double velocidadeVento;
}
