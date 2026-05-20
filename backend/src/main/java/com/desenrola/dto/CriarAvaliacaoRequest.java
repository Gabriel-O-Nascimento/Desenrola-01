package com.desenrola.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarAvaliacaoRequest(
        @NotNull Long solicitacaoId,
        @Min(1) @Max(5) Integer nota,
        @NotBlank String comentario
) {
}
