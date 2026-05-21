package com.desenrola.controladores.dto;

import com.desenrola.repositorios.entidades.StatusSolicitacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtualizarStatusDTO {

    private StatusSolicitacao novoStatus;
    private Long idProfissional;
    private String motivoCancelamento;
}
