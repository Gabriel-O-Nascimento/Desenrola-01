package com.desenrola.servicos;

import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.AvaliacaoProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoProducer avaliacaoProducer;
    private final EventoFactory eventoFactory;

    public void criarAvaliacao(Long avaliacaoId, Long solicitacaoId, Long profissionalId,
                                Long clienteId, Integer nota, String comentario) {
        AvaliacaoCriadaEvento evento = eventoFactory.criarAvaliacaoCriada(
                avaliacaoId, solicitacaoId, profissionalId, clienteId, nota, comentario
        );
        avaliacaoProducer.publicar(evento);
    }
}
