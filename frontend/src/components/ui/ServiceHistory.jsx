import {
  Calendar,
  Clock,
  DollarSign,
  MapPin,
  User,
  Wrench,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import ActionButton from "./ActionButton";
import "../../styles/global.css";

const statusLabels = {
  aguardando_orcamento: "Aguardando orcamento",
  em_andamento: "Em andamento",
  aprovado: "Aprovado",
  concluido: "Concluido",
  cancelado: "Cancelado",
};

function getPrimaryButtonText(status) {
  if (status === "aguardando_orcamento") {
    return "Ver Orcamento";
  }

  if (status === "concluido") {
    return "Ver resumo";
  }

  return "Ver acompanhamento";
}

export default function ServiceHistory({ service }) {
  const navigate = useNavigate();
  const isCanceled = service.status === "cancelado";

  function handlePrimaryAction() {
    if (service.status === "aguardando_orcamento") {
      navigate(`/orcamento/${service.id}`);
      return;
    }

    if (service.status === "concluido") {
      navigate(`/historico/servico-concluido/${service.id}`);
      return;
    }

    navigate(`/historico/acompanhamento/${service.id}`);
  }

  return (
    <article className="service-history">
      <header className="service-history__header">
        <h2 className="service-history__title">{service.title}</h2>

        {/* Badge visual do status atual do servico. */}
        <span className={`service-history__status service-history__status--${service.status}`}>
          {statusLabels[service.status]}
        </span>
      </header>

      <div className="service-history__details">
        <p className="service-history__detail">
          <User className="service-history__icon" aria-hidden="true" />
          <span>Profissional: {service.professionalName}</span>
        </p>

        <div className="service-history__detail-row">
          <p className="service-history__detail">
            <Calendar className="service-history__icon" aria-hidden="true" />
            <span>Data: {service.date}</span>
          </p>

          <p className="service-history__detail">
            <Clock className="service-history__icon" aria-hidden="true" />
            <span>{service.time}</span>
          </p>
        </div>

        <p className="service-history__detail">
          <MapPin className="service-history__icon" aria-hidden="true" />
          <span>{service.address}</span>
        </p>

        <p className="service-history__detail">
          <DollarSign className="service-history__icon" aria-hidden="true" />
          <span>
            Valor estimado:{" "}
            <strong className="service-history__value">{service.estimatedValue}</strong>
          </span>
        </p>

        <p className="service-history__detail">
          <Wrench className="service-history__icon" aria-hidden="true" />
          <span>Categoria: {service.category}</span>
        </p>
      </div>

      <footer className="service-history__actions">
        {isCanceled ? (
          <ActionButton
            text="Ver Detalhes"
            className="action-button__outline service-history__button"
            onClick={() => navigate(`/historico/cancelamento/${service.id}`)}
          />
        ) : (
          <>
            <ActionButton
              text={getPrimaryButtonText(service.status)}
              className="service-history__button"
              onClick={handlePrimaryAction}
            />

            <ActionButton
              text="Mensagem"
              className="action-button__outline service-history__button"
              onClick={() => navigate(`/chat`)}
            />
          </>
        )}
      </footer>
    </article>
  );
}
