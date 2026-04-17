# Guia — Projeto Integrador ADS 2026/1
_____________________________________________________________________________

## Resumo

| Informação | Detalhe |
| **Nome do Projeto** | Desenrola

| **Tema** | Plataforma de conexão entre clientes e profissionais autônomos

| **Integrantes do Grupo** | Matheus Oliveira Mitter, Gabriel de Oliveira Nascimento, Huan Cláudio Souza Viana e Felipe Milhomem Rocha

| **Descrição da Proposta do Sistema**| O Desenrola é um aplicativo de intermediação de serviços que conecta clientes a profissionais qualificados. A proposta do app é facilitar a busca por especialistas capazes de resolver problemas do dia a dia que o cliente não consegue ou não tem tempo de resolver. Além de apresentar profissionais disponíveis, o Desenrola exibe avaliações de outros usuários, bem como fotos e vídeos de serviços realizados anteriormente, proporcionando mais segurança e confiança na escolha. O aplicativo também tem como objetivo apoiar os profissionais autônomos, ajudando-os a aumentar sua visibilidade e faturamento. Para isso, oferece ferramentas e informações que contribuem para o aprimoramento dos serviços prestados e para o crescimento profissional dos usuários da plataforma.

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
