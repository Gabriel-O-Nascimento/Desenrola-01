package com.desenrola.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarSolicitacaoRequest(
        @NotNull Long clienteId,
        @NotNull Long servicoId,
        @NotBlank String enderecoAtendimento,
        @NotBlank String observacoes
) {
}
