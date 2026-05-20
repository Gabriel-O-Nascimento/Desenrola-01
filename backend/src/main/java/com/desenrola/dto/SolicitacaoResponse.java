package com.desenrola.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoResponse(
        Long id,
        Long clienteId,
        String nomeCliente,
        Long profissionalId,
        String nomeProfissional,
        Long servicoId,
        String nomeServico,
        String status,
        String enderecoAtendimento,
        String observacoes,
        BigDecimal valorTotal,
        LocalDateTime dataCriacao
) {
}
