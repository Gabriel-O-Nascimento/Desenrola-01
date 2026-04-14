# 🧪 Plano de Testes - Desenrola

## 1. Introdução

### 1.1 Objetivo
Este documento define a estratégia de testes para o sistema Desenrola, garantindo a qualidade e confiabilidade da aplicação de entrega e solicitação de serviços.

### 1.2 Escopo
O plano cobre testes em todas as camadas do sistema:
- Frontend (React/Vue)
- API REST (Controladores)
- Lógica de Negócio (Serviços)
- Acesso a Dados (Repositórios)
- Mensageria (RabbitMQ)
- Integração entre componentes

---

## 2. Estratégia de Testes

### 2.1 Pirâmide de Testes

```
           /\
          /  \
         / E2E \          ← Poucos testes (10%)
        /--------\
       /          \
      / Integração \      ← Testes moderados (30%)
     /--------------\
    /                \
   /   Unitários      \   ← Muitos testes (60%)
  /____________________\
```

### 2.2 Tipos de Testes

| Tipo | Objetivo | Ferramentas | Cobertura Alvo |
|------|----------|-------------|----------------|
| **Unitários** | Testar funções/métodos isoladamente | JUnit 5, Mockito | 80% |
| **Integração** | Testar interação entre componentes | Spring Test, Testcontainers | 70% |
| **E2E** | Testar fluxos completos do usuário | Cypress, Selenium | Fluxos críticos |
| **Carga** | Testar performance sob stress | JMeter, K6 | Endpoints principais |

---

## 3. Cenários de Teste

### 3.1 Testes Unitários

#### 3.1.1 Camada de Serviços

**SolicitacaoService**
- ✅ Deve criar uma solicitação válida
- ✅ Deve lançar exceção ao criar solicitação sem serviço
- ✅ Deve calcular preço corretamente usando Strategy
- ✅ Deve publicar evento na fila após criação
- ✅ Deve atualizar status da solicitação
- ✅ Deve cancelar solicitação se status permitir
- ✅ Não deve cancelar solicitação já concluída

**UsuarioService**
- ✅ Deve autenticar usuário com credenciais válidas
- ✅ Deve rejeitar autenticação com senha incorreta
- ✅ Deve criar novo usuário com dados válidos
- ✅ Deve validar email único no cadastro
- ✅ Deve criptografar senha antes de salvar

**PagamentoService**
- ✅ Deve processar pagamento com cartão válido
- ✅ Deve rejeitar pagamento com cartão inválido
- ✅ Deve estornar pagamento quando solicitado
- ✅ Deve atualizar status da solicitação após pagamento

#### 3.1.2 Camada de Repositórios

**SolicitacaoRepository**
- ✅ Deve salvar solicitação no banco
- ✅ Deve buscar solicitação por ID
- ✅ Deve retornar lista de solicitações por cliente
- ✅ Deve filtrar solicitações por status
- ✅ Deve retornar lista vazia quando não houver resultados

#### 3.1.3 Estratégias de Cálculo

**PrecoFixoStrategy**
- ✅ Deve retornar preço base do serviço

**PrecoPorHoraStrategy**
- ✅ Deve calcular preço multiplicando horas pelo valor/hora
- ✅ Deve arredondar para 2 casas decimais

---

### 3.2 Testes de Integração

#### 3.2.1 API REST

**POST /api/solicitacoes**
- ✅ Deve criar solicitação e retornar 201 Created
- ✅ Deve retornar 400 Bad Request com dados inválidos
- ✅ Deve retornar 401 Unauthorized sem autenticação
- ✅ Deve publicar mensagem na fila RabbitMQ

**GET /api/solicitacoes/{id}**
- ✅ Deve retornar solicitação existente com 200 OK
- ✅ Deve retornar 404 Not Found para ID inexistente
- ✅ Deve retornar 403 Forbidden se não for dono da solicitação

**PUT /api/solicitacoes/{id}/status**
- ✅ Deve atualizar status e retornar 200 OK
- ✅ Deve validar transições de status permitidas
- ✅ Deve enviar notificação ao atualizar status

**POST /api/auth/login**
- ✅ Deve retornar token JWT com credenciais válidas
- ✅ Deve retornar 401 com credenciais inválidas

#### 3.2.2 Mensageria (RabbitMQ)

**Fila: solicitacao.criada**
- ✅ Deve consumir mensagem e notificar profissionais
- ✅ Deve reprocessar mensagem em caso de falha
- ✅ Deve enviar para DLQ após 3 tentativas

**Fila: pagamento.confirmado**
- ✅ Deve atualizar status da solicitação
- ✅ Deve enviar notificação ao cliente

**Fila: servico.concluido**
- ✅ Deve solicitar avaliação ao cliente
- ✅ Deve liberar pagamento ao profissional

#### 3.2.3 Banco de Dados

- ✅ Deve persistir dados corretamente no PostgreSQL
- ✅ Deve respeitar constraints de integridade referencial
- ✅ Deve executar transações com rollback em caso de erro
- ✅ Deve aplicar migrations corretamente

#### 3.2.4 WebSocket

- ✅ Deve estabelecer conexão WebSocket
- ✅ Deve enviar mensagem de chat em tempo real
- ✅ Deve notificar atualização de status em tempo real
- ✅ Deve desconectar corretamente ao fechar sessão

---

### 3.3 Testes End-to-End (E2E)

#### Fluxo 1: Cliente solicita serviço
1. Cliente faz login
2. Cliente navega para lista de serviços
3. Cliente seleciona um serviço
4. Cliente preenche formulário de solicitação
5. Sistema cria solicitação
6. Sistema notifica profissionais disponíveis
7. Cliente visualiza solicitação pendente

