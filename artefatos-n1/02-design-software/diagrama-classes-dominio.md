# 📊 Diagrama de Classes do Domínio - Desenrola

## Visão Geral

Este diagrama representa as principais entidades do domínio do sistema Desenrola e seus relacionamentos.

## Diagrama de Classes

```mermaid
classDiagram
    class Usuario {
        +String id
        +String nome
        +String email
        +String telefone
        +String senha
        +TipoUsuario tipo
        +DateTime dataCadastro
        +Boolean ativo
        +autenticar()
        +atualizarPerfil()
    }

    class Cliente {
        +String endereco
        +String cpf
        +List~Solicitacao~ solicitacoes
        +criarSolicitacao()
        +avaliarServico()
        +consultarHistorico()
    }

    class Profissional {
        +String cnpj
        +String especialidade
        +Float avaliacao
        +Boolean disponivel
        +List~Servico~ servicosOferecidos
        +aceitarSolicitacao()
        +recusarSolicitacao()
        +atualizarDisponibilidade()
    }

    class Solicitacao {
        +String id
        +String clienteId
        +String profissionalId
        +String servicoId
        +StatusSolicitacao status
        +DateTime dataHora
        +String enderecoAtendimento
        +Float valorTotal
        +String observacoes
        +criar()
        +atualizar()
        +cancelar()
        +finalizarAtendimento()
    }

    class Servico {
        +String id
        +String nome
        +String descricao
        +CategoriaServico categoria
        +Float precoBase
        +Integer tempoEstimado
        +Boolean ativo
        +calcularPreco()
    }

    class Pagamento {
        +String id
        +String solicitacaoId
        +Float valor
        +MetodoPagamento metodo
        +StatusPagamento status
        +DateTime dataProcessamento
        +String transacaoId
        +processar()
        +confirmar()
        +estornar()
    }

    class Avaliacao {
        +String id
        +String solicitacaoId
        +String clienteId
        +String profissionalId
        +Integer nota
        +String comentario
        +DateTime data
        +criar()
        +editar()
    }

    class Notificacao {
        +String id
        +String usuarioId
        +TipoNotificacao tipo
        +String mensagem
        +Boolean lida
        +DateTime dataEnvio
        +enviar()
        +marcarComoLida()
    }

    class Chat {
        +String id
        +String solicitacaoId
        +List~Mensagem~ mensagens
        +enviarMensagem()
        +obterHistorico()
    }

    class Mensagem {
        +String id
        +String remetenteId
        +String conteudo
        +DateTime dataHora
        +Boolean lida
    }

    %% Relacionamentos
    Usuario <|-- Cliente : herda
    Usuario <|-- Profissional : herda
    
    Cliente "1" --> "0..*" Solicitacao : cria
    Profissional "1" --> "0..*" Solicitacao : atende
    Profissional "1" --> "0..*" Servico : oferece
    
    Solicitacao "1" --> "1" Servico : referencia
    Solicitacao "1" --> "0..1" Pagamento : possui
    Solicitacao "1" --> "0..1" Avaliacao : recebe
    Solicitacao "1" --> "0..1" Chat : possui
    
    Chat "1" --> "0..*" Mensagem : contém
    
    Usuario "1" --> "0..*" Notificacao : recebe

    %% Enumerações
    class TipoUsuario {
        <<enumeration>>
        CLIENTE
        PROFISSIONAL
        ADMIN
    }

    class StatusSolicitacao {
        <<enumeration>>
        PENDENTE
        ACEITA
        EM_ANDAMENTO
        CONCLUIDA
        CANCELADA
    }

    class CategoriaServico {
        <<enumeration>>
        LIMPEZA
        MANUTENCAO
        BELEZA
        TRANSPORTE
        OUTROS
    }

    class MetodoPagamento {
        <<enumeration>>
        CARTAO_CREDITO
        CARTAO_DEBITO
        PIX
        DINHEIRO
    }

    class StatusPagamento {
        <<enumeration>>
        PENDENTE
        PROCESSANDO
        APROVADO
        RECUSADO
        ESTORNADO
    }

    class TipoNotificacao {
        <<enumeration>>
        NOVA_SOLICITACAO
        SOLICITACAO_ACEITA
        SOLICITACAO_RECUSADA
        PAGAMENTO_CONFIRMADO
        AVALIACAO_RECEBIDA
    }
```

## Descrição das Classes Principais

### Usuario (Classe Base)
Classe abstrata que representa qualquer usuário do sistema. Contém atributos e métodos comuns a todos os tipos de usuário.

### Cliente
Herda de Usuario. Representa os usuários que solicitam serviços. Pode criar solicitações, avaliar profissionais e consultar histórico.

### Profissional
Herda de Usuario. Representa os prestadores de serviço. Pode aceitar/recusar solicitações e gerenciar sua disponibilidade.

### Solicitacao
Entidade central do sistema. Representa uma requisição de serviço feita por um cliente e que será atendida por um profissional.

### Servico
Representa os tipos de serviços oferecidos na plataforma (limpeza, manutenção, etc.).

### Pagamento
Gerencia as transações financeiras relacionadas às solicitações.

### Avaliacao
Permite que clientes avaliem os serviços prestados pelos profissionais.

### Chat e Mensagem
Implementam o sistema de comunicação em tempo real entre cliente e profissional.

### Notificacao
Gerencia as notificações enviadas aos usuários sobre eventos importantes.

## Regras de Negócio Principais

1. **Solicitação**: Um cliente só pode criar uma solicitação se estiver autenticado
2. **Aceitação**: Um profissional só pode aceitar solicitações se estiver disponível
3. **Pagamento**: O pagamento só pode ser processado após a solicitação ser aceita
4. **Avaliação**: Só pode ser criada após a conclusão do serviço
5. **Chat**: Só é ativado após a solicitação ser aceita pelo profissional

## Padrões Aplicados

- **Herança**: Usuario → Cliente/Profissional
- **Composição**: Solicitacao contém Pagamento, Avaliacao, Chat
- **Agregação**: Chat contém Mensagens
- **Enumerações**: Para garantir valores válidos em status e tipos
