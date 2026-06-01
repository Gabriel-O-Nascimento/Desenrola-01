import { useState } from "react";
import {
  Bell,
  ChevronRight,
  CreditCard,
  FileText,
  Headphones,
  LogOut,
  Mail,
  MapPin,
  Phone,
  Shield,
  User,
} from "lucide-react";
import ActionButton from "../../components/ui/ActionButton";
import SegmentedControl from "../../components/ui/SegmentedControl";
import ToggleSwitch from "../../components/ui/ToggleSwitch";
import { profileData } from "../../data/ProfileData";
import "../../styles/global.css";
import { useNavigate } from "react-router-dom";

const contactOptions = [
  { label: "Chat", value: "chat" },
  { label: "Telefone", value: "phone" },
];

function ProfileListItem({ icon: Icon, title, description, onClick }) {
  return (
    <button className="profile-list__item" type="button" onClick={onClick}>
      <span className="profile-list__icon" aria-hidden="true">
        <Icon />
      </span>

      <span className="profile-list__content">
        <span className="profile-list__title">{title}</span>
        {description && <span className="profile-list__description">{description}</span>}
      </span>

      <ChevronRight className="profile-list__arrow" aria-hidden="true" />
    </button>
  );
}

function ProfilePreferenceItem({ icon: Icon, title, children }) {
  return (
    <div className="profile-list__item profile-list__item--static">
      <span className="profile-list__icon" aria-hidden="true">
        <Icon />
      </span>

      <span className="profile-list__content">
        <span className="profile-list__title">{title}</span>
      </span>

      {children}
    </div>
  );
}

function Profile() {
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);
  const [contactPreference, setContactPreference] = useState("chat");
  const [activeSupportItem, setActiveSupportItem] = useState("terms");
  const navigate = useNavigate();

  function handleOpenProfileOption(option) {
    // Reservado para futuras acoes de edicao do perfil.
  }

  return (
    <section className="profile-page">
      <article className="profile-card">
        <div className="profile-card__avatar-wrap">
          <span className="profile-card__avatar" aria-hidden="true">
            <User />
          </span>

        </div>

        <h1 className="profile-card__name">{profileData.name}</h1>
        <p className="profile-card__contact">{profileData.phone}</p>
        <p className="profile-card__contact">{profileData.email}</p>

      </article>

      <section className="profile-section">
        <h2 className="profile-section__title">Dados Pessoais</h2>
        <div className="profile-list">
          <ProfileListItem
            icon={Phone}
            title="Telefone"
            description={profileData.phone}
            onClick={() => handleOpenProfileOption("telefone")}
          />
          <ProfileListItem
            icon={MapPin}
            title="Endereco principal"
            description={profileData.address}
            onClick={() => handleOpenProfileOption("endereco")}
          />
          <ProfileListItem
            icon={CreditCard}
            title="Tipo de conta"
            description={profileData.accountType}
            onClick={() => handleOpenProfileOption("tipo_de_conta")}
          />
        </div>
      </section>

      <section className="profile-section">
        <h2 className="profile-section__title">Preferencias</h2>
        <div className="profile-list">
          <ProfilePreferenceItem icon={Bell} title="Notificacoes">
            <ToggleSwitch
              checked={notificationsEnabled}
              ariaLabel="Ativar notificacoes"
              onChange={() => setNotificationsEnabled((current) => !current)}
            />
          </ProfilePreferenceItem>

          <ProfilePreferenceItem icon={Mail} title="Preferencia de contato">
            <SegmentedControl
              options={contactOptions}
              activeValue={contactPreference}
              onChange={setContactPreference}
              className="profile-list__segmented"
            />
          </ProfilePreferenceItem>
        </div>
      </section>

      <section className="profile-section">
        <h2 className="profile-section__title">Suporte e Informacoes</h2>
        <div className="accordion accordion-flush profile-support-flush" id="profile-support-accordion">
          <div className="accordion-item profile-support-flush__item">
            <h3 className="accordion-header profile-support-flush__header" id="profile-support-terms-heading">
              <button
                className={`accordion-button profile-support-flush__button${
                  activeSupportItem === "terms" ? "" : " collapsed"
                }`}
                type="button"
                aria-expanded={activeSupportItem === "terms"}
                aria-controls="profile-support-terms"
                onClick={() => setActiveSupportItem((current) => (current === "terms" ? "" : "terms"))}
              >
                <span className="profile-list__icon" aria-hidden="true">
                  <FileText />
                </span>
                <span className="profile-support-flush__title">Termos de uso</span>
              </button>
            </h3>
            <div
              className={`accordion-collapse collapse${activeSupportItem === "terms" ? " show" : ""}`}
              id="profile-support-terms"
              aria-labelledby="profile-support-terms-heading"
            >
              <div className="accordion-body profile-support-flush__body">
                Ao usar o Desenrola, voce concorda em manter informacoes verdadeiras e respeitar as regras de contratacao da plataforma.
              </div>
            </div>
          </div>

          <div className="accordion-item profile-support-flush__item">
            <h3 className="accordion-header profile-support-flush__header" id="profile-support-privacy-heading">
              <button
                className={`accordion-button profile-support-flush__button${
                  activeSupportItem === "privacy" ? "" : " collapsed"
                }`}
                type="button"
                aria-expanded={activeSupportItem === "privacy"}
                aria-controls="profile-support-privacy"
                onClick={() => setActiveSupportItem((current) => (current === "privacy" ? "" : "privacy"))}
              >
                <span className="profile-list__icon" aria-hidden="true">
                  <Shield />
                </span>
                <span className="profile-support-flush__title">Politica de privacidade</span>
              </button>
            </h3>
            <div
              className={`accordion-collapse collapse${activeSupportItem === "privacy" ? " show" : ""}`}
              id="profile-support-privacy"
              aria-labelledby="profile-support-privacy-heading"
            >
              <div className="accordion-body profile-support-flush__body">
                Seus dados sao usados apenas para login, contato, seguranca da conta e melhoria da experiencia no aplicativo.
              </div>
            </div>
          </div>

          <div className="accordion-item profile-support-flush__item">
            <h3 className="accordion-header profile-support-flush__header" id="profile-support-contact-heading">
              <button
                className={`accordion-button profile-support-flush__button${
                  activeSupportItem === "contact" ? "" : " collapsed"
                }`}
                type="button"
                aria-expanded={activeSupportItem === "contact"}
                aria-controls="profile-support-contact"
                onClick={() => setActiveSupportItem((current) => (current === "contact" ? "" : "contact"))}
              >
                <span className="profile-list__icon" aria-hidden="true">
                  <Headphones />
                </span>
                <span className="profile-support-flush__title">Fale conosco</span>
              </button>
            </h3>
            <div
              className={`accordion-collapse collapse${activeSupportItem === "contact" ? " show" : ""}`}
              id="profile-support-contact"
              aria-labelledby="profile-support-contact-heading"
            >
              <div className="accordion-body profile-support-flush__body">
                Telefone: (11) 4002-8922. 
                <br/>
                SAC: atendimento@desenrola.com.br.
              </div>
            </div>
          </div>
        </div>
      </section>

      <div className="profile-actions">
        <ActionButton
          text="Tornar-se profissional"
          className="profile-actions__button"
          onClick={() => navigate("/cadastro/profissional")}
        />

        <ActionButton
          className="action-button__danger profile-actions__button"
          onClick={() => navigate("/login")}
        >
          <LogOut aria-hidden="true" />
          Sair da conta
        </ActionButton>
      </div>
    </section>
  );
}

export default Profile;
