# Requisitos Não-Funcionais - Desenrola

## 1. Introdução

Os Requisitos Não-Funcionais (RNFs) definem as características de qualidade do sistema Desenrola, especificando como o sistema deve se comportar em termos de performance, segurança, usabilidade e outros atributos de qualidade.

---

## 2. Performance

### 2.1 Tempo de Resposta

| Operação | Tempo Máximo | Justificativa |
|----------|--------------|---------------|
| Login de usuário | 1 segundo | Experiência fluida de autenticação |
| Listagem de serviços | 500ms | Navegação rápida |
| Criação de solicitação | 2 segundos | Inclui validações e publicação na fila |
| Busca de solicitações | 1 segundo | Consulta com filtros |
| Envio de mensagem (chat) | 200ms | Comunicação em tempo real |
| Atualização via WebSocket | 100ms | Notificações instantâneas |

### 2.2 Throughput

- **API REST**: Suportar mínimo de 500 requisições/segundo
- **RabbitMQ**: Processar mínimo de 1000 mensagens/segundo
- **WebSocket**: Suportar 2000 conexões simultâneas

### 2.3 Latência

- **P50 (mediana)**: < 200ms
- **P95**: < 500ms
- **P99**: < 1 segundo

---

## 3. Escalabilidade

### 3.1 Escalabilidade Horizontal

- Sistema deve suportar adição de novos servidores sem downtime
- Balanceamento de carga entre múltiplas instâncias da API
- Workers de mensageria devem poder ser escalados independentemente

### 3.2 Capacidade

| Recurso | Capacidade Inicial | Capacidade Alvo (6 meses) |
|---------|-------------------|---------------------------|
| Usuários ativos simultâneos | 1.000 | 10.000 |
| Solicitações/dia | 5.000 | 50.000 |
| Mensagens de chat/dia | 20.000 | 200.000 |
| Armazenamento (banco) | 10 GB | 100 GB |

### 3.3 Crescimento

- Sistema deve suportar crescimento de 100% ao ano sem refatoração arquitetural
- Banco de dados deve suportar particionamento (sharding) futuro

---

## 4. Disponibilidade

### 4.1 Uptime

- **SLA**: 99.5% de disponibilidade mensal
- **Downtime máximo permitido**: 3.6 horas/mês
- **Janela de manutenção**: Domingos, 02h-06h (horário de menor uso)

### 4.2 Recuperação

- **RTO (Recovery Time Objective)**: 1 hora
- **RPO (Recovery Point Objective)**: 15 minutos
- Backup automático diário do banco de dados
- Backup incremental a cada 4 horas

### 4.3 Resiliência

- Sistema deve continuar funcionando mesmo se:
  - RabbitMQ estiver temporariamente indisponível (mensagens em buffer)
  - Serviço de notificações falhar (retry automático)
  - Gateway de pagamento estiver lento (timeout configurável)

---

## 5. Segurança

### 5.1 Autenticação e Autorização

- **Autenticação**: JWT (JSON Web Token) com expiração de 24 horas
- **Refresh Token**: Válido por 7 dias
- **Senha**: Mínimo 8 caracteres, hash com BCrypt (cost factor 12)
- **2FA (Futuro)**: Autenticação de dois fatores opcional

### 5.2 Proteção de Dados

- **Criptografia em trânsito**: TLS 1.3 obrigatório (HTTPS)
- **Criptografia em repouso**: Dados sensíveis criptografados no banco (AES-256)
- **Dados sensíveis**: CPF, CNPJ, dados de pagamento nunca em logs
- **LGPD**: Conformidade com Lei Geral de Proteção de Dados

### 5.3 Proteção contra Ataques

| Ataque | Proteção |
|--------|----------|
| SQL Injection | Prepared Statements, ORM |
| XSS | Sanitização de inputs, CSP headers |
| CSRF | Tokens CSRF em formulários |
| DDoS | Rate limiting (100 req/min por IP) |
| Brute Force | Bloqueio após 5 tentativas de login |
| Session Hijacking | Tokens com expiração curta |

### 5.4 Auditoria

- Logs de todas as operações sensíveis (login, pagamentos, alterações)
- Retenção de logs por 12 meses
- Logs não devem conter dados sensíveis (senhas, tokens)

---

## 6. Usabilidade

### 6.1 Interface

- **Responsividade**: Funcionar em desktop, tablet e mobile
- **Navegadores suportados**: Chrome, Firefox, Safari, Edge (últimas 2 versões)
- **Acessibilidade**: WCAG 2.1 nível AA
- **Idioma**: Português (BR) inicialmente

### 6.2 Experiência do Usuário

- **Feedback visual**: Toda ação deve ter feedback em < 100ms
- **Loading states**: Indicadores de carregamento para operações > 500ms
- **Mensagens de erro**: Claras e orientadas à solução
- **Onboarding**: Tutorial interativo para novos usuários

### 6.3 Curva de Aprendizado

- Usuário deve conseguir criar primeira solicitação em < 3 minutos
- Interface intuitiva, sem necessidade de manual
- Tooltips e ajuda contextual disponíveis

---

## 7. Manutenibilidade

### 7.1 Código

