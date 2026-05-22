import "../../styles/global.css";

export default function ActionButton({
  text,
  children,
  onClick,
  type = "button",
  className = "",
  disabled = false,
}) {

  /* Mantem a classe base do componente e permite classes extras por pagina. */
  const buttonClassName = ["action-button", className]
    .filter(Boolean)
    .join(" ");

  return (
    <button
      className={buttonClassName}
      type={type}
      onClick={onClick}
      disabled={disabled}
    >
      {text || children}
    </button>
  );
}
