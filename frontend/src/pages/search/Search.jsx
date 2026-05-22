import { useMemo, useState } from "react";
import { ChevronRight, Star } from "lucide-react";
import SearchBar from "../../components/ui/SearchBar";
import ActionButton from "../../components/ui/ActionButton";
import SegmentedControl from "../../components/ui/SegmentedControl";
import { professionals, services } from "../../data/SearchData";
import { useNavigate } from "react-router-dom";
import "../../styles/global.css";

const searchTabs = [
  { label: "Profissional", value: "professional" },
  { label: "Servico", value: "service" },
];

function ProfessionalCard({ professional }) {
  const navigate = useNavigate();
  return (
    <article className="search-result-card search-result-card--professional">
      <span className="search-result-card__avatar" aria-hidden="true">
        {professional.initials}
      </span>

      <div className="search-result-card__content">
        <h2 className="search-result-card__title">{professional.name}</h2>
        <p className="search-result-card__description">{professional.category}</p>

        <span className="search-result-card__rating">
          <Star className="search-result-card__star" aria-hidden="true" />
          {professional.rating}
        </span>
      </div>

      <ActionButton
        text="Saiba mais"
        className="search-result-card__action"
        onClick={() => navigate(`/perfil/profissional/${professional.id}`)}
      />
    </article>
  );
}

function ServiceCard({ service }) {
  const Icon = service.icon;

  return (
    <article className="search-result-card search-result-card--service">
      <span className="search-result-card__service-icon" aria-hidden="true">
        <Icon />
      </span>

      <div className="search-result-card__content">
        <h2 className="search-result-card__title">{service.name}</h2>
        <p className="search-result-card__description">{service.description}</p>
      </div>

      <ChevronRight className="search-result-card__arrow" aria-hidden="true" />
    </article>
  );
}

function Search() {
  const [activeTab, setActiveTab] = useState("professional");
  const [searchTerm, setSearchTerm] = useState("");
  const isProfessionalTab = activeTab === "professional";

  // Filtra a lista correta com base no tab ativo e no termo digitado.
  const filteredProfessionals = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();

    if (!term) {
      return professionals;
    }

    return professionals.filter((professional) =>
      [professional.name, professional.category]
        .filter(Boolean)
        .some((field) => field.toLowerCase().includes(term))
    );
  }, [searchTerm]);

  const filteredServices = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();

    if (!term) {
      return services;
    }

    return services.filter((service) =>
      [service.name, service.description]
        .filter(Boolean)
        .some((field) => field.toLowerCase().includes(term))
    );
  }, [searchTerm]);

  return (
    <section className="search-page">
      <SearchBar
        value={searchTerm}
        onChange={(event) => setSearchTerm(event.target.value)}
        placeholder={
          isProfessionalTab
            ? "Pesquisar profissionais..."
            : "Pesquisar servicos..."
        }
        id="search-page-input"
      />

      {/* SegmentedControl controla visualmente qual lista sera exibida. */}
      <SegmentedControl
        options={searchTabs}
        activeValue={activeTab}
        onChange={setActiveTab}
        className="search-page__tabs"
      />

      {/* Lista filtrada de resultados. */}
      <div className="search-results">
        {isProfessionalTab ? (
          filteredProfessionals.length > 0 ? (
            filteredProfessionals.map((professional) => (
              <ProfessionalCard key={professional.id} professional={professional} />
            ))
          ) : (
            <p style={{ textAlign: "center", padding: "24px", color: "#6b7280" }}>
              Nenhum profissional encontrado.
            </p>
          )
        ) : filteredServices.length > 0 ? (
          filteredServices.map((service) => (
            <ServiceCard key={service.id} service={service} />
          ))
        ) : (
          <p style={{ textAlign: "center", padding: "24px", color: "#6b7280" }}>
            Nenhum servico encontrado.
          </p>
        )}
      </div>
    </section>
  );
}

export default Search;
