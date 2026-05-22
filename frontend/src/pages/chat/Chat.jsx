import { useMemo, useState } from "react";
import ChatList from "../../components/ui/ChatList";
import SearchBar from "../../components/ui/SearchBar";
import { ChatData } from "../../data/ChatData";

function Chat() {
  const [searchTerm, setSearchTerm] = useState("");

  const filteredChats = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();

    if (!term) {
      return ChatData;
    }

    // Filtra por nome do contato ou conteudo da ultima mensagem.
    return ChatData.filter((chat) => {
      const fields = [chat.name, chat.lastMessage, chat.message, chat.title]
        .filter(Boolean)
        .map((field) => field.toString().toLowerCase());

      return fields.some((field) => field.includes(term));
    });
  }, [searchTerm]);

  return (
    <div className="chat-page">
      {/* Barra de pesquisa para filtrar conversas por nome ou conteúdo. */}
      <SearchBar
        value={searchTerm}
        onChange={(event) => setSearchTerm(event.target.value)}
        placeholder="Filtrar conversas..."
        id="chat-search"
      />
      <ChatList chats={filteredChats} />
    </div>
  );
}

export default Chat;
