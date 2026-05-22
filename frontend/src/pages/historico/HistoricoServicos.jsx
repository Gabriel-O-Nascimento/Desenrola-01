import { useMemo, useState } from "react";
import SegmentedControl from "../../components/ui/SegmentedControl";
import ServiceHistory from "../../components/ui/ServiceHistory";
import { servicesHistory } from "../../data/ServicesData";
import "../../styles/global.css";

const statusOptions = [
  { label: "Todos", value: "todos" },
  { label: "Aguardando orcamento", value: "aguardando_orcamento" },
  { label: "Em andamento", value: "em_andamento" },
  { label: "Aprovados", value: "aprovado" },
  { label: "Concluidos", value: "concluido" },
  { label: "Cancelados", value: "cancelado" },
];

function parseDateBr(dateString) {
  // Converte "DD/MM/AAAA" em objeto Date para ordenacao confiavel.
  if (!dateString) {
    return new Date(0);
  }

  const [day, month, year] = dateString.split("/").map(Number);
  return new Date(year, month - 1, day);
}

function HistoricoServicos() {
  const [activeStatus, setActiveStatus] = useState("todos");

  const filteredServices = useMemo(() => {
    const list =
      activeStatus === "todos"
        ? servicesHistory
        : servicesHistory.filter((service) => service.status === activeStatus);

    // Ordena do mais recente para o mais antigo (data da solicitacao).
    return [...list].sort((a, b) => parseDateBr(b.date) - parseDateBr(a.date));
  }, [activeStatus]);

  return (
    <section className="historico-servicos">
      <header className="historico-servicos__header">
        <h1 className="historico-servicos__title">Historico de servicos</h1>
      </header>

      {/* Wrapper permite rolagem horizontal apenas quando as 6 opcoes nao couberem. */}
      <div className="historico-servicos__filter-scroll">
        <SegmentedControl
          options={statusOptions}
          activeValue={activeStatus}
          onChange={setActiveStatus}
          className="historico-servicos__filter"
          showNavigation
        />
      </div>

      <div className="historico-servicos__list">
        {filteredServices.map((service) => (
          <ServiceHistory key={service.id} service={service} />
        ))}
      </div>
    </section>
  );
}

export default HistoricoServicos;
