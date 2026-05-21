package com.desenrola.controladores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriarSolicitacaoDTO {

    private Long idCliente;
    private Integer idServico;
    private String titulo;
    private String descricao;
    private String enderecoAtendimento;
    private String cidadeAtendimento;
    private String estadoAtendimento;
    private BigDecimal valorEstimado;
    private LocalDateTime dataPreferencial;
}
