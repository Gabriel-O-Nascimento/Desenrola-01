import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import Home from "./pages/Home";
import Chat from "./pages/chat/Chat";
import ChatInterno from "./pages/chat/ChatInterno";
import Search from "./pages/search/Search";
import HistoricoServicos from "./pages/historico/HistoricoServicos";
import Profile from "./pages/profile/Profile";
import Orcamento from "./pages/historico/Orcamento";
import OrcamentoAprovado from "./pages/historico/OrcamentoAprovado";
import OrcamentoRecusado from "./pages/historico/OrcamentoRecusado";
import DetalhesCancelamento from "./pages/historico/DetalhesCancelamento";
import AcompanhamentoServicos from "./pages/historico/AcompanhamentoServicos";
import ServicoConcluido from "./pages/historico/ServicoConcluido";
import AvaliarServico from "./pages/historico/AvaliarServico";
import SolicitarServico from "./pages/servicos/SolicitarServico";
import CadastroUsuario from "./pages/cadastros/CadastroUsuario";
import CadastroProfissional from "./pages/cadastros/CadastroProfissional";
import PerfilProfissional from "./pages/profile/PerfilProfissional";
import ServicoAprovado from "./pages/servicos/ServicoAprovado";

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
          <Route path="/historico" element={<HistoricoServicos />} />
          <Route path="/Services" element={<HistoricoServicos />} />
          <Route path="/historico/orcamento/:id" element={<Orcamento />} />
          <Route path="/historico/cancelamento/:id" element={<DetalhesCancelamento />} />
          <Route path="/historico/acompanhamento/:id" element={<AcompanhamentoServicos />} />
          <Route path="/historico/servico-concluido/:id" element={<ServicoConcluido />} />
          <Route path="/historico/avaliar-servico/:id" element={<AvaliarServico />} />
          <Route path="/historico/orcamento-aprovado" element={<OrcamentoAprovado />} />
          <Route path="/historico/orcamento-recusado" element={<OrcamentoRecusado />} />
          <Route path="/orcamento/:id" element={<Orcamento />} />
          <Route path="/servicos/solicitar/:id" element={<SolicitarServico />} />
          <Route path="/servicos/solicitar/profissional/:professionalId" element={<SolicitarServico />} />
          <Route path="/servicos/aprovado" element={<ServicoAprovado />} />
          <Route path="/cadastro/usuario" element={<CadastroUsuario />} />
          <Route path="/cadastro/profissional" element={<CadastroProfissional />} />
          <Route path="/perfil/profissional" element={<PerfilProfissional />} />
          <Route path="/perfil/profissional/:professionalId" element={<PerfilProfissional />} />
          <Route path="/profile/profissional/:professionalId" element={<PerfilProfissional />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/chat" element={<Chat />} />
          <Route path="/chat/:chatId" element={<ChatInterno />} />
          <Route path="/chat/profissional/:professionalId" element={<ChatInterno />} />
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
