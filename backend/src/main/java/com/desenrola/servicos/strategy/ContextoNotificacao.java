package com.desenrola.servicos.strategy;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ContextoNotificacao {

    private String categoria;
    private BigDecimal valor;
    private String statusAnterior;
    private String statusNovo;
    private Integer nota;
}
