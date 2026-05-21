package com.desenrola.controladores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroClienteDTO {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
}
