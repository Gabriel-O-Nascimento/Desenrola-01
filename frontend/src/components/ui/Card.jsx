import { Link } from "react-router-dom";
import { Package } from "lucide-react";
import "../../styles/global.css";

/* Card clicavel de servico, usado dentro da lista de cards. */
export default function Card({ item }) {
  /* Define um icone padrao caso o item ainda nao tenha icone cadastrado. */
  const Icon = item.icon || Package;

  return (
    <Link
      to={`/servicos/solicitar/${item.id}`}
      className="card-service"
      aria-label={`Solicitar ${item.name}`}
    >
      {/* Area visual do card: vazia, proporcional e com icone centralizado. */}
      <span className="card-service__container">
        <Icon className="card-service__icon" aria-hidden="true" />
      </span>

      {/* Nome do servico abaixo do card, separado por 8px via gap no CSS. */}
      <span className="card-service__name">{item.name}</span>
    </Link>
  );
}
