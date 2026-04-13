# ⚙️ Camada de Serviços (Regras de Negócio + Mensageria)

> **Responsável:** Integrante 2
> **Foco:** Lógica de negócio, orquestração e comunicação assíncrona (RabbitMQ)

## O que esta pasta precisa conter

- **Serviços:** Um arquivo por domínio (ex: `PedidoService`, `UsuarioService`). Contêm toda a lógica de negócio — validações de regra, cálculos, decisões.
- **Publicadores:** Código que envia mensagens para as filas do RabbitMQ.
- **Consumidores (Workers):** Código que escuta as filas e processa mensagens.
- **Config de mensageria:** Setup de conexão, filas e exchanges do RabbitMQ.

## Fluxo assíncrono

```
Controlador chama Serviço
    → Serviço executa regra de negócio
    → Serviço publica mensagem na fila (RabbitMQ)
    → Worker consome a mensagem
    → Worker atualiza banco via Repositório
    → Worker emite evento via WebSocket → Tela atualiza
```

## Regra

Serviços chamam repositórios para acessar dados, nunca acessam o banco diretamente.

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Arquitetura/Técnica | Mensageria funcionando, desacoplamento via filas |
| Qualidade/Código | Design Patterns, regras de negócio bem isoladas |
| Testes/Qualidade | Testes unitários dos serviços e consumidores |
