# 🗄️ Camada de Repositórios (Banco de Dados)

> **Responsável:** Integrante 3
> **Foco:** Acesso ao banco de dados (PostgreSQL), queries e mapeamento ORM

## O que esta pasta precisa conter

- **Repositórios:** Um arquivo por entidade (ex: `PedidoRepository`, `UsuarioRepository`). Contêm as operações de banco: criar, buscar, atualizar, deletar.
- **Entidades/Modelos:** Definição das tabelas do banco via ORM (ex: Sequelize, TypeORM, Prisma).
- **Migrations:** Scripts de criação e alteração de tabelas.
- **Config do banco:** Conexão com o PostgreSQL.

## Regra

Repositórios são a única camada que fala com o banco de dados. Serviços e controladores nunca fazem queries diretas.

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Arquitetura/Técnica | Separação clara do acesso a dados |
| Qualidade/Código | Queries otimizadas, modelos bem definidos |
| Testes/Qualidade | Testes de integração com o banco |
