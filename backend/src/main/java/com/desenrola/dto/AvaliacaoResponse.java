package com.desenrola.dto;

import java.time.LocalDateTime;

public record AvaliacaoResponse(
        Long id,
        Long solicitacaoId,
        Long clienteId,
        String nomeCliente,
        Long profissionalId,
        String nomeProfissional,
        Integer nota,
        String comentario,
        LocalDateTime dataCriacao
) {
}
