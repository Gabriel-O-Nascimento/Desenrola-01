package com.desenrola.controladores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriarOrcamentoDTO {

    private Long idSolicitacao;
    private Long idProfissional;
    private String observacoes;
    private BigDecimal totalMaoObra;
    private BigDecimal totalMateriais;
}
