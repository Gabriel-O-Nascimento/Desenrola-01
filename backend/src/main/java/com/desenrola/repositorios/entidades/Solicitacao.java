package com.desenrola.repositorios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cliente", "profissional", "servico"})
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profissional")
    private Profissional profissional;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "endereco_atendimento", length = 255)
    private String enderecoAtendimento;

    @Column(name = "cidade_atendimento", length = 100)
    private String cidadeAtendimento;

    @Column(name = "estado_atendimento", length = 2)
    private String estadoAtendimento;

    @Column(name = "valor_estimado", precision = 10, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Column(name = "data_preferencial")
    private LocalDateTime dataPreferencial;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "motivo_cancelamento", length = 255)
    private String motivoCancelamento;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
