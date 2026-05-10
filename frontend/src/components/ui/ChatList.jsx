import ChatCard from "./ChatCard";
import "../../styles/global.css";

export default function ChatList({ chats, onChatClick }) {
  return (
    /* Lista sem dados internos: recebe as conversas por props. */
    <section className="chat-list" aria-label="Lista de conversas">
      {chats.map((chat) => (
        <ChatCard key={chat.id} chat={chat} onClick={onChatClick} />
      ))}
    </section>
  );
}
