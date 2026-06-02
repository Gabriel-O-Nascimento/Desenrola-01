package com.desenrola.controladores.dto;

import com.desenrola.repositorios.entidades.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private TipoUsuario tipo;
    private String endereco;
    private String cidade;
    private String estado;
}
