import { useRef, useState } from "react";
import { ArrowLeft, Camera, Eye, EyeOff } from "lucide-react";
import { useNavigate } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import FormInput from "../../components/ui/FormInput";
import SegmentedControl from "../../components/ui/SegmentedControl";
import { categorias } from "../../data/Categorias";
import { profissionalService } from "../../services/profissionalService";
import "../../styles/global.css";

const documentTypeOptions = [
  { label: "CPF", value: "cpf" },
  { label: "CNPJ", value: "cnpj" },
];

const initialFormData = {
  fullName: "",
  email: "",
  password: "",
  phone: "",
  documentType: "cpf",
  cpf: "",
  cnpj: "",
  city: "",
  state: "",
  mainCategory: "",
  serviceDescription: "",
  professionalExperience: "",
  personalDocument: null,
};

const acceptedDocumentTypes = ["image/png", "image/jpeg"];

function onlyDigits(value) {
  return value.replace(/\D/g, "");
}

function formatPhone(value) {
  const digits = onlyDigits(value).slice(0, 11);

  if (digits.length <= 2) {
    return digits ? `(${digits}` : "";
  }

  if (digits.length <= 7) {
    return `(${digits.slice(0, 2)}) ${digits.slice(2)}`;
  }

  return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
}

function formatCpf(value) {
  const digits = onlyDigits(value).slice(0, 11);

  return digits
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

function formatCnpj(value) {
  const digits = onlyDigits(value).slice(0, 14);

  return digits
    .replace(/(\d{2})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1/$2")
    .replace(/(\d{4})(\d{1,2})$/, "$1-$2");
}

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidCpf(value) {
  const cpf = onlyDigits(value);

  if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) {
    return false;
  }

  const calculateDigit = (base, factor) => {
    const total = base
      .split("")
      .reduce((sum, digit) => sum + Number(digit) * factor--, 0);
    const rest = (total * 10) % 11;

    return rest === 10 ? 0 : rest;
  };

  const firstDigit = calculateDigit(cpf.slice(0, 9), 10);
  const secondDigit = calculateDigit(cpf.slice(0, 10), 11);

  return firstDigit === Number(cpf[9]) && secondDigit === Number(cpf[10]);
}

function isValidCnpj(value) {
  const cnpj = onlyDigits(value);

  if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) {
    return false;
  }

  const calculateDigit = (base, factors) => {
    const total = base
      .split("")
      .reduce((sum, digit, index) => sum + Number(digit) * factors[index], 0);
    const rest = total % 11;

    return rest < 2 ? 0 : 11 - rest;
  };

  const firstDigit = calculateDigit(cnpj.slice(0, 12), [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
  const secondDigit = calculateDigit(cnpj.slice(0, 13), [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);

  return firstDigit === Number(cnpj[12]) && secondDigit === Number(cnpj[13]);
}

function isValidPersonalDocument(file) {
  if (!file) {
    return false;
  }

  if (acceptedDocumentTypes.includes(file.type)) {
    return true;
  }

  return /\.(png|jpe?g)$/i.test(file.name);
}

function getFormattedValue(name, value) {
  if (name === "phone") {
    return formatPhone(value);
  }

  if (name === "cpf") {
    return formatCpf(value);
  }

  if (name === "cnpj") {
    return formatCnpj(value);
  }

  return value;
}

export default function CadastroProfissional() {
  const navigate = useNavigate();
  const documentInputRef = useRef(null);

  const [formData, setFormData] = useState(initialFormData);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const isCpfSelected = formData.documentType === "cpf";
  const documentFieldName = isCpfSelected ? "cpf" : "cnpj";
  const serviceDescriptionLength = formData.serviceDescription.trim().length;
  const documentDescription = isCpfSelected
    ? "Envie fotos frente e verso de algum documento pessoal para verificação (CNH ou RG)"
    : "Envie fotos frente e verso de algum documento pessoal para verificação (Contrato Social e documento do Sócio administrador)";

  function validateField(name, value, data = formData) {
    const fieldValue = typeof value === "string" ? value.trim() : value;

    switch (name) {
      case "fullName":
        if (!fieldValue) return "Informe seu nome completo.";
        if (fieldValue.length < 3) return "O nome deve ter pelo menos 3 caracteres.";
        if (/\d/.test(fieldValue)) return "O nome não pode conter números.";
        return "";
      case "email":
        if (!fieldValue) return "Informe seu e-mail.";
        if (!isValidEmail(fieldValue)) return "Informe um e-mail válido.";
        return "";
      case "password":
        if (!value) return "Informe uma senha.";
        if (value.length < 6) return "A senha deve ter pelo menos 6 caracteres.";
        if (!/[A-Z]/.test(value)) return "A senha deve conter pelo menos uma letra maiúscula.";
        return "";
      case "phone":
        if (!fieldValue) return "Informe seu telefone.";
        if (!/^\(\d{2}\) \d{5}-\d{4}$/.test(fieldValue)) return "Informe um telefone no formato (00) 00000-0000.";
        return "";
      case "cpf":
        if (data.documentType !== "cpf") return "";
        if (!fieldValue) return "Informe seu CPF.";
        if (!/^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(fieldValue)) return "Informe um CPF no formato 000.000.000-00.";
        if (!isValidCpf(fieldValue)) return "Informe um CPF válido.";
        return "";
      case "cnpj":
        if (data.documentType !== "cnpj") return "";
        if (!fieldValue) return "Informe seu CNPJ.";
        if (!/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/.test(fieldValue)) return "Informe um CNPJ no formato 00.000.000/0000-00.";
        if (!isValidCnpj(fieldValue)) return "Informe um CNPJ válido.";
        return "";
      case "city":
        if (!fieldValue) return "Informe sua cidade.";
        return "";
      case "state":
        if (!fieldValue) return "Informe seu estado.";
        return "";
      case "mainCategory":
        if (!fieldValue) return "Selecione uma categoria principal.";
        return "";
      case "serviceDescription":
        if (!fieldValue || fieldValue.length < 50) return "Descreva seus serviços com pelo menos 50 caracteres.";
        return "";
      case "personalDocument":
        // Documento pessoal e opcional. Validamos apenas o tipo do arquivo quando enviado.
        if (!value) return "";
        if (!isValidPersonalDocument(value)) return "Envie um arquivo PNG ou JPG.";
        return "";
      default:
        return "";
    }
  }

  function validateForm() {
    const requiredFields = [
      "fullName",
      "email",
      "password",
      "phone",
      documentFieldName,
      "city",
      "state",
      "mainCategory",
      "serviceDescription",
    ];
    const newTouched = requiredFields.reduce((fields, field) => ({ ...fields, [field]: true }), {});
    const newErrors = requiredFields.reduce((fieldErrors, field) => {
      const error = validateField(field, formData[field], formData);

      return error ? { ...fieldErrors, [field]: error } : fieldErrors;
    }, {});

    setTouched((currentTouched) => ({ ...currentTouched, ...newTouched }));
    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  }

  function updateFieldError(name, value, nextData) {
    if (!touched[name]) {
      return;
    }

    setErrors((currentErrors) => ({
      ...currentErrors,
      [name]: validateField(name, value, nextData),
    }));
  }

  function handleFieldChange(event) {
    const { name, value } = event.target;
    const nextValue = getFormattedValue(name, value);
    const nextData = {
      ...formData,
      [name]: nextValue,
    };

    setFormData(nextData);
    updateFieldError(name, nextValue, nextData);
  }

  function handleFieldBlur(event) {
    const { name } = event.target;

    setTouched((currentTouched) => ({
      ...currentTouched,
      [name]: true,
    }));
    setErrors((currentErrors) => ({
      ...currentErrors,
      [name]: validateField(name, formData[name], formData),
    }));
  }

  function handleDocumentTypeChange(value) {
    const previousDocumentField = value === "cpf" ? "cnpj" : "cpf";
    const nextData = {
      ...formData,
      documentType: value,
      [previousDocumentField]: "",
    };

    setFormData(nextData);
    setTouched((currentTouched) => ({
      ...currentTouched,
      cpf: false,
      cnpj: false,
    }));
    setErrors((currentErrors) => ({
      ...currentErrors,
      cpf: "",
      cnpj: "",
    }));
  }

  function handleDocumentUpload(event) {
    const file = event.target.files?.[0] || null;
    const nextData = {
      ...formData,
      personalDocument: file,
    };
    const error = validateField("personalDocument", file, nextData);

    setFormData(nextData);
    setTouched((currentTouched) => ({
      ...currentTouched,
      personalDocument: true,
    }));
    setErrors((currentErrors) => ({
      ...currentErrors,
      personalDocument: error,
    }));
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    profissionalService
      .cadastrar(formData)
      .then(() => {
        navigate("/login");
      })
      .catch(() => {
        navigate("/login");
      });
  }

  return (
    <section className="user-register-page professional-register-page">
      <header className="user-register-header">
        <button
          className="user-register-header__back"
          type="button"
          aria-label="Voltar"
          onClick={() => navigate(-1)}
        >
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="user-register-header__title">Cadastro Profissional</h1>
      </header>

      <form className="user-register-form" onSubmit={handleSubmit} noValidate>
        <section className="professional-register-hero">
          <h2 className="professional-register-hero__title">Seja um Desenrolado!</h2>
          <p className="professional-register-hero__text">
            Cadastre-se como profissional no Desenrola, receba solicitações de
            serviços e aumente suas oportunidades de trabalho.
          </p>
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Dados Pessoais</h2>

          <FormInput
            label="Nome completo *"
            name="fullName"
            type="texto"
            value={formData.fullName}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="Digite seu nome completo"
            errorMessage={errors.fullName}
          />

          <FormInput
            label="E-mail *"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="nome@exemplo.com"
            errorMessage={errors.email}
          />

          <FormInput
            label="Senha *"
            name="password"
            type={showPassword ? "texto" : "senha"}
            value={formData.password}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="Digite sua senha"
            errorMessage={errors.password}
            rightElement={
              <button
                className="form-input__icon-button"
                type="button"
                onClick={() => setShowPassword((current) => !current)}
                aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
              >
                {showPassword ? <EyeOff aria-hidden="true" /> : <Eye aria-hidden="true" />}
              </button>
            }
          />

          <FormInput
            label="Telefone / WhatsApp *"
            name="phone"
            type="telefone"
            value={formData.phone}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="(00) 00000-0000"
            errorMessage={errors.phone}
          />

          <div className="user-register-field-group">
            <span className="user-register-field-group__label">Tipo de Documento</span>
            <SegmentedControl
              options={documentTypeOptions}
              activeValue={formData.documentType}
              onChange={handleDocumentTypeChange}
              className="user-register-segmented"
            />
          </div>

          {isCpfSelected ? (
            <FormInput
              label="CPF *"
              name="cpf"
              type="cpf"
              value={formData.cpf}
              onChange={handleFieldChange}
              onBlur={handleFieldBlur}
              placeholder="000.000.000-00"
              errorMessage={errors.cpf}
            />
          ) : (
            <FormInput
              label="CNPJ *"
              name="cnpj"
              type="cnpj"
              value={formData.cnpj}
              onChange={handleFieldChange}
              onBlur={handleFieldBlur}
              placeholder="00.000.000/0000-00"
              errorMessage={errors.cnpj}
            />
          )}
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Localização</h2>

          <FormInput
            label="Cidade *"
            name="city"
            type="cidade"
            value={formData.city}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="Digite sua cidade"
            errorMessage={errors.city}
          />

          <FormInput
            label="Estado *"
            name="state"
            type="estado"
            value={formData.state}
            onChange={handleFieldChange}
            onBlur={handleFieldBlur}
            placeholder="Selecione seu estado"
            errorMessage={errors.state}
          />
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Informações do Serviço</h2>

          <label className="form-input" htmlFor="professional-main-category">
            <span className="form-input__label">Categoria principal *</span>
            <select
              className="form-input__control"
              id="professional-main-category"
              name="mainCategory"
              value={formData.mainCategory}
              onChange={handleFieldChange}
              onBlur={handleFieldBlur}
              aria-invalid={Boolean(errors.mainCategory)}
              aria-describedby={errors.mainCategory ? "professional-main-category-error" : undefined}
            >
              <option value="" disabled>
                Selecione uma categoria
              </option>
              {categorias.map((categoria) => (
                <option key={categoria.id} value={categoria.id}>
                  {categoria.nome}
                </option>
              ))}
            </select>
            {errors.mainCategory && (
              <p className="form-input__error" id="professional-main-category-error">
                {errors.mainCategory}
              </p>
            )}
          </label>

          <label className="service-request-field" htmlFor="professional-service-description">
            <span className="service-request-field__label">Descrição dos Serviços *</span>
            <textarea
              className="service-request-field__textarea"
              id="professional-service-description"
              name="serviceDescription"
              value={formData.serviceDescription}
              placeholder="Descreva os serviços que você realiza, especialidades e tipos de atendimento."
              onChange={handleFieldChange}
              onBlur={handleFieldBlur}
              aria-invalid={Boolean(errors.serviceDescription)}
              aria-describedby={errors.serviceDescription ? "professional-service-description-error" : undefined}
            />
            {errors.serviceDescription && (
              <p className="form-input__error" id="professional-service-description-error">
                {errors.serviceDescription}
              </p>
            )}
          </label>
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Experiência Profissional</h2>

          <label className="service-request-field" htmlFor="professional-experience">
            <span className="service-request-field__label">Experiência profissional</span>
            <textarea
              className="service-request-field__textarea service-request-field__textarea--small"
              id="professional-experience"
              name="professionalExperience"
              value={formData.professionalExperience}
              placeholder="Conte um pouco sobre sua experiência, tempo de atuação ou trabalhos realizados."
              onChange={handleFieldChange}
            />
          </label>
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Documento Pessoal</h2>
          <p className="user-register-card__text">{documentDescription}</p>

          <input
            className="user-register-upload__input"
            ref={documentInputRef}
            name="personalDocument"
            type="file"
            accept="image/png, image/jpeg"
            onChange={handleDocumentUpload}
          />

          <button
            className="user-register-upload"
            type="button"
            onBlur={() => {
              setTouched((currentTouched) => ({ ...currentTouched, personalDocument: true }));
              setErrors((currentErrors) => ({
                ...currentErrors,
                personalDocument: validateField("personalDocument", formData.personalDocument, formData),
              }));
            }}
            onClick={() => documentInputRef.current?.click()}
          >
            <Camera className="user-register-upload__icon" aria-hidden="true" />
            <span className="user-register-upload__text">
              {formData.personalDocument
                ? formData.personalDocument.name
                : "Clique para adicionar fotos"}
            </span>
            <span className="user-register-upload__hint">PNG ou JPG até 10MB</span>
          </button>

          {errors.personalDocument && (
            <p className="form-input__error">{errors.personalDocument}</p>
          )}
        </section>

        <div className="user-register-actions">
          <ActionButton
            text="Realizar Cadastro"
            type="submit"
            className="user-register-actions__button"
          />
          <ActionButton
            text="Voltar"
            className="action-button__outline user-register-actions__button"
            onClick={() => navigate(-1)}
          />
        </div>
      </form>
    </section>
  );
}
