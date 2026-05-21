import { useRef, useState } from "react";
import { ArrowLeft, Camera } from "lucide-react";
import { useNavigate } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import FormInput from "../../components/ui/FormInput";
import SegmentedControl from "../../components/ui/SegmentedControl";
import { api } from "../../services/api";
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
  personalDocument: null,
};

const acceptedDocumentTypes = ["image/png", "image/jpeg", "application/pdf"];

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

  return /\.(png|jpe?g|pdf)$/i.test(file.name);
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

export default function CadastroUsuario() {
  const navigate = useNavigate();
  const documentInputRef = useRef(null);

  const [formData, setFormData] = useState(initialFormData);
  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});
  const [generalError, setGeneralError] = useState("");

  const isCpfSelected = formData.documentType === "cpf";
  const documentFieldName = isCpfSelected ? "cpf" : "cnpj";
  const documentDescription = isCpfSelected
    ? "Envie fotos frente e verso de algum documento pessoal para verificacao (CNH ou RG)"
    : "Envie fotos frente e verso de algum documento pessoal para verificacao (Contrato Social e documento do socio administrador)";

  function validateField(name, value, data = formData) {
    const fieldValue = typeof value === "string" ? value.trim() : value;

    switch (name) {
      case "fullName":
        if (!fieldValue) return "Informe seu nome completo.";
        if (fieldValue.length < 3) return "O nome deve ter pelo menos 3 caracteres.";
        if (/\d/.test(fieldValue)) return "O nome nao pode conter numeros.";
        return "";
      case "email":
        if (!fieldValue) return "Informe seu e-mail.";
        if (!isValidEmail(fieldValue)) return "Informe um e-mail valido.";
        return "";
      case "password":
        if (!value) return "Informe uma senha.";
        if (value.length < 6) return "A senha deve ter pelo menos 6 caracteres.";
        if (!/[A-Z]/.test(value)) return "A senha deve conter pelo menos uma letra maiuscula.";
        return "";
      case "phone":
        if (!fieldValue) return "Informe seu telefone.";
        if (!/^\(\d{2}\) \d{5}-\d{4}$/.test(fieldValue)) return "Informe um telefone no formato (00) 00000-0000.";
        return "";
      case "cpf":
        if (data.documentType !== "cpf") return "";
        if (!fieldValue) return "Informe seu CPF.";
        if (!/^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(fieldValue)) return "Informe um CPF no formato 000.000.000-00.";
        if (!isValidCpf(fieldValue)) return "Informe um CPF valido.";
        return "";
      case "cnpj":
        if (data.documentType !== "cnpj") return "";
        if (!fieldValue) return "Informe seu CNPJ.";
        if (!/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/.test(fieldValue)) return "Informe um CNPJ no formato 00.000.000/0000-00.";
        if (!isValidCnpj(fieldValue)) return "Informe um CNPJ valido.";
        return "";
      case "city":
        if (!fieldValue) return "Informe sua cidade.";
        return "";
      case "state":
        if (!fieldValue) return "Informe seu estado.";
        return "";
      case "personalDocument":
        if (!value) return "Envie uma foto do documento pessoal.";
        if (!isValidPersonalDocument(value)) return "Envie um arquivo PNG, JPG ou PDF.";
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
      "personalDocument",
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

  async function handleSubmit(event) {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      await api.registerUser({
        fullName: formData.fullName,
        email: formData.email,
        password: formData.password,
        phone: formData.phone,
        documentType: formData.documentType,
        cpf: formData.cpf,
        cnpj: formData.cnpj,
        city: formData.city,
        state: formData.state,
        personalDocumentName: formData.personalDocument?.name || "",
      });
      navigate("/login");
    } catch (error) {
      setGeneralError(error.message);
    }
  }

  return (
    <section className="user-register-page">
      <header className="user-register-header">
        <button
          className="user-register-header__back"
          type="button"
          aria-label="Voltar"
          onClick={() => navigate(-1)}
        >
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="user-register-header__title">Cadastro de Usuario</h1>
      </header>

      <form className="user-register-form" onSubmit={handleSubmit} noValidate>
        <section className="user-register-card">
          <h2 className="user-register-card__title">Dados Pessoais</h2>

          <FormInput label="Nome completo *" name="fullName" type="texto" value={formData.fullName} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="Digite seu nome completo" errorMessage={errors.fullName} />
          <FormInput label="E-mail *" name="email" type="email" value={formData.email} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="nome@exemplo.com" errorMessage={errors.email} />
          <FormInput label="Senha *" name="password" type="senha" value={formData.password} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="Digite sua senha" errorMessage={errors.password} />
          <FormInput label="Telefone / WhatsApp *" name="phone" type="telefone" value={formData.phone} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="(00) 00000-0000" errorMessage={errors.phone} />

          <div className="user-register-field-group">
            <span className="user-register-field-group__label">Tipo de Documento</span>
            <SegmentedControl options={documentTypeOptions} activeValue={formData.documentType} onChange={handleDocumentTypeChange} className="user-register-segmented" />
          </div>

          {isCpfSelected ? (
            <FormInput label="CPF *" name="cpf" type="cpf" value={formData.cpf} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="000.000.000-00" errorMessage={errors.cpf} />
          ) : (
            <FormInput label="CNPJ *" name="cnpj" type="cnpj" value={formData.cnpj} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="00.000.000/0000-00" errorMessage={errors.cnpj} />
          )}
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Localizacao</h2>

          <FormInput label="Cidade *" name="city" type="cidade" value={formData.city} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="Digite sua cidade" errorMessage={errors.city} />
          <FormInput label="Estado *" name="state" type="estado" value={formData.state} onChange={handleFieldChange} onBlur={handleFieldBlur} placeholder="Selecione seu estado" errorMessage={errors.state} />
        </section>

        <section className="user-register-card">
          <h2 className="user-register-card__title">Documento Pessoal</h2>
          <p className="user-register-card__text">{documentDescription}</p>

          <input
            className="user-register-upload__input"
            ref={documentInputRef}
            name="personalDocument"
            type="file"
            accept="image/png, image/jpeg, application/pdf"
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
              {formData.personalDocument ? formData.personalDocument.name : "Clique para adicionar fotos"}
            </span>
            <span className="user-register-upload__hint">PNG, JPG ou PDF ate 10MB</span>
          </button>

          {errors.personalDocument && <p className="form-input__error">{errors.personalDocument}</p>}
        </section>

        {generalError && <p className="form-input__error">{generalError}</p>}

        <div className="user-register-actions">
          <ActionButton text="Realizar Cadastro" type="submit" className="user-register-actions__button" />
          <ActionButton text="Voltar" className="action-button__outline user-register-actions__button" onClick={() => navigate(-1)} />
        </div>
      </form>
    </section>
  );
}
