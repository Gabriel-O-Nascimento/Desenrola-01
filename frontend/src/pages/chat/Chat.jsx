import ChatList from "../../components/ui/ChatList";
import SearchBar from "../../components/ui/SearchBar";
import { ChatData } from "../../data/ChatData";

function Chat() {
  return (
    <div className="chat-page">
      <SearchBar /> {/* Barra de pesquisa para filtrar conversas por nome ou conteúdo. */}
      <ChatList chats={ChatData} />
    </div>
  );
}

export default Chat;
