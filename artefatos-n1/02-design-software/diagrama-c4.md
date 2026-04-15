# Diagrama C4 (Contexto e Container)

Este documento guarda os diagramas arquiteturais do projeto **Desenrola**, utilizando o modelo C4 para documentar o design de software construído pela equipe no Draw.io.

## Nível 1: Diagrama de Contexto

O Diagrama de Contexto mostra o sistema Desenrola no centro, rodeado por seus usuários (Clientes, Profissionais, Administradores) e os sistemas externos com os quais ele interage (Gateways de pagamento, Provedores de Email, etc.).

> **Visualize a Imagem Original abaixo:**

![Diagrama de Contexto C4 - Nível 1](./diagrama-c4-contexto.png)

---

## Nível 2: Diagrama de Container

O Diagrama de Container dá um "zoom" no sistema Desenrola, mostrando os containers (aplicativos e bancos de dados) que fazem o sistema funcionar (ex: Frontend App, Backend API Spring Boot, PostgreSQL, RabbitMQ).

> **Visualize a Imagem Original abaixo:**

![Diagrama de Container C4 - Nível 2](./diagrama-c4-container.png)

---

## Justificativa Arquitetural

Para entender o porquê destas escolhas e como essas peças se integram em tempo real, consulte também os arquivos:
[ARQUITETURA.md](../ARQUITETURA.md) e [Design Patterns](./design-patterns.md)
