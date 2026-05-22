# Documentação - Projeto Desenrola

## Entrega N1 - Artefatos Iniciais

Este diretório contém toda a documentação produzida para a **Etapa N1** do Projeto Integrador ADS 2026/1.

---

## Estrutura da Documentação

### 1. Identificação do Projeto

- **Arquivo**: [`guia_projeto_integrador.md`](./01-identificacao-projeto/guia_projeto_integrador.md)
- **Conteúdo**: Resumo do projeto, divisão de entregas e critérios de avaliação.

---

### 2. Design de Software

#### 2.1 Arquitetura do Sistema
- **Arquivo**: [`ARQUITETURA.md`](./02-design-software/ARQUITETURA.md)
- **Conteúdo**: Descrição e justificativa da arquitetura em camadas utilizada no projeto.

#### 2.2 Diagramas C4
- **Arquivo**: [`diagrama-c4.md`](./02-design-software/diagrama-c4.md)
- **Imagens**: Localizadas em [`02-design-software/imagens-c4model/`](./02-design-software/imagens-c4model/)
  - `diagrama-c4-contexto.png` — Nível 1: Diagrama de Contexto
  - `diagrama-c4-container.png` — Nível 2: Diagrama de Container
- **Links do Draw.io**: Disponíveis em [`02-design-software/link-drawio/`](./02-design-software/link-drawio/)

#### 2.3 Diagrama de Classes do Domínio
- **Arquivo**: [`diagrama-classes-dominio.md`](./02-design-software/diagrama-classes-dominio.md)
- **Conteúdo**: Classes principais, relacionamentos, cardinalidades e regras de negócio.

#### 2.4 Design Patterns
- **Arquivo**: [`design-patterns.md`](./02-design-software/design-patterns.md)
- **Conteúdo**: Strategy, Factory, Repository, Dependency Injection, DTO, Layered Architecture e princípios SOLID aplicados ao projeto.

---

### 3. Modelagem de Interfaces (UI)

#### 3.1 Protótipo de Alta Fidelidade
- **Arquivo**: [`prototipo.md`](./03-modelagem-interface/prototipo.md)
- **Ferramenta**: Figma
- **Link**: [Protótipo Navegável no Figma](https://www.figma.com/proto/ocQ7efhZH9H6kJIYypVDPS/Sem-t%C3%ADtulo?node-id=0-1&t=hvHmS5RkJzGQMfnL-1)

#### 3.2 Guia de Estilos (Design System)
- **Arquivo**: [`guia-estilo.md`](./03-modelagem-interface/guia-estilo.md)
- **Conteúdo**: Paleta de cores, tipografia (fonte Inter) e escala tipográfica.

---

### 4. Qualidade de Software

#### 4.1 Plano de Testes
- **Arquivo**: [`plano-de-testes.md`](./04-qualidade/plano-de-testes.md)
- **Conteúdo**: Estratégia de testes, cenários detalhados e ferramentas utilizadas.

#### 4.2 Requisitos Não-Funcionais
- **Arquivo**: [`requisitos-nao-funcionais.md`](./04-qualidade/requisitos-nao-funcionais.md)
- **Conteúdo**: Performance, escalabilidade, segurança, usabilidade e conformidade legal.

#### 4.3 Relatório de Testes
- **Arquivo**: [`relatorio-testes.md`](./04-qualidade/relatorio-testes.md)
- **Conteúdo**: Resumo da execução dos testes unitários, cobertura por pacote (JaCoCo) e cenários cobertos.

---

### 5. Web/Mensageria

#### 5.1 Repositório Git
- **Arquivo**: [`repositorio-git.md`](./05-mensageria/repositorio-git.md)
- **URL**: https://github.com/Gabriel-O-Nascimento/Desenrola-01

#### 5.2 Estrutura Inicial (Docker + Integração)
- **Arquivo**: [`estrutura-inicial.md`](./05-mensageria/estrutura-inicial.md)
- **Conteúdo**: Configuração do Docker (RabbitMQ e MySQL) e descrição do fluxo de mensageria entre frontend e backend.

---

## Estrutura de Pastas

```
artefatos-n1/
├── 01-identificacao-projeto/
│   └── guia_projeto_integrador.md
├── 02-design-software/
│   ├── ARQUITETURA.md
│   ├── design-patterns.md
│   ├── diagrama-c4.md
│   ├── diagrama-classes-dominio.md
│   ├── imagens-c4model/
│   │   ├── diagrama-c4-contexto.png
│   │   └── diagrama-c4-container.png
│   └── link-drawio/
│       ├── c4model.txt
│       └── c4model2.txt
├── 03-modelagem-interface/
│   ├── guia-estilo.md
│   └── prototipo.md
├── 04-qualidade/
│   ├── plano-de-testes.md
│   ├── relatorio-testes.md
│   └── requisitos-nao-funcionais.md
├── 05-mensageria/
│   ├── estrutura-inicial.md
│   └── repositorio-git.md
└── README.md
```

---

## Resumo dos Artefatos

| Categoria | Artefato | Status |
|-----------|----------|--------|
| **Identificação** | Guia do Projeto Integrador | Concluído |
| **Design de Software** | Arquitetura do Sistema | Concluído |
| | Diagrama C4 (Contexto + Container) | Concluído |
| | Diagrama de Classes do Domínio | Concluído |
| | Design Patterns | Concluído |
| **Modelagem de UI** | Protótipo Figma (Alta Fidelidade) | Concluído |
| | Guia de Estilos (Design System) | Concluído |
| **Qualidade** | Plano de Testes | Concluído |
| | Requisitos Não-Funcionais | Concluído |
| | Relatório de Testes (N2) | Concluído |
| **Mensageria** | Repositório Git | Concluído |
| | Docker Compose + Integração | Concluído |

---

## Documentos de Referência

- [README Principal do Projeto](../README.md)
- [Docker Compose](../docker-compose.yml)
