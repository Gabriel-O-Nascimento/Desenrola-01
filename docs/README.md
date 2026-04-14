# 📚 Documentação - Projeto Desenrola

## Entrega N1 - Artefatos Iniciais

Este diretório contém toda a documentação necessária para a **Etapa N1** do Projeto Integrador ADS 2026/1.

---

## 📂 Estrutura da Documentação

### 1️⃣ Design de Software

#### 1.1 Diagramas C4
- **Localização**: `diagrama-c4-contexto-container.png` (ou link do diagrama)
- **Conteúdo**: 
  - Nível 1: Diagrama de Contexto (usuários e sistemas externos)
  - Nível 2: Diagrama de Container (aplicação mobile/web, API, banco, RabbitMQ, etc.)

#### 1.2 Diagrama de Classes do Domínio
- **Arquivo**: [`diagrama-classes-dominio.md`](./diagrama-classes-dominio.md)
- **Conteúdo**:
  - Classes principais: Usuario, Cliente, Profissional, Solicitacao, Servico, Pagamento, etc.
  - Relacionamentos e cardinalidades
  - Enumerações (Status, Tipos, etc.)
  - Regras de negócio

#### 1.3 Design Patterns (Padrões de Projeto)
- **Arquivo**: [`design-patterns.md`](./design-patterns.md)
- **Conteúdo**:
  - Repository Pattern
  - Strategy Pattern (cálculo de preços)
  - Observer Pattern (mensageria)
  - Factory Pattern (notificações)
  - Dependency Injection
  - DTO Pattern
  - Singleton Pattern
  - Justificativa de cada padrão
  - Exemplos de implementação

---

### 2️⃣ Modelagem de Interfaces (UI)

#### 2.1 Protótipo de Alta Fidelidade
- **Ferramenta**: Figma
- **Link**: [Inserir link do Figma aqui]
- **Conteúdo**:
  - Telas do Cliente (login, lista de serviços, criar solicitação, chat, avaliação)
  - Telas do Profissional (dashboard, solicitações, aceitar/recusar, chat)
  - Fluxos navegáveis
  - Interações e transições

#### 2.2 Guia de Estilos (Design System)
- **Localização**: [Inserir link ou arquivo]
- **Conteúdo**:
  - Paleta de cores (primária, secundária, neutras)
  - Tipografia (fontes, tamanhos, pesos)
  - Componentes (botões, inputs, cards, modais)
  - Ícones e ilustrações
  - Espaçamentos e grid

---

### 3️⃣ Qualidade de Software

#### 3.1 Plano de Testes
- **Arquivo**: [`plano-de-testes.md`](./plano-de-testes.md)
- **Conteúdo**:
  - Estratégia de testes (pirâmide de testes)
  - Tipos de testes: Unitários, Integração, E2E, Carga
  - Cenários de teste detalhados
  - Ferramentas: JUnit 5, Mockito, Spring Test, Cypress, JMeter
  - Critérios de aceitação (75% cobertura)
  - Cronograma e responsabilidades

#### 3.2 Requisitos Não-Funcionais
- **Arquivo**: [`requisitos-nao-funcionais.md`](./requisitos-nao-funcionais.md)
- **Conteúdo**:
  - Performance (tempo de resposta < 500ms)
  - Escalabilidade (suportar 10.000 usuários simultâneos)
  - Disponibilidade (99.5% uptime)
  - Segurança (JWT, HTTPS, criptografia, LGPD)
  - Usabilidade (responsividade, acessibilidade WCAG 2.1)
  - Manutenibilidade (cobertura de testes, documentação)
  - Conformidade legal (LGPD)

---

### 4️⃣ Ambiente e Infraestrutura

#### 4.1 Repositório Git
- **URL**: https://github.com/Gabriel-O-Nascimento/Desenrola-01
- **Estrutura**:
  ```
  Desenrola-01/
  ├── src/
  │   ├── controladores/    # API REST
  │   ├── servicos/         # Lógica de negócio + RabbitMQ
  │   ├── repositorios/     # Acesso ao banco
  │   └── frontend/         # Interface React/Vue
  ├── docs/                 # Documentação
  ├── tests/                # Testes
  └── docker-compose.yml    # Infraestrutura
  ```

#### 4.2 Docker Compose
- **Arquivo**: [`../docker-compose.yml`](../docker-compose.yml)
- **Serviços**:
  - PostgreSQL (banco de dados)
  - RabbitMQ (mensageria)
  - Aplicação Backend (Spring Boot)
  - Aplicação Frontend (React/Vue)

#### 4.3 "Hello World" da Arquitetura
- **Status**: [Em desenvolvimento / Concluído]
- **Demonstração**:
  - Frontend conecta com Backend via HTTP
  - Backend publica mensagem no RabbitMQ
  - Worker consome mensagem
  - Frontend recebe atualização via WebSocket

---

## 📊 Resumo dos Artefatos

| Categoria | Artefato | Status |
|-----------|----------|--------|
| **Design de Software** | Diagrama C4 (Contexto + Container) | ✅ Concluído |
| | Diagrama de Classes do Domínio | ✅ Concluído |
| | Documentação de Design Patterns | ✅ Concluído |
| **Modelagem de UI** | Protótipo Figma (Alta Fidelidade) | ✅ Concluído |
| | Guia de Estilos (Design System) | ⚠️ Verificar |
| **Qualidade** | Plano de Testes | ✅ Concluído |
| | Requisitos Não-Funcionais | ✅ Concluído |
| **Infraestrutura** | Repositório Git Organizado | ✅ Concluído |
| | Docker Compose Configurado | ✅ Concluído |
| | Hello World (Front + Back + Fila) | ⚠️ Verificar |

---

## 🎯 Critérios de Avaliação N1

| Critério | Peso | O que será avaliado |
|----------|------|---------------------|
| **Coerência da Arquitetura** | 0,3 | O desenho da solução suporta o problema? Diagramas C4 e Classes estão consistentes? |
| **Qualidade da Interface** | 0,3 | Protótipo segue heurísticas de usabilidade? Design System está completo? |
| **Documentação** | 0,2 | Plano de testes e requisitos não-funcionais estão claros e detalhados? |
| **Organização** | 0,2 | Uso do Git está adequado? Apresentação está clara? |

---

## 📅 Cronograma

- **Entrega dos Artefatos**: 08/04/2026
- **Apresentação N1**: 15/04/2026 (Quarta-feira)
  - Duração: 5 minutos
  - Foco: Mostrar Figma e Diagramas de Arquitetura

---

## 👥 Equipe

| Integrante | Responsabilidade |
|------------|------------------|
| Integrante 1 | Controladores (API REST) |
| Integrante 2 | Serviços (Lógica + RabbitMQ) |
| Integrante 3 | Repositórios (Banco de Dados) |
| Integrante 4 | Frontend (Interface + WebSocket) |

---

## 📖 Documentos de Referência

- [Documento Norteador do Projeto Integrador](../guia_projeto_integrador.md)
- [Arquitetura do Sistema](../ARQUITETURA.md)
- [README Principal](../README.md)

---

## 📝 Notas

- Todos os diagramas foram criados seguindo as melhores práticas
- A documentação está alinhada com os requisitos da banca
- Os Design Patterns escolhidos são adequados para o escopo do projeto
- O Plano de Testes é executável e mensurável
- Os Requisitos Não-Funcionais são realistas e atingíveis

---

**Última atualização**: 13/04/2026
