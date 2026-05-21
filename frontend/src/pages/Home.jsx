import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SearchBar from "../components/ui/SearchBar";
import ListaDeCards from "../components/ui/ListaDeCards";
import ActionButton from "../components/ui/ActionButton";
import { api } from "../services/api";
import { normalizeSection } from "../utils/normalize";

function HomeDestaque() {
  const navigate = useNavigate();

  return (
    <section className="home-destaque" aria-labelledby="home-destaque-title">
      <h2 className="home-destaque__title" id="home-destaque-title">
        Trabalhe Conosco
      </h2>

      <p className="home-destaque__description">
        Faca o seu perfil profissional no desenrola e comece a trabalhar para voce mesmo, hoje.
        <br />
        Trabalhe conosco, seja um desenrolado.
      </p>

      <ActionButton
        text="Seja Desenrolado!"
        onClick={() => navigate("/cadastro/profissional")}
      />
    </section>
  );
}

function Home() {
  const [lists, setLists] = useState([]);

  useEffect(() => {
    let active = true;

    api.getHome()
      .then((response) => {
        if (active) {
          setLists(response.map(normalizeSection));
        }
      })
      .catch((error) => {
        console.error("Erro ao carregar a home:", error);
      });

    return () => {
      active = false;
    };
  }, []);

  const [firstList, secondList, thirdList] = lists;

  return (
    <div className="home">
      <SearchBar />
      {firstList && <ListaDeCards list={firstList} />}
      {secondList && <ListaDeCards list={secondList} />}
      <HomeDestaque />
      {thirdList && <ListaDeCards list={thirdList} />}
    </div>
  );
}

export default Home;
