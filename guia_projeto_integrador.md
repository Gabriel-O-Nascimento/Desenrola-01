# 📘 Guia Mestre — Projeto Integrador ADS 2026/1
_____________________________________________________________________________

## 📋 Resumo Executivo

| Informação | Detalhe |
| **Tema Atual** | **Livre** (Aguardando definição) |
| **Arquitetura** | Em Camadas (Layered Architecture) |
| **Tecnologia Chave** | Mensageria Assíncrona (RabbitMQ/Kafka) |

_____________________________________________________________________________

## 🚀 Divisão de Entrega e Prazos

### 🗓️ ETAPA N1: Fundação e Design
**Prazo Final:** 14/04/2026

- [ ] **Modelagem de Software:** Diagramas C4 (Níveis 1 e 2) e Diagrama de Classes.
- [ ] **Interface (UI):** Protótipo de alta fidelidade no Figma + Design System.
- [ ] **Documentação:** Plano de testes e requisitos não-funcionais.
- [ ] **Ambiente:** Repo Git organizado e infraestrutura base via Docker.

_____________________________________________________________________________

### 🗓️ ETAPA N2: Produto Funcional
**Prazo Final:** 22/05/2026

- [ ] **Fluxo Assíncrono:** Cadastro → Fila → Worker → WebSocket → Tela atualizada.
- [ ] **Código Limpo:** Uso de Injeção de Dependência e Padrões de Projeto.
- [ ] **Qualidade:** Testes unitários com boa cobertura e análise SonarCloud.

_____________________________________________________________________________

## ⚖️ Critérios de Avaliação

### Distribuição da Pontuação:

| Critério | O que a banca busca? |
| :--- | :---: | :---: | :--- |
| **Arquitetura/Técnica** | Desacoplamento e uso de mensageria. |
| **Qualidade/Código** | Design Patterns e Clean Code. |
| **Interface/UI** | Fidelidade ao Figma e usabilidade. |
| **Testes/Qualidade** | Testes unitários e análise estática. |
| **Organização/Git** | Fluxo de branches e commits claros. |

_____________________________________________________________________________

## 🏗️ Pilares da Solução Distribuída

> [!IMPORTANT]
> **Não será aceito um "CRUD simples"**. O diferencial do semestre é a **reatividade assíncrona**.

1. **Mensageria:** Toda ação pesada ou atualização de status deve passar por uma fila (Broker).
2. **WebSocket/Streams:** O frontend deve ser "vivo", recebendo atualizações sem refresh.
3. **Resiliência:** Se o backend principal cair, as mensagens devem continuar seguras na fila.

_____________________________________________________________________________


2. **Mock-up Figma:** Começar a desenhar a jornada do Cliente e do Admin.
## 📋 Divisão de Tarefas Sugerida

| Quem | Responsabilidade Principal | Foco em Pastas |
| **Integrante 1** | **Backend Principal + API** | `src/controladores/` |
| **Integrante 2** | **Regras de Negócio + Mensageria** | `src/servicos/` |
| **Integrante 3** | **Banco de Dados** | `src/repositorios/` |
| **Integrante 4** | **Frontend + Real-time (WebSocket)** | `src/frontend/` |

_____________________________________________________________________________

> 📌 *Dica: Cada integrante cuida de uma camada diferente da arquitetura, minimizando conflitos de código. Veja o [ARQUITETURA.md](ARQUITETURA.md) para entender como as camadas se conectam.*
