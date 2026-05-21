-- =============================================================================
-- DESENROLA - Script de Criação do Banco de Dados
-- Plataforma de conexão entre clientes e profissionais autônomos
-- Projeto Integrador ADS 2026/1
-- Compatível com: MySQL 8.0+
-- Backend: Java + Spring Boot + JPA/Hibernate
-- =============================================================================

-- -----------------------------------------------------------------------------
-- CONFIGURAÇÕES INICIAIS
-- -----------------------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS desenrola_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE desenrola_db;

SET FOREIGN_KEY_CHECKS = 0;


-- =============================================================================
-- BLOCO 1: TABELAS DE DOMÍNIO (sem dependências externas)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: categoria_servico
-- Categorias disponíveis na plataforma (ex: Elétrica, Hidráulica, Beleza)
-- -----------------------------------------------------------------------------
CREATE TABLE categoria_servico (
    id          INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(100)    NOT NULL,
    descricao   VARCHAR(255)        NULL,
    icone       VARCHAR(100)        NULL COMMENT 'Nome do ícone (ex: Zap, Wrench)',
    grupo       VARCHAR(100)        NULL COMMENT 'Agrupamento visual (ex: Casa e Construção)',
    ativo       TINYINT(1)      NOT NULL DEFAULT 1,
    criado_em   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_categoria_servico PRIMARY KEY (id),
    CONSTRAINT uq_categoria_nome    UNIQUE (nome)
) ENGINE=InnoDB COMMENT='Categorias de serviço disponíveis na plataforma';

-- Dados iniciais (seed)
INSERT INTO categoria_servico (nome, descricao, icone, grupo) VALUES
    ('Hidráulica',      'Consertos, vazamentos e instalações hidráulicas',          'Wrench',       'Casa e Construção'),
    ('Elétrica',        'Instalações, manutenções e reparos elétricos',             'Zap',          'Casa e Construção'),
    ('Climatização',    'Instalação e manutenção de ar-condicionado e ventilação',  'Wind',         'Casa e Construção'),
    ('Pintura',         'Pintura residencial, comercial e acabamentos',             'PaintRoller',  'Casa e Construção'),
    ('Construção',      'Obras, reformas e pequenos reparos',                       'Hammer',       'Casa e Construção'),
    ('Jardinagem',      'Poda, paisagismo e manutenção de jardins',                 'Leaf',         'Casa e Construção'),
    ('Consultoria',     'Consultoria empresarial e estratégica',                    'Briefcase',    'Empresarial'),
    ('Design',          'Design gráfico, de interiores e identidade visual',        'PenTool',      'Empresarial'),
    ('TI',              'Suporte técnico, redes e sistemas',                        'MonitorCog',   'Empresarial'),
    ('Manutenção',      'Manutenção geral de equipamentos e instalações',           'Wrench',       'Empresarial'),
    ('Massagem',        'Massagem terapêutica e relaxante',                         'Sparkles',     'Saúde e Beleza'),
    ('Manicure',        'Unhas, nail art e cuidados das mãos',                      'Hand',         'Saúde e Beleza'),
    ('Cabeleireiro',    'Corte, coloração e tratamentos capilares',                 'Scissors',     'Saúde e Beleza'),
    ('Barbearia',       'Corte masculino, barba e bigode',                          'Scissors',     'Saúde e Beleza'),
    ('Outros',          'Serviços que não se encaixam nas categorias anteriores',   NULL,           'Outros');


-- -----------------------------------------------------------------------------
-- Tabela: servico
-- Tipos de serviços que podem ser ofertados e solicitados
-- -----------------------------------------------------------------------------
CREATE TABLE servico (
    id                  INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    id_categoria        INT UNSIGNED    NOT NULL,
    nome                VARCHAR(150)    NOT NULL,
    descricao           TEXT                NULL,
    preco_base          DECIMAL(10,2)       NULL COMMENT 'Preço de referência; pode ser nulo se negociável',
    tipo_precificacao   ENUM(
                            'FIXO',
                            'POR_HORA',
                            'SOB_CONSULTA'
                        )               NOT NULL DEFAULT 'SOB_CONSULTA',
    tempo_estimado_min  INT UNSIGNED        NULL COMMENT 'Tempo estimado em minutos',
    ativo               TINYINT(1)      NOT NULL DEFAULT 1,
    criado_em           DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_servico            PRIMARY KEY (id),
    CONSTRAINT fk_servico_categoria  FOREIGN KEY (id_categoria) REFERENCES categoria_servico(id)
) ENGINE=InnoDB COMMENT='Tipos de serviços disponíveis na plataforma';


