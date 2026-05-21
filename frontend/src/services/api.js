const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    throw new Error(data?.message || "Nao foi possivel concluir a requisicao.");
  }

  return data;
}

export const api = {
  getHome: () => request("/home"),
  getSearch: () => request("/search"),
  login: (payload) => request("/auth/login", { method: "POST", body: JSON.stringify(payload) }),
  registerUser: (payload) => request("/users/register", { method: "POST", body: JSON.stringify(payload) }),
  registerProfessional: (payload) => request("/professionals/register", { method: "POST", body: JSON.stringify(payload) }),
  getCategories: () => request("/categories"),
  getProfessionals: () => request("/professionals"),
  getProfessionalById: (professionalId) => request(`/professionals/${professionalId}`),
  getProfile: (userId) => request(userId ? `/profile?userId=${userId}` : "/profile"),
  getChats: () => request("/chats"),
  getChatById: (chatId) => request(`/chats/${chatId}`),
  getChatByProfessionalId: (professionalId) => request(`/chats/by-professional/${professionalId}`),
  sendChatMessage: ({ chatId, professionalId, text }) =>
    request(
      professionalId ? `/chats/by-professional/${professionalId}/messages` : `/chats/${chatId}/messages`,
      { method: "POST", body: JSON.stringify({ text }) }
    ),
  getServicesHistory: () => request("/services/history"),
  getBudgetById: (id) => request(`/services/budgets/${id}`),
  decideBudget: (id, decision) => request(`/services/budgets/${id}/decision`, {
    method: "POST",
    body: JSON.stringify({ decision }),
  }),
  getTrackingById: (id) => request(`/services/tracking/${id}`),
  getCompletedServiceById: (id) => request(`/services/completed/${id}`),
  getCancelledServiceById: (id) => request(`/services/cancelled/${id}`),
  createServiceRequest: (payload) => request("/services/requests", { method: "POST", body: JSON.stringify(payload) }),
  submitReview: (payload) => request("/services/reviews", { method: "POST", body: JSON.stringify(payload) }),
};
