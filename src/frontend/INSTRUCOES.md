# 🖥️ Frontend (Interface + Real-time)

> **Responsável:** Integrante 4
> **Foco:** Interface do usuário e conexão WebSocket para atualizações em tempo real

## O que esta pasta precisa conter

- **Componentes:** Componentes reutilizáveis (Botão, Card, Modal, etc.).
- **Páginas/Telas:** Cada tela da aplicação seguindo o protótipo do Figma.
- **Serviços HTTP:** Funções que chamam a API REST do backend (fetch/axios).
- **WebSocket:** Conexão que escuta eventos do servidor e atualiza a tela sem refresh.
- **Estilos:** CSS/Design System baseado no Figma.

## Fluxo de atualização em tempo real

```
Usuário clica "Criar Pedido"
    → Faz POST na API (camada de controladores)
    → Backend processa via fila (assíncrono)
    → Worker finaliza e emite evento via WebSocket
    → Tela atualiza automaticamente
```

## Regra

A interface nunca contém lógica de negócio. Ela exibe dados e envia ações para a API.

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Interface/UI | Fidelidade ao Figma, usabilidade, Design System |
| Arquitetura/Técnica | WebSocket funcionando, tela reativa |
| Testes/Qualidade | Testes de componentes |