-- =============================================================================
-- BLOCO 2: USUÁRIOS (tabelas de herança — estratégia TABLE_PER_CLASS adaptada)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: usuario
-- Entidade-base para clientes e profissionais (herança com tabela única + joins)
-- -----------------------------------------------------------------------------
CREATE TABLE usuario (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(150)        NOT NULL,
    email           VARCHAR(254)        NOT NULL,
    senha_hash      VARCHAR(255)        NOT NULL COMMENT 'BCrypt ou Argon2 — nunca texto puro',
    telefone        VARCHAR(20)             NULL,
    foto_url        VARCHAR(500)            NULL,
    tipo            ENUM(
                        'CLIENTE',
                        'PROFISSIONAL',
                        'ADMIN'
                    )                   NOT NULL,
    ativo           TINYINT(1)          NOT NULL DEFAULT 1,
    email_verificado TINYINT(1)         NOT NULL DEFAULT 0,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ultimo_acesso   DATETIME                NULL,

    CONSTRAINT pk_usuario       PRIMARY KEY (id),
    CONSTRAINT uq_usuario_email UNIQUE (email)
) ENGINE=InnoDB COMMENT='Tabela-base para todos os usuários do sistema';

CREATE INDEX idx_usuario_tipo  ON usuario(tipo);
CREATE INDEX idx_usuario_ativo ON usuario(ativo);


-- -----------------------------------------------------------------------------
-- Tabela: cliente
-- Dados específicos de usuários do tipo CLIENTE
-- -----------------------------------------------------------------------------
CREATE TABLE cliente (
    id_usuario      BIGINT UNSIGNED     NOT NULL COMMENT 'FK para usuario.id',
    cpf             VARCHAR(14)             NULL COMMENT 'Formato: 000.000.000-00',
    data_nascimento DATE                    NULL,
    endereco        VARCHAR(255)            NULL,
    cidade          VARCHAR(100)            NULL,
    estado          CHAR(2)                 NULL,
    cep             VARCHAR(9)              NULL COMMENT 'Formato: 00000-000',
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_cliente           PRIMARY KEY (id_usuario),
    CONSTRAINT fk_cliente_usuario   FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT uq_cliente_cpf       UNIQUE (cpf)
) ENGINE=InnoDB COMMENT='Dados complementares de clientes';


-- -----------------------------------------------------------------------------
-- Tabela: profissional
-- Dados específicos de usuários do tipo PROFISSIONAL
-- -----------------------------------------------------------------------------
CREATE TABLE profissional (
    id_usuario          BIGINT UNSIGNED     NOT NULL COMMENT 'FK para usuario.id',
    documento           VARCHAR(18)             NULL COMMENT 'CPF ou CNPJ',
    tipo_documento      ENUM('CPF','CNPJ')      NULL,
    id_categoria        INT UNSIGNED            NULL COMMENT 'Categoria principal',
    especialidade       VARCHAR(200)            NULL COMMENT 'Descrição livre da especialidade',
    descricao_perfil    TEXT                    NULL COMMENT 'Bio exibida no perfil',
    avaliacao_media     DECIMAL(3,2)        NOT NULL DEFAULT 0.00 COMMENT 'Calculado automaticamente (0.00 a 5.00)',
    total_avaliacoes    INT UNSIGNED        NOT NULL DEFAULT 0,
    disponivel          TINYINT(1)          NOT NULL DEFAULT 1,
    cidade              VARCHAR(100)            NULL,
    estado              CHAR(2)                 NULL,
    raio_atendimento_km INT UNSIGNED            NULL COMMENT 'Raio máximo de atendimento em km',
    criado_em           DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_profissional              PRIMARY KEY (id_usuario),
    CONSTRAINT fk_profissional_usuario      FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_profissional_categoria    FOREIGN KEY (id_categoria) REFERENCES categoria_servico(id)
) ENGINE=InnoDB COMMENT='Dados complementares de profissionais';

