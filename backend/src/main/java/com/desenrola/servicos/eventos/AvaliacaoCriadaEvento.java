package com.desenrola.servicos.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoCriadaEvento implements Serializable {

    private Long avaliacaoId;
    private Long solicitacaoId;
    private Long profissionalId;
    private Long clienteId;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
}
