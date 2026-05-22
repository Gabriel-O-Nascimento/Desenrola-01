# Artefatos de Web/Mensageria

Este documento descreve a localização dos artefatos técnicos do projeto Desenrola, demonstrando a configuração do ambiente, o uso de Docker e a integração entre os componentes da arquitetura.

## Configuração do Ambiente
O ambiente está configurado para suportar o desenvolvimento com:
- **Backend**: Java 21 + Spring Boot 3.4
- **Frontend**: React + Vite
- **Infraestrutura**: Docker Compose para os serviços de suporte (banco de dados e mensageria)

## Uso de Docker (Fila/Broker e Banco de Dados)
A infraestrutura de serviços necessários (RabbitMQ para mensageria e MySQL para banco de dados) foi isolada em containers Docker para garantir a paridade do ambiente entre os desenvolvedores.

- **Arquivo de configuração**: [`docker-compose.yml`](../../docker-compose.yml) na raiz do projeto.
- **Serviços definidos**:
  - `servidor-mensageria`: RabbitMQ 3 (Management UI em http://localhost:15672)
  - `banco-dados`: MySQL 8.0 (porta 3306)

## Integração entre Frontend, Backend e Mensageria

A arquitetura está integrada com comunicação assíncrona via RabbitMQ e atualização em tempo real via WebSocket:

1. **Frontend (React)**:
   - Localização: [`frontend/`](../../frontend/)
   - Função: Faz chamadas HTTP para a API REST do backend e mantém uma conexão WebSocket (STOMP/SockJS) para receber notificações em tempo real.

2. **Backend (Spring Boot)**:
   - Localização: [`backend/src/main/java/com/desenrola/`](../../backend/src/main/java/com/desenrola/)
   - Função: Expõe endpoints REST sob o prefixo `/api`, publica eventos no RabbitMQ e envia notificações via WebSocket no canal `/topic/notificacoes/{idUsuario}`.

3. **Fluxo da Mensageria**:
   - Cliente cria uma solicitação no frontend → `POST /api/solicitacoes`
   - Backend salva a solicitação no MySQL e publica um evento na fila do RabbitMQ
   - Um consumer assíncrono escuta a fila, processa o evento e gera uma notificação
   - A notificação é enviada via WebSocket e exibida em tempo real na tela do usuário
