package com.desenrola.controladores.dto;

import com.desenrola.repositorios.entidades.Profissional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroProfissionalDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String documento;
    private Profissional.TipoDocumento tipoDocumento;
    private Integer idCategoria;
    private String especialidade;
    private String descricaoPerfil;
    private String cidade;
    private String estado;
    private Integer raioAtendimentoKm;
}
