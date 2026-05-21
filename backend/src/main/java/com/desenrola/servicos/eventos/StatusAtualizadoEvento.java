package com.desenrola.servicos.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusAtualizadoEvento implements Serializable {

    private Long solicitacaoId;
    private Long clienteId;
    private Long profissionalId;
    private String statusAnterior;
    private String statusNovo;
    private LocalDateTime dataAtualizacao;
}
