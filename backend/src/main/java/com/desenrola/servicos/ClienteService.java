package com.desenrola.servicos;

import com.desenrola.dto.ClienteResponse;
import com.desenrola.dto.CriarClienteRequest;
import com.desenrola.modelos.Cliente;
import com.desenrola.repositorios.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteResponse criar(CriarClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setCpf(request.cpf());
        cliente.setEndereco(request.endereco());
        return toResponse(clienteRepository.save(cliente));
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Cliente buscarEntidade(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado."));
    }

    public ClienteResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCpf(),
                cliente.getEndereco()
        );
    }
}
