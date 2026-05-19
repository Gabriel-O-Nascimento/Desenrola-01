import "../../styles/global.css";

const inputTypeMap = {
  texto: "text",
  numero: "number",
  hora: "time",
  data: "date",
  email: "email",
  senha: "password",
  imagem: "file",
  cpf: "text",
  cnpj: "text",
  telefone: "tel",
  cidade: "text",
  estado: "select",
};

const inputConfigMap = {
  cpf: {
    inputMode: "numeric",
    maxLength: 14,
    placeholder: "000.000.000-00",
  },
  cnpj: {
    inputMode: "numeric",
    maxLength: 18,
    placeholder: "00.000.000/0000-00",
  },
  telefone: {
    inputMode: "tel",
    maxLength: 15,
    placeholder: "(00) 00000-0000",
  },
  imagem: {
    accept: "image/*",
  },
  estado: {
    options: [
      "AC",
      "AL",
      "AP",
      "AM",
      "BA",
      "CE",
      "DF",
      "ES",
      "GO",
      "MA",
      "MT",
      "MS",
      "MG",
      "PA",
      "PB",
      "PR",
      "PE",
      "PI",
      "RJ",
      "RN",
      "RS",
      "RO",
      "RR",
      "SC",
      "SP",
      "SE",
      "TO",
    ],
  },
};

export default function FormInput({
  label,
  name,
  type = "texto",
  value,
  onChange,
  onBlur,
  placeholder,
  required = false,
  disabled = false,
  className = "",
  as = "input",
  multiline = false,
  rows,
  min,
  inputRef,
  errorMessage,
}) {
  const htmlType = inputTypeMap[type] || "text";
  const typeConfig = inputConfigMap[type] || {};
  const inputId = `form-input-${name}`;
  const errorId = `${inputId}-error`;
  const fieldClassName = ["form-input", className].filter(Boolean).join(" ");
  const isTextarea = as === "textarea" || multiline;
  const isSelect = htmlType === "select";

  return (
    <label className={fieldClassName} htmlFor={inputId}>
      {label && <span className="form-input__label">{label}</span>}

      {isTextarea ? (
        /* Textarea opcional para campos longos, mantendo a mesma API base. */
        <textarea
          className="form-input__control form-input__control--textarea"
          id={inputId}
          name={name}
          value={value}
          onChange={onChange}
          onBlur={onBlur}
          placeholder={placeholder || typeConfig.placeholder}
          required={required}
          disabled={disabled}
          rows={rows}
          aria-invalid={Boolean(errorMessage)}
          aria-describedby={errorMessage ? errorId : undefined}
        />
      ) : isSelect ? (
        /* Select reutiliza o visual do input para variacoes como estado. */
        <select
          className="form-input__control"
          id={inputId}
          name={name}
          value={value}
          onChange={onChange}
          onBlur={onBlur}
          required={required}
          disabled={disabled}
          ref={inputRef}
          aria-invalid={Boolean(errorMessage)}
          aria-describedby={errorMessage ? errorId : undefined}
        >
          <option value="" disabled>
            {placeholder || "Selecione uma opção"}
          </option>
          {typeConfig.options?.map((option) => (
            <option key={option} value={option}>
              {option}
            </option>
          ))}
        </select>
      ) : (
        /* Input controlado por props, com tipos personalizados mapeados para HTML. */
        <input
          className="form-input__control"
          id={inputId}
          name={name}
          type={htmlType}
          value={htmlType === "file" ? undefined : value}
          onChange={onChange}
          onBlur={onBlur}
          placeholder={placeholder || typeConfig.placeholder}
          required={required}
          disabled={disabled}
          min={min}
          ref={inputRef}
          inputMode={typeConfig.inputMode}
          maxLength={typeConfig.maxLength}
          accept={typeConfig.accept}
          aria-invalid={Boolean(errorMessage)}
          aria-describedby={errorMessage ? errorId : undefined}
        />
      )}

      {errorMessage && (
        <p className="form-input__error" id={errorId}>
          {errorMessage}
        </p>
      )}
    </label>
  );
}
