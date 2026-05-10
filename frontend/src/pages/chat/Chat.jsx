import ChatList from "../../components/ui/ChatList";
import SearchBar from "../../components/ui/SearchBar";
import { SimulChats } from "../../data/SimulChats";

function Chat() {
  return (
    <div className="chat-page">
      <SearchBar /> {/* Barra de pesquisa para filtrar conversas por nome ou conteúdo. */}
      <ChatList chats={SimulChats} />
    </div>
  );
}

export default Chat;
