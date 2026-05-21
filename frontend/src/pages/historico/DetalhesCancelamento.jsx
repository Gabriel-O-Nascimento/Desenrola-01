import {
  AlertCircle,
  ArrowLeft,
  Calendar,
  DollarSign,
  MapPin,
  RotateCcw,
  User,
} from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import { servicosCancelados } from "../../data/ServicosCancelados";
import "../../styles/global.css";

function InfoItem({ icon: Icon, label, value, variant = "default" }) {
  return (
    <div className={`cancel-detail-info__item cancel-detail-info__item--${variant}`}>
      <Icon className="cancel-detail-info__icon" aria-hidden="true" />
      <div>
        <span className="cancel-detail-info__label">{label}</span>
        <strong className="cancel-detail-info__value">{value}</strong>
      </div>
    </div>
  );
}

export default function DetalhesCancelamento() {
  const { id } = useParams();
  const navigate = useNavigate();
  const service = servicosCancelados.find((item) => String(item.id) === String(id));

  function handleSolicitarNovamente() {
    if (!service?.professionalId) {
      console.warn("Profissional não encontrado para este serviço cancelado.");
      return;
    }

    navigate(`/servicos/solicitar/profissional/${service.professionalId}`);
  }

  if (!service) {
    return (
      <section className="cancel-detail-page">
        <header className="cancel-detail-header">
          <button className="cancel-detail-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="cancel-detail-header__title">Detalhes Cancelamento</h1>
        </header>

        <p className="cancel-detail-empty">Serviço cancelado não encontrado.</p>
      </section>
    );
  }

  return (
    <section className="cancel-detail-page">
      <header className="cancel-detail-header">
        <button className="cancel-detail-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="cancel-detail-header__title">Detalhes Cancelamento</h1>
      </header>

      <main className="cancel-detail-content">
        <section className="cancel-detail-code">
          <span className="cancel-detail-code__label">Código do Serviço</span>
          <strong className="cancel-detail-code__value">{service.serviceCode}</strong>
        </section>

        <section className="cancel-detail-status">
          <span className="cancel-detail-status__icon" aria-hidden="true">
            <AlertCircle />
          </span>
          <div>
            <h2 className="cancel-detail-status__title">Status: Cancelado</h2>
            <p className="cancel-detail-status__text">
              Este serviço foi cancelado em {service.cancellationDate}
            </p>
          </div>
        </section>

        <section className="cancel-detail-card">
          <h2 className="cancel-detail-card__title">Informações do Serviço</h2>

          <div className="cancel-detail-info">
            <div className="cancel-detail-info__plain">
              <span className="cancel-detail-info__label">Tipo de Serviço</span>
              <strong className="cancel-detail-info__value">{service.serviceType}</strong>
            </div>

            <div className="cancel-detail-info__plain">
              <span className="cancel-detail-info__label">Categoria</span>
              <strong className="cancel-detail-info__value">{service.category}</strong>
            </div>

            <InfoItem icon={User} label="Cliente" value={service.clientName} />
            <InfoItem icon={User} label="Profissional" value={service.professionalName} />
            <InfoItem icon={Calendar} label="Data da Solicitação" value={service.requestDate} />
            <InfoItem icon={Calendar} label="Data do Cancelamento" value={service.cancellationDate} variant="danger" />
            <InfoItem icon={MapPin} label="Endereço" value={service.address} />
            <InfoItem icon={DollarSign} label="Valor Estimado" value={service.estimatedValue} variant="money" />
          </div>
        </section>

        <section className="cancel-detail-card">
          <h2 className="cancel-detail-card__title">Descrição do Serviço</h2>
          <p className="cancel-detail-card__text">{service.description}</p>
        </section>

        <section className="cancel-detail-card cancel-detail-card--reason">
          <h2 className="cancel-detail-card__title">Motivo do Cancelamento</h2>
          <p className="cancel-detail-card__text">{service.cancellationReason}</p>
        </section>

        <section className="cancel-detail-card">
          <h2 className="cancel-detail-card__title">Histórico do Serviço</h2>
          <ol className="cancel-detail-timeline">
            {service.history.map((item) => (
              <li className={`cancel-detail-timeline__item cancel-detail-timeline__item--${item.status}`} key={item.id}>
                <span className="cancel-detail-timeline__marker" aria-hidden="true" />
                <div>
                  <strong className="cancel-detail-timeline__label">{item.label}</strong>
                  <span className="cancel-detail-timeline__date">{item.date}</span>
                </div>
              </li>
            ))}
          </ol>
        </section>

        <div className="cancel-detail-actions">
          <ActionButton
            className="cancel-detail-actions__primary"
            onClick={handleSolicitarNovamente}
          >
            <RotateCcw aria-hidden="true" />
            Solicitar Novamente
          </ActionButton>

          <ActionButton
            text="Voltar"
            className="action-button__outline cancel-detail-actions__secondary"
            onClick={() => navigate(-1)}
          />
        </div>
      </main>
    </section>
  );
}
