# Camada de Repositórios (Banco de Dados)

> **Responsável:** Integrante 3
> **Foco:** Acesso ao banco de dados (PostgreSQL), queries e mapeamento ORM

## Disciplinas Envolvidas

- **Desenvolvimento de Software Web** - Implementação da camada de dados
- **Design de Software** - Aplicação do padrão Repository
- **Qualidade de Software** - Testes de integração com o banco

## O que você precisa criar

### Repositórios principais do Desenrola:
- `UsuarioRepository` - Criar, buscar e atualizar usuários (base para Cliente e Profissional)
- `ClienteRepository` - Operações específicas de clientes
- `ProfissionalRepository` - Operações específicas de profissionais (buscar por categoria, disponibilidade)
- `SolicitacaoRepository` - Criar, buscar e atualizar solicitações
- `ServicoRepository` - Listar serviços disponíveis (categorias)
- `AvaliacaoRepository` - Salvar e buscar avaliações

### Entidades/Modelos principais:
```
Usuario (base)
  ├── Cliente (herda de Usuario)
  └── Profissional (herda de Usuario)

Solicitacao
  ├── relaciona com Cliente
  ├── relaciona com Profissional
  ├── relaciona com Servico
  └── pode ter Avaliacao e Chat

Avaliacao
Chat
Servico
```

### Também precisa:
- **Migrations:** Scripts para criar tabelas no banco
- **Config do banco:** Conexão com PostgreSQL (já está no docker-compose.yml)

## Regra importante

Repositórios são a **única camada** que fala com o banco de dados. Serviços e controladores **nunca** fazem queries diretas.

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Arquitetura/Técnica | Separação clara do acesso a dados |
| Qualidade/Código | Queries otimizadas, modelos bem definidos |
| Testes/Qualidade | Testes de integração com o banco |
