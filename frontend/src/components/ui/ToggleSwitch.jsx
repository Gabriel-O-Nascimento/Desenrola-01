import "../../styles/global.css";

export default function ToggleSwitch({
  checked,
  onChange,
  className = "",
  disabled = false,
  ariaLabel = "Alternar configuracao",
}) {
  const switchClassName = [
    "toggle-switch",
    checked ? "toggle-switch--checked" : "",
    className,
  ]
    .filter(Boolean)
    .join(" ");

  return (
    /* Botao controlado externamente: a pagina pai decide o estado checked. */
    <button
      className={switchClassName}
      type="button"
      aria-label={ariaLabel}
      aria-pressed={checked}
      disabled={disabled}
      onClick={onChange}
    >
      {/* Bolinha visual que muda de posicao via CSS. */}
      <span className="toggle-switch__thumb" aria-hidden="true" />
    </button>
  );
}
