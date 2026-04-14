# 🚀 MVP - Desenrola (N1)

## O que é este MVP?

Este é um **Minimum Viable Product (Produto Mínimo Viável)** que demonstra a arquitetura do projeto Desenrola funcionando. Ele mostra:

✅ Frontend conectando com Backend via HTTP  
✅ Backend publicando mensagens no RabbitMQ  
✅ Worker consumindo mensagens da fila  
✅ Todas as camadas da arquitetura se comunicando  

---

## 📋 Pré-requisitos

Antes de rodar, você precisa ter instalado:

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven** ([Download](https://maven.apache.org/download.cgi))
- **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop/))

---

## 🏃 Como Rodar

### Passo 1: Subir a Infraestrutura (Docker)

No terminal, dentro da pasta `Desenrola-01`:

```bash
docker compose up -d
```

Isso vai subir:
- PostgreSQL (banco de dados)
- RabbitMQ (mensageria)

**Aguarde 30 segundos** para os serviços iniciarem completamente.

---

### Passo 2: Rodar o Backend

Abra um novo terminal na pasta `Desenrola-01/backend`:

```bash
mvn spring-boot:run
```

Você verá:
```
🚀 Desenrola Backend iniciado em http://localhost:8080
```

**Deixe este terminal aberto!** É aqui que você verá as mensagens sendo processadas.

---

### Passo 3: Abrir o Frontend

Abra o arquivo `frontend/index.html` no navegador:

- **Opção 1**: Clique duas vezes no arquivo
- **Opção 2**: Arraste o arquivo para o navegador
- **Opção 3**: Use um servidor local:
  ```bash
  cd frontend
  python -m http.server 3000
  # Acesse: http://localhost:3000
  ```

---

## 🧪 Testando a Arquitetura

### Teste 1: Conexão com Backend

1. Clique no botão **"Testar Conexão"**
2. Você deve ver: ✅ Backend conectado com sucesso!

### Teste 2: Mensageria (RabbitMQ)

1. Digite uma mensagem (ex: "Olá do Frontend!")
2. Clique em **"Enviar para Fila RabbitMQ"**
3. Observe o log no frontend
4. **Olhe o terminal do backend** - você verá:
   ```
   📤 Enviando mensagem para RabbitMQ: Olá do Frontend!
   📥 Worker recebeu mensagem: Olá do Frontend!
   ✅ Mensagem processada com sucesso!
   ```

---

## 🎯 O que está sendo demonstrado?

### Fluxo Completo:

```
┌─────────────┐
│  Frontend   │  1. Usuário digita mensagem
│  (HTML/JS)  │  2. Envia POST para /api/teste-mensageria
└──────┬──────┘
       │ HTTP
       ↓
┌─────────────┐
│   Backend   │  3. Controlador recebe requisição
│ (Spring)    │  4. Serviço publica na fila RabbitMQ
└──────┬──────┘
       │ AMQP
       ↓
┌─────────────┐
│  RabbitMQ   │  5. Mensagem fica na fila
│   (Fila)    │
└──────┬──────┘
       │ AMQP
       ↓
┌─────────────┐
│   Worker    │  6. Worker consome mensagem
│ (Consumer)  │  7. Processa e loga no console
└─────────────┘
```

---

## 🔍 Verificando os Serviços

### RabbitMQ Management

Acesse: http://localhost:15672

- **Usuário**: admin
- **Senha**: admin123

Você pode ver:
- Filas criadas
- Mensagens sendo processadas
- Conexões ativas

### PostgreSQL

Conecte usando qualquer cliente SQL:

- **Host**: localhost:5432
- **Banco**: projeto_db
- **Usuário**: usuario_projeto
- **Senha**: senha_projeto

---

## 📁 Estrutura do MVP

```
Desenrola-01/
├── backend/
│   ├── src/main/java/com/desenrola/
│   │   ├── DesenrolaApplication.java      # Aplicação principal
│   │   ├── config/
│   │   │   ├── RabbitMQConfig.java        # Configuração das filas
│   │   │   └── WebConfig.java             # Configuração CORS
│   │   ├── controladores/
│   │   │   └── TesteController.java       # API REST
│   │   └── servicos/
│   │       ├── TesteService.java          # Lógica de negócio
│   │       └── TesteWorker.java           # Consumidor RabbitMQ
│   └── pom.xml                            # Dependências Maven
├── frontend/
│   └── index.html                         # Interface web
└── docker-compose.yml                     # Infraestrutura
```

---

## 🐛 Problemas Comuns

### Backend não inicia

**Erro**: `Port 8080 already in use`

**Solução**: Algum processo está usando a porta 8080
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <numero> /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9
```

---

### RabbitMQ não conecta

**Erro**: `Connection refused`

**Solução**: Verifique se o Docker está rodando
```bash
docker ps
# Deve mostrar rabbitmq e postgres rodando
```

Se não estiver:
```bash
docker compose up -d
```

---

### Frontend não conecta com Backend

**Erro**: `CORS error` ou `Network error`

**Solução**: 
1. Verifique se o backend está rodando (http://localhost:8080/api/health)
2. Limpe o cache do navegador (Ctrl+Shift+Delete)
3. Tente outro navegador

---

## 📊 Para a Apresentação N1

Na apresentação, mostre:

1. **Frontend funcionando** (interface bonita)
2. **Teste de conexão** (backend respondendo)
3. **Teste de mensageria** (enviar mensagem)
4. **Console do backend** (worker processando)
5. **RabbitMQ Management** (fila funcionando)

Fale:
> "Implementamos um MVP que demonstra nossa arquitetura em camadas funcionando. O frontend se comunica com o backend via API REST, o backend publica mensagens no RabbitMQ de forma assíncrona, e um worker consome essas mensagens. Isso atende o requisito de mensageria obrigatória do projeto."

---

## 🎓 Conceitos Demonstrados

✅ **Arquitetura em Camadas** (Frontend → Controlador → Serviço → Mensageria)  
✅ **API REST** (HTTP endpoints)  
✅ **Mensageria Assíncrona** (RabbitMQ)  
✅ **Padrão Repository** (preparado para N2)  
✅ **Dependency Injection** (Spring Boot)  
✅ **Containerização** (Docker)  

---

## 🚀 Próximos Passos (N2)

Este MVP é só o começo! Na N2 vocês vão:

- Implementar as entidades do domínio (Usuario, Solicitacao, etc.)
- Criar CRUD completo
- Adicionar autenticação (JWT)
- Implementar WebSocket para notificações em tempo real
- Criar frontend completo (React/Vue)
- Adicionar testes unitários e de integração

---

## 📞 Ajuda

Se algo não funcionar:

1. Verifique se Docker está rodando
2. Verifique se Java 17+ está instalado (`java -version`)
3. Verifique se Maven está instalado (`mvn -version`)
4. Leia as mensagens de erro no console
5. Consulte este README novamente

---

**Boa sorte na apresentação! 🎉**
