# Por que Arquitetura em Camadas?

## O que é

A Arquitetura em Camadas (Layered Architecture) organiza o código em camadas horizontais, onde cada uma tem uma responsabilidade única e só se comunica com a camada imediatamente abaixo.

```
┌─────────────────────┐
│      Frontend        │  Telas, WebSocket, chamadas HTTP
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Controladores     │  Recebe requisições, valida, devolve respostas
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│      Serviços        │  Regras de negócio, mensageria (RabbitMQ)
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│    Repositórios      │  Acesso ao banco de dados (PostgreSQL)
└─────────────────────┘
```

## Por que escolhemos essa arquitetura

### 1. Simplicidade
É a arquitetura mais ensinada e documentada. Qualquer tutorial de Node.js, Java ou Python segue esse padrão. Isso significa que quando alguém do grupo travar, vai encontrar ajuda fácil.

### 2. Divisão natural pra 4 integrantes
Cada camada é uma responsabilidade clara que pode ser atribuída a uma pessoa:

| Camada | Integrante | O que faz |
|--------|-----------|-----------|
| Controladores | Integrante 1 | API REST, rotas, validações |
| Serviços | Integrante 2 | Lógica de negócio, RabbitMQ |
| Repositórios | Integrante 3 | Banco de dados, queries, ORM |
| Frontend | Integrante 4 | Interface, WebSocket |

### 3. Menos conflitos de código
Como cada pessoa trabalha em uma camada diferente, as chances de dois integrantes editarem o mesmo arquivo são baixas.

### 4. Fácil de testar
Cada camada pode ser testada isoladamente:
- Controladores: testes de integração (a rota responde certo?)
- Serviços: testes unitários (a regra de negócio funciona?)
- Repositórios: testes de integração com banco
- Frontend: testes de componentes

### 5. Atende os critérios da banca
A banca pede desacoplamento e separação de responsabilidades — a arquitetura em camadas entrega isso de forma clara e demonstrável, sem a complexidade de padrões como Hexagonal ou Clean Architecture que são mais adequados para sistemas maiores.

## Regra de dependência

A regra é simples: cada camada só conhece a camada imediatamente abaixo.

```
Frontend      → conhece Controladores (via HTTP)
Controladores → conhece Serviços
Serviços      → conhece Repositórios
Repositórios  → conhece o Banco de Dados
```

Nunca o contrário. Um repositório nunca chama um controlador. Um serviço nunca chama um controlador.