- **Cobertura de testes**: Mínimo 75%
- **Complexidade ciclomática**: Máximo 10 por método
- **Duplicação de código**: Máximo 3%
- **Code smells**: Máximo 50 (SonarQube)

### 7.2 Documentação

- Código documentado com JavaDoc/JSDoc
- README atualizado em cada módulo
- Diagramas de arquitetura atualizados
- API documentada com Swagger/OpenAPI

### 7.3 Versionamento

- Git flow para gerenciamento de branches
- Semantic Versioning (MAJOR.MINOR.PATCH)
- Changelog mantido atualizado
- Tags para cada release

---

## 8. Portabilidade

### 8.1 Infraestrutura

- **Containerização**: Docker para todos os serviços
- **Orquestração**: Docker Compose (dev) / Kubernetes (prod)
- **Cloud-agnostic**: Deve funcionar em AWS, Azure, GCP ou on-premise

### 8.2 Banco de Dados

- Uso de ORM para facilitar migração entre bancos
- Migrations versionadas e reversíveis
- Suporte inicial: PostgreSQL 14+

### 8.3 Dependências

- Dependências gerenciadas via Maven/Gradle (backend) e npm/yarn (frontend)
- Versões fixadas para garantir reprodutibilidade
- Atualização trimestral de dependências

---

## 9. Compatibilidade

### 9.1 Integrações

- **API REST**: Versionamento via URL (/api/v1/)
- **Backward compatibility**: Manter versões antigas por 6 meses
- **Webhooks**: Para integrações externas (futuro)

### 9.2 Formatos de Dados

- **API**: JSON (application/json)
- **Datas**: ISO 8601 (YYYY-MM-DDTHH:mm:ss.sssZ)
- **Moeda**: BRL, formato decimal (ex: 150.50)
- **Encoding**: UTF-8

---

## 10. Confiabilidade

### 10.1 Tolerância a Falhas

- **Retry automático**: 3 tentativas com backoff exponencial
- **Circuit Breaker**: Para serviços externos (pagamento, notificações)
- **Dead Letter Queue**: Para mensagens que falharam após retries
- **Graceful degradation**: Funcionalidades não-críticas podem falhar sem derrubar o sistema

### 10.2 Monitoramento

- **Health checks**: Endpoint /health para verificar status
- **Métricas**: Prometheus para coleta de métricas
- **Alertas**: Notificação automática em caso de:
  - CPU > 80% por 5 minutos
  - Memória > 85%
  - Disco > 90%
  - Taxa de erro > 5%
  - Tempo de resposta > 2s

### 10.3 Logs

- **Níveis**: ERROR, WARN, INFO, DEBUG
- **Formato**: JSON estruturado
- **Centralização**: ELK Stack ou similar
- **Correlação**: Request ID para rastrear requisições

---

## 11. Conformidade Legal

### 11.1 LGPD (Lei Geral de Proteção de Dados)

- ✅ Consentimento explícito para coleta de dados
- ✅ Direito ao esquecimento (exclusão de dados)
- ✅ Portabilidade de dados (exportação)
- ✅ Notificação de vazamento em até 72h
- ✅ DPO (Data Protection Officer) designado

### 11.2 Nota Fiscal Eletrônica

- Emissão de NF-e para serviços prestados (futuro)
- Integração com SEFAZ

### 11.3 Termos de Uso

- Termos de uso e política de privacidade claros
- Aceite obrigatório no cadastro
- Versionamento de termos

---

## 12. Internacionalização (Futuro)

### 12.1 Idiomas

- Suporte inicial: Português (BR)
- Preparado para: Inglês, Espanhol

### 12.2 Localização

- Formato de data/hora por região
- Moeda por região
- Fuso horário configurável

---

## 13. Sustentabilidade

### 13.1 Eficiência Energética

- Otimização de queries para reduzir processamento
- Cache para reduzir chamadas ao banco
- Compressão de assets (gzip, brotli)

### 13.2 Recursos

- Imagens otimizadas (WebP, lazy loading)
- Minificação de CSS/JS
- CDN para assets estáticos

---

## 14. Priorização dos RNFs

### Críticos (Obrigatórios para N2)
- Performance (tempo de resposta)
- Segurança (autenticação, HTTPS)
- Disponibilidade
- Usabilidade

### Importantes (Desejáveis para N2)
- Escalabilidade horizontal
- Monitoramento básico
- Logs estruturados
- Cobertura de testes 75%

### Opcionais (Futuro)
- 2FA
- Internacionalização
- Webhooks
- Nota Fiscal Eletrônica

---

## 15. Métricas de Sucesso

| Métrica | Valor Alvo | Como Medir |
|---------|-----------|------------|
| Tempo de resposta (p95) | < 500ms | APM (Application Performance Monitoring) |
| Uptime | > 99.5% | Monitoramento contínuo |
| Taxa de erro | < 1% | Logs + Sentry |
| Cobertura de testes | > 75% | SonarQube |
| Satisfação do usuário | > 4.5/5 | Pesquisa in-app |
| Tempo de onboarding | < 3 min | Analytics |

---

## 16. Conclusão

Estes requisitos não-funcionais garantem que o sistema Desenrola não apenas funcione corretamente, mas também seja:
- Rápido e responsivo
- Seguro e confiável
- Escalável e sustentável
- Fácil de usar e manter
