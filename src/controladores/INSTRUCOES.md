# Camada de Controladores (API REST)

> **Responsável:** Integrante 1
> **Foco:** Receber requisições HTTP e devolver respostas

## Disciplinas Envolvidas

- **Desenvolvimento de Software Web** - Implementação da API REST
- **Design de Software** - Aplicação de Design Patterns (DTO, Dependency Injection)
- **Qualidade de Software** - Testes de integração dos endpoints

## O que você precisa criar

### Controladores principais do Desenrola:
- `SolicitacaoController` - Criar, listar e gerenciar solicitações de serviço
- `ProfissionalController` - Buscar profissionais, ver perfil e avaliações
- `ClienteController` - Gerenciar dados do cliente
- `AvaliacaoController` - Criar e listar avaliações de serviços

### Exemplos de endpoints:
```
POST   /api/solicitacoes          - Cliente cria solicitação
GET    /api/solicitacoes/:id      - Ver detalhes da solicitação
GET    /api/profissionais         - Buscar profissionais disponíveis
POST   /api/avaliacoes            - Cliente avalia serviço
```

### Também precisa:
- **DTOs:** Objetos que definem formato de entrada/saída (ex: `CriarSolicitacaoDTO`)
- **Validações:** Verificar se dados estão corretos antes de processar

## Regra importante

Controladores **NÃO** têm lógica de negócio. Eles apenas:
1. Recebem a requisição
2. Validam os dados
3. Chamam o serviço correspondente
4. Devolvem a resposta

## Critérios de avaliação

| Critério | Contribuição |
|----------|-------------|
| Arquitetura/Técnica | API bem estruturada, separação clara |
| Qualidade/Código | Clean Code, sem lógica de negócio aqui |
| Testes/Qualidade | Testes de integração dos endpoints |
