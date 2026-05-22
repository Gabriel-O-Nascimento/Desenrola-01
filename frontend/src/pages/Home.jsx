import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/ui/SearchBar";
import ListaDeCards from "../components/ui/ListaDeCards";
import { professionalsLists } from "../data/Professionals";
import ActionButton from "../components/ui/ActionButton";
import { conectarNotificacoes } from "../services/notificacaoSocket";

function HomeDestaque() {
  const navigate = useNavigate();
  return (
    <section className="home-destaque" aria-labelledby="home-destaque-title">
      <h2 className="home-destaque__title" id="home-destaque-title">
        Trabalhe Conosco
      </h2>

      <p className="home-destaque__description">
        Faça o seu perfil profissional no desenrola e comece a trabalhar para você mesmo, hoje.
        <br></br>
        trabalhe conosco, seja um desenrolado.
      </p>

      <ActionButton
        text="Seja Desenrolado!"
        onClick={() => navigate("/cadastro/profissional")}
      />
    </section>
  );
}

function Home() {
  const [firstList, secondList, thirdList] = professionalsLists;
  const [notificacao, setNotificacao] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    // Conecta no canal WebSocket do usuario para receber notificacoes em tempo real
    // publicadas pelos consumers do RabbitMQ.
    const idUsuarioLogado = 1;

    const desconectar = conectarNotificacoes(idUsuarioLogado, (recebida) => {
      setNotificacao(recebida);
      setTimeout(() => setNotificacao(null), 5000);
    });

    return desconectar;
  }, []);

  // Filtra cada lista por categoria (titulo da lista) ou nome do servico.
  const filteredLists = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();

    if (!term) {
      return [firstList, secondList, thirdList].filter(Boolean);
    }

    return [firstList, secondList, thirdList]
      .filter(Boolean)
      .map((list) => ({
        ...list,
        items: list.items?.filter((item) =>
          [item.name, item.description, list.title]
            .filter(Boolean)
            .some((field) => field.toLowerCase().includes(term))
        ) ?? [],
      }))
      .filter((list) => list.items.length > 0);
  }, [firstList, secondList, thirdList, searchTerm]);

  return (
    <div className="home">
      {notificacao && (
        <div
          role="status"
          style={{
            position: "fixed",
            top: 16,
            right: 16,
            background: "#1f2937",
            color: "#fff",
            padding: "12px 16px",
            borderRadius: 8,
            boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
            zIndex: 1000,
            maxWidth: 320,
          }}
        >
          <strong style={{ display: "block", marginBottom: 4 }}>
            {notificacao.titulo}
          </strong>
          <span>{notificacao.mensagem}</span>
        </div>
      )}
      <SearchBar
        value={searchTerm}
        onChange={(event) => setSearchTerm(event.target.value)}
        placeholder="Filtrar por categoria de servico..."
      />
      {filteredLists.map((list) => (
        <ListaDeCards key={list.title} list={list} />
      ))}
      {filteredLists.length === 0 && (
        <p style={{ textAlign: "center", padding: "24px", color: "#6b7280" }}>
          Nenhum servico encontrado para "{searchTerm}".
        </p>
      )}
      {!searchTerm && <HomeDestaque />}
    </div>
  );
}

export default Home;
