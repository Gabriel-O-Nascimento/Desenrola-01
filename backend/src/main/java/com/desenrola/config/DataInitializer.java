package com.desenrola.config;

import com.desenrola.modelos.Cliente;
import com.desenrola.modelos.Profissional;
import com.desenrola.modelos.Servico;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.ServicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
                cliente.setTelefone("11999990000");
                cliente.setCpf("12345678900");
                cliente.setEndereco("Rua das Flores, 100");
                clienteRepository.save(cliente);
            }

            if (profissionalRepository.count() == 0) {
                Profissional eletricista = new Profissional();
                eletricista.setNome("Carlos Lima");
                eletricista.setEmail("carlos@desenrola.com");
                eletricista.setTelefone("11999991111");
                eletricista.setEspecialidade("Eletricista");
                eletricista.setDisponivel(true);
                eletricista.setAvaliacaoMedia(4.8);
                profissionalRepository.save(eletricista);

                Profissional encanadora = new Profissional();
                encanadora.setNome("Patricia Gomes");
                encanadora.setEmail("patricia@desenrola.com");
                encanadora.setTelefone("11999992222");
                encanadora.setEspecialidade("Encanadora");
                encanadora.setDisponivel(true);
                encanadora.setAvaliacaoMedia(4.9);
                profissionalRepository.save(encanadora);
            }

            if (servicoRepository.count() == 0) {
                Servico instalacao = new Servico();
                instalacao.setNome("Instalacao de ventilador");
                instalacao.setDescricao("Instalacao residencial com avaliacao inicial.");
                instalacao.setCategoria("MANUTENCAO");
                instalacao.setPrecoBase(180.0);
                servicoRepository.save(instalacao);

                Servico vazamento = new Servico();
                vazamento.setNome("Reparo de vazamento");
                vazamento.setDescricao("Correcao de vazamentos simples em pia ou torneira.");
                vazamento.setCategoria("MANUTENCAO");
                vazamento.setPrecoBase(150.0);
                servicoRepository.save(vazamento);
            }
        };
    }
}
