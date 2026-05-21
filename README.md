# 🔧 Desenrola — Plataforma de Serviços Residenciais

Solução Web Full-Stack distribuída com **Arquitetura em Camadas**, **Mensageria Assíncrona** (RabbitMQ) e **Notificações em Tempo Real** (WebSocket).

**Repositório:** [https://github.com/Gabriel-O-Nascimento/Desenrola-01](https://github.com/Gabriel-O-Nascimento/Desenrola-01)

---

## 📋 Sobre o Projeto

O Desenrola conecta **clientes** que precisam de serviços residenciais (hidráulica, elétrica, pintura, etc.) com **profissionais** qualificados. O sistema gerencia todo o fluxo: desde a criação da solicitação até a avaliação do serviço prestado.

### Fluxo Principal

```
Cliente cria solicitação
    ↓ (API REST)
Backend salva no banco e publica evento no RabbitMQ
    ↓ (Mensageria assíncrona)
Consumer processa evento e gera notificação
    ↓ (WebSocket)
Cliente recebe notificação em tempo real na tela
```

---

## 🏗️ Arquitetura

Arquitetura em Camadas com comunicação assíncrona via RabbitMQ.

```
┌─────────────────────┐
│      Frontend        │  React — Interface do usuário
└──────────┬──────────┘
           ↓ HTTP / WebSocket
┌─────────────────────┐
│    Controladores     │  API REST (endpoints)
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│      Serviços        │  Regras de negócio + Mensageria (RabbitMQ)
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Repositórios      │  JPA/Hibernate → MySQL
└─────────────────────┘
```

> Justificativa detalhada em [artefatos-n1/02-design-software/ARQUITETURA.md](artefatos-n1/02-design-software/ARQUITETURA.md)

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Frontend | React |
| Backend | Java 21 + Spring Boot 3.4 |
| Banco de Dados | MySQL 8.0 |
| Mensageria | RabbitMQ 3 |
| WebSocket | Spring WebSocket (STOMP) |
| Testes | JUnit 5 + Mockito + AssertJ |
| Cobertura | JaCoCo |
| Build | Maven |
| Infraestrutura | Docker Compose |

---

## 🎨 Design Patterns Aplicados

| Padrão | Onde | Propósito |
|--------|------|-----------|
| **Strategy** | `NotificacaoService` + `strategy/impl/` | Cada tipo de notificação tem sua própria estratégia de montar título e mensagem |
| **Factory** | `EventoFactory` | Centraliza a criação dos eventos de mensageria |
| **Injeção de Dependência** | Todo o projeto (Spring) | Desacoplamento entre camadas |
| **Repository** | `repositorios/` | Abstração do acesso a dados |
| **DTO** | `controladores/dto/` | Controle do que entra e sai da API |
| **Layered Architecture** | Estrutura geral | Separação de responsabilidades |

> Detalhes em [artefatos-n1/02-design-software/design-patterns.md](artefatos-n1/02-design-software/design-patterns.md)

---

## 📡 Endpoints da API

### Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/clientes` | Cadastrar cliente |

### Profissionais
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/profissionais` | Cadastrar profissional |
| GET | `/api/profissionais` | Listar profissionais disponíveis |
| GET | `/api/profissionais?idCategoria=1` | Filtrar por categoria |

### Solicitações
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/solicitacoes` | Criar solicitação → dispara mensageria |
| GET | `/api/solicitacoes/{id}` | Buscar por ID |
| GET | `/api/solicitacoes?idCliente=1` | Listar por cliente |
| GET | `/api/solicitacoes?idProfissional=1` | Listar por profissional |
| PUT | `/api/solicitacoes/{id}/status` | Atualizar status → dispara mensageria |

### Orçamentos
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/orcamentos` | Enviar orçamento → dispara mensageria |

### Avaliações
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/avaliacoes` | Criar avaliação → dispara mensageria |
| GET | `/api/avaliacoes/profissional/{id}` | Listar avaliações do profissional |

---

## 🐇 Filas RabbitMQ

| Fila | Evento | Consumer |
|------|--------|----------|
| `solicitacao.criada` | Nova solicitação criada | `SolicitacaoConsumer` |
| `orcamento.enviado` | Profissional enviou orçamento | `OrcamentoConsumer` |
| `status.atualizado` | Status da solicitação mudou | `StatusConsumer` |
| `avaliacao.criada` | Cliente avaliou o serviço | `AvaliacaoConsumer` |

Cada consumer processa o evento e gera uma notificação via WebSocket para o usuário.

---

## 🚀 Como Rodar

### Pré-requisitos
- Java 21+
- Maven
- Docker Desktop

### 1. Subir infraestrutura (MySQL + RabbitMQ)
```bash
docker compose up -d
```

### 2. Rodar o backend
```bash
cd backend
mvn spring-boot:run
```

O servidor inicia em `http://localhost:8080`.

### 3. Acessar painéis
- **RabbitMQ Management:** http://localhost:15672 (admin / admin123)
- **MySQL:** localhost:3306 (desenrola_user / desenrola_pass)

---

## ✅ Testes e Cobertura

### Rodar testes
```bash
cd backend
mvn test
```

**Resultado:** 48 testes unitários passando (0 falhas).

### Ver relatório de cobertura (JaCoCo)
Após rodar `mvn test`, abra no navegador:
```
backend/target/site/jacoco/index.html
```

**Cobertura atual: 67%** (excluindo entidades, DTOs, eventos e configurações).

| Pacote | Cobertura |
|--------|-----------|
| `servicos/` (domínio) | 100% |
| `servicos/factory/` | 100% |
| `servicos/strategy/impl/` | 80% |
| `servicos/aplicacao/` | 71% |
| `controladores/` | 68% |
| `servicos/producers/` | 50% |
| `servicos/consumers/` | 21% |

---

## 📁 Estrutura do Projeto

```
Desenrola-01/
├── backend/
│   ├── src/main/java/com/desenrola/
│   │   ├── config/                    # RabbitMQ, WebSocket, CORS
│   │   ├── controladores/            # Endpoints REST
│   │   │   └── dto/                   # Objetos de transferência
│   │   ├── repositorios/             # Interfaces JPA
│   │   │   └── entidades/            # Mapeamento do banco
│   │   └── servicos/                  # Lógica de negócio
│   │       ├── aplicacao/             # Orquestração (AppServices)
│   │       ├── consumers/            # Consumidores RabbitMQ
│   │       ├── eventos/              # Objetos de evento
│   │       ├── factory/              # Factory de eventos
│   │       ├── producers/            # Publicadores RabbitMQ
│   │       └── strategy/             # Strategy de notificações
│   │           └── impl/             # Implementações concretas
│   └── src/test/java/com/desenrola/  # Testes unitários
├── frontend/                          # Interface React
├── DataBase/                          # Script SQL inicial
├── artefatos-n1/                      # Documentação (diagramas, protótipo, etc.)
└── docker-compose.yml                 # MySQL + RabbitMQ
```

---

## 👥 Equipe

| Responsabilidade | Camada |
|-----------------|--------|
| API REST + Controladores | `controladores/` |
| Mensageria + Regras de Negócio | `servicos/` |
| Banco de Dados + Entidades | `repositorios/` |
| Interface Web | `frontend/` |
