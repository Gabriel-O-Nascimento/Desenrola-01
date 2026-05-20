package com.desenrola.dto;

public record ServicoResponse(
        Long id,
        String nome,
        String descricao,
        String categoria,
        Double precoBase
) {
}