#### Fluxo 2: Profissional aceita solicitação
1. Profissional faz login
2. Profissional recebe notificação de nova solicitação
3. Profissional visualiza detalhes
4. Profissional aceita solicitação
5. Sistema ativa chat entre cliente e profissional
6. Cliente recebe notificação de aceitação

#### Fluxo 3: Pagamento e conclusão
1. Cliente confirma início do serviço
2. Sistema processa pagamento
3. Profissional marca serviço como concluído
4. Sistema solicita avaliação ao cliente
5. Cliente avalia o serviço
6. Sistema libera pagamento ao profissional

---

### 3.4 Testes de Carga

#### Cenário 1: Criação de solicitações
- **Objetivo**: Verificar quantas solicitações/segundo o sistema suporta
- **Usuários simultâneos**: 100, 500, 1000
- **Duração**: 5 minutos
- **Métrica de sucesso**: 95% das requisições < 2 segundos

#### Cenário 2: Processamento de mensagens
- **Objetivo**: Verificar throughput do RabbitMQ
- **Mensagens/segundo**: 100, 500, 1000
- **Duração**: 10 minutos
- **Métrica de sucesso**: Todas as mensagens processadas sem perda

#### Cenário 3: WebSocket simultâneos
- **Objetivo**: Verificar quantas conexões simultâneas o sistema suporta
- **Conexões**: 500, 1000, 2000
- **Duração**: 5 minutos
- **Métrica de sucesso**: Todas as mensagens entregues em < 1 segundo

---

## 4. Ambiente de Testes

### 4.1 Ambientes

| Ambiente | Propósito | Dados |
|----------|-----------|-------|
| **Local** | Desenvolvimento e testes unitários | Mock/Fake |
| **CI/CD** | Testes automatizados no pipeline | Testcontainers |
| **Staging** | Testes de integração e E2E | Dados de teste |
| **Produção** | Smoke tests após deploy | Dados reais |

### 4.2 Ferramentas

- **JUnit 5**: Framework de testes unitários (Java)
- **Mockito**: Mock de dependências
- **Spring Test**: Testes de integração Spring Boot
- **Testcontainers**: Containers Docker para testes
- **Cypress**: Testes E2E do frontend
- **JMeter**: Testes de carga
- **SonarQube**: Análise de cobertura de código
- **GitHub Actions**: CI/CD pipeline

---

## 5. Critérios de Aceitação

### 5.1 Cobertura de Código

| Camada | Cobertura Mínima |
|--------|------------------|
| Serviços | 80% |
| Repositórios | 70% |
| Controladores | 75% |
| **Geral** | **75%** |

### 5.2 Performance

| Métrica | Valor Esperado |
|---------|----------------|
| Tempo de resposta API (p95) | < 500ms |
| Tempo de resposta API (p99) | < 1s |
| Throughput RabbitMQ | > 500 msg/s |
| Latência WebSocket | < 100ms |

### 5.3 Qualidade

- ✅ Zero bugs críticos
- ✅ Máximo 5 bugs médios
- ✅ Code smells < 50 (SonarQube)
- ✅ Duplicação de código < 3%
- ✅ Complexidade ciclomática < 10

---

## 6. Cronograma de Testes

### Fase N1 (até 08/04/2026)
- [x] Definir plano de testes
- [ ] Configurar ambiente de testes
- [ ] Criar estrutura de testes unitários

### Fase N2 (até 22/05/2026)
- [ ] Implementar testes unitários (80% cobertura)
- [ ] Implementar testes de integração
- [ ] Implementar testes E2E dos fluxos críticos
- [ ] Executar testes de carga
- [ ] Gerar relatório de cobertura

---

## 7. Responsabilidades

| Integrante | Responsabilidade |
|------------|------------------|
| **Integrante 1** | Testes dos Controladores |
| **Integrante 2** | Testes dos Serviços e Mensageria |
| **Integrante 3** | Testes dos Repositórios |
| **Integrante 4** | Testes E2E do Frontend |
| **Todos** | Code review e testes de integração |

---

## 8. Relatórios

### 8.1 Relatório de Execução
Será gerado automaticamente pelo CI/CD contendo:
- Total de testes executados
- Testes aprovados/reprovados
- Cobertura de código
- Tempo de execução
- Bugs encontrados

### 8.2 Relatório de Cobertura
Gerado pelo SonarQube com:
- Cobertura por camada
- Linhas não cobertas
- Branches não testados
- Code smells identificados

---

## 9. Gestão de Defeitos

### 9.1 Severidade

| Nível | Descrição | Prazo de Correção |
|-------|-----------|-------------------|
| **Crítico** | Sistema não funciona | Imediato |
| **Alto** | Funcionalidade principal quebrada | 1 dia |
| **Médio** | Funcionalidade secundária com problema | 3 dias |
| **Baixo** | Problema cosmético ou de UX | 1 semana |

### 9.2 Fluxo de Correção
1. Bug identificado → Criar issue no GitHub
2. Classificar severidade
3. Atribuir responsável
4. Corrigir e criar teste que reproduz o bug
5. Code review
6. Merge e deploy

---

## 10. Conclusão

Este plano de testes garante que o sistema Desenrola seja entregue com qualidade, cobrindo todos os aspectos críticos da aplicação através de uma estratégia abrangente de testes automatizados e manuais.

A execução rigorosa deste plano resultará em:
- ✅ Software confiável e estável
- ✅ Menos bugs em produção
- ✅ Facilidade de manutenção
- ✅ Confiança para refatorações
- ✅ Documentação viva do comportamento esperado