CREATE INDEX idx_profissional_categoria    ON profissional(id_categoria);
CREATE INDEX idx_profissional_disponivel   ON profissional(disponivel);
CREATE INDEX idx_profissional_estado       ON profissional(estado);


-- -----------------------------------------------------------------------------
-- Tabela: profissional_servico
-- Associação N:N entre profissional e os serviços que ele oferece
-- -----------------------------------------------------------------------------
CREATE TABLE profissional_servico (
    id_profissional BIGINT UNSIGNED     NOT NULL,
    id_servico      INT UNSIGNED        NOT NULL,
    preco_proprio   DECIMAL(10,2)           NULL COMMENT 'Preço cobrado por este profissional (sobrescreve preco_base)',
    ativo           TINYINT(1)          NOT NULL DEFAULT 1,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_profissional_servico          PRIMARY KEY (id_profissional, id_servico),
    CONSTRAINT fk_prof_serv_profissional        FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_prof_serv_servico             FOREIGN KEY (id_servico)      REFERENCES servico(id)
) ENGINE=InnoDB COMMENT='Serviços oferecidos por cada profissional';


-- =============================================================================
-- BLOCO 3: SOLICITAÇÃO (entidade central)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: solicitacao
-- Representa uma requisição de serviço de um cliente para um profissional
-- -----------------------------------------------------------------------------
CREATE TABLE solicitacao (
    id                      BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_cliente              BIGINT UNSIGNED     NOT NULL,
    id_profissional         BIGINT UNSIGNED         NULL COMMENT 'Preenchido quando profissional aceita',
    id_servico              INT UNSIGNED        NOT NULL,
    status                  ENUM(
                                'PENDENTE',
                                'AGUARDANDO_ORCAMENTO',
                                'ORCAMENTO_ENVIADO',
                                'APROVADA',
                                'EM_ANDAMENTO',
                                'CONCLUIDA',
                                'CANCELADA',
                                'RECUSADA'
                            )                   NOT NULL DEFAULT 'PENDENTE',
    titulo                  VARCHAR(200)        NOT NULL COMMENT 'Descrição resumida do problema',
    descricao               TEXT                    NULL,
    endereco_atendimento    VARCHAR(255)            NULL,
    cidade_atendimento      VARCHAR(100)            NULL,
    estado_atendimento      CHAR(2)                 NULL,
    valor_estimado          DECIMAL(10,2)           NULL COMMENT 'Valor informado pelo cliente',
    valor_final             DECIMAL(10,2)           NULL COMMENT 'Valor confirmado após orçamento',
    data_preferencial       DATETIME                NULL COMMENT 'Data/hora preferida pelo cliente',
    data_conclusao          DATETIME                NULL,
    observacoes             TEXT                    NULL,
    motivo_cancelamento     VARCHAR(255)            NULL,
    criado_em               DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em           DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_solicitacao               PRIMARY KEY (id),
    CONSTRAINT fk_solic_cliente             FOREIGN KEY (id_cliente)      REFERENCES cliente(id_usuario),
    CONSTRAINT fk_solic_profissional        FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario),
    CONSTRAINT fk_solic_servico             FOREIGN KEY (id_servico)      REFERENCES servico(id)
) ENGINE=InnoDB COMMENT='Solicitações de serviço feitas pelos clientes';

CREATE INDEX idx_solic_cliente       ON solicitacao(id_cliente);
CREATE INDEX idx_solic_profissional  ON solicitacao(id_profissional);
CREATE INDEX idx_solic_status        ON solicitacao(status);
CREATE INDEX idx_solic_criado_em     ON solicitacao(criado_em);


