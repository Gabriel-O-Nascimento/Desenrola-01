import { useRef, useState } from "react";
import {
  ArrowLeft,
  Calendar,
  Camera,
  CheckCircle,
  DollarSign,
  MapPin,
  Star,
  Zap,
} from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import { servicosConcluidos } from "../../data/ServicosConcluidos";
import "../../styles/global.css";

export default function AvaliarServico() {
  const { id } = useParams();
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const service = servicosConcluidos.find((item) => String(item.id) === String(id));
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const [comment, setComment] = useState("");
  const [photo, setPhoto] = useState(null);
  const [checks, setChecks] = useState({
    punctual: false,
    clean: false,
    recommended: false,
  });
  const [ratingError, setRatingError] = useState("");

  function handleCheckChange(event) {
    const { name, checked } = event.target;

    setChecks((currentChecks) => ({
      ...currentChecks,
      [name]: checked,
    }));
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (!rating) {
      setRatingError("Selecione uma nota para sua experiência.");
      return;
    }

    const reviewData = {
      serviceId: service.id,
      professionalId: service.professionalId,
      rating,
      comment,
      photo,
      checks,
    };

    navigate("/");
  }

  if (!service) {
    return (
      <section className="review-service-page">
        <header className="review-service-header">
          <button className="review-service-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="review-service-header__title">Avaliar Profissional</h1>
        </header>

        <p className="review-service-empty">Serviço para avaliação não encontrado.</p>
      </section>
    );
  }

  return (
    <section className="review-service-page">
      <header className="review-service-header">
        <button className="review-service-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="review-service-header__title">Avaliar Profissional</h1>
      </header>

      <form className="review-service-content" onSubmit={handleSubmit}>
        <section className="review-service-card review-service-professional">
          <span className="review-service-professional__avatar" aria-hidden="true">
            {service.professionalInitials}
          </span>
          <div>
            <h2 className="review-service-professional__name">{service.professionalName}</h2>
            <p className="review-service-professional__category">{service.professionalCategory}</p>
            <span className="review-service-professional__status">
              <CheckCircle aria-hidden="true" />
              Serviço concluído
            </span>
          </div>
        </section>

        <section className="review-service-card">
          <h2 className="review-service-card__title">
            <Zap aria-hidden="true" />
            Serviço realizado
          </h2>
          <div className="review-service-summary">
            <p><strong>{service.title}</strong></p>
            <p><Calendar aria-hidden="true" />{service.dateTime}</p>
            <p><MapPin aria-hidden="true" />{service.address}</p>
            <p><DollarSign aria-hidden="true" />{service.financialSummary.totalPaid}</p>
          </div>
        </section>

        <section className="review-service-card">
          <h2 className="review-service-card__title">Como foi sua experiência?</h2>
          <div className="review-service-stars" onMouseLeave={() => setHoverRating(0)}>
            {Array.from({ length: 5 }, (_, index) => {
              const value = index + 1;
              const isActive = value <= (hoverRating || rating);

              return (
                <button
                  className={`review-service-star${isActive ? " review-service-star--active" : ""}`}
                  key={value}
                  type="button"
                  aria-label={`${value} estrela${value > 1 ? "s" : ""}`}
                  onClick={() => {
                    setRating(value);
                    setRatingError("");
                  }}
                  onMouseEnter={() => setHoverRating(value)}
                >
                  <Star aria-hidden="true" />
                </button>
              );
            })}
          </div>
          {ratingError && <p className="form-input__error">{ratingError}</p>}
        </section>

        <section className="review-service-card">
          <label className="review-service-field" htmlFor="review-comment">
            <span>Comentário opcional</span>
            <textarea
              id="review-comment"
              value={comment}
              placeholder="Conte como foi sua experiência com o profissional."
              onChange={(event) => setComment(event.target.value)}
            />
          </label>
        </section>

        <section className="review-service-card">
          <span className="review-service-field__label">Fotos opcionais</span>
          <input
            ref={fileInputRef}
            className="review-service-photo__input"
            type="file"
            accept="image/*"
            onChange={(event) => setPhoto(event.target.files?.[0] || null)}
          />
          <button
            className="review-service-photo"
            type="button"
            onClick={() => fileInputRef.current?.click()}
          >
            <Camera aria-hidden="true" />
            {photo ? photo.name : "Adicionar foto"}
          </button>
        </section>

        <section className="review-service-card review-service-checks">
          <label>
            <input type="checkbox" name="punctual" checked={checks.punctual} onChange={handleCheckChange} />
            Profissional foi pontual
          </label>
          <label>
            <input type="checkbox" name="clean" checked={checks.clean} onChange={handleCheckChange} />
            Profissional deixou tudo limpo
          </label>
          <label>
            <input type="checkbox" name="recommended" checked={checks.recommended} onChange={handleCheckChange} />
            Recomendaria para outras pessoas
          </label>
        </section>

        <ActionButton
          text="Enviar Avaliação"
          type="submit"
          className="review-service-submit"
        />
      </form>
    </section>
  );
}
