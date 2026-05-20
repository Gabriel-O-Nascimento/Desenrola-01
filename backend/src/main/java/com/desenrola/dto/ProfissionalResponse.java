package com.desenrola.dto;

public record ProfissionalResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade,
        Boolean disponivel,
        Double avaliacaoMedia
) {
}
