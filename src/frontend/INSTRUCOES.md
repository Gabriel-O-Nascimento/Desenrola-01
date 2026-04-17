# Frontend (Interface + Real-time)

> **Responsável:** Gabriel
> **Foco:** Interface do usuário e conexão WebSocket para atualizações em tempo real

## Disciplinas Envolvidas

- **Modelagem de Interfaces (UI)** - Implementação do protótipo Figma e Design System
- **Desenvolvimento de Software Web** - Implementação do frontend (React/Vue)
- **Mensageria e Streams** - Conexão WebSocket para atualizações em tempo real
- **Qualidade de Software** - Testes de componentes

## O que você precisa criar

### Telas principais do Desenrola (baseadas no Figma):

**Para o Cliente:**
- Home - Tela inicial com categorias de serviços
- Busca de Profissionais - Listar profissionais por categoria
- Solicitar Serviço - Formulário para criar solicitação
- Chat - Conversar com profissional
- Histórico - Ver solicitações passadas
- Avaliação - Avaliar serviço concluído

**Para o Profissional:**
- Cadastro - Cadastrar como profissional
- Perfil - Editar dados e portfólio
- Solicitações - Ver solicitações recebidas
- Orçamento - Criar orçamento para cliente
- Chat - Conversar com cliente
- Histórico - Ver serviços realizados

### Também precisa:
- **Componentes reutilizáveis:** Botão, Card, Modal, Input, etc.
- **Serviços HTTP:** Funções que chamam a API (fetch/axios)
- **WebSocket:** Conexão para receber notificações em tempo real
- **Estilos:** CSS baseado no Design System do Figma

## Fluxo de atualização em tempo real

```
Cliente cria solicitação
    → Frontend faz POST na API
    → Backend processa via fila (assíncrono)
    → Worker finaliza e emite evento via WebSocket
    → Frontend recebe notificação
    → Tela atualiza automaticamente (sem refresh)
```

## Regra importante

A interface **nunca** contém lógica de negócio. Ela apenas:
- Exibe dados vindos da API
- Envia ações para a API
- Atualiza em tempo real via WebSocket

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Interface/UI | Fidelidade ao Figma, usabilidade, Design System |
| Arquitetura/Técnica | WebSocket funcionando, tela reativa |
| Testes/Qualidade | Testes de componentes |
