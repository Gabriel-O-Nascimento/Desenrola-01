import { useState } from "react";
import { Link } from "react-router-dom";
import Logo from "../../assets/logo.png";

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
                {/* NAVEGAÇÃO */}
                <nav className={`nav ${menuOpen ? "open" : ""}`}>
                    <ul>
                        <li><Link to="/">Início</Link></li>
                        <li><Link to="/chat">Chat</Link></li>
                        <li><Link to="/search">Pesquisar</Link></li>
                        <li><Link to="/services">Serviços</Link></li>
                        <li><Link to="/profile">Perfil</Link></li>
                    </ul>
                </nav>
            </div>

        </header>
    );
};

export default Header;