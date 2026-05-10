import { useNavigate } from "react-router-dom";
import "../../styles/global.css";

function getInitials(chat) {
  if (chat.initials) {
    return chat.initials;
  }

  const name = chat.name || chat.user?.name || "";

  return name
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0])
    .join("")
    .toUpperCase();
}

export default function ChatCard({ chat, onClick }) {
  const navigate = useNavigate();
  const name = chat.name || chat.user?.name;
  const time = chat.time || chat.lastMessageTime;
  const unreadCount = Number(chat.unreadCount) || 0;

  function handleClick() {
    onClick?.(chat);
    navigate(`/chat/${chat.id}`);
  }

  return (
    /* Botao deixa o card clicavel e pronto para navegar futuramente. */
    <button className="chat-card" type="button" onClick={handleClick}>
      {/* Avatar circular com iniciais calculadas a partir dos dados da conversa. */}
      <span className="chat-card__avatar" aria-hidden="true">
        {getInitials(chat)}
      </span>

      {/* Area central com nome e ultima mensagem. */}
      <span className="chat-card__content">
        <span className="chat-card__name">{name}</span>
        <span className="chat-card__message">{chat.lastMessage}</span>
      </span>

      {/* Area lateral com horario/data e badge quando houver mensagens nao lidas. */}
      <span className="chat-card__meta">
        <span className="chat-card__time">{time}</span>
        {unreadCount > 0 && (
          <span className="chat-card__badge" aria-label={`${unreadCount} mensagens nao lidas`}>
            {unreadCount}
          </span>
        )}
      </span>
    </button>
  );
}
