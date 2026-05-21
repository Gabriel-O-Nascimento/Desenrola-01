package com.desenrola.servicos.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoEnviadoEvento implements Serializable {

    private Long solicitacaoId;
    private Long profissionalId;
    private Long clienteId;
    private BigDecimal valor;
    private String descricao;
    private LocalDateTime dataEnvio;
}
