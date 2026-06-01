import {
  ArrowLeft,
  Briefcase,
  CheckCircle,
  ChevronRight,
  Clock,
  MapPin,
  MessageCircle,
  MoreVertical,
  Star,
} from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import { cadastrosProfissionais } from "../../data/CadastroProfissional";
import "../../styles/global.css";

function RatingStars({ rating }) {
  return (
    <div className="professional-profile-review__stars" aria-label={`${rating} estrelas`}>
      {Array.from({ length: 5 }, (_, index) => (
        <Star
          key={index}
          aria-hidden="true"
          className={index < rating ? "professional-profile-star--active" : "professional-profile-star--muted"}
        />
      ))}
    </div>
  );
}

export default function PerfilProfissional() {
  const navigate = useNavigate();
  const { professionalId } = useParams();
  const professional =
    cadastrosProfissionais.find((item) => String(item.id) === String(professionalId)) ||
    (!professionalId ? cadastrosProfissionais[0] : null);

  function handleSolicitarServico() {
    navigate(`/servicos/solicitar/profissional/${professional.id}`);
  }

  function handleAbrirChat() {
    navigate(`/chat/profissional/${professional.id}`);
  }

  if (!professional) {
    return (
      <section className="professional-profile-page">
        <header className="professional-profile-header">
          <button
            className="professional-profile-header__button"
            type="button"
            aria-label="Voltar"
            onClick={() => navigate(-1)}
          >
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="professional-profile-header__title">Perfil do profissional</h1>
          <span aria-hidden="true" />
        </header>

        <p className="professional-profile-empty">Profissional não encontrado.</p>
      </section>
    );
  }

  return (
    <section className="professional-profile-page">
      <header className="professional-profile-header">
        <button
          className="professional-profile-header__button"
          type="button"
          aria-label="Voltar"
          onClick={() => navigate(-1)}
        >
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="professional-profile-header__title">Perfil do profissional</h1>
      </header>

      <main className="professional-profile-content">
        <section className="professional-profile-hero">
          <div className="professional-profile-avatar" aria-hidden="true">
            {professional.initials}
          </div>
          <h2 className="professional-profile-name">{professional.name}</h2>
          <p className="professional-profile-profession">{professional.profession}</p>

          {professional.verified && (
            <span className="professional-profile-verified">
              <CheckCircle aria-hidden="true" />
              Verificado
            </span>
          )}
        </section>

        <section className="professional-profile-metrics" aria-label="Métricas do profissional">
          <article className="professional-profile-metric">
            <span className="professional-profile-metric__icon professional-profile-metric__icon--primary">
              <Star aria-hidden="true" />
            </span>
            <div>
              <p className="professional-profile-metric__label">Avaliação</p>
              <strong className="professional-profile-metric__value">{professional.rating}</strong>
            </div>
          </article>

          <article className="professional-profile-metric">
            <span className="professional-profile-metric__icon">
              <Briefcase aria-hidden="true" />
            </span>
            <div>
              <p className="professional-profile-metric__label">Serviços</p>
              <strong className="professional-profile-metric__value">{professional.servicesCount}</strong>
            </div>
          </article>

          <article className="professional-profile-metric">
            <span className="professional-profile-metric__icon">
              <MapPin aria-hidden="true" />
            </span>
            <div>
              <p className="professional-profile-metric__label">Distância</p>
              <strong className="professional-profile-metric__value">{professional.distance}</strong>
            </div>
          </article>

          <article className="professional-profile-metric">
            <span className="professional-profile-metric__icon">
              <Clock aria-hidden="true" />
            </span>
            <div>
              <p className="professional-profile-metric__label">Responde em</p>
              <strong className="professional-profile-metric__value">{professional.responseTime}</strong>
            </div>
          </article>
        </section>

        <section className="professional-profile-section">
          <h2 className="professional-profile-section__title">Sobre o profissional</h2>
          <p className="professional-profile-section__text">{professional.about}</p>
        </section>

        <section className="professional-profile-section">
          <h2 className="professional-profile-section__title">Trabalhos realizados</h2>
          <div className="professional-profile-works" aria-label="Trabalhos realizados">
            {professional.works.map((work) => {
              const WorkIcon = work.icon;

              return (
                <article className="professional-profile-work" key={work.id}>
                  <span className="professional-profile-work__icon" aria-hidden="true">
                    <WorkIcon size={42} />
                  </span>
                  <p className="professional-profile-work__title">{work.title}</p>
                </article>
              );
            })}
          </div>
        </section>

        <section className="professional-profile-section">
          <h2 className="professional-profile-section__title">Últimas Avaliações</h2>
          <div className="professional-profile-reviews">
            {professional.reviews.map((review) => (
              <article className="professional-profile-review" key={review.id}>
                <header className="professional-profile-review__header">
                  <strong className="professional-profile-review__name">{review.clientName}</strong>
                  <span className="professional-profile-review__date">{review.date}</span>
                </header>
                <RatingStars rating={review.rating} />
                <p className="professional-profile-review__comment">{review.comment}</p>
                {review.imageIcon && (() => {
                  const ReviewImageIcon = review.imageIcon;

                  return (
                    <span className="professional-profile-review__image" aria-label="Imagem enviada pelo cliente">
                      <ReviewImageIcon size={32} />
                    </span>
                  );
                })()}
              </article>
            ))}
          </div>
        </section>
      </main>

      <footer className="professional-profile-actions">
        <ActionButton
          text="Solicitar orçamento"
          className="professional-profile-actions__button"
          onClick={handleSolicitarServico}
        />
        <ActionButton
          className="action-button__outline professional-profile-actions__button"
          onClick={handleAbrirChat}
        >
          <MessageCircle aria-hidden="true" />
          Mensagem
        </ActionButton>
      </footer>
    </section>
  );
}
