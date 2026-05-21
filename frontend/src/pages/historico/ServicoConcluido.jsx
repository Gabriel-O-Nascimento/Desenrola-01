import { useEffect, useState } from "react";
import {
  ArrowLeft,
  BadgeDollarSign,
  BriefcaseBusiness,
  Calendar,
  CheckCircle,
  CreditCard,
  Image,
  MapPin,
  MessageCircle,
  Star,
  Wrench,
  Zap,
} from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import { api } from "../../services/api";
import { normalizeCompletedService } from "../../utils/normalize";
import "../../styles/global.css";

function RatingStars({ rating }) {
  return (
    <span className="completed-service-professional__stars" aria-label={`${rating} estrelas`}>
      {Array.from({ length: 5 }, (_, index) => (
        <Star
          key={index}
          aria-hidden="true"
          className={index < Math.round(rating) ? "completed-service-star--active" : "completed-service-star--muted"}
        />
      ))}
    </span>
  );
}

export default function ServicoConcluido() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [service, setService] = useState(null);

  useEffect(() => {
    api.getCompletedServiceById(id)
      .then((response) => setService(normalizeCompletedService(response)))
      .catch((error) => {
        console.error("Erro ao carregar servico concluido:", error);
        setService(null);
      });
  }, [id]);

  if (!service) {
    return (
      <section className="completed-service-page">
        <header className="completed-service-header">
          <button className="completed-service-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="completed-service-header__title">Servico Concluido</h1>
        </header>

        <p className="completed-service-empty">Servico concluido nao encontrado.</p>
      </section>
    );
  }

  return (
    <section className="completed-service-page">
      <header className="completed-service-header">
        <button className="completed-service-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="completed-service-header__title">Servico Concluido</h1>
      </header>

      <main className="completed-service-content">
        <section className="completed-service-success">
          <span className="completed-service-success__icon" aria-hidden="true">
            <CheckCircle />
          </span>
          <h2 className="completed-service-success__title">Servico Concluido com Sucesso</h2>
          <p className="completed-service-success__text">
            Este servico foi finalizado e registrado no Desenrola.
          </p>
        </section>

        <section className="completed-service-card completed-service-professional">
          <span className="completed-service-professional__avatar" aria-hidden="true">
            {service.professionalInitials}
          </span>
          <div className="completed-service-professional__content">
            <h2 className="completed-service-professional__name">{service.professionalName}</h2>
            <p className="completed-service-professional__category">{service.professionalCategory}</p>
            <div className="completed-service-professional__rating">
              <RatingStars rating={service.professionalRating} />
              <strong>{service.professionalRating.toFixed(1)}</strong>
            </div>
          </div>
        </section>

        <section className="completed-service-card">
          <h2 className="completed-service-card__title">
            <Wrench aria-hidden="true" />
            Informacoes do Servico
          </h2>
          <div className="completed-service-info-grid">
            <p><Zap aria-hidden="true" /><span>Servico</span><strong>{service.title}</strong></p>
            <p><BriefcaseBusiness aria-hidden="true" /><span>Categoria</span><strong>{service.category}</strong></p>
            <p><Calendar aria-hidden="true" /><span>Data de inicio</span><strong>{service.startDate}</strong></p>
            <p><CheckCircle aria-hidden="true" /><span>Data de conclusao</span><strong>{service.conclusionDate}</strong></p>
            <p className="completed-service-info-grid__full"><MapPin aria-hidden="true" /><span>Local do atendimento</span><strong>{service.address}</strong></p>
          </div>
        </section>

        <section className="completed-service-card">
          <h2 className="completed-service-card__title">Descricao do servico</h2>
          <p className="completed-service-card__text">{service.description}</p>
        </section>

        <section className="completed-service-card">
          <h2 className="completed-service-card__title">
            <Image aria-hidden="true" />
            Fotos do servico
          </h2>
          <div className="completed-service-photos">
            {service.photos.map((photo) => {
              const PhotoIcon = photo.icon;

              return (
                <article className="completed-service-photo" key={photo.id}>
                  <PhotoIcon aria-hidden="true" />
                  <span>{photo.label}</span>
                </article>
              );
            })}
          </div>
        </section>

        <section className="completed-service-card">
          <h2 className="completed-service-card__title">
            <BadgeDollarSign aria-hidden="true" />
            Resumo Financeiro
          </h2>
          <div className="completed-service-financial">
            <p><span>Mao de obra</span><strong>{service.financialSummary.labor}</strong></p>
            <p><span>Materiais</span><strong>{service.financialSummary.materials}</strong></p>
            <p><span>Desconto</span><strong>{service.financialSummary.discount}</strong></p>
            <p className="completed-service-financial__total"><span>Valor total pago</span><strong>{service.financialSummary.totalPaid}</strong></p>
          </div>
        </section>

        <section className="completed-service-card completed-service-payment">
          <h2 className="completed-service-card__title">
            <CreditCard aria-hidden="true" />
            Metodo de pagamento
          </h2>
          <p><strong>{service.paymentMethod.type}</strong><span>{service.paymentMethod.status}</span></p>
        </section>

        <section className="completed-service-card">
          <h2 className="completed-service-card__title">Observacoes finais</h2>
          <p className="completed-service-card__text">{service.finalNotes}</p>
        </section>

        <div className="completed-service-actions">
          <ActionButton
            text="Avaliar profissional"
            className="completed-service-actions__button"
            onClick={() => navigate(`/historico/avaliar-servico/${service.id}`)}
          />
          <ActionButton
            className="action-button__outline completed-service-actions__button"
            onClick={() => navigate(`/chat/profissional/${service.professionalId}`)}
          >
            <MessageCircle aria-hidden="true" />
            Abrir chat
          </ActionButton>
        </div>
      </main>
    </section>
  );
}
