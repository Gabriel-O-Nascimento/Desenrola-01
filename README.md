# Desenrola

Plataforma que conecta clientes a profissionais de serviços residenciais (hidráulica, elétrica, pintura e outros).

Repositório: https://github.com/Gabriel-O-Nascimento/Desenrola-01

## Sobre o projeto

O cliente cria uma solicitação no app, profissionais qualificados recebem essa solicitação e enviam orçamentos. Depois que o serviço é realizado, o cliente avalia o profissional. Todo o fluxo passa por uma fila de mensageria, e o cliente recebe atualizações em tempo real na tela.

## Como o sistema está organizado

```
Frontend (React)
       ↓ HTTP / WebSocket
Controladores (API REST)
       ↓
Serviços (regras de negócio)
       ↓
Repositórios (acesso ao banco)
       ↓
MySQL
```

Para ações importantes (criar solicitação, enviar orçamento, mudar status, avaliar), os serviços publicam um evento no RabbitMQ. Um consumer escuta a fila, processa o evento e envia uma notificação para o usuário pelo WebSocket. A justificativa completa da arquitetura está em [`artefatos-n1/02-design-software/ARQUITETURA.md`](artefatos-n1/02-design-software/ARQUITETURA.md).

## Tecnologias

- Frontend: React + Vite
- Backend: Java 21 + Spring Boot 3.4
- Banco de dados: MySQL 8.0
- Mensageria: RabbitMQ 3
- WebSocket: Spring WebSocket (STOMP)
- Testes: JUnit 5, Mockito, AssertJ
- Cobertura: JaCoCo
- Infraestrutura: Docker Compose

## Padrões de projeto aplicados

- Strategy: cada tipo de notificação tem sua própria estratégia para montar título e mensagem (`servicos/strategy/`)
- Factory: a `EventoFactory` centraliza a criação dos eventos de mensageria (`servicos/factory/`)
- Repository: as interfaces JPA isolam o acesso ao banco (`repositorios/`)
- DTO: controla o que entra e sai da API (`controladores/dto/`)
- Injeção de Dependência: o Spring resolve as dependências entre as camadas

Detalhamento em [`artefatos-n1/02-design-software/design-patterns.md`](artefatos-n1/02-design-software/design-patterns.md).

## Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/clientes` | Cadastrar cliente |
| POST | `/api/profissionais` | Cadastrar profissional |
| GET | `/api/profissionais` | Listar profissionais disponíveis |
| POST | `/api/solicitacoes` | Criar solicitação (dispara mensageria) |
| GET | `/api/solicitacoes/{id}` | Buscar solicitação por id |
| GET | `/api/solicitacoes?idCliente=X` | Listar solicitações de um cliente |
| PUT | `/api/solicitacoes/{id}/status` | Atualizar status (dispara mensageria) |
| POST | `/api/orcamentos` | Enviar orçamento (dispara mensageria) |
| POST | `/api/avaliacoes` | Avaliar serviço (dispara mensageria) |

## Filas RabbitMQ

| Fila | Quando publica |
|------|----------------|
| `solicitacao.criada` | Cliente cria uma solicitação |
| `orcamento.enviado` | Profissional envia orçamento |
| `status.atualizado` | Status da solicitação muda |
| `avaliacao.criada` | Cliente avalia o serviço |

## Como rodar

Antes de começar, é preciso ter Java 21, Maven e Docker Desktop instalados.

1. Subir o MySQL e o RabbitMQ:
```bash
docker compose up -d
```

2. Rodar o backend:
```bash
cd backend
mvn spring-boot:run
```

O servidor inicia em `http://localhost:8080`.

3. Rodar o frontend (em outro terminal):
```bash
cd frontend
npm install
npm run dev
```

A interface fica disponível em `http://localhost:5173`.

4. Painéis de administração:
- RabbitMQ: http://localhost:15672 (usuário `admin`, senha `admin123`)
- MySQL: porta 3306, usuário `desenrola_user`, senha `desenrola_pass`

## Testes e cobertura

Para rodar os testes:
```bash
cd backend
mvn test
```

Resultado atual: 48 testes, 0 falhas. A cobertura geral fica em torno de 67%, com os pacotes de domínio e factory cobertos em 100%.

Depois de rodar os testes, o relatório HTML do JaCoCo é gerado em:
```
backend/target/site/jacoco/index.html
```

Basta abrir esse arquivo no navegador para ver a cobertura por pacote, classe e método.

## Estrutura de pastas

```
Desenrola-01/
├── backend/                 Spring Boot (API + mensageria)
│   ├── src/main/java/com/desenrola/
│   │   ├── config/          RabbitMQ, WebSocket, CORS
│   │   ├── controladores/   Endpoints REST e DTOs
│   │   ├── repositorios/    Interfaces JPA e entidades
│   │   └── servicos/        Regras de negócio, producers, consumers,
│   │                        eventos, factory e strategies
│   └── src/test/            Testes unitários (JUnit 5)
├── frontend/                React + Vite
├── DataBase/                Script SQL inicial
├── artefatos-n1/            Documentação (diagramas, plano de testes, etc.)
└── docker-compose.yml       MySQL + RabbitMQ
```
