import { Search } from "lucide-react";
import "../../styles/global.css";

/**
 * Barra de pesquisa reutilizavel.
 * Pode ser usada de forma controlada (com value/onChange) ou apenas visual.
 */
export default function SearchBar({
  value,
  onChange,
  placeholder = "Pesquisar serviços ou profissionais...",
  id = "home-search",
}) {
  return (
    /* Container externo usado para centralizar a barra. */
    <div className="search-bar">
      <label className="search-bar__field" htmlFor={id}>
        {/* Icone visual da busca, alinhado a esquerda do input. */}
        <Search className="search-bar__icon" aria-hidden="true" />

        <input
          className="search-bar__input"
          id={id}
          name={id}
          type="search"
          placeholder={placeholder}
          value={value ?? ""}
          onChange={onChange}
        />
      </label>
    </div>
  );
}
