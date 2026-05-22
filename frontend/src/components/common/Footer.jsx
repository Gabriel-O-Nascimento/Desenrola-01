import { Link } from "react-router-dom";
import Logo from "../../assets/logo.png";

const Footer = () => {
  return (
    <footer className="footer">

      {/* PARTE SUPERIOR */}
      <div className="container footer-top">

        {/* LOGO (ESQUERDA) */}
        <div className="logo">
          <Link to="/home">
            <img src={Logo} alt="Desenrola Logo" />
          </Link>
        </div>

        {/* NAV (DIREITA) */}
        <nav className="footer-nav">
          <ul>
            <li><Link to="/home">Início</Link></li>
            <li><Link to="/chat">Chat</Link></li>
            <li><Link to="/search">Pesquisar</Link></li>
            <li><Link to="/historico">Serviços</Link></li>
            <li><Link to="/profile">Perfil</Link></li>
          </ul>
        </nav>

      </div>

      {/* PARTE INFERIOR */}
      <div className="container footer-bottom">
        <p>&copy; 2026 Desenrola. Todos os direitos reservados.</p>
      </div>

    </footer>
  );
};

export default Footer;