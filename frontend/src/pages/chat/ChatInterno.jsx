import { useEffect } from "react";
import { ArrowLeft, MoreVertical, Paperclip, Send } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { cadastrosProfissionais } from "../../data/CadastroProfissional";
import { ChatData } from "../../data/ChatData";
import "../../styles/global.css";

function getInitials(chat) {
  if (chat?.initials) {
    return chat.initials;
  }

  return (chat?.name || "")
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0])
    .join("")
    .toUpperCase();
}

function getMessagesWithDateSeparators(messages) {
  return messages.map((message, index) => {
    const previousMessage = messages[index - 1];
    const shouldShowDate = !previousMessage || previousMessage.dateLabel !== message.dateLabel;

    return {
      ...message,
      shouldShowDate,
    };
  });
}

function ChatInterno() {
  const { chatId, professionalId } = useParams();
  const navigate = useNavigate();
  const professional = cadastrosProfissionais.find(
    (item) => String(item.id) === String(professionalId)
  );
  const existingChat = professionalId
    ? ChatData.find((item) => String(item.professionalId) === String(professionalId))
    : ChatData.find((item) => String(item.id) === String(chatId));
  const chat = existingChat || (
    professional
      ? {
          id: `new-${professional.id}`,
          professionalId: professional.id,
          name: professional.name,
          initials: professional.initials,
          status: "Nova conversa",
          messages: [],
        }
      : null
  );

  useEffect(() => {
    // Reservado para acoes futuras quando um profissional especifico for selecionado.
  }, [professionalId]);

  if (!chat) {
    return (
      <section className="chat-interno chat-interno--empty">
        <button className="chat-interno__back" type="button" onClick={() => navigate("/chat")}>
          <ArrowLeft aria-hidden="true" />
          Voltar
        </button>
        <p>Profissional ou conversa não encontrada.</p>
      </section>
    );
  }

  const messages = getMessagesWithDateSeparators(chat.messages || []);

  return (
    <section className="chat-interno">
      {/* Header interno com dados da conversa atual. */}
      <header className="chat-interno__header">
        <button className="chat-interno__icon-button" type="button" onClick={() => navigate("/chat")} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>

        <span className="chat-interno__avatar" aria-hidden="true">
          {getInitials(chat)}
        </span>

        <div className="chat-interno__person">
          <h1 className="chat-interno__name">{chat.name}</h1>
          <span className="chat-interno__status">{chat.status}</span>
        </div>

        <button className="chat-interno__icon-button chat-interno__menu" type="button" aria-label="Abrir menu da conversa">
          <MoreVertical aria-hidden="true" />
        </button> {/*Incluir opções - Ver perfil, Silenciar, Bloquear, Denunciar*/}
      </header>


      {/* Area de mensagens renderizada a partir de ChatData. */}
      <div className="chat-interno__messages" aria-label="Mensagens da conversa">
        {messages.length === 0 && (
          <p className="chat-interno__empty-message">
            Nenhuma mensagem ainda. Envie a primeira mensagem.
          </p>
        )}

        {messages.map((message) => (
          <div className="chat-interno__message-group" key={message.id}>
            {message.shouldShowDate && (
              <span className="chat-interno__date">{message.dateLabel}</span>
            )}

            <article className={`chat-message chat-message--${message.type}`}>
              <p className="chat-message__text">{message.text}</p>
              <time className="chat-message__time">{message.time}</time>
            </article>
          </div>
        ))}
      </div>

      {/* Barra visual de envio, sem logica funcional por enquanto. */}
      <form className="chat-interno__composer">
        <button className="chat-interno__icon-button" type="button" aria-label="Anexar arquivo">
          <Paperclip aria-hidden="true" />
        </button>

        <input
          className="chat-interno__input"
          type="text"
          placeholder="Digite uma mensagem..."
        />

        <button className="chat-interno__send" type="button" aria-label="Enviar mensagem">
          <Send aria-hidden="true" />
        </button>
      </form>
    </section>
  );
}

export default ChatInterno;
