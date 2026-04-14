# 🎨 Padrões de Projeto (Design Patterns) - Desenrola

## O que são Design Patterns?

Design Patterns são **soluções prontas para problemas comuns** que aparecem quando estamos programando. É como ter uma "receita de bolo" testada e aprovada para resolver situações específicas no código.

---

## Por que usar?

- ✅ Código mais organizado e fácil de entender
- ✅ Facilita trabalhar em equipe (todo mundo entende o padrão)
- ✅ Menos bugs
- ✅ Mais fácil de testar
- ✅ Professores vão reconhecer que você sabe o que está fazendo

---

## Padrões que vamos usar no Desenrola

### 1. Separação em Camadas (Layered Architecture)

**O que é**: Dividir o código em "andares", cada um com sua responsabilidade.

**Como funciona no nosso projeto**:
```
Frontend (Telas)
    ↓
Controladores (Recebe requisições HTTP)
    ↓
Serviços (Regras de negócio)
    ↓
Repositórios (Banco de dados)
```

**Por que usar**: Cada pessoa do grupo cuida de uma camada, menos conflitos no Git.

---

### 2. Repository (Repositório)

**O que é**: Uma "gaveta" onde guardamos e buscamos dados do banco.

**Exemplo prático**:
```java
// Ao invés de escrever SQL toda hora:
SELECT * FROM solicitacoes WHERE cliente_id = ?

// Usamos:
solicitacaoRepository.buscarPorCliente(clienteId);
```

**Por que usar**: Se mudarmos de PostgreSQL para MongoDB, só mexemos no repositório, o resto do código continua igual.

---

### 3. Mensageria (Filas)

**O que é**: Ao invés de fazer tudo na hora, colocamos tarefas em uma "fila" para serem processadas depois.

**Exemplo prático**:
```
Cliente cria solicitação
    ↓
Sistema coloca na fila do RabbitMQ
    ↓
Cliente recebe resposta rápida: "Solicitação criada!"
    ↓
(Em segundo plano) Sistema notifica profissionais
```

**Por que usar**: 
- Cliente não fica esperando
- Se o sistema cair, as mensagens não se perdem
- Atende o requisito da banca (mensageria obrigatória)

---

### 4. Injeção de Dependência (Dependency Injection)

**O que é**: Ao invés de criar objetos dentro da classe, recebemos eles prontos.

**Exemplo prático**:
```java
// ❌ Ruim (criando dentro)
public class SolicitacaoService {
    private SolicitacaoRepository repo = new SolicitacaoRepository();
}

// ✅ Bom (recebendo pronto)
public class SolicitacaoService {
    private SolicitacaoRepository repo;
    
    public SolicitacaoService(SolicitacaoRepository repo) {
        this.repo = repo;
    }
}
```

**Por que usar**: Facilita testes (podemos passar um repositório fake).

---

### 5. DTO (Data Transfer Object)

**O que é**: Objetos simples só para transportar dados entre camadas.

**Exemplo prático**:
```java
// Cliente envia isso:
{
  "servicoId": "123",
  "endereco": "Rua X",
  "dataHora": "2026-04-15T10:00:00"
}

// Vira um DTO:
public class CriarSolicitacaoDTO {
    private String servicoId;
    private String endereco;
    private LocalDateTime dataHora;
}
```

**Por que usar**: 
- Não expõe dados sensíveis (senha, etc.)
- Controla exatamente o que entra e sai da API

---

### 6. Strategy (Estratégia)

**O que é**: Ter várias formas de fazer a mesma coisa e escolher qual usar.

**Exemplo prático no Desenrola**:
```
Calcular preço do serviço:
- Preço fixo: R$ 50,00
- Preço por hora: R$ 30,00 x 3 horas = R$ 90,00
- Preço com desconto: R$ 50,00 - 10% = R$ 45,00
```

**Por que usar**: Fácil adicionar novos tipos de cálculo sem mexer no código existente.

---

## Resumo Visual

| Padrão | Onde Usar | Benefício Principal |
|--------|-----------|---------------------|
| **Camadas** | Estrutura geral | Organização e divisão de trabalho |
| **Repository** | Acesso ao banco | Facilita trocar banco de dados |
| **Mensageria** | Tarefas assíncronas | Sistema mais rápido e resiliente |
| **Injeção de Dependência** | Todas as classes | Facilita testes |
| **DTO** | API (entrada/saída) | Segurança e controle |
| **Strategy** | Cálculo de preços | Flexibilidade |

---

## Conclusão

Os padrões de projeto selecionados para o sistema Desenrola foram escolhidos com base nas necessidades específicas do projeto e nas melhores práticas de desenvolvimento de software. A implementação destes padrões será realizada na fase N2, garantindo um código limpo, testável e de fácil manutenção.
