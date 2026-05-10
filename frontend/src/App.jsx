import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import Home from "./pages/Home";
import Chat from "./pages/chat/Chat";
import ChatInterno from "./pages/chat/ChatInterno";
import Search from "./pages/search/Search";
import HistoricoServicos from "./pages/historico/HistoricoServicos";

function Layout() {
  const location = useLocation();

  /* Oculta o footer em todas as rotas do chat */
  const hideFooter = location.pathname.startsWith("/chat");

  return (
    <>
      <Header />

      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/search" element={<Search />} />
          <Route path="/Services" element={<HistoricoServicos />} />
          <Route path="/chat" element={<Chat />} />
          <Route path="/chat/:chatId" element={<ChatInterno />} />
        </Routes>
      </main>

      {!hideFooter && <Footer />}
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Layout />
    </BrowserRouter>
  );
}

export default App;
