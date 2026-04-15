# Guia — Projeto Integrador ADS 2026/1
_____________________________________________________________________________

## Resumo

| Informação | Detalhe |
| **Nome do Projeto** | Desenrola

| **Tema** | Plataforma de conexão entre clientes e profissionais autônomos

| **Integrantes do Grupo** | Matheus Oliveira Mitter, Gabriel de Oliveira Nascimento, Huan Cláudio Souza Viana e Felipe Milhomem Rocha

| **Descrição da Proposta do Sistema**| O Desenrola é uma plataforma que conecta clientes a profissionais autônomos, permitindo que os usuários encontrem serviços de qualidade e que os profissionais divulguem seus trabalhos. A plataforma conta com um sistema de avaliações, um sistema de mensagens e um sistema de agendamento de serviços

| **Arquitetura** | Em Camadas (Layered Architecture)

| **Tecnologia Chave** | Mensageria Assíncrona (RabbitMQ/Kafka)

_____________________________________________________________________________

## Divisão de Entrega 

### ETAPA N1: Fundação e Design

- [ ] **Modelagem de Software:** Diagramas C4 (Níveis 1 e 2) e Diagrama de Classes.
- [ ] **Interface (UI):** Protótipo de média/ alta fidelidade no Figma + Design System.
- [ ] **Documentação:** Plano de testes e requisitos não-funcionais.
- [ ] **Ambiente:** Repo Git organizado e infraestrutura base via Docker.

_____________________________________________________________________________

### ETAPA N2: Produto Funcional

- [ ] **Fluxo Assíncrono:** Cadastro → Fila → Worker → WebSocket → Tela atualizada.
- [ ] **Código Limpo:** Uso de Injeção de Dependência e Padrões de Projeto.
- [ ] **Qualidade:** Testes unitários com boa cobertura e análise SonarCloud.

_____________________________________________________________________________

## Critérios de Avaliação

| Critério | O que a banca busca? |
| **Arquitetura/Técnica** | Desacoplamento e uso de mensageria. |
| **Qualidade/Código** | Design Patterns e Clean Code. |
| **Interface/UI** | Fidelidade ao Figma e usabilidade. |
| **Testes/Qualidade** | Testes unitários e análise estática. |
| **Organização/Git** | Fluxo de branches e commits claros. |

_____________________________________________________________________________

## Pilares da Solução Distribuída

> **Não será aceito um "CRUD simples"**. O diferencial do semestre é a **reatividade assíncrona**.

1. **Mensageria:** Toda ação pesada ou atualização de status deve passar por uma fila (Broker).
2. **WebSocket/Streams:** O frontend deve ser "vivo", recebendo atualizações sem refresh.
3. **Resiliência:** Se o backend principal cair, as mensagens devem continuar seguras na fila.

_____________________________________________________________________________

## Divisão de Tarefas Sugerida

| Quem | Responsabilidade Principal | Foco em Pastas |
| **Integrante 1** | **Backend Principal + API** | `src/controladores/` |
| **Integrante 2** | **Regras de Negócio + Mensageria** | `src/servicos/` |
| **Integrante 3** | **Banco de Dados** | `src/repositorios/` |
| **Integrante 4** | **Frontend + Real-time (WebSocket)** | `src/frontend/` |

_____________________________________________________________________________
