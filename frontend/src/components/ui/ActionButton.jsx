import "../../styles/global.css";

export default function ActionButton({
  children,
  onClick,
  type = "button",
  className = "",
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
    >
      {children}
    </button>
  );
}