import { useEffect, useState } from "react";
import { ArrowLeft, Calendar, Clock, MapPin, User } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import SegmentedControl from "../../components/ui/SegmentedControl";
import { api } from "../../services/api";
import "../../styles/global.css";

const budgetTabs = [
  { label: "Mao de Obra", value: "labor" },
  { label: "Materiais", value: "materials" },
];

function BudgetItem({ item, type }) {
  const isLabor = type === "labor";

  return (
    <article className="budget-item">
      <div className="budget-item__content">
        <h3 className="budget-item__title">{item.name}</h3>
        <p className="budget-item__description">
          {isLabor ? `Tempo: ${item.time}` : `Quantidade: ${item.quantity}`}
        </p>
        <p className="budget-item__description">
          {isLabor ? `Valor por hora: ${item.hourlyValue}` : `Valor unitario: ${item.unitValue}`}
        </p>
      </div>

      <strong className="budget-item__value">{item.totalValue}</strong>
    </article>
  );
}

function Orcamento() {
  const [activeTab, setActiveTab] = useState("labor");
  const [budget, setBudget] = useState(null);
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    api.getBudgetById(id)
      .then(setBudget)
      .catch((error) => {
        console.error("Erro ao carregar orcamento:", error);
        setBudget(null);
      });
  }, [id]);

  async function handleDecision(decision) {
    try {
      await api.decideBudget(id, decision);

      if (decision === "approve") {
        navigate("/historico/orcamento-aprovado");
        return;
      }

      navigate("/historico/orcamento-recusado");
    } catch (error) {
      console.error("Erro ao atualizar orcamento:", error);
    }
  }

  if (!budget) {
    return null;
  }

  const activeItems = activeTab === "labor" ? budget.labor : budget.materials;

  return (
    <section className="budget-page">
      <header className="budget-header">
        <button className="budget-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>

        <h1 className="budget-header__title">Orcamento</h1>
      </header>

      <article className="budget-service-card">
        <h2 className="budget-service-card__title">{budget.service.name}</h2>

        <div className="budget-service-card__professional">
          <span className="budget-service-card__avatar" aria-hidden="true">
            {budget.professional.initials}
          </span>

          <p className="budget-service-card__detail">
            <User className="budget-service-card__icon" aria-hidden="true" />
            <span>{budget.professional.name}</span>
          </p>
        </div>

        <div className="budget-service-card__grid">
          <p className="budget-service-card__detail">
            <Calendar className="budget-service-card__icon" aria-hidden="true" />
            <span>{budget.service.date}</span>
          </p>

          <p className="budget-service-card__detail">
            <Clock className="budget-service-card__icon" aria-hidden="true" />
            <span>{budget.service.time}</span>
          </p>

          <p className="budget-service-card__detail budget-service-card__detail--full">
            <MapPin className="budget-service-card__icon" aria-hidden="true" />
            <span>{budget.service.address}</span>
          </p>
        </div>
      </article>

      <SegmentedControl
        options={budgetTabs}
        activeValue={activeTab}
        onChange={setActiveTab}
        className="budget-page__tabs"
      />

      <div className="budget-list">
        {activeItems.map((item) => (
          <BudgetItem key={item.id} item={item} type={activeTab} />
        ))}
      </div>

      <section className="budget-summary">
        <h2 className="budget-summary__title">Resumo do orcamento</h2>
        <div className="budget-summary__row">
          <span>Total Mao de Obra</span>
          <strong>{budget.summary.laborTotal}</strong>
        </div>
        <div className="budget-summary__row">
          <span>Total Materiais</span>
          <strong>{budget.summary.materialsTotal}</strong>
        </div>
        <div className="budget-summary__row budget-summary__row--total">
          <span>Valor Total do Servico</span>
          <strong>{budget.summary.serviceTotal}</strong>
        </div>
      </section>

      <section className="budget-observation">
        <h2 className="budget-observation__title">Observacoes do profissional</h2>
        <p className="budget-observation__text">{budget.observation}</p>
      </section>

      <div className="budget-actions">
        <ActionButton
          text="Aprovar"
          className="action-button--primary budget-actions__button"
          onClick={() => handleDecision("approve")}
        />

        <ActionButton
          text="Recusar"
          className="action-button--danger-outline budget-actions__button"
          onClick={() => handleDecision("reject")}
        />
      </div>
    </section>
  );
}

export default Orcamento;
