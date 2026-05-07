import { Search } from "lucide-react";
import "../../styles/global.css";

export default function SearchBar() {
  return (
    /* Container externo usado para centralizar a barra na Home. */
    <div className="search-bar">
      <label className="search-bar__field" htmlFor="home-search">
        {/* Icone visual da busca, alinhado a esquerda do input. */}
        <Search className="search-bar__icon" aria-hidden="true" />

        {/* Input preparado para receber texto quando a busca funcional for criada. */}
        <input
          className="search-bar__input"
          id="home-search"
          name="home-search"
          type="search"
          placeholder="Pesquisar serviços ou profissionais..."
        />
      </label>
    </div>
  );
}
