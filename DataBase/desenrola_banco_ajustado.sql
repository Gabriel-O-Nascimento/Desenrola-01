-- =============================================================================
-- DESENROLA - Banco ajustado para integracao com frontend e backend
-- MySQL 8.0+
-- =============================================================================

CREATE DATABASE IF NOT EXISTS desenrola_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE desenrola_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP VIEW IF EXISTS vw_profissional_perfil;
DROP VIEW IF EXISTS vw_chat_resumo;
DROP VIEW IF EXISTS vw_orcamento_completo;
DROP VIEW IF EXISTS vw_historico_cliente;
DROP VIEW IF EXISTS vw_profissional_resumo;

DROP TABLE IF EXISTS historico_status_solicitacao;
DROP TABLE IF EXISTS token_autenticacao;
DROP TABLE IF EXISTS notificacao;
DROP TABLE IF EXISTS mensagem;
DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS avaliacao_anexo;
DROP TABLE IF EXISTS avaliacao;
DROP TABLE IF EXISTS pagamento;
DROP TABLE IF EXISTS orcamento_item_material;
DROP TABLE IF EXISTS orcamento_item_mao_obra;
DROP TABLE IF EXISTS orcamento;
DROP TABLE IF EXISTS solicitacao_anexo;
DROP TABLE IF EXISTS solicitacao;
DROP TABLE IF EXISTS profissional_portfolio;
DROP TABLE IF EXISTS profissional_servico;
DROP TABLE IF EXISTS profissional;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS servico;
DROP TABLE IF EXISTS categoria_servico;

CREATE TABLE categoria_servico (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NULL,
    icone VARCHAR(100) NULL,
    grupo VARCHAR(100) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_categoria_servico PRIMARY KEY (id),
    CONSTRAINT uq_categoria_nome UNIQUE (nome)
) ENGINE=InnoDB;

CREATE TABLE servico (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_categoria INT UNSIGNED NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT NULL,
    preco_base DECIMAL(10,2) NULL,
    tipo_precificacao ENUM('FIXO', 'POR_HORA', 'SOB_CONSULTA') NOT NULL DEFAULT 'SOB_CONSULTA',
    tempo_estimado_min INT UNSIGNED NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_servico PRIMARY KEY (id),
    CONSTRAINT fk_servico_categoria FOREIGN KEY (id_categoria) REFERENCES categoria_servico(id)
) ENGINE=InnoDB;

