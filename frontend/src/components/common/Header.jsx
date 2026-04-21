import { useState } from "react";
import { Link } from "react-router-dom";
import Logo from "../../assets/logo.png";
import { Menu } from 'lucide-react';
import { CircleX } from 'lucide-react';

const Header = () => {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="header">
      <div className="container header-content">

        {/* LOGO */}
        <div className="logo">
          <Link to="/">
            <img src={Logo} alt="Desenrola Logo" />
          </Link>
        </div>

        {/* BOTÃO MOBILE */}
        <button className="menu-toggle" onClick={() => setMenuOpen(!menuOpen)}>
          <Menu size={32} stroke-width={2.2}/>
        </button>

        {/* NAV */}
        <nav className={`nav ${menuOpen ? "open" : ""}`}>
            <button className="close-menu" onClick={() => setMenuOpen(false)}>
                <CircleX size={32} stroke-width={2}/>
            </button>

          <ul>
            <li><Link to="/" onClick={() => setMenuOpen(false)}>Início</Link></li>
            <li><Link to="/chat" onClick={() => setMenuOpen(false)}>Chat</Link></li>
            <li><Link to="/search" onClick={() => setMenuOpen(false)}>Pesquisar</Link></li>
            <li><Link to="/services" onClick={() => setMenuOpen(false)}>Serviços</Link></li>
            <li><Link to="/profile" onClick={() => setMenuOpen(false)}>Perfil</Link></li>
          </ul>
        </nav>

      </div>
    </header>
  );
};


export default Header;