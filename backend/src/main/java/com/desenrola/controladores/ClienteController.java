package com.desenrola.controladores;

import com.desenrola.controladores.dto.CadastroClienteDTO;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.servicos.aplicacao.ClienteAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteAppService clienteAppService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody CadastroClienteDTO dto) {
        Cliente cliente = clienteAppService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }
}