CREATE TABLE usuario (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(254) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NULL,
    foto_url VARCHAR(500) NULL,
    tipo ENUM('CLIENTE', 'PROFISSIONAL', 'ADMIN') NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    email_verificado TINYINT(1) NOT NULL DEFAULT 0,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ultimo_acesso DATETIME NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE INDEX idx_usuario_tipo ON usuario(tipo);
CREATE INDEX idx_usuario_ativo ON usuario(ativo);

CREATE TABLE cliente (
    id_usuario BIGINT UNSIGNED NOT NULL,
    cpf VARCHAR(14) NULL,
    data_nascimento DATE NULL,
    endereco VARCHAR(255) NULL,
    cidade VARCHAR(100) NULL,
    estado CHAR(2) NULL,
    cep VARCHAR(9) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_cliente PRIMARY KEY (id_usuario),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT uq_cliente_cpf UNIQUE (cpf)
) ENGINE=InnoDB;

CREATE TABLE profissional (
    id_usuario BIGINT UNSIGNED NOT NULL,
    documento VARCHAR(18) NULL,
    tipo_documento ENUM('CPF', 'CNPJ') NULL,
    id_categoria INT UNSIGNED NULL,
    especialidade VARCHAR(200) NULL,
    descricao_perfil TEXT NULL,
    avaliacao_media DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    total_avaliacoes INT UNSIGNED NOT NULL DEFAULT 0,
    disponivel TINYINT(1) NOT NULL DEFAULT 1,
    cidade VARCHAR(100) NULL,
    estado CHAR(2) NULL,
    raio_atendimento_km INT UNSIGNED NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_profissional PRIMARY KEY (id_usuario),
    CONSTRAINT fk_profissional_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_profissional_categoria FOREIGN KEY (id_categoria) REFERENCES categoria_servico(id)
) ENGINE=InnoDB;

CREATE INDEX idx_profissional_categoria ON profissional(id_categoria);
CREATE INDEX idx_profissional_disponivel ON profissional(disponivel);
CREATE INDEX idx_profissional_estado ON profissional(estado);

CREATE TABLE profissional_servico (
    id_profissional BIGINT UNSIGNED NOT NULL,
    id_servico INT UNSIGNED NOT NULL,
    preco_proprio DECIMAL(10,2) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_profissional_servico PRIMARY KEY (id_profissional, id_servico),
    CONSTRAINT fk_prof_serv_profissional FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_prof_serv_servico FOREIGN KEY (id_servico) REFERENCES servico(id)
) ENGINE=InnoDB;

CREATE TABLE profissional_portfolio (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_profissional BIGINT UNSIGNED NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao VARCHAR(255) NULL,
    icone VARCHAR(100) NULL,
    imagem_url VARCHAR(500) NULL,
    ordem_exibicao INT UNSIGNED NOT NULL DEFAULT 0,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_profissional_portfolio PRIMARY KEY (id),
    CONSTRAINT fk_profissional_portfolio_usuario FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE solicitacao (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cliente BIGINT UNSIGNED NOT NULL,
    id_profissional BIGINT UNSIGNED NULL,
    id_servico INT UNSIGNED NOT NULL,
    status ENUM('PENDENTE', 'AGUARDANDO_ORCAMENTO', 'ORCAMENTO_ENVIADO', 'APROVADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA', 'RECUSADA') NOT NULL DEFAULT 'PENDENTE',
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT NULL,
    endereco_atendimento VARCHAR(255) NULL,
    cidade_atendimento VARCHAR(100) NULL,
    estado_atendimento CHAR(2) NULL,
    valor_estimado DECIMAL(10,2) NULL,
    valor_final DECIMAL(10,2) NULL,
    data_preferencial DATETIME NULL,
    data_conclusao DATETIME NULL,
    observacoes TEXT NULL,
    motivo_cancelamento VARCHAR(255) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_solicitacao PRIMARY KEY (id),
    CONSTRAINT fk_solic_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_usuario),
    CONSTRAINT fk_solic_profissional FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario),
    CONSTRAINT fk_solic_servico FOREIGN KEY (id_servico) REFERENCES servico(id)
) ENGINE=InnoDB;

CREATE INDEX idx_solic_cliente ON solicitacao(id_cliente);
CREATE INDEX idx_solic_profissional ON solicitacao(id_profissional);
CREATE INDEX idx_solic_status ON solicitacao(status);
CREATE INDEX idx_solic_criado_em ON solicitacao(criado_em);

