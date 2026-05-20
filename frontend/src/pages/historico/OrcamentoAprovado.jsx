import { ArrowLeft, Check } from "lucide-react";
import { useNavigate } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import "../../styles/global.css";

function OrcamentoAprovado() {
  const navigate = useNavigate();

  return (
    <section className="budget-result-page">
      <header className="budget-header">
        <button className="budget-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>

        <h1 className="budget-header__title">Orçamento</h1>
      </header>

      <div className="budget-result-page__content">
        <span className="budget-result-page__icon budget-result-page__icon--success" aria-hidden="true">
          <Check />
        </span>

        <div className="budget-result-page__text">
          <h2 className="budget-result-page__title">Orçamento Aprovado</h2>
          <p className="budget-result-page__message">
            O profissional foi notificado e dará continuidade ao serviço.
          </p>
          <p className="budget-result-page__message">
            Você receberá atualizações sobre o andamento do serviço em tempo real.
          </p>
        </div>

        <ActionButton
          text="Ir para Meus Serviços"
          className="budget-result-page__button"
          onClick={() => navigate("/historico")}
        />
      </div>
    </section>
  );
}

export default OrcamentoAprovado;
