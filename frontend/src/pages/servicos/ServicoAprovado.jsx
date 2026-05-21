import { ArrowLeft, Check } from "lucide-react";
import { useNavigate } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import "../../styles/global.css";

function ServicoAprovado() {
  const navigate = useNavigate();

  return (
    <section className="service-result-page">
      <header className="service-header">
        <button className="service-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>

        <h1 className="service-header__title">Solicitar Serviço</h1>
      </header>

      <div className="service-result-page__content">
        <span className="service-result-page__icon service-result-page__icon--success" aria-hidden="true">
          <Check />
        </span>

        <div className="service-result-page__text">
          <h2 className="service-result-page__title">Solicitação Enviada</h2>
          <p className="service-result-page__message">
            Sua solicitação foi enviada com sucesso.
          </p>
          <p className="service-result-page__message">
            em breve um profissional irá entrar em contato.
          </p>
        </div>

        <ActionButton
          text="Ir para Meus Serviços"
          className="service-result-page__button"
          onClick={() => navigate("/")}
        />
      </div>
    </section>
  );
}

export default ServicoAprovado;
