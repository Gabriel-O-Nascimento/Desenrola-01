import {
  ArrowLeft,
  Calendar,
  Check,
  Clock,
  FileText,
  Hash,
  MapPin,
  Wrench,
} from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { acompanhamentosServicos } from "../../data/AcompanhamentoServicos";
import "../../styles/global.css";

const statusLabels = {
  em_andamento: "Em andamento",
  aprovado: "Aprovado",
  concluido: "Concluído",
};

const timelineIcons = [Check, Clock, FileText, Wrench, Check];

function TimelineIcon({ index }) {
  const Icon = timelineIcons[index] || Check;
  return <Icon aria-hidden="true" />;
}

export default function AcompanhamentoServicos() {
  const { id } = useParams();
  const navigate = useNavigate();
  const service = acompanhamentosServicos.find((item) => String(item.id) === String(id));

  if (!service) {
    return (
      <section className="tracking-page">
        <header className="tracking-header">
          <button className="tracking-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="tracking-header__title">Acompanhamento</h1>
        </header>

        <p className="tracking-empty">Acompanhamento não encontrado.</p>
      </section>
    );
  }

  return (
    <section className="tracking-page">
      <header className="tracking-header">
        <button className="tracking-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="tracking-header__title">Acompanhamento</h1>
      </header>

      <main className="tracking-content">
        <section className="tracking-service-card">
          <div className="tracking-service-card__header">
            <h2 className="tracking-service-card__title">{service.title}</h2>
            <span className={`tracking-service-card__badge tracking-service-card__badge--${service.status}`}>
              {statusLabels[service.status] || service.status}
            </span>
          </div>

          <article className="tracking-professional-card">
            <span className="tracking-professional-card__avatar" aria-hidden="true">
              {service.professionalInitials}
            </span>
            <div className="tracking-professional-card__content">
              <h3 className="tracking-professional-card__name">{service.professionalName}</h3>
              <p className="tracking-professional-card__category">{service.professionalCategory}</p>
            </div>
            <button
              className="tracking-professional-card__link"
              type="button"
              onClick={() => navigate(`/profile/profissional/${service.professionalId}`)}
            >
              Ver perfil
            </button>
          </article>

          <div className="tracking-service-card__meta">
            <span>
              <Calendar aria-hidden="true" />
              {service.date}
            </span>
            <span>
              <Clock aria-hidden="true" />
              {service.time}
            </span>
            <span className="tracking-service-card__address">
              <MapPin aria-hidden="true" />
              {service.address}
            </span>
          </div>
        </section>

        <section className="tracking-section">
          <h2 className="tracking-section__title">Status da solicitação</h2>

          <ol className="tracking-timeline">
            {service.timeline.map((item, index) => (
              <li className={`tracking-timeline__item tracking-timeline__item--${item.status}`} key={item.id}>
                <span className="tracking-timeline__icon">
                  <TimelineIcon index={index} />
                </span>
                <div>
                  <h3 className="tracking-timeline__title">{item.title}</h3>
                  <p className="tracking-timeline__description">{item.description}</p>
                </div>
              </li>
            ))}
          </ol>
        </section>

        <section className="tracking-detail-card">
          <h2 className="tracking-detail-card__title">
            <FileText aria-hidden="true" />
            Detalhes da solicitação
          </h2>

          <div className="tracking-detail-card__block">
            <span className="tracking-detail-card__label">Descrição do problema:</span>
            <p className="tracking-detail-card__text">{service.description}</p>
          </div>

          <div className="tracking-detail-card__block">
            <span className="tracking-detail-card__label">Fotos enviadas:</span>
            {service.photos.length > 0 ? (
              <div className="tracking-photos">
                {service.photos.map((photo) => {
                  const PhotoIcon = photo.icon;

                  return (
                    <span className="tracking-photo" key={photo.id} aria-label="Foto enviada">
                      <PhotoIcon aria-hidden="true" />
                    </span>
                  );
                })}
              </div>
            ) : (
              <p className="tracking-detail-card__text">Nenhuma foto enviada.</p>
            )}
          </div>
        </section>

        <section className="tracking-summary" aria-label="Informações finais">
          <p>
            <Calendar aria-hidden="true" />
            <span>Enviado em:</span>
            <strong>{service.sentAt}</strong>
          </p>
          <p>
            <Hash aria-hidden="true" />
            <span>Código:</span>
            <strong className="tracking-summary__code">{service.serviceCode}</strong>
          </p>
          <p>
            <MapPin aria-hidden="true" />
            <span>Endereço:</span>
            <strong>{service.address}</strong>
          </p>
        </section>
      </main>
    </section>
  );
}
