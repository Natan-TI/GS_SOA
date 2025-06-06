package com.powercast.predicao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredicaoDto {
	private  String bairro;
	private double risco;
	private double temperatura;
	private double umidade;
	private int falhasRecentes;
}
