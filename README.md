# 📦 Projeto Integrador - ADS 2026/1

Solução distribuída com **Arquitetura em Camadas** e **Mensageria Assíncrona** (RabbitMQ).

**Repositório:** [https://github.com/Gabriel-O-Nascimento/Desenrola-01](https://github.com/Gabriel-O-Nascimento/Desenrola-01)

> Para entender por que escolhemos essa arquitetura, leia o [ARQUITETURA.md](ARQUITETURA.md).

## Equipe e Responsabilidades

| Pasta | Integrante | Responsabilidade |
|-------|-----------|-----------------|
| `src/controladores/` | Integrante 1 | API REST, rotas, validações, DTOs |
| `src/servicos/` | Integrante 2 | Regras de negócio, RabbitMQ (filas/workers) |
| `src/repositorios/` | Integrante 3 | Banco de dados, queries, ORM, migrations |
| `src/frontend/` | Integrante 4 | Interface (React/Vue), WebSocket |

> Cada pasta tem um `INSTRUCOES.md` com tudo que precisa ser feito.

## Como as camadas se conectam

```
┌─────────────────────┐
│      Frontend        │  ← Integrante 4
└──────────┬──────────┘
           ↓ HTTP / WebSocket
┌─────────────────────┐
│    Controladores     │  ← Integrante 1
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│      Serviços        │  ← Integrante 2
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Repositórios      │  ← Integrante 3
└─────────────────────┘
```

## Como preparar o ambiente

Todos devem ter o **Docker Desktop** instalado.

### 1. Subir os serviços
```bash
docker compose up -d
```

### 2. Painel do RabbitMQ
- **URL:** `http://localhost:15672`
- **Usuário:** `admin`
- **Senha:** `admin123`

### 3. Banco de Dados (PostgreSQL)
- **Host:** `localhost:5432`
- **Banco:** `projeto_db`
- **Usuário:** `usuario_projeto`
- **Senha:** `senha_projeto`
