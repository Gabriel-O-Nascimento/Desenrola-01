import { api } from "./api";

/**
 * Servico responsavel por chamadas ao endpoint /api/profissionais.
 */
export const profissionalService = {
  /**
   * Cadastra um novo profissional no backend.
   * Mapeia os campos do formulario para o DTO esperado pela API.
   */
  async cadastrar(formData) {
    const payload = {
      nome: formData.fullName,
      email: formData.email,
      senha: formData.password,
      telefone: formData.phone?.replace(/\D/g, ""),
      documento:
        formData.documentType === "cpf"
          ? formData.cpf?.replace(/\D/g, "")
          : formData.cnpj?.replace(/\D/g, ""),
      tipoDocumento: formData.documentType === "cpf" ? "CPF" : "CNPJ",
      idCategoria: formData.mainCategory ? Number(formData.mainCategory) : null,
      especialidade: formData.serviceDescription,
      descricaoPerfil: formData.professionalExperience,
      cidade: formData.city,
      estado: formData.state,
    };

    return api.post("/api/profissionais", payload);
  },

  async listar(idCategoria) {
    const query = idCategoria ? `?idCategoria=${idCategoria}` : "";
    return api.get(`/api/profissionais${query}`);
  },
};
