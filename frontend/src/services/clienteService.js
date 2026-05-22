import { api } from "./api";

/**
 * Servico responsavel por chamadas ao endpoint /api/clientes.
 */
export const clienteService = {
  /**
   * Cadastra um novo cliente no backend.
   * Mapeia os campos do formulario para o DTO esperado pela API.
   */
  async cadastrar(formData) {
    const payload = {
      nome: formData.fullName,
      email: formData.email,
      senha: formData.password,
      telefone: formData.phone?.replace(/\D/g, ""),
      cpf: formData.documentType === "cpf" ? formData.cpf?.replace(/\D/g, "") : null,
      cidade: formData.city,
      estado: formData.state,
    };

    return api.post("/api/clientes", payload);
  },
};
