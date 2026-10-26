package br.com.fiap.dimdim.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ValorRequest(@NotNull(message = "Valor é obrigatório") BigDecimal valor) {
}
