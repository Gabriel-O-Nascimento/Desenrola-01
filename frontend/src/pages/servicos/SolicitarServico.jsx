import { useEffect, useMemo, useRef, useState } from "react";
import { ArrowLeft, Calendar, Camera, Clock, Info, MapPin, Package } from "lucide-react";
import { useNavigate, useParams } from "react-router-dom";
import ActionButton from "../../components/ui/ActionButton";
import FormInput from "../../components/ui/FormInput";
import { cadastrosProfissionais } from "../../data/CadastroProfissional";
import { professionalsLists } from "../../data/Professionals";
import "../../styles/global.css";

function getAllServices() {
  // Une as listas da Home para encontrar o servico recebido pela rota.
  return professionalsLists.flatMap((list) =>
    list.items.map((item) => ({
      ...item,
      category: list.title,
    }))
  );
}

function getTodayDateString() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

function getCurrentTimeString() {
  const now = new Date();
  const hours = String(now.getHours()).padStart(2, "0");
  const minutes = String(now.getMinutes()).padStart(2, "0");

  return `${hours}:${minutes}`;
}

function isValidDateString(dateValue) {
  return /^\d{4}-\d{2}-\d{2}$/.test(dateValue);
}

function isDateBeforeToday(dateValue) {
  return dateValue < getTodayDateString();
}

function isToday(dateValue) {
  return dateValue === getTodayDateString();
}

function isTimeBeforeNow(timeValue) {
  return timeValue < getCurrentTimeString();
}

function openNativePicker(inputRef) {
  // Abre o seletor nativo ao clicar no icone customizado.
  const input = inputRef.current;

  if (!input) {
    return;
  }

  if (typeof input.showPicker === "function") {
    input.showPicker();
    return;
  }

  input.focus();
}

