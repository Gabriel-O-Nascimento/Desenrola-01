package com.desenrola.dto;

public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        String endereco
) {
}
