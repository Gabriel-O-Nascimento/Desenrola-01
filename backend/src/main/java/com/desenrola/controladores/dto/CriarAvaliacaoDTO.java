package com.desenrola.controladores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriarAvaliacaoDTO {

    private Long idSolicitacao;
    private Long idCliente;
    private Long idProfissional;
    private Integer nota;
    private String comentario;
}