function SolicitarServico() {
  const { id, professionalId } = useParams();
  const navigate = useNavigate();
  const dateInputRef = useRef(null);
  const timeInputRef = useRef(null);
  const photoInputRef = useRef(null);

  const professional = useMemo(
    () => cadastrosProfissionais.find((item) => String(item.id) === String(professionalId)),
    [professionalId]
  );

  const service = useMemo(() => {
    const services = getAllServices();

    if (professionalId && professional) {
      return services.find((item) => String(item.id) === String(professional.serviceId));
    }

    return services.find((item) => String(item.id) === String(id));
  }, [id, professional, professionalId]);

  useEffect(() => {
    if (professionalId) {
      console.log("Profissional selecionado para solicitação:", professionalId);
    }
  }, [professionalId]);

  const [formData, setFormData] = useState({
    address: "",
    city: "",
    date: "",
    time: "",
    description: "",
    photos: [],
    observations: "",
  });
  const [errors, setErrors] = useState({});

  const todayDate = getTodayDateString();

  function handleFieldChange(event) {
    const { name, value, files } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: files ? files[0] : value,
    }));

    setErrors((currentErrors) => ({
      ...currentErrors,
      [name]: "",
    }));
  }

  function handlePhotoChange(event) {
    const files = Array.from(event.target.files || []);

    setFormData((currentData) => ({
      ...currentData,
      photos: files,
    }));
  }

  function validateForm() {
    const newErrors = {};

    if (!formData.address.trim()) {
      newErrors.address = "Informe o endereço do serviço.";
    }

    if (!formData.city.trim()) {
      newErrors.city = "Informe a cidade.";
    }

    if (!formData.date) {
      newErrors.date = "Informe a data desejada.";
    } else if (!isValidDateString(formData.date)) {
      newErrors.date = "Informe uma data válida.";
    } else if (isDateBeforeToday(formData.date)) {
      newErrors.date = "A data não pode ser anterior à data atual.";
    }

    if (!formData.time) {
      newErrors.time = "Informe o horário desejado.";
    } else if (isValidDateString(formData.date) && isToday(formData.date) && isTimeBeforeNow(formData.time)) {
      newErrors.time = "O horário não pode ser anterior ao horário atual.";
    }

    if (!formData.description.trim()) {
      newErrors.description = "Descreva o serviço que precisa ser realizado.";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    console.log("Solicitacao enviada:", formData);
  }

  if (professionalId && !professional) {
    return (
      <section className="service-request-page">
        <header className="service-request-header">
          <button className="service-request-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="service-request-header__title">Solicitar Servico</h1>
        </header>

        <p className="service-request-page__empty">Profissional não encontrado.</p>
      </section>
    );
  }

  if (!service) {
    return (
      <section className="service-request-page">
        <header className="service-request-header">
          <button className="service-request-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft aria-hidden="true" />
          </button>
          <h1 className="service-request-header__title">Solicitar Servico</h1>
        </header>

        <p className="service-request-page__empty">
          {professionalId ? "Serviço não encontrado para este profissional." : "Servico nao encontrado."}
        </p>
      </section>
    );
  }

  return (
    <section className="service-request-page">
      <header className="service-request-header">
        <button className="service-request-header__back" type="button" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft aria-hidden="true" />
        </button>
        <h1 className="service-request-header__title">Solicitar Servico</h1>
      </header>

      <article className="service-request-card">
        <span className="service-request-card__icon" aria-hidden="true">
          <Package />
        </span>
        <h2 className="service-request-card__title">{service.name}</h2>
        <p className="service-request-card__category">{service.category}</p>
        <p className="service-request-card__description">{service.description}</p>
      </article>

      <form className="service-request-form" onSubmit={handleSubmit}>
        <h2 className="service-request-form__title">Informacoes do Servico</h2>

        <FormInput
          label="Endereco do servico *"
          name="address"
          type="texto"
          value={formData.address}
          placeholder="Digite o endereco onde o profissional deve ir"
          onChange={handleFieldChange}
          errorMessage={errors.address}
        />

        <div className="service-request-picker service-request-picker--static">
          <FormInput
            label="Cidade *"
            name="city"
            type="cidade"
            value={formData.city}
            placeholder="Digite a cidade do endereco"
            onChange={handleFieldChange}
            errorMessage={errors.city}
          />
          <span className="service-request-picker__button service-request-picker__button--static" aria-hidden="true">
            <MapPin />
          </span>
        </div>

        <div className="service-request-picker">
          {/* FormInput recebe ref e min para controlar o seletor nativo de data. */}
          <FormInput
            label="Data desejada *"
            name="date"
            type="data"
            value={formData.date}
            min={todayDate}
            inputRef={dateInputRef}
            onChange={handleFieldChange}
            errorMessage={errors.date}
          />
          <button
            className="service-request-picker__button"
            type="button"
            aria-label="Abrir seletor de data"
            onClick={() => openNativePicker(dateInputRef)}
          >
            <Calendar aria-hidden="true" />
          </button>
        </div>

        <div className="service-request-picker">
          <FormInput
            label="Horario desejado *"
            name="time"
            type="hora"
            value={formData.time}
            inputRef={timeInputRef}
            onChange={handleFieldChange}
            errorMessage={errors.time}
          />
          <button
            className="service-request-picker__button"
            type="button"
            aria-label="Abrir seletor de horario"
            onClick={() => openNativePicker(timeInputRef)}
          >
            <Clock aria-hidden="true" />
          </button>
        </div>

        <label className="service-request-field">
          <span className="service-request-field__label">Descreva o servico *</span>
          <textarea
            className="service-request-field__textarea"
            name="description"
            value={formData.description}
            placeholder="Explique para o profissional o que precisa ser feito com maximo de detalhes."
            onChange={handleFieldChange}
            aria-invalid={Boolean(errors.description)}
            aria-describedby={errors.description ? "service-request-description-error" : undefined}
          />
          {errors.description && (
            <p className="form-input__error" id="service-request-description-error">
              {errors.description}
            </p>
          )}
        </label>

        <div className="service-request-field">
          <span className="service-request-field__label">Fotos (opcional)</span>
          {/* Input real fica oculto; o bloco visual abaixo dispara a selecao. */}
          <input
            className="service-request-photo__input"
            ref={photoInputRef}
            name="photos"
            type="file"
            accept="image/*"
            multiple
            onChange={handlePhotoChange}
          />
          <button
            className="service-request-photo"
            type="button"
            onClick={() => photoInputRef.current?.click()}
          >
            <Camera className="service-request-photo__icon" aria-hidden="true" />
            <span>
              {formData.photos.length > 0
                ? `${formData.photos.length} foto(s) selecionada(s)`
                : "Adicionar fotos do problema"}
            </span>
          </button>
        </div>

        <label className="service-request-field">
          <span className="service-request-field__label">Observacoes</span>
          <textarea
            className="service-request-field__textarea service-request-field__textarea--small"
            name="observations"
            value={formData.observations}
            placeholder="Informacoes adicionais..."
            onChange={handleFieldChange}
          />
        </label>

        <aside className="service-request-info">
          <span className="service-request-info__icon" aria-hidden="true">
            <Info />
          </span>
          <div>
            <h2 className="service-request-info__title">Como funciona o orcamento?</h2>
            <p className="service-request-info__text">
              O profissional ira analisar as informacoes enviadas e retornara com o
              orcamento de mao de obra e, se necessario, o orcamento de materiais.
            </p>
          </div>
        </aside>        

        <ActionButton
          text="Enviar solicitacao"
          type="submit"
          className="service-request-form__button"
          onClick={() => navigate ("/servicos/aprovado")}
        />
      </form>
    </section>
  );
}

export default SolicitarServico;
