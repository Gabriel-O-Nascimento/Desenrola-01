# 🌐 Camada de Controladores (API REST)

> **Responsável:** Integrante 1
> **Foco:** Receber requisições HTTP e devolver respostas

## O que esta pasta precisa conter

- **Controladores:** Um arquivo por recurso (ex: `PedidoController`, `UsuarioController`). Recebem a requisição, validam os dados de entrada e chamam o serviço correto.
- **Rotas:** Definição dos endpoints (GET, POST, PUT, DELETE).
- **DTOs:** Objetos que definem o formato dos dados de entrada e saída da API.
- **Validações:** Schemas de validação dos dados recebidos.

## Regra

Controladores não têm lógica de negócio. Eles apenas:
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
