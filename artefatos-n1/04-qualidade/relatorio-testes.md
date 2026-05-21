# 📊 Relatório de Testes e Cobertura de Código

## Resumo da Execução

| Métrica | Valor |
|---------|-------|
| **Total de testes** | 48 |
| **Testes passando** | 48 |
| **Falhas** | 0 |
| **Erros** | 0 |
| **Cobertura geral** | 67% |

---

## Ferramenta de Cobertura

Utilizamos o **JaCoCo (Java Code Coverage)** integrado ao Maven como plugin. Ele instrumenta o bytecode durante a execução dos testes e gera um relatório HTML mostrando quais linhas foram exercitadas.

### Como gerar o relatório

```bash
cd backend
mvn test
```

O relatório HTML é gerado automaticamente em:
```
backend/target/site/jacoco/index.html
```

Basta abrir esse arquivo no navegador para visualizar a cobertura detalhada por pacote, classe e método.

---

## Cobertura por Pacote

| Pacote | Cobertura | Descrição |
|--------|-----------|-----------|
| `com.desenrola.servicos` | **100%** | Services de domínio (SolicitacaoService, OrcamentoService, AvaliacaoService, StatusService, NotificacaoService) |
| `com.desenrola.servicos.factory` | **100%** | EventoFactory — criação de eventos de mensageria |
| `com.desenrola.servicos.strategy.impl` | **80%** | Strategies de notificação (7 implementações) |
| `com.desenrola.servicos.aplicacao` | **71%** | AppServices — orquestração de casos de uso |
| `com.desenrola.controladores` | **68%** | Controllers REST |
| `com.desenrola.servicos.producers` | **50%** | Producers RabbitMQ |
| `com.desenrola.servicos.consumers` | **21%** | Consumers RabbitMQ |

> **Nota:** Entidades JPA, DTOs, eventos e configurações foram excluídos da análise de cobertura por serem classes de dados sem lógica de negócio.

---

## Testes por Categoria

### Testes de Controllers (13 testes)
| Classe de Teste | Testes | Status |
|----------------|--------|--------|
| `SolicitacaoControllerTest` | 6 | ✅ |
| `ProfissionalControllerTest` | 3 | ✅ |
| `AvaliacaoControllerTest` | 2 | ✅ |
| `ClienteControllerTest` | 1 | ✅ |
| `OrcamentoControllerTest` | 1 | ✅ |

### Testes de Services de Aplicação (15 testes)
| Classe de Teste | Testes | Status |
|----------------|--------|--------|
| `SolicitacaoAppServiceTest` | 5 | ✅ |
| `AvaliacaoAppServiceTest` | 4 | ✅ |
| `ClienteAppServiceTest` | 3 | ✅ |
| `OrcamentoAppServiceTest` | 3 | ✅ |

### Testes de Services de Domínio (4 testes)
| Classe de Teste | Testes | Status |
|----------------|--------|--------|
| `SolicitacaoServiceTest` | 1 | ✅ |
| `OrcamentoServiceTest` | 1 | ✅ |
| `AvaliacaoServiceTest` | 1 | ✅ |
| `StatusServiceTest` | 1 | ✅ |

### Testes de Mensageria (3 testes)
| Classe de Teste | Testes | Status |
|----------------|--------|--------|
| `SolicitacaoConsumerTest` | 1 | ✅ |
| `OrcamentoProducerTest` | 1 | ✅ |
| `SolicitacaoProducerTest` | 1 | ✅ |

### Testes de Design Patterns (13 testes)
| Classe de Teste | Testes | Status |
|----------------|--------|--------|
| `StrategiesTest` | 8 | ✅ |
| `EventoFactoryTest` | 4 | ✅ |
| `NotificacaoServiceTest` | 1 | ✅ |

---

## Cenários Testados

### Fluxo de Solicitação
- ✅ Criar solicitação com sucesso (salva no banco + publica evento no RabbitMQ)
- ✅ Erro quando cliente não encontrado
- ✅ Atualizar status com sucesso (publica evento de status)
- ✅ Buscar solicitação por ID
- ✅ Listar solicitações por cliente
- ✅ Listar solicitações por profissional
- ✅ Retornar bad request quando nenhum filtro informado

### Fluxo de Orçamento
- ✅ Criar orçamento com sucesso (calcula total + atualiza status da solicitação + publica evento)
- ✅ Erro quando solicitação não encontrada
- ✅ Cálculo correto com valores nulos (trata como zero)

### Fluxo de Avaliação
- ✅ Criar avaliação com sucesso (salva + publica evento)
- ✅ Erro quando nota inválida (fora de 1-5)
- ✅ Erro quando nota nula
- ✅ Erro quando solicitação já foi avaliada

### Fluxo de Cadastro
- ✅ Cadastrar cliente com sucesso (cria usuário + cliente)
- ✅ Erro quando email já existe
- ✅ Erro quando CPF já existe
- ✅ Cadastrar profissional com sucesso

### Design Patterns
- ✅ Factory cria eventos com data preenchida automaticamente
- ✅ Cada Strategy monta título e mensagem corretos para seu tipo
- ✅ NotificacaoService seleciona a strategy correta pelo tipo

---

## Tecnologias de Teste

| Ferramenta | Uso |
|-----------|-----|
| **JUnit 5** | Framework de testes |
| **Mockito** | Mocks e verificações |
| **AssertJ** | Assertions fluentes |
| **Spring MockMvc** | Testes de controllers sem subir servidor |
| **JaCoCo** | Cobertura de código |

---

## Como Reproduzir

```bash
# 1. Entrar na pasta do backend
cd backend

# 2. Rodar todos os testes
mvn test

# 3. Ver relatório de cobertura (abrir no navegador)
open target/site/jacoco/index.html
```

Resultado esperado no terminal:
```
[INFO] Tests run: 48, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
