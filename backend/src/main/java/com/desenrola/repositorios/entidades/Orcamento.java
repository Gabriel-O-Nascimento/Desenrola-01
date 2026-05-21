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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "solicitacao")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_solicitacao", nullable = false)
    private Solicitacao solicitacao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "total_mao_obra", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalMaoObra = BigDecimal.ZERO;

    @Column(name = "total_materiais", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalMateriais = BigDecimal.ZERO;

    @Column(name = "total_geral", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalGeral = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrcamento status = StatusOrcamento.PENDENTE;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
