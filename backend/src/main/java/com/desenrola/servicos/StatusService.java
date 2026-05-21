package com.desenrola.servicos;

import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.StatusProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusProducer statusProducer;
    private final EventoFactory eventoFactory;

    public void atualizarStatus(Long solicitacaoId, Long clienteId, Long profissionalId,
                                 String statusAnterior, String statusNovo) {
        StatusAtualizadoEvento evento = eventoFactory.criarStatusAtualizado(
                solicitacaoId, clienteId, profissionalId, statusAnterior, statusNovo
        );
        statusProducer.publicar(evento);
    }
}
