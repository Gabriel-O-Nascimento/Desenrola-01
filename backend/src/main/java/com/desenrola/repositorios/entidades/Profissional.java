package com.desenrola.repositorios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "profissional")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "categoria"})
public class Profissional {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(length = 18)
    private String documento;

    public enum TipoDocumento { CPF, CNPJ }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaServico categoria;

    @Column(length = 200)
    private String especialidade;

    @Column(name = "descricao_perfil", columnDefinition = "TEXT")
    private String descricaoPerfil;

    @Column(name = "avaliacao_media", nullable = false, precision = 3, scale = 2)
    private BigDecimal avaliacaoMedia = BigDecimal.ZERO;

    @Column(name = "total_avaliacoes", nullable = false)
    private Integer totalAvaliacoes = 0;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(name = "raio_atendimento_km")
    private Integer raioAtendimentoKm;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
