import "../../styles/global.css";

export default function SegmentedControl({
  options,
  activeValue,
  onChange,
  className = "",
}) {
  const segmentedClassName = ["segmented-control", className]
    .filter(Boolean)
    .join(" ");

  return (
    /* Navegacao controlada por props, sem estado interno fixo. */
    <div className={segmentedClassName} role="tablist" aria-label="Opcoes de busca">
      {options.map((option) => {
        const isActive = option.value === activeValue;

        return (
          /* Cada opcao divide igualmente o espaco disponivel. */
          <button
            className={`segmented-control__option${
              isActive ? " segmented-control__option--active" : ""
            }`}
            key={option.value}
            type="button"
            role="tab"
            aria-selected={isActive}
            onClick={() => onChange(option.value)}
          >
            {option.label}
          </button>
        );
      })}
    </div>
  );
}
