import { ChevronLeft, ChevronRight } from "lucide-react";
import { useRef } from "react";
import "../../styles/global.css";

export default function SegmentedControl({
  options,
  activeValue,
  onChange,
  className = "",
  showNavigation = false,
}) {
  const segmentedRef = useRef(null);

  const segmentedClassName = ["segmented-control", className]
    .filter(Boolean)
    .join(" ");

  function scrollOptions(direction) {
    segmentedRef.current?.scrollBy({
      left: direction * 200,
      behavior: "smooth",
    });
  }

  function renderOptions() {
    return options.map((option) => {
      const isActive = option.value === activeValue;

      return (
        /* Cada opcao cresce conforme o proprio texto, sem quebrar linha. */
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
    });
  }

  if (!showNavigation) {
    return (
      /* Navegacao controlada por props, sem estado interno fixo. */
      <div ref={segmentedRef} className={segmentedClassName} role="tablist" aria-label="Opcoes de busca">
        {renderOptions()}
      </div>
    );
  }

  return (
    /* Botoes laterais sao opcionais e ficam visiveis apenas no desktop via CSS. */
    <div className="segmented-control-navigation">
      <button
        className="segmented-control-navigation__button"
        type="button"
        aria-label="Voltar opcoes"
        onClick={() => scrollOptions(-1)}
      >
        <ChevronLeft aria-hidden="true" />
      </button>

      <div ref={segmentedRef} className={segmentedClassName} role="tablist" aria-label="Opcoes de busca">
        {renderOptions()}
      </div>

      <button
        className="segmented-control-navigation__button"
        type="button"
        aria-label="Avancar opcoes"
        onClick={() => scrollOptions(1)}
      >
        <ChevronRight aria-hidden="true" />
      </button>
    </div>
  );
}
