import { useEffect, useState } from "react";
import ChatList from "../../components/ui/ChatList";
import SearchBar from "../../components/ui/SearchBar";
import { api } from "../../services/api";

function Chat() {
  const [chats, setChats] = useState([]);

  useEffect(() => {
    api.getChats()
      .then(setChats)
      .catch((error) => {
        console.error("Erro ao carregar chats:", error);
      });
  }, []);

  return (
    <div className="chat-page">
      <SearchBar />
      <ChatList chats={chats} />
    </div>
  );
}

export default Chat;
