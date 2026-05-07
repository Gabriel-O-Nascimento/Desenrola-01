import SearchBar from "../components/ui/SearchBar";
import ListaDeCards from "../components/ui/ListaDeCards";
import { professionalsLists } from "../data/Professionals";
import ActionButton from "../components/ui/ActionButton";

function HomeDestaque() {
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

      <ActionButton>
        Seja Desenrolado!
      </ActionButton>
    </section>
  );
}

function Home() {
  const [firstList, secondList, thirdList] = professionalsLists;

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