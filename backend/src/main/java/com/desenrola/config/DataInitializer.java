package com.desenrola.config;

import com.desenrola.modelos.Cliente;
import com.desenrola.modelos.Profissional;
import com.desenrola.modelos.Servico;
import com.desenrola.modelos.enums.TipoUsuario;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.ServicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Profile("!mysql")
public class DataInitializer {

    @Bean
    CommandLineRunner carregarDadosIniciais(
            ClienteRepository clienteRepository,
            ProfissionalRepository profissionalRepository,
            ServicoRepository servicoRepository
    ) {
        return args -> {
            if (clienteRepository.count() == 0) {
                Cliente cliente = new Cliente();
                cliente.setNome("Mariana Souza");
                cliente.setEmail("mariana@desenrola.com");
                cliente.setSenhaHash("senha-temporaria");
                cliente.setTelefone("11999990000");
                cliente.setTipo(TipoUsuario.CLIENTE);
                cliente.setCpf("12345678900");
                cliente.setEndereco("Rua das Flores, 100");
                clienteRepository.save(cliente);
            }

            if (profissionalRepository.count() == 0) {
                Profissional eletricista = new Profissional();
                eletricista.setNome("Carlos Lima");
                eletricista.setEmail("carlos@desenrola.com");
                eletricista.setSenhaHash("senha-temporaria");
                eletricista.setTelefone("11999991111");
                eletricista.setTipo(TipoUsuario.PROFISSIONAL);
                eletricista.setEspecialidade("Eletricista");
                eletricista.setDisponivel(true);
                eletricista.setAvaliacaoMedia(4.8);
                eletricista.setTotalAvaliacoes(1);
                profissionalRepository.save(eletricista);

                Profissional encanadora = new Profissional();
                encanadora.setNome("Patricia Gomes");
                encanadora.setEmail("patricia@desenrola.com");
                encanadora.setSenhaHash("senha-temporaria");
                encanadora.setTelefone("11999992222");
                encanadora.setTipo(TipoUsuario.PROFISSIONAL);
                encanadora.setEspecialidade("Encanadora");
                encanadora.setDisponivel(true);
                encanadora.setAvaliacaoMedia(4.9);
                encanadora.setTotalAvaliacoes(1);
                profissionalRepository.save(encanadora);
            }

            // No modo H2 a estrutura real de servico por categoria nao existe;
            // por isso o carregamento inicial fica restrito a cliente/profissional.
        };
    }
}
