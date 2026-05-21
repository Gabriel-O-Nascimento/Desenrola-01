package com.desenrola.servicos.eventos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCriadaEvento implements Serializable {

    private Long solicitacaoId;
    private Long clienteId;
    private String categoria;
    private String descricao;
    private String endereco;
    private LocalDateTime dataCriacao;
}
