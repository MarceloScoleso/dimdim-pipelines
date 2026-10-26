package br.com.fiap.dimdim.dto;

import br.com.fiap.dimdim.model.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ContaRequest(
        @NotBlank(message = "Número é obrigatório") @Size(max = 20) String numero,
        @NotBlank(message = "Agência é obrigatória") @Size(max = 10) String agencia,
        @NotNull(message = "Tipo é obrigatório") TipoConta tipo,
        @PositiveOrZero(message = "Saldo inicial não pode ser negativo") BigDecimal saldo,
        @NotNull(message = "clienteId é obrigatório") Long clienteId
) {
}
