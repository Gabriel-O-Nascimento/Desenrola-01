// URL base do backend Spring Boot
const API_BASE_URL = "http://localhost:8080";

/**
 * Cliente HTTP simples baseado em fetch.
 * Centraliza headers, tratamento de erro e parse de JSON.
 */
async function request(path, { method = "GET", body, headers = {} } = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...headers,
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = data?.mensagem || data?.message || `Erro ${response.status}`;
    throw new Error(message);
  }

  return data;
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body }),
  put: (path, body) => request(path, { method: "PUT", body }),
  delete: (path) => request(path, { method: "DELETE" }),
};

export const API_BASE = API_BASE_URL;
