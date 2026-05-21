import { useEffect, useState } from "react";
import { ArrowLeft, MoreVertical, Paperclip, Send } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import { api } from "../../services/api";
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
  const [chat, setChat] = useState(null);
  const [draft, setDraft] = useState("");

  useEffect(() => {
    const request = professionalId
      ? api.getChatByProfessionalId(professionalId)
      : api.getChatById(chatId);

    request
      .then(setChat)
      .catch((error) => {
        console.error("Erro ao carregar conversa:", error);
        setChat(null);
      });
  }, [chatId, professionalId]);

  async function handleSendMessage(event) {
    event.preventDefault();

    if (!draft.trim()) {
      return;
    }

    try {
      const updatedChat = await api.sendChatMessage({
        chatId,
        professionalId,
        text: draft.trim(),
      });
      setChat(updatedChat);
      setDraft("");
    } catch (error) {
      console.error("Erro ao enviar mensagem:", error);
    }
  }

  if (!chat) {
    return (
      <section className="chat-interno chat-interno--empty">
        <button className="chat-interno__back" type="button" onClick={() => navigate("/chat")}>
          <ArrowLeft aria-hidden="true" />
          Voltar
        </button>
        <p>Profissional ou conversa nao encontrada.</p>
      </section>
    );
  }

  const messages = getMessagesWithDateSeparators(chat.messages || []);

  return (
    <section className="chat-interno">
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
        </button>
      </header>

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

      <form className="chat-interno__composer" onSubmit={handleSendMessage}>
        <button className="chat-interno__icon-button" type="button" aria-label="Anexar arquivo">
          <Paperclip aria-hidden="true" />
        </button>

        <input
          className="chat-interno__input"
          type="text"
          placeholder="Digite uma mensagem..."
          value={draft}
          onChange={(event) => setDraft(event.target.value)}
        />

        <button className="chat-interno__send" type="submit" aria-label="Enviar mensagem">
          <Send aria-hidden="true" />
        </button>
      </form>
    </section>
  );
}

export default ChatInterno;
