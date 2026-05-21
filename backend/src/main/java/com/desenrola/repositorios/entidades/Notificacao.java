package com.desenrola.repositorios.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(nullable = false)
    private Boolean lida = false;

    @Column(name = "id_referencia")
    private Long idReferencia;

    @Column(name = "tipo_referencia", length = 50)
    private String tipoReferencia;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}
