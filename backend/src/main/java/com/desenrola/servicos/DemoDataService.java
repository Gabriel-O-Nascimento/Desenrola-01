package com.desenrola.servicos;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DemoDataService {

    private static final long DEFAULT_CLIENT_ID = 1L;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy 'as' HH:mm");

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DemoDataService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    public Map<String, Object> health() {
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        Integer ping = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return map(
            "status", Objects.equals(ping, 1) ? "OK" : "ERROR",
            "message", "Desenrola backend conectado ao MySQL",
            "database", databaseName
        );
    }

    public Map<String, Object> login(Map<String, Object> payload) {
        String email = text(payload.get("email")).toLowerCase(Locale.ROOT);
        String password = text(payload.get("password"));

        Map<String, Object> user = querySingle(
            """
            SELECT u.id, u.nome, u.email, u.senha_hash, u.telefone, u.tipo,
                   c.endereco, c.cidade, c.estado
            FROM usuario u
            LEFT JOIN cliente c ON c.id_usuario = u.id
            WHERE LOWER(u.email) = :email
            """,
            params("email", email)
        );

        if (user == null || !passwordMatches(password, text(user.get("senha_hash")))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos.");
        }

        Long professionalId = "PROFISSIONAL".equals(user.get("tipo"))
            ? parseId(user.get("id"))
            : null;

        return map(
            "token", "db-token-" + user.get("id"),
            "user", map(
                "id", user.get("id"),
                "name", user.get("nome"),
                "email", user.get("email"),
                "phone", user.get("telefone"),
                "address", fullAddress(text(user.get("endereco")), text(user.get("cidade")), text(user.get("estado"))),
                "accountType", humanAccountType(text(user.get("tipo"))),
                "professionalId", professionalId
            )
        );
    }

    @Transactional
    public Map<String, Object> registerUser(Map<String, Object> payload) {
        String email = text(payload.get("email")).toLowerCase(Locale.ROOT);
        ensureEmailAvailable(email);

        Long userId = insertUsuario(
            text(payload.get("fullName")),
            email,
            text(payload.get("password")),
            text(payload.get("phone")),
            "CLIENTE"
        );

        namedJdbcTemplate.update(
            """
            INSERT INTO cliente (id_usuario, cpf, endereco, cidade, estado, criado_em, atualizado_em)
            VALUES (:idUsuario, :cpf, :endereco, :cidade, :estado, NOW(), NOW())
            """,
            params(
                "idUsuario", userId,
                "cpf", nullIfBlank(text(payload.get("cpf"))),
                "endereco", text(payload.get("city")) + " - " + text(payload.get("state")),
                "cidade", text(payload.get("city")),
                "estado", text(payload.get("state"))
            )
        );

        return map(
            "message", "Cadastro de usuario realizado com sucesso.",
            "user", getProfile(userId)
        );
    }

    @Transactional
    public Map<String, Object> registerProfessional(Map<String, Object> payload) {
        String email = text(payload.get("email")).toLowerCase(Locale.ROOT);
        ensureEmailAvailable(email);

        Long userId = insertUsuario(
            text(payload.get("fullName")),
            email,
            text(payload.get("password")),
            text(payload.get("phone")),
            "PROFISSIONAL"
        );

        long categoryId = parseId(payload.get("mainCategory"));
        namedJdbcTemplate.update(
            """
            INSERT INTO profissional (
                id_usuario, documento, tipo_documento, id_categoria, especialidade, descricao_perfil,
                disponivel, cidade, estado, raio_atendimento_km, criado_em, atualizado_em
            )
            VALUES (
                :idUsuario, :documento, :tipoDocumento, :idCategoria, :especialidade, :descricaoPerfil,
                1, :cidade, :estado, 10, NOW(), NOW()
            )
            """,
            params(
                "idUsuario", userId,
                "documento", resolveDocument(payload),
                "tipoDocumento", text(payload.get("documentType")).toUpperCase(Locale.ROOT),
                "idCategoria", categoryId,
                "especialidade", text(payload.get("serviceDescription")),
                "descricaoPerfil", blankToNull(text(payload.get("professionalExperience")), text(payload.get("serviceDescription"))),
                "cidade", text(payload.get("city")),
                "estado", text(payload.get("state"))
            )
        );

        Long serviceId = querySingleValue(
            """
            SELECT s.id
            FROM servico s
            WHERE s.id_categoria = :idCategoria
            ORDER BY s.id
            LIMIT 1
            """,
            params("idCategoria", categoryId),
            Long.class
        );

        if (serviceId != null) {
            namedJdbcTemplate.update(
                """
                INSERT INTO profissional_servico (id_profissional, id_servico, ativo, criado_em)
                VALUES (:idProfissional, :idServico, 1, NOW())
                """,
                params("idProfissional", userId, "idServico", serviceId)
            );
        }

        namedJdbcTemplate.update(
            """
            INSERT INTO profissional_portfolio (id_profissional, titulo, descricao, icone, ordem_exibicao, criado_em)
            VALUES
            (:idProfissional, 'Atendimento profissional', 'Novo cadastro aprovado para atendimento.', 'Briefcase', 1, NOW()),
            (:idProfissional, 'Servico sob demanda', 'Disponivel para novos chamados do app.', 'Wrench', 2, NOW()),
            (:idProfissional, 'Novo cadastro', 'Perfil criado e pronto para receber solicitacoes.', 'CheckCircle', 3, NOW())
            """,
            params("idProfissional", userId)
        );

        return map(
            "message", "Cadastro profissional realizado com sucesso.",
            "user", getProfile(userId),
            "professional", getProfessionalById(userId)
        );
    }

    public List<Map<String, Object>> getCategories() {
        return namedJdbcTemplate.query(
            """
            SELECT id, nome, descricao, icone, grupo
            FROM categoria_servico
            WHERE ativo = 1
            ORDER BY id
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "nome", rs.getString("nome"),
                "descricao", rs.getString("descricao"),
                "icone", rs.getString("icone"),
                "grupo", rs.getString("grupo")
            )
        );
    }

    public List<Map<String, Object>> getHomeSections() {
        List<Map<String, Object>> rows = namedJdbcTemplate.query(
            """
            SELECT c.grupo, c.nome AS categoria_nome, s.id, s.nome, s.descricao, c.icone
            FROM servico s
            JOIN categoria_servico c ON c.id = s.id_categoria
            WHERE s.ativo = 1 AND c.ativo = 1
            ORDER BY c.id, s.id
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> map(
                "group", rs.getString("grupo"),
                "category", rs.getString("categoria_nome"),
                "id", rs.getLong("id"),
                "name", rs.getString("nome"),
                "description", rs.getString("descricao"),
                "iconName", rs.getString("icone")
            )
        );

        Map<String, List<Map<String, Object>>> grouped = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            grouped.computeIfAbsent(text(row.get("group")), key -> new ArrayList<>())
                .add(map(
                    "id", row.get("id"),
                    "name", row.get("name"),
                    "description", row.get("description"),
                    "iconName", row.get("iconName")
                ));
        }

        return grouped.entrySet().stream()
            .map(entry -> map("title", entry.getKey(), "items", entry.getValue()))
            .collect(Collectors.toList());
    }

    public Map<String, Object> getSearchData() {
        List<Map<String, Object>> professionals = namedJdbcTemplate.query(
            """
            SELECT id, nome, categoria_principal, avaliacao_media
            FROM vw_profissional_resumo
            ORDER BY avaliacao_media DESC, nome
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "name", rs.getString("nome"),
                "initials", initials(rs.getString("nome")),
                "category", blankToNull(rs.getString("categoria_principal"), rs.getString("categoria_principal")),
                "rating", rs.getBigDecimal("avaliacao_media")
            )
        );

        List<Map<String, Object>> services = namedJdbcTemplate.query(
            """
            SELECT s.id, s.nome, s.descricao, c.icone
            FROM servico s
            JOIN categoria_servico c ON c.id = s.id_categoria
            ORDER BY s.id
            LIMIT 4
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "name", rs.getString("nome"),
                "description", rs.getString("descricao"),
                "iconName", rs.getString("icone")
            )
        );

        return map("professionals", professionals, "services", services);
    }

    public List<Map<String, Object>> getProfessionals() {
        return namedJdbcTemplate.query(
            """
            SELECT vp.id, vp.nome, vp.categoria_principal, vp.especialidade, vp.avaliacao_media, vp.descricao_perfil,
                   vp.raio_atendimento_km,
                   vp.icone, vp.grupo,
                   COALESCE((SELECT ps.id_servico FROM profissional_servico ps WHERE ps.id_profissional = vp.id ORDER BY ps.id_servico LIMIT 1), 1) AS service_id,
                   COALESCE((SELECT COUNT(*) FROM solicitacao s WHERE s.id_profissional = vp.id), 0) AS services_count
            FROM vw_profissional_perfil vp
            ORDER BY vp.avaliacao_media DESC, vp.nome
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> professionalSummary(rs)
        );
    }

    public Map<String, Object> getProfessionalById(long professionalId) {
        Map<String, Object> professional = querySingle(
            """
            SELECT vp.*, 
                   COALESCE((SELECT ps.id_servico FROM profissional_servico ps WHERE ps.id_profissional = vp.id ORDER BY ps.id_servico LIMIT 1), 1) AS service_id,
                   COALESCE((SELECT COUNT(*) FROM solicitacao s WHERE s.id_profissional = vp.id), 0) AS services_count
            FROM vw_profissional_perfil vp
            WHERE vp.id = :id
            """,
            params("id", professionalId)
        );

        if (professional == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional nao encontrado.");
        }

        List<Map<String, Object>> works = namedJdbcTemplate.query(
            """
            SELECT id, titulo, descricao, icone
            FROM profissional_portfolio
            WHERE id_profissional = :idProfissional
            ORDER BY ordem_exibicao, id
            """,
            params("idProfissional", professionalId),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "title", rs.getString("titulo"),
                "description", rs.getString("descricao"),
                "iconName", rs.getString("icone")
            )
        );

        List<Map<String, Object>> reviews = namedJdbcTemplate.query(
            """
            SELECT a.id, u.nome AS client_name, a.criado_em, a.nota, a.comentario,
                   CASE WHEN aa.id IS NOT NULL THEN 'Camera' ELSE NULL END AS image_icon_name
            FROM avaliacao a
            JOIN usuario u ON u.id = a.id_cliente
            LEFT JOIN avaliacao_anexo aa ON aa.id_avaliacao = a.id
            WHERE a.id_profissional = :idProfissional
            ORDER BY a.criado_em DESC
            """,
            params("idProfissional", professionalId),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "clientName", rs.getString("client_name"),
                "date", formatDate(rs.getTimestamp("criado_em")),
                "rating", rs.getInt("nota"),
                "comment", rs.getString("comentario"),
                "imageIconName", rs.getString("image_icon_name")
            )
        );

        return map(
            "id", professional.get("id"),
            "name", professional.get("nome"),
            "initials", initials(text(professional.get("nome"))),
            "profession", blankToNull(text(professional.get("especialidade")), text(professional.get("categoria_principal"))),
            "category", professional.get("categoria_principal"),
            "categoryId", professional.get("id_categoria"),
            "serviceId", professional.get("service_id"),
            "verified", true,
            "rating", professional.get("avaliacao_media"),
            "servicesCount", professional.get("services_count"),
            "distance", text(professional.get("raio_atendimento_km")) + " km",
            "responseTime", "1h",
            "about", professional.get("descricao_perfil"),
            "works", works,
            "reviews", reviews
        );
    }

    public Map<String, Object> getProfile(Long userId) {
        long resolvedUserId = userId != null ? userId : DEFAULT_CLIENT_ID;
        Map<String, Object> user = querySingle(
            """
            SELECT u.id, u.nome, u.email, u.telefone, u.tipo, c.endereco, c.cidade, c.estado
            FROM usuario u
            LEFT JOIN cliente c ON c.id_usuario = u.id
            WHERE u.id = :id
            """,
            params("id", resolvedUserId)
        );

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado.");
        }

        return map(
            "id", user.get("id"),
            "name", user.get("nome"),
            "phone", user.get("telefone"),
            "address", fullAddress(text(user.get("endereco")), text(user.get("cidade")), text(user.get("estado"))),
            "email", user.get("email"),
            "accountType", humanAccountType(text(user.get("tipo")))
        );
    }

    public List<Map<String, Object>> getChats() {
        return namedJdbcTemplate.query(
            """
            SELECT id_chat, id_profissional, nome_profissional, ultima_mensagem, data_ultima_mensagem, mensagens_nao_lidas
            FROM vw_chat_resumo
            ORDER BY data_ultima_mensagem DESC, id_chat DESC
            """,
            new MapSqlParameterSource(),
            (rs, rowNum) -> map(
                "id", rs.getLong("id_chat"),
                "professionalId", rs.getLong("id_profissional"),
                "name", rs.getString("nome_profissional"),
                "initials", initials(rs.getString("nome_profissional")),
                "status", "Online agora",
                "lastMessage", rs.getString("ultima_mensagem"),
                "lastMessageTime", formatChatTime(rs.getTimestamp("data_ultima_mensagem")),
                "unreadCount", rs.getLong("mensagens_nao_lidas")
            )
        );
    }

    @Transactional
    public Map<String, Object> getChat(Long chatId, Long professionalId) {
        long resolvedChatId = professionalId != null
            ? ensureChatByProfessional(professionalId)
            : Objects.requireNonNull(chatId);

        Map<String, Object> header = querySingle(
            """
            SELECT ch.id, s.id_profissional, u.nome AS professional_name
            FROM chat ch
            JOIN solicitacao s ON s.id = ch.id_solicitacao
            JOIN usuario u ON u.id = s.id_profissional
            WHERE ch.id = :chatId
            """,
            params("chatId", resolvedChatId)
        );

        if (header == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversa nao encontrada.");
        }

        List<Map<String, Object>> messages = namedJdbcTemplate.query(
            """
            SELECT id, id_remetente, conteudo, criado_em
            FROM mensagem
            WHERE id_chat = :chatId
            ORDER BY criado_em
            """,
            params("chatId", resolvedChatId),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "type", rs.getLong("id_remetente") == DEFAULT_CLIENT_ID ? "user" : "professional",
                "text", rs.getString("conteudo"),
                "time", formatTime(rs.getTimestamp("criado_em")),
                "dateLabel", chatDateLabel(rs.getTimestamp("criado_em"))
            )
        );

        return map(
            "id", header.get("id"),
            "professionalId", header.get("id_profissional"),
            "name", header.get("professional_name"),
            "initials", initials(text(header.get("professional_name"))),
            "status", "Online agora",
            "lastMessage", messages.isEmpty() ? "" : messages.get(messages.size() - 1).get("text"),
            "lastMessageTime", messages.isEmpty() ? "" : messages.get(messages.size() - 1).get("time"),
            "unreadCount", 0,
            "messages", messages
        );
    }

    @Transactional
    public Map<String, Object> sendMessage(Long chatId, Long professionalId, Map<String, Object> payload) {
        String messageText = text(payload.get("text"));
        if (messageText.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mensagem vazia.");
        }

        long resolvedChatId = professionalId != null
            ? ensureChatByProfessional(professionalId)
            : Objects.requireNonNull(chatId);

        namedJdbcTemplate.update(
            """
            INSERT INTO mensagem (id_chat, id_remetente, conteudo, lida, criado_em)
            VALUES (:chatId, :idRemetente, :conteudo, 0, NOW())
            """,
            params("chatId", resolvedChatId, "idRemetente", DEFAULT_CLIENT_ID, "conteudo", messageText)
        );

        return getChat(resolvedChatId, null);
    }

    public List<Map<String, Object>> getServiceHistory() {
        return namedJdbcTemplate.query(
            """
            SELECT s.id, s.titulo, s.status, s.endereco_atendimento, s.valor_estimado, s.valor_final,
                   s.data_preferencial, u.nome AS professional_name, cs.nome AS category_name
            FROM solicitacao s
            JOIN servico sv ON sv.id = s.id_servico
            JOIN categoria_servico cs ON cs.id = sv.id_categoria
            LEFT JOIN usuario u ON u.id = s.id_profissional
            WHERE s.id_cliente = :clientId
            ORDER BY s.id DESC
            """,
            params("clientId", DEFAULT_CLIENT_ID),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "title", rs.getString("titulo"),
                "professionalName", rs.getString("professional_name"),
                "date", formatDate(rs.getTimestamp("data_preferencial")),
                "time", formatTime(rs.getTimestamp("data_preferencial")),
                "address", rs.getString("endereco_atendimento"),
                "estimatedValue", formatCurrency(coalesce(rs.getBigDecimal("valor_final"), rs.getBigDecimal("valor_estimado"))),
                "category", rs.getString("category_name"),
                "status", frontendStatus(rs.getString("status"))
            )
        );
    }

    public Map<String, Object> getBudgetById(long id) {
        Map<String, Object> budget = querySingle(
            """
            SELECT o.id, o.id_solicitacao, o.observacoes, o.total_mao_obra, o.total_materiais, o.total_geral,
                   s.titulo, s.data_preferencial, s.endereco_atendimento, u.nome AS professional_name
            FROM orcamento o
            JOIN solicitacao s ON s.id = o.id_solicitacao
            LEFT JOIN usuario u ON u.id = s.id_profissional
            WHERE s.id = :solicitacaoId
            """,
            params("solicitacaoId", id)
        );

        if (budget == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orcamento nao encontrado.");
        }

        List<Map<String, Object>> labor = namedJdbcTemplate.query(
            """
            SELECT id, descricao, tempo_horas, valor_hora, valor_total
            FROM orcamento_item_mao_obra
            WHERE id_orcamento = :orcamentoId
            ORDER BY id
            """,
            params("orcamentoId", budget.get("id")),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "name", rs.getString("descricao"),
                "time", rs.getBigDecimal("tempo_horas").stripTrailingZeros().toPlainString() + " horas",
                "totalValue", formatCurrency(rs.getBigDecimal("valor_total")),
                "hourlyValue", formatCurrency(rs.getBigDecimal("valor_hora")) + "/h"
            )
        );

        List<Map<String, Object>> materials = namedJdbcTemplate.query(
            """
            SELECT id, nome, quantidade, unidade, valor_unitario, valor_total
            FROM orcamento_item_material
            WHERE id_orcamento = :orcamentoId
            ORDER BY id
            """,
            params("orcamentoId", budget.get("id")),
            (rs, rowNum) -> map(
                "id", rs.getLong("id"),
                "name", rs.getString("nome"),
                "quantity", rs.getBigDecimal("quantidade").stripTrailingZeros().toPlainString() + " " + text(rs.getString("unidade")),
                "totalValue", formatCurrency(rs.getBigDecimal("valor_total")),
                "unitValue", formatCurrency(rs.getBigDecimal("valor_unitario"))
            )
        );

        return map(
            "id", budget.get("id_solicitacao"),
            "service", map(
                "name", budget.get("titulo"),
                "date", formatDate(budget.get("data_preferencial")),
                "time", formatTime(budget.get("data_preferencial")),
                "address", budget.get("endereco_atendimento")
            ),
            "professional", map(
                "name", budget.get("professional_name"),
                "initials", initials(text(budget.get("professional_name")))
            ),
            "labor", labor,
            "materials", materials,
            "summary", map(
                "laborTotal", formatCurrency((BigDecimal) budget.get("total_mao_obra")),
                "materialsTotal", formatCurrency((BigDecimal) budget.get("total_materiais")),
                "serviceTotal", formatCurrency((BigDecimal) budget.get("total_geral"))
            ),
            "observation", budget.get("observacoes")
        );
    }

    @Transactional
    public Map<String, Object> decideBudget(long id, Map<String, Object> payload) {
        String decision = text(payload.get("decision")).toLowerCase(Locale.ROOT);

        if ("approve".equals(decision)) {
            namedJdbcTemplate.update(
                "UPDATE orcamento SET status = 'APROVADO', atualizado_em = NOW() WHERE id_solicitacao = :id",
                params("id", id)
            );
            namedJdbcTemplate.update(
                "UPDATE solicitacao SET status = 'APROVADA', atualizado_em = NOW() WHERE id = :id",
                params("id", id)
            );
            return map("message", "Orcamento aprovado com sucesso.");
        }

        if ("reject".equals(decision)) {
            namedJdbcTemplate.update(
                "UPDATE orcamento SET status = 'RECUSADO', atualizado_em = NOW() WHERE id_solicitacao = :id",
                params("id", id)
            );
            namedJdbcTemplate.update(
                "UPDATE solicitacao SET status = 'CANCELADA', motivo_cancelamento = 'O orcamento foi recusado pelo cliente.', atualizado_em = NOW() WHERE id = :id",
                params("id", id)
            );
            return map("message", "Orcamento recusado.");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decisao invalida.");
    }

    public Map<String, Object> getTrackingById(long id) {
        Map<String, Object> service = querySingle(
            """
            SELECT s.id, s.titulo, s.status, s.descricao, s.endereco_atendimento, s.cidade_atendimento, s.estado_atendimento,
                   s.data_preferencial, s.criado_em, u.id AS professional_id, u.nome AS professional_name,
                   p.especialidade
            FROM solicitacao s
            LEFT JOIN usuario u ON u.id = s.id_profissional
            LEFT JOIN profissional p ON p.id_usuario = u.id
            WHERE s.id = :id
            """,
            params("id", id)
        );

        if (service == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Acompanhamento nao encontrado.");
        }

        List<Map<String, Object>> photos = namedJdbcTemplate.query(
            "SELECT id FROM solicitacao_anexo WHERE id_solicitacao = :id ORDER BY id",
            params("id", id),
            (rs, rowNum) -> map("id", rs.getLong("id"), "iconName", "Camera")
        );

        return map(
            "id", service.get("id"),
            "serviceCode", serviceCode(id),
            "title", service.get("titulo"),
            "status", frontendStatus(text(service.get("status"))),
            "professionalId", service.get("professional_id"),
            "professionalName", service.get("professional_name"),
            "professionalInitials", initials(text(service.get("professional_name"))),
            "professionalCategory", service.get("especialidade"),
            "date", formatDate(service.get("data_preferencial")),
            "time", formatTime(service.get("data_preferencial")),
            "address", fullAddress(text(service.get("endereco_atendimento")), text(service.get("cidade_atendimento")), text(service.get("estado_atendimento"))),
            "description", service.get("descricao"),
            "sentAt", formatDateTime(service.get("criado_em")),
            "photos", photos,
            "timeline", buildTimeline(text(service.get("status")))
        );
    }

    public Map<String, Object> getCompletedServiceById(long id) {
        Map<String, Object> service = querySingle(
            """
            SELECT s.id, s.titulo, s.descricao, s.endereco_atendimento, s.cidade_atendimento, s.estado_atendimento,
                   s.criado_em, s.data_conclusao, u.id AS professional_id, u.nome AS professional_name,
                   p.especialidade, p.avaliacao_media, pay.valor, pay.metodo, pay.status AS payment_status
            FROM solicitacao s
            LEFT JOIN usuario u ON u.id = s.id_profissional
            LEFT JOIN profissional p ON p.id_usuario = u.id
            LEFT JOIN pagamento pay ON pay.id_solicitacao = s.id
            WHERE s.id = :id
            """,
            params("id", id)
        );

        if (service == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico concluido nao encontrado.");
        }

        List<Map<String, Object>> photos = namedJdbcTemplate.query(
            "SELECT id FROM solicitacao_anexo WHERE id_solicitacao = :id ORDER BY id",
            params("id", id),
            (rs, rowNum) -> map("id", rs.getLong("id"), "label", "Foto " + (rowNum + 1), "iconName", "Image")
        );

        return map(
            "id", service.get("id"),
            "professionalId", service.get("professional_id"),
            "professionalName", service.get("professional_name"),
            "professionalInitials", initials(text(service.get("professional_name"))),
            "professionalCategory", service.get("especialidade"),
            "professionalRating", coalesce((BigDecimal) service.get("avaliacao_media"), BigDecimal.ZERO),
            "title", service.get("titulo"),
            "category", service.get("especialidade"),
            "startDate", formatDate(service.get("criado_em")),
            "conclusionDate", formatDate(service.get("data_conclusao")),
            "dateTime", formatDateTime(service.get("data_conclusao")),
            "address", fullAddress(text(service.get("endereco_atendimento")), text(service.get("cidade_atendimento")), text(service.get("estado_atendimento"))),
            "description", service.get("descricao"),
            "photos", photos,
            "financialSummary", map(
                "labor", formatCurrency(coalesce(queryBudgetAmount(id, "total_mao_obra"), BigDecimal.ZERO)),
                "materials", formatCurrency(coalesce(queryBudgetAmount(id, "total_materiais"), BigDecimal.ZERO)),
                "discount", "R$ 0,00",
                "totalPaid", formatCurrency(coalesce((BigDecimal) service.get("valor"), BigDecimal.ZERO))
            ),
            "paymentMethod", map(
                "type", text(service.get("metodo")),
                "status", paymentStatus(text(service.get("payment_status")))
            ),
            "finalNotes", "Servico executado e registrado no sistema."
        );
    }

    public Map<String, Object> getCancelledServiceById(long id) {
        Map<String, Object> service = querySingle(
            """
            SELECT s.id, s.titulo, s.descricao, s.endereco_atendimento, s.valor_estimado, s.data_preferencial,
                   s.motivo_cancelamento, u_cliente.nome AS client_name, u_prof.nome AS professional_name,
                   cs.nome AS category_name, sv.id_categoria
            FROM solicitacao s
            JOIN usuario u_cliente ON u_cliente.id = s.id_cliente
            LEFT JOIN usuario u_prof ON u_prof.id = s.id_profissional
            JOIN servico sv ON sv.id = s.id_servico
            JOIN categoria_servico cs ON cs.id = sv.id_categoria
            WHERE s.id = :id
            """,
            params("id", id)
        );

        if (service == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Servico cancelado nao encontrado.");
        }

        return map(
            "id", service.get("id"),
            "professionalId", querySingleValue("SELECT id_profissional FROM solicitacao WHERE id = :id", params("id", id), Long.class),
            "serviceId", service.get("id"),
            "categoryId", service.get("id_categoria"),
            "serviceCode", serviceCode(id),
            "status", "cancelado",
            "serviceType", service.get("titulo"),
            "category", service.get("category_name"),
            "clientName", service.get("client_name"),
            "professionalName", service.get("professional_name"),
            "requestDate", formatDate(service.get("data_preferencial")),
            "cancellationDate", LocalDate.now().format(DATE_FORMAT),
            "address", service.get("endereco_atendimento"),
            "estimatedValue", formatCurrency(coalesce((BigDecimal) service.get("valor_estimado"), BigDecimal.ZERO)),
            "description", service.get("descricao"),
            "cancellationReason", blankToNull(text(service.get("motivo_cancelamento")), "Servico cancelado."),
            "history", List.of(
                map("id", 1, "label", "Servico Solicitado", "date", formatDate(service.get("data_preferencial")), "status", "success"),
                map("id", 2, "label", "Em Analise", "date", formatDate(service.get("data_preferencial")), "status", "info"),
                map("id", 3, "label", "Cancelado", "date", LocalDate.now().format(DATE_FORMAT), "status", "danger")
            )
        );
    }

    @Transactional
    public Map<String, Object> createServiceRequest(Map<String, Object> payload) {
        Long professionalId = nullableNumber(payload.get("professionalId"));
        long serviceId = parseId(payload.get("serviceId"));
        String requestedTime = text(payload.get("time"));
        LocalTime preferredTime = requestedTime.isBlank() ? LocalTime.of(9, 0) : LocalTime.parse(requestedTime);
        LocalDateTime preferredDateTime = LocalDate.parse(text(payload.get("date"))).atTime(preferredTime);

        String serviceName = querySingleValue("SELECT nome FROM servico WHERE id = :id", params("id", serviceId), String.class);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(
            """
            INSERT INTO solicitacao (
                id_cliente, id_profissional, id_servico, status, titulo, descricao,
                endereco_atendimento, cidade_atendimento, estado_atendimento, valor_estimado,
                data_preferencial, observacoes, criado_em, atualizado_em
            )
            VALUES (
                :idCliente, :idProfissional, :idServico, 'AGUARDANDO_ORCAMENTO', :titulo, :descricao,
                :endereco, :cidade, 'SP', 120.00, :dataPreferencial, :observacoes, NOW(), NOW()
            )
            """,
            params(
                "idCliente", DEFAULT_CLIENT_ID,
                "idProfissional", professionalId,
                "idServico", serviceId,
                "titulo", serviceName,
                "descricao", text(payload.get("description")),
                "endereco", text(payload.get("address")),
                "cidade", text(payload.get("city")),
                "dataPreferencial", Timestamp.valueOf(preferredDateTime),
                "observacoes", text(payload.get("observations"))
            ),
            keyHolder,
            new String[]{"id"}
        );

        long solicitacaoId = requireGeneratedId(keyHolder);

        @SuppressWarnings("unchecked")
        List<Object> photoNames = payload.get("photoNames") instanceof List<?> list ? new ArrayList<>(list) : List.of();
        for (Object photoName : photoNames) {
            String filename = text(photoName);
            if (!filename.isBlank()) {
                namedJdbcTemplate.update(
                    """
                    INSERT INTO solicitacao_anexo (id_solicitacao, nome_arquivo, url_arquivo, tipo_mime, criado_em)
                    VALUES (:idSolicitacao, :nomeArquivo, :urlArquivo, 'image/jpeg', NOW())
                    """,
                    params(
                        "idSolicitacao", solicitacaoId,
                        "nomeArquivo", filename,
                        "urlArquivo", "/uploads/solicitacoes/" + filename
                    )
                );
            }
        }

        if (professionalId != null) {
            namedJdbcTemplate.update(
                """
                INSERT INTO chat (id_solicitacao, ativo, criado_em)
                VALUES (:idSolicitacao, 1, NOW())
                ON DUPLICATE KEY UPDATE ativo = VALUES(ativo)
                """,
                params("idSolicitacao", solicitacaoId)
            );
        }

        return map(
            "message", "Solicitacao enviada com sucesso.",
            "serviceRequest", querySingle(
                """
                SELECT s.id, s.titulo, u.nome AS professional_name, s.data_preferencial, s.endereco_atendimento
                FROM solicitacao s
                LEFT JOIN usuario u ON u.id = s.id_profissional
                WHERE s.id = :id
                """,
                params("id", solicitacaoId),
                (rs, rowNum) -> map(
                    "id", rs.getLong("id"),
                    "title", rs.getString("titulo"),
                    "professionalName", rs.getString("professional_name"),
                    "date", formatDate(rs.getTimestamp("data_preferencial")),
                    "time", formatTime(rs.getTimestamp("data_preferencial")),
                    "address", rs.getString("endereco_atendimento"),
                    "estimatedValue", "R$ 120,00",
                    "category", serviceName,
                    "status", "aguardando_orcamento"
                )
            )
        );
    }

    @Transactional
    public Map<String, Object> submitReview(Map<String, Object> payload) {
        long serviceId = parseId(payload.get("serviceId"));
        long professionalId = parseId(payload.get("professionalId"));
        int rating = Integer.parseInt(text(payload.get("rating")));
        String comment = text(payload.get("comment"));
        String photoName = text(payload.get("photoName"));

        Long existingReviewId = querySingleValue(
            "SELECT id FROM avaliacao WHERE id_solicitacao = :idSolicitacao",
            params("idSolicitacao", serviceId),
            Long.class
        );

        long reviewId;
        if (existingReviewId == null) {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            namedJdbcTemplate.update(
                """
                INSERT INTO avaliacao (id_solicitacao, id_cliente, id_profissional, nota, comentario, criado_em, atualizado_em)
                VALUES (:idSolicitacao, :idCliente, :idProfissional, :nota, :comentario, NOW(), NOW())
                """,
                params(
                    "idSolicitacao", serviceId,
                    "idCliente", DEFAULT_CLIENT_ID,
                    "idProfissional", professionalId,
                    "nota", rating,
                    "comentario", comment
                ),
                keyHolder,
                new String[]{"id"}
            );
            reviewId = requireGeneratedId(keyHolder);
        } else {
            reviewId = existingReviewId;
            namedJdbcTemplate.update(
                """
                UPDATE avaliacao
                SET nota = :nota, comentario = :comentario, atualizado_em = NOW()
                WHERE id = :id
                """,
                params("nota", rating, "comentario", comment, "id", reviewId)
            );
            namedJdbcTemplate.update("DELETE FROM avaliacao_anexo WHERE id_avaliacao = :id", params("id", reviewId));
        }

        if (!photoName.isBlank()) {
            namedJdbcTemplate.update(
                """
                INSERT INTO avaliacao_anexo (id_avaliacao, nome_arquivo, url_arquivo, tipo_mime, criado_em)
                VALUES (:idAvaliacao, :nomeArquivo, :urlArquivo, 'image/jpeg', NOW())
                """,
                params(
                    "idAvaliacao", reviewId,
                    "nomeArquivo", photoName,
                    "urlArquivo", "/uploads/avaliacoes/" + photoName
                )
            );
        }

        return map("message", "Avaliacao enviada com sucesso.");
    }

    private Map<String, Object> professionalSummary(ResultSet rs) throws SQLException {
        return map(
            "id", rs.getLong("id"),
            "name", rs.getString("nome"),
            "initials", initials(rs.getString("nome")),
            "profession", blankToNull(rs.getString("especialidade"), rs.getString("categoria_principal")),
            "category", rs.getString("categoria_principal"),
            "categoryId", null,
            "serviceId", rs.getLong("service_id"),
            "verified", true,
            "rating", rs.getBigDecimal("avaliacao_media"),
            "servicesCount", rs.getLong("services_count"),
            "distance", text(rs.getObject("raio_atendimento_km")) + " km",
            "responseTime", "1h",
            "about", rs.getString("descricao_perfil")
        );
    }

    private Long insertUsuario(String nome, String email, String password, String telefone, String tipo) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(
            """
            INSERT INTO usuario (nome, email, senha_hash, telefone, tipo, ativo, email_verificado, criado_em, atualizado_em)
            VALUES (:nome, :email, :senhaHash, :telefone, :tipo, 1, 1, NOW(), NOW())
            """,
            params(
                "nome", nome,
                "email", email,
                "senhaHash", passwordEncoder.encode(password),
                "telefone", telefone,
                "tipo", tipo
            ),
            keyHolder,
            new String[]{"id"}
        );
        return requireGeneratedId(keyHolder);
    }

    private void ensureEmailAvailable(String email) {
        Long count = querySingleValue(
            "SELECT COUNT(*) FROM usuario WHERE LOWER(email) = :email",
            params("email", email),
            Long.class
        );
        if (count != null && count > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe uma conta com este e-mail.");
        }
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (storedPassword.startsWith("$2")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        return Objects.equals(rawPassword, storedPassword);
    }

    @Transactional
    private long ensureChatByProfessional(long professionalId) {
        Long existingChatId = querySingleValue(
            """
            SELECT ch.id
            FROM chat ch
            JOIN solicitacao s ON s.id = ch.id_solicitacao
            WHERE s.id_profissional = :professionalId
              AND s.id_cliente = :clientId
            ORDER BY s.id DESC
            LIMIT 1
            """,
            params("professionalId", professionalId, "clientId", DEFAULT_CLIENT_ID),
            Long.class
        );

        if (existingChatId != null) {
            return existingChatId;
        }

        Long solicitacaoId = querySingleValue(
            """
            SELECT id
            FROM solicitacao
            WHERE id_profissional = :professionalId
              AND id_cliente = :clientId
            ORDER BY id DESC
            LIMIT 1
            """,
            params("professionalId", professionalId, "clientId", DEFAULT_CLIENT_ID),
            Long.class
        );

        if (solicitacaoId == null) {
            Long serviceId = querySingleValue(
                """
                SELECT id_servico
                FROM profissional_servico
                WHERE id_profissional = :professionalId
                ORDER BY id_servico
                LIMIT 1
                """,
                params("professionalId", professionalId),
                Long.class
            );

            if (serviceId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profissional sem servico vinculado.");
            }

            GeneratedKeyHolder requestKey = new GeneratedKeyHolder();
            String serviceName = querySingleValue("SELECT nome FROM servico WHERE id = :id", params("id", serviceId), String.class);
            namedJdbcTemplate.update(
                """
                INSERT INTO solicitacao (
                    id_cliente, id_profissional, id_servico, status, titulo, descricao,
                    endereco_atendimento, cidade_atendimento, estado_atendimento, valor_estimado,
                    data_preferencial, criado_em, atualizado_em
                )
                VALUES (
                    :idCliente, :idProfissional, :idServico, 'PENDENTE', :titulo, 'Conversa iniciada pelo chat.',
                    'Endereco a definir', 'Sao Paulo', 'SP', 0.00, NOW(), NOW(), NOW()
                )
                """,
                params(
                    "idCliente", DEFAULT_CLIENT_ID,
                    "idProfissional", professionalId,
                    "idServico", serviceId,
                    "titulo", blankToNull(serviceName, "Conversa iniciada")
                ),
                requestKey,
                new String[]{"id"}
            );
            solicitacaoId = requireGeneratedId(requestKey);
        }

        GeneratedKeyHolder chatKey = new GeneratedKeyHolder();
        namedJdbcTemplate.update(
            """
            INSERT INTO chat (id_solicitacao, ativo, criado_em)
            VALUES (:idSolicitacao, 1, NOW())
            """,
            params("idSolicitacao", solicitacaoId),
            chatKey,
            new String[]{"id"}
        );
        return requireGeneratedId(chatKey);
    }

    private List<Map<String, Object>> buildTimeline(String backendStatus) {
        List<Map<String, Object>> timeline = new ArrayList<>();
        timeline.add(map("id", 1, "title", "Solicitacao enviada", "description", "Sua solicitacao foi enviada com sucesso", "status", "completed"));
        timeline.add(map("id", 2, "title", "Profissional recebeu", "description", "O profissional visualizou sua solicitacao", "status", "completed"));
        timeline.add(map("id", 3, "title", "Orcamento enviado", "description", "Aguardando aprovacao do orcamento", "status", "pending"));
        timeline.add(map("id", 4, "title", "Servico em andamento", "description", "Aguardando execucao do servico", "status", "pending"));
        timeline.add(map("id", 5, "title", "Concluido", "description", "Servico finalizado com sucesso", "status", "pending"));

        String status = frontendStatus(backendStatus);
        if ("aguardando_orcamento".equals(status)) {
            timeline.get(2).put("status", "active");
        } else if ("aprovado".equals(status)) {
            timeline.get(2).put("status", "completed");
            timeline.get(3).put("status", "active");
        } else if ("em_andamento".equals(status)) {
            timeline.get(2).put("status", "completed");
            timeline.get(3).put("status", "active");
        } else if ("concluido".equals(status)) {
            timeline.get(2).put("status", "completed");
            timeline.get(3).put("status", "completed");
            timeline.get(4).put("status", "completed");
        } else if ("cancelado".equals(status)) {
            timeline.get(2).put("description", "Solicitacao cancelada");
            timeline.get(2).put("status", "active");
        }

        return timeline;
    }

    private BigDecimal queryBudgetAmount(long solicitacaoId, String column) {
        String sql = "SELECT " + column + " FROM orcamento WHERE id_solicitacao = :id";
        return querySingleValue(sql, params("id", solicitacaoId), BigDecimal.class);
    }

    private String frontendStatus(String backendStatus) {
        return switch (backendStatus == null ? "" : backendStatus.toUpperCase(Locale.ROOT)) {
            case "AGUARDANDO_ORCAMENTO", "ORCAMENTO_ENVIADO" -> "aguardando_orcamento";
            case "APROVADA" -> "aprovado";
            case "EM_ANDAMENTO" -> "em_andamento";
            case "CONCLUIDA" -> "concluido";
            case "CANCELADA", "RECUSADA" -> "cancelado";
            default -> "aguardando_orcamento";
        };
    }

    private String paymentStatus(String status) {
        if (status == null || status.isBlank()) {
            return "Pagamento pendente";
        }
        return switch (status.toUpperCase(Locale.ROOT)) {
            case "APROVADO" -> "Pagamento confirmado";
            case "RECUSADO" -> "Pagamento recusado";
            case "PROCESSANDO" -> "Pagamento em processamento";
            default -> "Pagamento pendente";
        };
    }

    private String serviceCode(long id) {
        return "#SV-" + String.format("%05d", id);
    }

    private String humanAccountType(String type) {
        if ("PROFISSIONAL".equalsIgnoreCase(type)) {
            return "Profissional";
        }
        if ("ADMIN".equalsIgnoreCase(type)) {
            return "Admin";
        }
        return "Cliente";
    }

    private String formatCurrency(BigDecimal value) {
        BigDecimal safeValue = coalesce(value, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        return "R$ " + safeValue.toPlainString().replace(".", ",");
    }

    private String formatDate(Object value) {
        LocalDateTime dateTime = toLocalDateTime(value);
        if (dateTime == null) {
            return "";
        }
        return dateTime.toLocalDate().format(DATE_FORMAT);
    }

    private String formatTime(Object value) {
        LocalDateTime dateTime = toLocalDateTime(value);
        if (dateTime == null) {
            return "";
        }
        return dateTime.toLocalTime().format(TIME_FORMAT);
    }

    private String formatDateTime(Object value) {
        LocalDateTime dateTime = toLocalDateTime(value);
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMAT);
    }

    private String formatChatTime(Object value) {
        LocalDateTime dateTime = toLocalDateTime(value);
        if (dateTime == null) {
            return "";
        }

        LocalDate today = LocalDate.now();
        if (dateTime.toLocalDate().isEqual(today)) {
            return dateTime.toLocalTime().format(TIME_FORMAT);
        }
        if (dateTime.toLocalDate().isEqual(today.minusDays(1))) {
            return "Ontem";
        }
        return dateTime.toLocalDate().format(DATE_FORMAT);
    }

    private String chatDateLabel(Object value) {
        LocalDateTime dateTime = toLocalDateTime(value);
        if (dateTime == null) {
            return "";
        }

        LocalDate date = dateTime.toLocalDate();
        LocalDate today = LocalDate.now();
        if (date.isEqual(today)) {
            return "Hoje";
        }
        if (date.isEqual(today.minusDays(1))) {
            return "Ontem";
        }
        return date.format(DATE_FORMAT);
    }

    private String initials(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "";
        }
        return Arrays.stream(fullName.trim().split("\\s+"))
            .filter(part -> !part.isBlank())
            .limit(2)
            .map(part -> String.valueOf(Character.toUpperCase(part.charAt(0))))
            .collect(Collectors.joining());
    }

    private Long requireGeneratedId(GeneratedKeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Nao foi possivel gerar o identificador.");
        }
        return key.longValue();
    }

    private long parseId(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(text(value));
    }

    private Long nullableNumber(Object value) {
        String text = text(value);
        return text.isBlank() ? null : Long.parseLong(text);
    }

    private String resolveDocument(Map<String, Object> payload) {
        String documentType = text(payload.get("documentType")).toLowerCase(Locale.ROOT);
        return "cnpj".equals(documentType) ? text(payload.get("cnpj")) : text(payload.get("cpf"));
    }

    private String blankToNull(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String nullIfBlank(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private String fullAddress(String endereco, String cidade, String estado) {
        if (!endereco.isBlank()) {
            if (!cidade.isBlank() || !estado.isBlank()) {
                return endereco + ", " + cidade + (estado.isBlank() ? "" : " - " + estado);
            }
            return endereco;
        }
        if (!cidade.isBlank() || !estado.isBlank()) {
            return cidade + (estado.isBlank() ? "" : " - " + estado);
        }
        return "";
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (value instanceof LocalDateTime dateTime) {
            return dateTime;
        }
        if (value instanceof java.util.Date date) {
            return new Timestamp(date.getTime()).toLocalDateTime();
        }
        String textValue = text(value);
        return textValue.isBlank() ? null : LocalDateTime.parse(textValue.replace(" ", "T"));
    }

    private BigDecimal coalesce(BigDecimal first, BigDecimal second) {
        return first != null ? first : second;
    }

    private MapSqlParameterSource params(Object... values) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        for (int index = 0; index < values.length; index += 2) {
            source.addValue(String.valueOf(values[index]), values[index + 1]);
        }
        return source;
    }

    private Map<String, Object> map(Object... values) {
        Map<String, Object> data = new LinkedHashMap<>();
        for (int index = 0; index < values.length; index += 2) {
            data.put(String.valueOf(values[index]), values[index + 1]);
        }
        return data;
    }

    private Map<String, Object> querySingle(String sql, MapSqlParameterSource params) {
        return querySingle(sql, params, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            int count = rs.getMetaData().getColumnCount();
            for (int index = 1; index <= count; index++) {
                row.put(rs.getMetaData().getColumnLabel(index), rs.getObject(index));
            }
            return row;
        });
    }

    private Map<String, Object> querySingle(String sql, MapSqlParameterSource params, RowMapper<Map<String, Object>> rowMapper) {
        try {
            return namedJdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    private <T> T querySingleValue(String sql, MapSqlParameterSource params, Class<T> type) {
        try {
            return namedJdbcTemplate.queryForObject(sql, params, type);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }
}
