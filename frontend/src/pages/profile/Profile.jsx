import { useState } from "react";
import {
  Bell,
  ChevronRight,
  CreditCard,
  Edit2,
  FileText,
  Headphones,
  HelpCircle,
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

          <button className="profile-card__edit-avatar" type="button" aria-label="Editar foto">
            <Edit2 aria-hidden="true" />
          </button>
        </div>

        <h1 className="profile-card__name">{profileData.name}</h1>
        <p className="profile-card__contact">{profileData.phone}</p>
        <p className="profile-card__contact">{profileData.email}</p>

        <ActionButton
          className="action-button__outline profile-card__button"
          disabled
          onClick={() => {}}
        >
          <Edit2 aria-hidden="true" />
          Editar perfil
        </ActionButton>
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
        <div className="profile-list">
          <ProfileListItem
            icon={HelpCircle}
            title="Central de ajuda"
            onClick={() => handleOpenProfileOption("central_de_ajuda")}
          />
          <ProfileListItem
            icon={FileText}
            title="Termos de uso"
            onClick={() => handleOpenProfileOption("termos_de_uso")}
          />
          <ProfileListItem
            icon={Shield}
            title="Politica de privacidade"
            onClick={() => handleOpenProfileOption("politica_de_privacidade")}
          />
          <ProfileListItem
            icon={Headphones}
            title="Fale conosco"
            onClick={() => handleOpenProfileOption("fale_conosco")}
          />
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
