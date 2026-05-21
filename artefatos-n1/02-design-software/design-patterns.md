# 🎨 Padrões de Projeto (Design Patterns) — Desenrola

## Padrões Implementados

### 1. Strategy Pattern — Notificações

**Problema:** Cada tipo de notificação (nova solicitação, orçamento recebido, serviço concluído, etc.) precisa de um título e mensagem diferentes. Sem um padrão, teríamos um `if/else` gigante dentro do `NotificacaoService`.

**Solução:** Cada tipo de notificação tem sua própria classe (estratégia) que sabe montar o título e a mensagem.

**Estrutura:**

```
NotificacaoStrategy (interface)
├── NovaSolicitacaoStrategy
├── OrcamentoRecebidoStrategy
├── SolicitacaoAceitaStrategy
├── SolicitacaoRecusadaStrategy
├── ServicoConcluidoStrategy
├── AvaliacaoRecebidaStrategy
└── SistemaStrategy
```

**Interface:**
```java
public interface NotificacaoStrategy {
    TipoNotificacao getTipo();
    String montarTitulo(ContextoNotificacao contexto);
    String montarMensagem(ContextoNotificacao contexto);
}
```

**Exemplo de implementação:**
```java
@Component
public class OrcamentoRecebidoStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.ORCAMENTO_RECEBIDO;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Orçamento recebido";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Você recebeu um orçamento no valor de R$ " + contexto.getValor();
    }
}
```

**Como o NotificacaoService usa:**
```java
@Service
public class NotificacaoService {

    private final Map<TipoNotificacao, NotificacaoStrategy> mapaEstrategias;

    @PostConstruct
    void registrarEstrategias() {
        estrategias.forEach(s -> mapaEstrategias.put(s.getTipo(), s));
    }

    public Notificacao notificar(Long idUsuario, TipoNotificacao tipo,
                                  ContextoNotificacao contexto, ...) {
        NotificacaoStrategy estrategia = mapaEstrategias.get(tipo);
        // Usa a estratégia correta automaticamente
        String titulo = estrategia.montarTitulo(contexto);
        String mensagem = estrategia.montarMensagem(contexto);
        // ...
    }
}
```

**Benefício:** Para adicionar um novo tipo de notificação, basta criar uma nova classe que implementa `NotificacaoStrategy`. Não precisa mexer no `NotificacaoService` — respeita o princípio Open/Closed (SOLID).

---

### 2. Factory Pattern — Criação de Eventos

**Problema:** Os eventos de mensageria (SolicitacaoCriadaEvento, OrcamentoEnviadoEvento, etc.) precisam ser criados em vários lugares do código, sempre com data/hora preenchida e campos validados.

**Solução:** Uma classe `EventoFactory` centraliza a criação de todos os eventos.

**Implementação:**
```java
@Component
public class EventoFactory {

    public SolicitacaoCriadaEvento criarSolicitacaoCriada(
            Long solicitacaoId, Long clienteId, String categoria,
            String descricao, String endereco) {
        SolicitacaoCriadaEvento evento = new SolicitacaoCriadaEvento();
        evento.setSolicitacaoId(solicitacaoId);
        evento.setClienteId(clienteId);
        evento.setCategoria(categoria);
        evento.setDescricao(descricao);
        evento.setEndereco(endereco);
        evento.setDataCriacao(LocalDateTime.now());  // Sempre preenchido
        return evento;
    }

    public OrcamentoEnviadoEvento criarOrcamentoEnviado(...) { ... }
    public StatusAtualizadoEvento criarStatusAtualizado(...) { ... }
    public AvaliacaoCriadaEvento criarAvaliacaoCriada(...) { ... }
}
```

**Benefício:** 
- Garante que todos os eventos são criados de forma consistente (data sempre preenchida)
- Se a estrutura do evento mudar, só altera em um lugar
- Facilita testes (pode mockar a factory)

---

### 3. Injeção de Dependência (Dependency Injection)

**Problema:** Se uma classe cria suas próprias dependências, fica impossível testar isoladamente e trocar implementações.

**Solução:** O Spring injeta as dependências automaticamente via construtor.

**Exemplo:**
```java
@Service
@RequiredArgsConstructor  // Lombok gera o construtor
public class SolicitacaoService {

    private final SolicitacaoProducer solicitacaoProducer;  // Injetado
    private final EventoFactory eventoFactory;              // Injetado

    public void criarSolicitacao(...) {
        // Usa as dependências sem precisar criá-las
    }
}
```

**Benefício:** Nos testes, podemos injetar mocks:
```java
@Mock private SolicitacaoProducer solicitacaoProducer;
@Mock private EventoFactory eventoFactory;
@InjectMocks private SolicitacaoService solicitacaoService;
```

---

### 4. Repository Pattern

**Problema:** O código de negócio não deve conhecer detalhes do banco de dados (SQL, JPA, etc.).

**Solução:** Interfaces de repositório abstraem o acesso a dados.

**Exemplo:**
```java
@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByClienteIdUsuarioOrderByCriadoEmDesc(Long idCliente);
    List<Solicitacao> findByProfissionalIdUsuarioOrderByCriadoEmDesc(Long idProfissional);
}
```

**Benefício:** O serviço chama `solicitacaoRepository.findByClienteIdUsuario(id)` sem saber se é MySQL, PostgreSQL ou MongoDB por baixo.

---

### 5. DTO (Data Transfer Object)

**Problema:** Expor a entidade JPA diretamente na API pode vazar dados sensíveis e acoplar o banco à interface.

**Solução:** DTOs controlam exatamente o que entra e sai da API.

**Exemplo:**
```java
@Data @Builder
public class CriarSolicitacaoDTO {
    private Long idCliente;
    private Integer idServico;
    private String titulo;
    private String descricao;
    private String enderecoAtendimento;
    // Não tem campos internos como criadoEm, status, etc.
}
```

---

### 6. Arquitetura em Camadas (Layered Architecture)

**Estrutura:**
```
Controladores  →  Serviços de Aplicação  →  Serviços de Domínio  →  Repositórios
     (HTTP)          (orquestração)          (mensageria)            (banco)
```

**Regra:** Cada camada só conhece a camada imediatamente abaixo. Um repositório nunca chama um controlador.

---

## Princípios SOLID Aplicados

| Princípio | Onde |
|-----------|------|
| **S** — Single Responsibility | Cada classe tem uma responsabilidade (Controller só roteia, Service só processa, Repository só persiste) |
| **O** — Open/Closed | Strategy permite adicionar novos tipos de notificação sem alterar código existente |
| **L** — Liskov Substitution | Todas as strategies são intercambiáveis via interface `NotificacaoStrategy` |
| **I** — Interface Segregation | Interfaces de repositório expõem apenas os métodos necessários |
| **D** — Dependency Inversion | Serviços dependem de abstrações (interfaces), não de implementações concretas |

---

## Resumo

| Padrão | Arquivo Principal | Propósito |
|--------|-------------------|-----------|
| Strategy | `strategy/NotificacaoStrategy.java` + `strategy/impl/` | Montar notificações por tipo |
| Factory | `factory/EventoFactory.java` | Criar eventos de mensageria |
| DI | Todo o projeto (Spring `@RequiredArgsConstructor`) | Desacoplamento |
| Repository | `repositorios/*Repository.java` | Abstração do banco |
| DTO | `controladores/dto/` | Controle de entrada/saída da API |
| Layered | Estrutura de pacotes | Separação de responsabilidades |
