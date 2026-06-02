import { useState } from "react";
import { Eye, EyeOff, Lock, Mail } from "lucide-react";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/logo.png";
import ActionButton from "../../components/ui/ActionButton";
import FormInput from "../../components/ui/FormInput";
import { usuarioService } from "../../services/usuarioService";
import "../../styles/global.css";

const initialFormData = {
  email: "",
  password: "",
};

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState(initialFormData);
  const [errors, setErrors] = useState({});
  const [generalError, setGeneralError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function validateForm() {
    const newErrors = {};

    if (!formData.email.trim()) {
      newErrors.email = "Informe seu e-mail.";
    } else if (!isValidEmail(formData.email)) {
      newErrors.email = "Informe um e-mail valido.";
    }

    if (!formData.password) {
      newErrors.password = "Informe sua senha.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }

  function handleFieldChange(event) {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));

    setErrors((currentErrors) => ({
      ...currentErrors,
      [name]: "",
    }));
    setGeneralError("");
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);
    setGeneralError("");

    try {
      await usuarioService.login(formData);
      navigate("/home");
    } catch (error) {
      setGeneralError(error.message || "E-mail ou senha invalidos.");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="login-page">
      <div className="login-card">
        <header className="login-brand">
          <img className="login-brand__logo" src={logo} alt="Logo Desenrola" />
        </header>

        <form className="login-form" onSubmit={handleSubmit} noValidate>
          <h1 className="login-form__title">Entrar na sua conta</h1>

          <div className="login-field">
            <FormInput
              label="E-mail"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleFieldChange}
              placeholder="seu@email.com"
              className="login-form-input"
              errorMessage={errors.email}
              leftIcon={<Mail aria-hidden="true" />}
            />
          </div>

          <div className="login-field">
            <FormInput
              label="Senha"
              name="password"
              type={showPassword ? "texto" : "senha"}
              value={formData.password}
              onChange={handleFieldChange}
              placeholder="Digite sua senha"
              className="login-form-input"
              errorMessage={errors.password}
              leftIcon={<Lock aria-hidden="true" />}
              rightElement={
                <button
                  className="form-input__icon-button"
                  type="button"
                  onClick={() => setShowPassword((currentValue) => !currentValue)}
                  aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
                >
                  {showPassword ? <EyeOff aria-hidden="true" /> : <Eye aria-hidden="true" />}
                </button>
              }
            />
          </div>

          {generalError && <p className="login-form__error">{generalError}</p>}

          <ActionButton
            text={isSubmitting ? "Entrando..." : "Entrar"}
            type="submit"
            className="login-form__submit"
            disabled={isSubmitting}
          />

          <div className="login-divider" aria-hidden="true">
            <span />
            <strong>ou</strong>
            <span />
          </div>

          <div className="login-register">
            <p>Nao tem uma conta?</p>
            <button type="button" onClick={() => navigate("/cadastro/usuario")}>
              Cadastre-se
            </button>
          </div>
        </form>

        <p className="login-terms">
          Ao continuar, voce concorda com nossos Termos de Uso e Politica de Privacidade
        </p>
      </div>
    </section>
  );
}
