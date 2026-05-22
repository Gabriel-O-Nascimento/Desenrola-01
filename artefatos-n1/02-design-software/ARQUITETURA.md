# Arquitetura em Camadas

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
│    Repositórios      │  Acesso ao banco de dados (MySQL)
└─────────────────────┘
```

## Justificativa da escolha

### 1. Separação de responsabilidades
Cada camada tem uma responsabilidade clara, facilitando a manutenção, testes e evolução do sistema. Mudanças em uma camada (ex: trocar o banco de dados) não afetam as demais.

### 2. Adequada ao escopo do projeto
A arquitetura em camadas é a abordagem mais adequada para o escopo deste projeto, oferecendo o nível certo de desacoplamento sem a complexidade adicional de padrões como Hexagonal ou Clean Architecture, que são mais indicados para sistemas de maior porte e múltiplos contextos.

### 3. Testabilidade
Cada camada pode ser testada isoladamente:
- Controladores: testes de integração (a rota responde corretamente?)
- Serviços: testes unitários (a regra de negócio funciona?)
- Repositórios: testes de integração com o banco de dados
- Frontend: testes de componentes

### 4. Atende aos requisitos do projeto
A arquitetura em camadas entrega o desacoplamento e a separação de responsabilidades exigidos pelo projeto integrador, com clareza para demonstrar e justificar as decisões técnicas.

## Regra de dependência

A regra é simples: cada camada só conhece a camada imediatamente abaixo.

```
Frontend      → conhece Controladores (via HTTP)
Controladores → conhece Serviços
Serviços      → conhece Repositórios
Repositórios  → conhece o Banco de Dados
```

Nunca o contrário. Um repositório nunca chama um controlador. Um serviço nunca chama um controlador.