CREATE TABLE solicitacao_anexo (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    url_arquivo VARCHAR(500) NULL,
    tipo_mime VARCHAR(100) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_solicitacao_anexo PRIMARY KEY (id),
    CONSTRAINT fk_solicitacao_anexo_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE orcamento (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    observacoes TEXT NULL,
    total_mao_obra DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_materiais DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_geral DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status ENUM('PENDENTE', 'APROVADO', 'RECUSADO') NOT NULL DEFAULT 'PENDENTE',
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_orcamento PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_orcamento_solicitacao ON orcamento(id_solicitacao);

CREATE TABLE orcamento_item_mao_obra (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_orcamento BIGINT UNSIGNED NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    tempo_horas DECIMAL(5,2) NULL,
    valor_hora DECIMAL(10,2) NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_orcamento_mao_obra PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_mob_orcamento FOREIGN KEY (id_orcamento) REFERENCES orcamento(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE orcamento_item_material (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_orcamento BIGINT UNSIGNED NOT NULL,
    nome VARCHAR(150) NOT NULL,
    quantidade DECIMAL(10,3) NOT NULL DEFAULT 1,
    unidade VARCHAR(30) NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_orcamento_material PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_mat_orcamento FOREIGN KEY (id_orcamento) REFERENCES orcamento(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE pagamento (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    metodo ENUM('CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'DINHEIRO') NOT NULL,
    status ENUM('PENDENTE', 'PROCESSANDO', 'APROVADO', 'RECUSADO', 'ESTORNADO') NOT NULL DEFAULT 'PENDENTE',
    transacao_id VARCHAR(255) NULL,
    data_processamento DATETIME NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_pagamento PRIMARY KEY (id),
    CONSTRAINT fk_pagamento_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id)
) ENGINE=InnoDB;

CREATE TABLE avaliacao (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    id_cliente BIGINT UNSIGNED NOT NULL,
    id_profissional BIGINT UNSIGNED NOT NULL,
    nota TINYINT UNSIGNED NOT NULL,
    comentario TEXT NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_avaliacao PRIMARY KEY (id),
    CONSTRAINT fk_aval_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id),
    CONSTRAINT fk_aval_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_usuario),
    CONSTRAINT fk_aval_profissional FOREIGN KEY (id_profissional) REFERENCES profissional(id_usuario),
    CONSTRAINT uq_avaliacao_solicitacao UNIQUE (id_solicitacao),
    CONSTRAINT ck_avaliacao_nota CHECK (nota BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE INDEX idx_avaliacao_profissional ON avaliacao(id_profissional);

CREATE TABLE avaliacao_anexo (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_avaliacao BIGINT UNSIGNED NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    url_arquivo VARCHAR(500) NULL,
    tipo_mime VARCHAR(100) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_avaliacao_anexo PRIMARY KEY (id),
    CONSTRAINT fk_avaliacao_anexo_avaliacao FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE chat (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_chat PRIMARY KEY (id),
    CONSTRAINT fk_chat_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE,
    CONSTRAINT uq_chat_solicitacao UNIQUE (id_solicitacao)
) ENGINE=InnoDB;

CREATE TABLE mensagem (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_chat BIGINT UNSIGNED NOT NULL,
    id_remetente BIGINT UNSIGNED NOT NULL,
    conteudo TEXT NOT NULL,
    lida TINYINT(1) NOT NULL DEFAULT 0,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_mensagem PRIMARY KEY (id),
    CONSTRAINT fk_mensagem_chat FOREIGN KEY (id_chat) REFERENCES chat(id) ON DELETE CASCADE,
    CONSTRAINT fk_mensagem_usuario FOREIGN KEY (id_remetente) REFERENCES usuario(id)
) ENGINE=InnoDB;

CREATE TABLE notificacao (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT UNSIGNED NOT NULL,
    tipo ENUM('NOVA_SOLICITACAO', 'SOLICITACAO_ACEITA', 'SOLICITACAO_RECUSADA', 'ORCAMENTO_RECEBIDO', 'ORCAMENTO_APROVADO', 'ORCAMENTO_RECUSADO', 'PAGAMENTO_CONFIRMADO', 'PAGAMENTO_RECUSADO', 'SERVICO_CONCLUIDO', 'AVALIACAO_RECEBIDA', 'NOVA_MENSAGEM', 'SISTEMA') NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    mensagem TEXT NULL,
    lida TINYINT(1) NOT NULL DEFAULT 0,
    id_referencia BIGINT UNSIGNED NULL,
    tipo_referencia VARCHAR(50) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notificacao PRIMARY KEY (id),
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE token_autenticacao (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_usuario BIGINT UNSIGNED NOT NULL,
    token VARCHAR(512) NOT NULL,
    tipo ENUM('REFRESH', 'RESET_SENHA', 'VERIFICACAO_EMAIL') NOT NULL,
    expira_em DATETIME NOT NULL,
    usado TINYINT(1) NOT NULL DEFAULT 0,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_token PRIMARY KEY (id),
    CONSTRAINT fk_token_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT uq_token_valor UNIQUE (token)
) ENGINE=InnoDB;

CREATE TABLE historico_status_solicitacao (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_solicitacao BIGINT UNSIGNED NOT NULL,
    status_anterior ENUM('PENDENTE', 'AGUARDANDO_ORCAMENTO', 'ORCAMENTO_ENVIADO', 'APROVADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA', 'RECUSADA') NULL,
    status_novo ENUM('PENDENTE', 'AGUARDANDO_ORCAMENTO', 'ORCAMENTO_ENVIADO', 'APROVADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA', 'RECUSADA') NOT NULL,
    id_alterado_por BIGINT UNSIGNED NULL,
    observacao VARCHAR(255) NULL,
    criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_hist_status PRIMARY KEY (id),
    CONSTRAINT fk_hist_status_solicitacao FOREIGN KEY (id_solicitacao) REFERENCES solicitacao(id),
    CONSTRAINT fk_hist_status_usuario FOREIGN KEY (id_alterado_por) REFERENCES usuario(id)
) ENGINE=InnoDB;

DELIMITER $$

CREATE TRIGGER trg_avaliacao_after_insert
AFTER INSERT ON avaliacao
FOR EACH ROW
BEGIN
    UPDATE profissional
    SET
        total_avaliacoes = (
            SELECT COUNT(*) FROM avaliacao WHERE id_profissional = NEW.id_profissional
        ),
        avaliacao_media = (
            SELECT ROUND(AVG(nota), 2) FROM avaliacao WHERE id_profissional = NEW.id_profissional
        )
    WHERE id_usuario = NEW.id_profissional;
END$$

CREATE TRIGGER trg_avaliacao_after_update
AFTER UPDATE ON avaliacao
FOR EACH ROW
BEGIN
    UPDATE profissional
    SET
        total_avaliacoes = (
            SELECT COUNT(*) FROM avaliacao WHERE id_profissional = NEW.id_profissional
        ),
        avaliacao_media = (
            SELECT ROUND(AVG(nota), 2) FROM avaliacao WHERE id_profissional = NEW.id_profissional
        )
    WHERE id_usuario = NEW.id_profissional;
END$$

CREATE TRIGGER trg_solicitacao_status_update
AFTER UPDATE ON solicitacao
FOR EACH ROW
BEGIN
    IF OLD.status <> NEW.status THEN
        INSERT INTO historico_status_solicitacao (id_solicitacao, status_anterior, status_novo)
        VALUES (NEW.id, OLD.status, NEW.status);
    END IF;
END$$

DELIMITER ;

INSERT INTO categoria_servico (id, nome, descricao, icone, grupo) VALUES
    (1, 'Hidraulica', 'Consertos, vazamentos e instalacoes hidraulicas', 'Wrench', 'Casa e Construcao'),
    (2, 'Eletrica', 'Instalacoes, manutencoes e reparos eletricos', 'Zap', 'Casa e Construcao'),
    (3, 'Climatizacao', 'Instalacao e manutencao de ar-condicionado e ventilacao', 'Wind', 'Casa e Construcao'),
    (4, 'Pintura', 'Pintura residencial, comercial e acabamentos', 'PaintRoller', 'Casa e Construcao'),
    (5, 'Construcao', 'Obras, reformas e pequenos reparos', 'Hammer', 'Casa e Construcao'),
    (6, 'Jardinagem', 'Poda, paisagismo e manutencao de jardins', 'Leaf', 'Casa e Construcao'),
    (7, 'Consultoria', 'Consultoria empresarial e estrategica', 'Briefcase', 'Empresarial'),
    (8, 'Design', 'Design grafico, de interiores e identidade visual', 'PenTool', 'Empresarial'),
    (9, 'TI', 'Suporte tecnico, redes e sistemas', 'MonitorCog', 'Empresarial'),
    (10, 'Manutencao', 'Manutencao geral de equipamentos e instalacoes', 'Wrench', 'Empresarial'),
    (11, 'Massagem', 'Massagem terapeutica e relaxante', 'Sparkles', 'Saude e Beleza'),
    (12, 'Manicure', 'Unhas, nail art e cuidados das maos', 'Hand', 'Saude e Beleza'),
    (13, 'Cabeleireiro', 'Corte, coloracao e tratamentos capilares', 'Scissors', 'Saude e Beleza'),
    (14, 'Barbearia', 'Corte masculino, barba e bigode', 'Scissors', 'Saude e Beleza');

INSERT INTO servico (id, id_categoria, nome, descricao, tipo_precificacao, tempo_estimado_min) VALUES
    (1, 5, 'Pedreiro', 'Reparos, construcoes e pequenas reformas residenciais.', 'SOB_CONSULTA', 180),
    (2, 4, 'Pintor', 'Pintura de paredes, acabamentos e retoques em geral.', 'SOB_CONSULTA', 240),
    (3, 2, 'Eletricista', 'Instalacoes, manutencoes e reparos eletricos.', 'SOB_CONSULTA', 120),
    (4, 1, 'Encanador', 'Reparo e instalacao de tubulacoes, torneiras e sistemas hidraulicos.', 'SOB_CONSULTA', 120),
    (5, 7, 'Consultor', 'Orientacao profissional para processos e negocios.', 'SOB_CONSULTA', 90),
    (6, 8, 'Designer', 'Criacao visual, identidade e materiais digitais.', 'SOB_CONSULTA', 180),
    (7, 9, 'Tecnico TI', 'Suporte tecnico, manutencao e configuracao de sistemas.', 'SOB_CONSULTA', 90),
    (8, 10, 'Manutencao', 'Servicos gerais de manutencao para empresas.', 'SOB_CONSULTA', 120),
    (9, 11, 'Massagista', 'Atendimento para relaxamento e bem-estar corporal.', 'SOB_CONSULTA', 60),
    (10, 12, 'Manicure', 'Cuidados com unhas, esmaltacao e acabamento.', 'SOB_CONSULTA', 60),
    (11, 13, 'Cabeleireiro', 'Cortes, tratamentos e finalizacao de cabelo.', 'SOB_CONSULTA', 90),
    (12, 14, 'Barbeiro', 'Corte masculino, barba e acabamento.', 'SOB_CONSULTA', 60);

INSERT INTO usuario (id, nome, email, senha_hash, telefone, tipo, ativo, email_verificado) VALUES
    (1, 'Joao da Silva', 'joao@email.com', '$2a$10$Km74Irh2aCoBuTiSGwCy6eNT0eA17yDitoYov7eRwOLhMaJYLK2iO', '(11) 98765-4321', 'CLIENTE', 1, 1),
    (2, 'Joao Silva', 'joao.prof@email.com', '$2a$10$Km74Irh2aCoBuTiSGwCy6eNT0eA17yDitoYov7eRwOLhMaJYLK2iO', '(11) 97777-1111', 'PROFISSIONAL', 1, 1),
    (3, 'Maria Santos', 'maria.prof@email.com', '$2a$10$Km74Irh2aCoBuTiSGwCy6eNT0eA17yDitoYov7eRwOLhMaJYLK2iO', '(11) 97777-2222', 'PROFISSIONAL', 1, 1),
    (4, 'Carlos Mendes', 'carlos.prof@email.com', '$2a$10$Km74Irh2aCoBuTiSGwCy6eNT0eA17yDitoYov7eRwOLhMaJYLK2iO', '(11) 97777-3333', 'PROFISSIONAL', 1, 1),
    (5, 'Pedro Costa', 'pedro.prof@email.com', '$2a$10$Km74Irh2aCoBuTiSGwCy6eNT0eA17yDitoYov7eRwOLhMaJYLK2iO', '(11) 97777-4444', 'PROFISSIONAL', 1, 1);

INSERT INTO cliente (id_usuario, cpf, endereco, cidade, estado, cep) VALUES
    (1, '111.444.777-35', 'Rua das Flores, 123 - Centro', 'Sao Paulo', 'SP', '01000-000');

INSERT INTO profissional (id_usuario, documento, tipo_documento, id_categoria, especialidade, descricao_perfil, disponivel, cidade, estado, raio_atendimento_km) VALUES
    (2, '123.456.789-00', 'CPF', 2, 'Eletricista residencial', 'Profissional com experiencia em instalacoes e manutencao eletrica residencial.', 1, 'Sao Paulo', 'SP', 10),
    (3, '234.567.890-11', 'CPF', 4, 'Pintora profissional', 'Pintora especializada em pintura residencial, acabamento e textura.', 1, 'Sao Paulo', 'SP', 10),
    (4, '345.678.901-22', 'CPF', 1, 'Encanador', 'Encanador com experiencia em manutencao hidraulica residencial e comercial.', 1, 'Sao Paulo', 'SP', 10),
    (5, '456.789.012-33', 'CPF', 5, 'Pedreiro', 'Pedreiro com experiencia em pequenos reparos, alvenaria e reformas.', 1, 'Sao Paulo', 'SP', 10);

INSERT INTO profissional_servico (id_profissional, id_servico, preco_proprio) VALUES
    (2, 3, NULL),
    (3, 2, NULL),
    (4, 4, NULL),
    (5, 1, NULL);

INSERT INTO profissional_portfolio (id_profissional, titulo, descricao, icone, ordem_exibicao) VALUES
    (2, 'Instalacao residencial', 'Servicos de instalacao em residencias.', 'Home', 1),
    (2, 'Troca de tomadas', 'Troca e revisao de tomadas e interruptores.', 'Plug', 2),
    (2, 'Iluminacao', 'Ajustes e melhorias em iluminacao interna.', 'Lightbulb', 3),
    (3, 'Pintura interna', 'Acabamento fino para ambientes internos.', 'Paintbrush', 1),
    (3, 'Pintura comercial', 'Atendimento para espacos comerciais.', 'Building2', 2),
    (3, 'Texturas', 'Aplicacao de textura decorativa.', 'Layers', 3),
    (4, 'Reparo de chuveiro', 'Trocas e reparos em chuveiros.', 'ShowerHead', 1),
    (4, 'Manutencao hidraulica', 'Ajustes em encanamento residencial.', 'Wrench', 2),
    (4, 'Troca de torneira', 'Instalacao e troca de torneiras.', 'Droplets', 3),
    (5, 'Alvenaria', 'Pequenas obras e reparos estruturais.', 'BrickWall', 1),
    (5, 'Reformas', 'Reformas residenciais de pequeno porte.', 'Home', 2),
    (5, 'Reparos gerais', 'Correcao e acabamentos.', 'Hammer', 3);

INSERT INTO solicitacao (id, id_cliente, id_profissional, id_servico, status, titulo, descricao, endereco_atendimento, cidade_atendimento, estado_atendimento, valor_estimado, valor_final, data_preferencial, data_conclusao, motivo_cancelamento) VALUES
    (1, 1, 4, 4, 'AGUARDANDO_ORCAMENTO', 'Conserto de vazamento', 'Vazamento em conexao de pia da cozinha.', 'Rua das Flores, 123 - Centro', 'Sao Paulo', 'SP', 80.00, 214.00, '2026-04-02 12:03:00', NULL, NULL),
    (2, 1, 2, 3, 'EM_ANDAMENTO', 'Instalacao de tomada', 'A tomada esta soltando faisca e fica quente quando uso o secador.', 'Rua das Flores, 123 - Centro', 'Sao Paulo', 'SP', 80.00, 180.00, '2026-04-01 14:51:00', NULL, NULL),
    (3, 1, 4, 4, 'APROVADA', 'Instalacao de ar-condicionado', 'Instalacao de ar-condicionado split com verificacao de ponto eletrico.', 'Rua das Flores, 123 - Centro', 'Sao Paulo', 'SP', 200.00, 200.00, '2026-03-29 20:30:00', NULL, NULL),
    (4, 1, 2, 3, 'CONCLUIDA', 'Manutencao eletrica', 'Revisao de pontos eletricos, troca de disjuntor e manutencao preventiva.', 'Rua das Flores, 123 - Centro', 'Sao Paulo', 'SP', 180.00, 412.00, '2025-12-15 08:00:00', '2025-12-15 14:00:00', NULL),
    (5, 1, 3, 2, 'CANCELADA', 'Pintura de parede', 'Pintura completa de sala e quarto.', 'Av. Central, 321 - Bairro Jardim', 'Sao Paulo', 'SP', 450.00, 450.00, '2025-12-15 14:00:00', NULL, 'O profissional cancelou este servico devido a um imprevisto pessoal.');

INSERT INTO solicitacao_anexo (id_solicitacao, nome_arquivo, url_arquivo, tipo_mime) VALUES
    (2, 'tomada-1.jpg', '/uploads/solicitacoes/tomada-1.jpg', 'image/jpeg'),
    (2, 'tomada-2.jpg', '/uploads/solicitacoes/tomada-2.jpg', 'image/jpeg'),
    (3, 'ar-condicionado.jpg', '/uploads/solicitacoes/ar-condicionado.jpg', 'image/jpeg');

INSERT INTO orcamento (id, id_solicitacao, observacoes, total_mao_obra, total_materiais, total_geral, status) VALUES
    (1, 1, 'O reparo inclui identificacao do ponto de vazamento, substituicao das conexoes necessarias e teste final.', 180.00, 34.00, 214.00, 'PENDENTE'),
    (2, 3, 'Instalacao prevista com revisao do ponto eletrico.', 150.00, 50.00, 200.00, 'APROVADO');

INSERT INTO orcamento_item_mao_obra (id_orcamento, descricao, tempo_horas, valor_hora, valor_total) VALUES
    (1, 'Diagnostico e reparo do vazamento', 2.00, 60.00, 120.00),
    (1, 'Teste de pressao e acabamento', 1.00, 60.00, 60.00),
    (2, 'Instalacao do equipamento', 3.00, 50.00, 150.00);

INSERT INTO orcamento_item_material (id_orcamento, nome, quantidade, unidade, valor_unitario, valor_total) VALUES
    (1, 'Conexao PVC', 2.000, 'unidade', 12.00, 24.00),
    (1, 'Fita veda rosca', 1.000, 'unidade', 10.00, 10.00),
    (2, 'Suporte de fixacao', 1.000, 'kit', 50.00, 50.00);

INSERT INTO pagamento (id_solicitacao, valor, metodo, status, transacao_id, data_processamento) VALUES
    (4, 412.00, 'PIX', 'APROVADO', 'PIX-412-OK', '2025-12-15 14:30:00');

INSERT INTO avaliacao (id, id_solicitacao, id_cliente, id_profissional, nota, comentario) VALUES
    (1, 4, 1, 2, 5, 'Excelente profissional! Resolveu o problema rapidamente e deixou tudo limpo.'),
    (2, 2, 1, 2, 4, 'Muito bom! Pontual e eficiente.'),
    (3, 5, 1, 3, 5, 'Acabamento impecavel. Profissional muito cuidadosa e organizada.');

INSERT INTO avaliacao_anexo (id_avaliacao, nome_arquivo, url_arquivo, tipo_mime) VALUES
    (2, 'avaliacao-tomada.jpg', '/uploads/avaliacoes/avaliacao-tomada.jpg', 'image/jpeg');

INSERT INTO chat (id, id_solicitacao, ativo) VALUES
    (1, 2, 1),
    (2, 5, 1),
    (3, 3, 1),
    (4, 1, 1);

INSERT INTO mensagem (id_chat, id_remetente, conteudo, lida, criado_em) VALUES
    (1, 2, 'Ola! Recebi sua solicitacao.', 1, '2026-04-01 14:15:00'),
    (1, 1, 'Perfeito. Voce tem disponibilidade para hoje?', 1, '2026-04-01 14:18:00'),
    (1, 2, 'Tenho sim. Posso passar no fim da tarde.', 1, '2026-04-01 14:21:00'),
    (1, 1, 'Obrigado pelo servico! Ficou perfeito.', 0, '2026-04-02 10:30:00'),
    (2, 3, 'Bom dia! Ja vi sua solicitacao.', 1, '2025-12-08 08:40:00'),
    (2, 3, 'Pode ser retomado em outra data?', 0, '2025-12-08 09:15:00'),
    (3, 4, 'Consigo verificar a instalacao amanha cedo.', 0, '2026-03-29 16:12:00'),
    (4, 4, 'Ola! Vi seu pedido de manutencao hidraulica.', 0, '2026-04-02 16:10:00');

INSERT INTO notificacao (id_usuario, tipo, titulo, mensagem, lida, id_referencia, tipo_referencia) VALUES
    (1, 'ORCAMENTO_RECEBIDO', 'Novo orcamento recebido', 'Voce recebeu um novo orcamento para Conserto de vazamento.', 0, 1, 'orcamento'),
    (1, 'NOVA_MENSAGEM', 'Nova mensagem', 'Joao Silva enviou uma nova mensagem.', 0, 1, 'chat');

SET FOREIGN_KEY_CHECKS = 1;

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
FROM usuario u
JOIN profissional p ON p.id_usuario = u.id
LEFT JOIN categoria_servico c ON c.id = p.id_categoria
WHERE u.ativo = 1
  AND u.tipo = 'PROFISSIONAL';

CREATE OR REPLACE VIEW vw_historico_cliente AS
SELECT
    s.id AS id_solicitacao,
    s.titulo,
    s.status,
    s.valor_final,
    s.criado_em AS data_solicitacao,
    s.data_conclusao,
    s.endereco_atendimento,
    u_prof.nome AS nome_profissional,
    sv.nome AS nome_servico,
    cs.nome AS categoria
FROM solicitacao s
JOIN servico sv ON sv.id = s.id_servico
JOIN categoria_servico cs ON cs.id = sv.id_categoria
LEFT JOIN usuario u_prof ON u_prof.id = s.id_profissional;

CREATE OR REPLACE VIEW vw_orcamento_completo AS
SELECT
    o.id AS id_orcamento,
    o.id_solicitacao,
    o.observacoes,
    o.total_mao_obra,
    o.total_materiais,
    o.total_geral,
    o.status,
    o.criado_em,
    s.titulo AS titulo_servico,
    u_cli.nome AS nome_cliente,
    u_prof.nome AS nome_profissional
FROM orcamento o
JOIN solicitacao s ON s.id = o.id_solicitacao
JOIN usuario u_cli ON u_cli.id = s.id_cliente
LEFT JOIN usuario u_prof ON u_prof.id = s.id_profissional;

CREATE OR REPLACE VIEW vw_chat_resumo AS
SELECT
    ch.id AS id_chat,
    ch.id_solicitacao,
    s.id_profissional,
    u.nome AS nome_profissional,
    SUBSTRING_INDEX(GROUP_CONCAT(m.conteudo ORDER BY m.criado_em DESC SEPARATOR '||'), '||', 1) AS ultima_mensagem,
    MAX(m.criado_em) AS data_ultima_mensagem,
    SUM(CASE WHEN m.lida = 0 THEN 1 ELSE 0 END) AS mensagens_nao_lidas
FROM chat ch
JOIN solicitacao s ON s.id = ch.id_solicitacao
LEFT JOIN usuario u ON u.id = s.id_profissional
LEFT JOIN mensagem m ON m.id_chat = ch.id
GROUP BY ch.id, ch.id_solicitacao, s.id_profissional, u.nome;

CREATE OR REPLACE VIEW vw_profissional_perfil AS
SELECT
    u.id,
    u.nome,
    u.email,
    u.telefone,
    u.foto_url,
    p.tipo_documento,
    p.documento,
    p.especialidade,
    p.descricao_perfil,
    p.avaliacao_media,
    p.total_avaliacoes,
    p.disponivel,
    p.cidade,
    p.estado,
    p.raio_atendimento_km,
    c.id AS id_categoria,
    c.nome AS categoria_principal,
    c.icone,
    c.grupo
FROM usuario u
JOIN profissional p ON p.id_usuario = u.id
LEFT JOIN categoria_servico c ON c.id = p.id_categoria
WHERE u.tipo = 'PROFISSIONAL';
