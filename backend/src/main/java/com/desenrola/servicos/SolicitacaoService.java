package com.desenrola.servicos;

import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.SolicitacaoProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoProducer solicitacaoProducer;
    private final EventoFactory eventoFactory;

    public void criarSolicitacao(Long solicitacaoId, Long clienteId, String categoria,
                                  String descricao, String endereco) {
        SolicitacaoCriadaEvento evento = eventoFactory.criarSolicitacaoCriada(
                solicitacaoId, clienteId, categoria, descricao, endereco
        );
        solicitacaoProducer.publicar(evento);
    }
}