-- =============================================================================
-- BLOCO 4: ORÇAMENTO (detalhamento financeiro de uma solicitação)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: orcamento
-- Proposta financeira enviada pelo profissional ao cliente
-- -----------------------------------------------------------------------------
CREATE TABLE orcamento (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_solicitacao  BIGINT UNSIGNED     NOT NULL,
    observacoes     TEXT                    NULL,
    total_mao_obra  DECIMAL(10,2)       NOT NULL DEFAULT 0.00,
    total_materiais DECIMAL(10,2)       NOT NULL DEFAULT 0.00,
    total_geral     DECIMAL(10,2)       NOT NULL DEFAULT 0.00 COMMENT 'Calculado: mao_obra + materiais',
    status          ENUM(
                        'PENDENTE',
                        'APROVADO',
                        'RECUSADO'
                    )                   NOT NULL DEFAULT 'PENDENTE',
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_orcamento             PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Orçamentos enviados pelos profissionais';

CREATE INDEX idx_orcamento_solicitacao ON orcamento(id_solicitacao);


-- -----------------------------------------------------------------------------
-- Tabela: orcamento_item_mao_obra
-- Itens de mão de obra dentro de um orçamento
-- -----------------------------------------------------------------------------
CREATE TABLE orcamento_item_mao_obra (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_orcamento    BIGINT UNSIGNED     NOT NULL,
    descricao       VARCHAR(255)        NOT NULL,
    tempo_horas     DECIMAL(5,2)            NULL COMMENT 'Horas estimadas',
    valor_hora      DECIMAL(10,2)           NULL,
    valor_total     DECIMAL(10,2)       NOT NULL,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_orcamento_mao_obra        PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_mob_orcamento   FOREIGN KEY (id_orcamento) REFERENCES orcamento(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Itens de mão de obra de um orçamento';


-- -----------------------------------------------------------------------------
-- Tabela: orcamento_item_material
-- Materiais listados em um orçamento
-- -----------------------------------------------------------------------------
CREATE TABLE orcamento_item_material (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_orcamento    BIGINT UNSIGNED     NOT NULL,
    nome            VARCHAR(150)        NOT NULL,
    quantidade      DECIMAL(10,3)       NOT NULL DEFAULT 1,
    unidade         VARCHAR(30)             NULL COMMENT 'Ex: unidade, metro, litro',
    valor_unitario  DECIMAL(10,2)       NOT NULL,
    valor_total     DECIMAL(10,2)       NOT NULL,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_orcamento_material        PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_mat_orcamento   FOREIGN KEY (id_orcamento) REFERENCES orcamento(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Materiais listados em um orçamento';


-- =============================================================================
-- BLOCO 5: PAGAMENTO
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: pagamento
-- Transação financeira vinculada a uma solicitação aprovada
-- -----------------------------------------------------------------------------
CREATE TABLE pagamento (
    id                  BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_solicitacao      BIGINT UNSIGNED     NOT NULL,
    valor               DECIMAL(10,2)       NOT NULL,
    metodo              ENUM(
                            'CARTAO_CREDITO',
                            'CARTAO_DEBITO',
                            'PIX',
                            'DINHEIRO'
                        )                   NOT NULL,
    status              ENUM(
                            'PENDENTE',
                            'PROCESSANDO',
                            'APROVADO',
                            'RECUSADO',
                            'ESTORNADO'
                        )                   NOT NULL DEFAULT 'PENDENTE',
    transacao_id        VARCHAR(255)            NULL COMMENT 'ID externo do gateway de pagamento',
    data_processamento  DATETIME                NULL,
    criado_em           DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_pagamento             PRIMARY KEY (id),
    CONSTRAINT fk_pagamento_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id)
) ENGINE=InnoDB COMMENT='Pagamentos vinculados a solicitações';

CREATE INDEX idx_pagamento_status       ON pagamento(status);
CREATE INDEX idx_pagamento_solicitacao  ON pagamento(id_solicitacao);


-- =============================================================================
-- BLOCO 6: AVALIAÇÃO
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: avaliacao
-- Avaliação do cliente sobre o serviço prestado (apenas após conclusão)
-- -----------------------------------------------------------------------------
CREATE TABLE avaliacao (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_solicitacao  BIGINT UNSIGNED     NOT NULL,
    id_cliente      BIGINT UNSIGNED     NOT NULL,
    id_profissional BIGINT UNSIGNED     NOT NULL,
    nota            TINYINT UNSIGNED    NOT NULL COMMENT 'De 1 a 5',
    comentario      TEXT                    NULL,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_avaliacao                 PRIMARY KEY (id),
    CONSTRAINT fk_aval_solicitacao          FOREIGN KEY (id_solicitacao)  REFERENCES solicitacao(id),
    CONSTRAINT fk_aval_cliente              FOREIGN KEY (id_cliente)      REFERENCES cliente(id_usuario),
    CONSTRAINT fk_aval_profissional         FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario),
    CONSTRAINT uq_avaliacao_solicitacao     UNIQUE (id_solicitacao) COMMENT 'Uma avaliação por solicitação',
    CONSTRAINT ck_avaliacao_nota            CHECK (nota BETWEEN 1 AND 5)
) ENGINE=InnoDB COMMENT='Avaliações de clientes sobre serviços prestados';

CREATE INDEX idx_avaliacao_profissional ON avaliacao(id_profissional);


-- Trigger: atualiza avaliacao_media e total_avaliacoes no profissional
DELIMITER $$

CREATE TRIGGER trg_avaliacao_after_insert
AFTER INSERT ON avaliacao
FOR EACH ROW
BEGIN
    UPDATE profissional
    SET
        total_avaliacoes = total_avaliacoes + 1,
        avaliacao_media  = (
            SELECT ROUND(AVG(nota), 2)
            FROM avaliacao
            WHERE id_profissional = NEW.id_profissional
        )
    WHERE id_usuario = NEW.id_profissional;
END$$

CREATE TRIGGER trg_avaliacao_after_update
AFTER UPDATE ON avaliacao
FOR EACH ROW
BEGIN
    UPDATE profissional
    SET avaliacao_media = (
        SELECT ROUND(AVG(nota), 2)
        FROM avaliacao
        WHERE id_profissional = NEW.id_profissional
    )
    WHERE id_usuario = NEW.id_profissional;
END$$

DELIMITER ;


-- =============================================================================
-- BLOCO 7: CHAT E MENSAGENS
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: chat
-- Canal de comunicação vinculado a uma solicitação aceita
-- -----------------------------------------------------------------------------
CREATE TABLE chat (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_solicitacao  BIGINT UNSIGNED     NOT NULL,
    ativo           TINYINT(1)          NOT NULL DEFAULT 1,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_chat              PRIMARY KEY (id),
    CONSTRAINT fk_chat_solicitacao  FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE,
    CONSTRAINT uq_chat_solicitacao  UNIQUE (id_solicitacao) COMMENT 'Um chat por solicitação'
) ENGINE=InnoDB COMMENT='Canais de chat entre cliente e profissional';


-- -----------------------------------------------------------------------------
-- Tabela: mensagem
-- Mensagens trocadas dentro de um chat
-- -----------------------------------------------------------------------------
CREATE TABLE mensagem (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_chat         BIGINT UNSIGNED     NOT NULL,
    id_remetente    BIGINT UNSIGNED     NOT NULL COMMENT 'FK para usuario.id',
    conteudo        TEXT                NOT NULL,
    lida            TINYINT(1)          NOT NULL DEFAULT 0,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_mensagem          PRIMARY KEY (id),
    CONSTRAINT fk_mensagem_chat     FOREIGN KEY (id_chat)      REFERENCES chat(id) ON DELETE CASCADE,
    CONSTRAINT fk_mensagem_usuario  FOREIGN KEY (id_remetente) REFERENCES usuario(id)
) ENGINE=InnoDB COMMENT='Mensagens trocadas em chats de solicitações';

CREATE INDEX idx_mensagem_chat      ON mensagem(id_chat);
CREATE INDEX idx_mensagem_remetente ON mensagem(id_remetente);
CREATE INDEX idx_mensagem_lida      ON mensagem(lida);


-- =============================================================================
-- BLOCO 8: NOTIFICAÇÕES
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: notificacao
-- Notificações do sistema enviadas a usuários (integra com RabbitMQ/WebSocket)
-- -----------------------------------------------------------------------------
CREATE TABLE notificacao (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_usuario      BIGINT UNSIGNED     NOT NULL,
    tipo            ENUM(
                        'NOVA_SOLICITACAO',
                        'SOLICITACAO_ACEITA',
                        'SOLICITACAO_RECUSADA',
                        'ORCAMENTO_RECEBIDO',
                        'ORCAMENTO_APROVADO',
                        'ORCAMENTO_RECUSADO',
                        'PAGAMENTO_CONFIRMADO',
                        'PAGAMENTO_RECUSADO',
                        'SERVICO_CONCLUIDO',
                        'AVALIACAO_RECEBIDA',
                        'NOVA_MENSAGEM',
                        'SISTEMA'
                    )                   NOT NULL,
    titulo          VARCHAR(150)        NOT NULL,
    mensagem        TEXT                    NULL,
    lida            TINYINT(1)          NOT NULL DEFAULT 0,
    id_referencia   BIGINT UNSIGNED         NULL COMMENT 'ID da entidade relacionada (solicitação, pagamento, etc.)',
    tipo_referencia VARCHAR(50)             NULL COMMENT 'Nome da entidade (solicitacao, pagamento, etc.)',
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_notificacao           PRIMARY KEY (id),
    CONSTRAINT fk_notificacao_usuario   FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Notificações enviadas aos usuários';

CREATE INDEX idx_notificacao_usuario  ON notificacao(id_usuario);
CREATE INDEX idx_notificacao_lida     ON notificacao(lida);
CREATE INDEX idx_notificacao_tipo     ON notificacao(tipo);


-- =============================================================================
-- BLOCO 9: TOKENS DE AUTENTICAÇÃO (JWT refresh / redefinição de senha)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: token_autenticacao
-- Refresh tokens e tokens de redefinição de senha (invalidados após uso)
-- -----------------------------------------------------------------------------
CREATE TABLE token_autenticacao (
    id          BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_usuario  BIGINT UNSIGNED     NOT NULL,
    token       VARCHAR(512)        NOT NULL,
    tipo        ENUM(
                    'REFRESH',
                    'RESET_SENHA',
                    'VERIFICACAO_EMAIL'
                )                   NOT NULL,
    expira_em   DATETIME            NOT NULL,
    usado       TINYINT(1)          NOT NULL DEFAULT 0,
    criado_em   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_token             PRIMARY KEY (id),
    CONSTRAINT fk_token_usuario     FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT uq_token_valor       UNIQUE (token)
) ENGINE=InnoDB COMMENT='Tokens de refresh JWT e redefinição de senha';

CREATE INDEX idx_token_usuario   ON token_autenticacao(id_usuario);
CREATE INDEX idx_token_expira_em ON token_autenticacao(expira_em);


-- =============================================================================
-- BLOCO 10: HISTÓRICO DE STATUS (auditoria de mudanças de estado)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Tabela: historico_status_solicitacao
-- Registra cada mudança de status de uma solicitação para auditoria
-- -----------------------------------------------------------------------------
CREATE TABLE historico_status_solicitacao (
    id              BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT,
    id_solicitacao  BIGINT UNSIGNED     NOT NULL,
    status_anterior ENUM(
                        'PENDENTE',
                        'AGUARDANDO_ORCAMENTO',
                        'ORCAMENTO_ENVIADO',
                        'APROVADA',
                        'EM_ANDAMENTO',
                        'CONCLUIDA',
                        'CANCELADA',
                        'RECUSADA'
                    )                       NULL,
    status_novo     ENUM(
                        'PENDENTE',
                        'AGUARDANDO_ORCAMENTO',
                        'ORCAMENTO_ENVIADO',
                        'APROVADA',
                        'EM_ANDAMENTO',
                        'CONCLUIDA',
                        'CANCELADA',
                        'RECUSADA'
                    )                   NOT NULL,
    id_alterado_por BIGINT UNSIGNED         NULL COMMENT 'Usuário que realizou a mudança',
    observacao      VARCHAR(255)            NULL,
    criado_em       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_hist_status               PRIMARY KEY (id),
    CONSTRAINT fk_hist_status_solicitacao   FOREIGN KEY (id_solicitacao)  REFERENCES solicitacao(id),
    CONSTRAINT fk_hist_status_usuario       FOREIGN KEY (id_alterado_por) REFERENCES usuario(id)
) ENGINE=InnoDB COMMENT='Auditoria de transições de status das solicitações';

CREATE INDEX idx_hist_status_solicitacao ON historico_status_solicitacao(id_solicitacao);


-- Trigger: registra automaticamente mudanças de status na solicitação
DELIMITER $$

CREATE TRIGGER trg_solicitacao_status_update
AFTER UPDATE ON solicitacao
FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status THEN
        INSERT INTO historico_status_solicitacao
            (id_solicitacao, status_anterior, status_novo)
        VALUES
            (NEW.id, OLD.status, NEW.status);
    END IF;
END$$

DELIMITER ;


-- =============================================================================
-- REATIVANDO AS FOREIGN KEYS
-- =============================================================================
SET FOREIGN_KEY_CHECKS = 1;


-- =============================================================================
-- VIEWS ÚTEIS PARA O BACKEND
-- =============================================================================

-- View: lista de profissionais com dados consolidados (usada na tela de busca)
CREATE OR REPLACE VIEW vw_profissional_resumo AS
SELECT
    u.id,
    u.nome,
    u.email,
    u.telefone,
    u.foto_url,
    p.especialidade,
    p.descricao_perfil,
    p.avaliacao_media,
    p.total_avaliacoes,
    p.disponivel,
    p.cidade,
    p.estado,
    p.raio_atendimento_km,
    c.nome AS categoria_principal
FROM usuario        u
JOIN profissional   p ON p.id_usuario   = u.id
LEFT JOIN categoria_servico c ON c.id   = p.id_categoria
WHERE u.ativo = 1
  AND u.tipo  = 'PROFISSIONAL';


-- View: histórico de serviços do cliente (usada na tela Histórico)
CREATE OR REPLACE VIEW vw_historico_cliente AS
SELECT
    s.id            AS id_solicitacao,
    s.titulo,
    s.status,
    s.valor_final,
    s.criado_em     AS data_solicitacao,
    s.data_conclusao,
    s.endereco_atendimento,
    u_prof.nome     AS nome_profissional,
    sv.nome         AS nome_servico,
    cs.nome         AS categoria
FROM solicitacao        s
JOIN servico            sv  ON sv.id           = s.id_servico
JOIN categoria_servico  cs  ON cs.id           = sv.id_categoria
LEFT JOIN usuario       u_prof ON u_prof.id    = s.id_profissional;


-- View: resumo de um orçamento completo (Mão de obra + Materiais + Total)
CREATE OR REPLACE VIEW vw_orcamento_completo AS
SELECT
    o.id            AS id_orcamento,
    o.id_solicitacao,
    o.observacoes,
    o.total_mao_obra,
    o.total_materiais,
    o.total_geral,
    o.status,
    o.criado_em,
    s.titulo        AS titulo_servico,
    u_cli.nome      AS nome_cliente,
    u_prof.nome     AS nome_profissional
FROM orcamento      o
JOIN solicitacao    s       ON s.id         = o.id_solicitacao
JOIN usuario        u_cli   ON u_cli.id     = s.id_cliente
LEFT JOIN usuario   u_prof  ON u_prof.id    = s.id_profissional;


-- =============================================================================
-- FIM DO SCRIPT
-- =============================================================================
