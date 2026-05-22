import { api } from "./api";

/**
 * Servico responsavel por chamadas ao endpoint /api/solicitacoes.
 */
export const solicitacaoService = {
  /**
   * Cria uma nova solicitacao de servico no backend.
   * Esta chamada dispara o fluxo de mensageria (RabbitMQ + WebSocket).
   */
  async criar(formData, { idCliente, idServico }) {
    const dataPreferencial =
      formData.date && formData.time ? `${formData.date}T${formData.time}:00` : null;

    const payload = {
      idCliente,
      idServico,
      titulo: `Solicitacao de servico`,
      descricao: formData.description,
      enderecoAtendimento: formData.address,
      cidadeAtendimento: formData.city,
      estadoAtendimento: null,
      valorEstimado: null,
      dataPreferencial,
    };

    return api.post("/api/solicitacoes", payload);
  },

  async listarPorCliente(idCliente) {
    return api.get(`/api/solicitacoes?idCliente=${idCliente}`);
  },

  async listarPorProfissional(idProfissional) {
    return api.get(`/api/solicitacoes?idProfissional=${idProfissional}`);
  },

  async atualizarStatus(idSolicitacao, payload) {
    return api.put(`/api/solicitacoes/${idSolicitacao}/status`, payload);
  },
};
