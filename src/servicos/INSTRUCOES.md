# Camada de Serviços (Regras de Negócio + Mensageria)

> **Responsável:** Integrante 2
> **Foco:** Lógica de negócio e comunicação assíncrona (RabbitMQ)

## Disciplinas Envolvidas

- **Mensageria e Streams** - Implementação do RabbitMQ (filas, workers, eventos)
- **Design de Software** - Aplicação de Design Patterns (Strategy, Observer, Factory)
- **Desenvolvimento de Software Web** - Lógica de negócio e orquestração
- **Qualidade de Software** - Testes unitários dos serviços

## O que você precisa criar

### Serviços principais do Desenrola:
- `SolicitacaoService` - Criar solicitação, validar dados, publicar na fila
- `NotificacaoService` - Enviar notificações para clientes e profissionais
- `AvaliacaoService` - Processar avaliações e calcular média
- `ChatService` - Gerenciar mensagens entre cliente e profissional

### Workers (Consumidores de fila):
- `NotificacaoProfissionalWorker` - Escuta fila e notifica profissionais quando há nova solicitação
- `AtualizacaoStatusWorker` - Atualiza status da solicitação e notifica cliente

### Eventos do RabbitMQ:
```
SolicitacaoCriadaEvent    → Notifica profissionais disponíveis
SolicitacaoAceitaEvent    → Ativa chat entre cliente e profissional
ServicoConcluidoEvent     → Solicita avaliação ao cliente
```

## Fluxo assíncrono do Desenrola
dis
```
Cliente cria solicitação
    → SolicitacaoService valida e salva
    → Publica "SolicitacaoCriadaEvent" na fila RabbitMQ
    → NotificacaoProfissionalWorker consome mensagem
    → Envia notificação para profissionais
    → Profissional aceita
    → Publica "SolicitacaoAceitaEvent"
    → ChatService ativa chat
```

## Regra importante

Serviços chamam repositórios para acessar dados, **nunca** acessam o banco diretamente.

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Arquitetura/Técnica | Mensageria funcionando, desacoplamento via filas |
| Qualidade/Código | Design Patterns, regras de negócio bem isoladas |
| Testes/Qualidade | Testes unitários dos serviços e consumidores |
