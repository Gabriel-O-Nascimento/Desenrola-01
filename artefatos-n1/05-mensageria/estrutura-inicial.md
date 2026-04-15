# Artefatos de Web/Mensageria

Este documento descreve a localização dos artefatos técnicos iniciais do projeto Desenrola, demonstrando a configuração do ambiente, o uso de Docker e a conexão básica entre os componentes da arquitetura.

## Evidência da Configuração Inicial do Ambiente
O ambiente está configurado para suportar o desenvolvimento com:
- **Backend**: Spring Boot.
- **Frontend**: HTML/JS.
- **Infraestrutura**: Docker Compose para serviços de suporte.

## Evidência do Uso de Docker (Fila/Broker e Banco de Dados)
A infraestrutura de serviços necessários (RabbitMQ para mensageria e PostgreSQL para banco de dados) foi isolada em containers Docker para garantir a paridade do ambiente entre os desenvolvedores.

- **Arquivo de Configuração**: [`docker-compose.yml`](../../docker-compose.yml) localizado na raiz da pasta `Desenrola-01`.
- **Serviços Definidos**:
  - `servidor-mensageria`: RabbitMQ
  - `banco-dados`: PostgreSQL

## Estrutura Inicial "Hello World" (Conexão Frontend-Backend)
Para demonstrar que a arquitetura está integrada e funcional, criamos uma estrutura "Hello World" que realiza uma conexão real entre as camadas:

1.  **Frontend (Consumidor)**: 
    - Localização: [`frontend/index.html`](../../frontend/index.html)
    - Função: Realiza chamadas `fetch` para os endpoints do backend e exibe o status da conexão em tempo real.

2.  **Backend (Provedor)**: 
    - Localização: [`backend/src/main/java/com/desenrola/controladores/TesteController.java`](../../backend/src/main/java/com/desenrola/controladores/TesteController.java)
    - Função: Expõe endpoints de `/health` para verificação de status e `/teste-mensageria` para integração com o RabbitMQ.

3.  **Fluxo da Demonstração**:
    - O usuário acessa a página no navegador.
    - O JavaScript envia uma mensagem para o Backend.
    - O Backend processa e publica essa mensagem na fila do RabbitMQ (conforme definido no `docker-compose`).
    - O status é atualizado na tela, confirmando a saúde de toda a pilha tecnológica.
