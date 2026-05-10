import {
  Calendar,
  Clock,
  DollarSign,
  MapPin,
  User,
  Wrench,
} from "lucide-react";
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
  return status === "aguardando_orcamento" ? "Ver Orcamento" : "Ver acompanhamento";
}

export default function ServiceHistory({ service }) {
  const isCanceled = service.status === "cancelado";

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
            onClick={() => console.log("Ver detalhes", service.id)}
          />
        ) : (
          <>
            <ActionButton
              text={getPrimaryButtonText(service.status)}
              className="service-history__button"
              onClick={() => console.log("Abrir servico", service.id)}
            />

            <ActionButton
              text="Mensagem"
              className="action-button__outline service-history__button"
              onClick={() => console.log("Abrir mensagem", service.id)}
            />
          </>
        )}
      </footer>
    </article>
  );
}
