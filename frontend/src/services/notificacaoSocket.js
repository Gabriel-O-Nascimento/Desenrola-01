import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { API_BASE } from "./api";

/**
 * Cliente WebSocket (STOMP sobre SockJS) para receber notificacoes em tempo real
 * publicadas pelo backend no canal /topic/notificacoes/{idUsuario}.
 */
let stompClient = null;

export function conectarNotificacoes(idUsuario, onNotificacao) {
  if (!idUsuario) {
    return () => {};
  }

  stompClient = new Client({
    webSocketFactory: () => new SockJS(`${API_BASE}/ws`),
    reconnectDelay: 5000,
    debug: () => {},
    onConnect: () => {
      stompClient.subscribe(`/topic/notificacoes/${idUsuario}`, (message) => {
        try {
          const notificacao = JSON.parse(message.body);
          onNotificacao(notificacao);
        } catch (error) {
          // Mensagem invalida — ignorada silenciosamente.
        }
      });
    },
  });

  stompClient.activate();

  // Funcao de cleanup para desconectar ao desmontar componentes.
  return () => {
    if (stompClient) {
      stompClient.deactivate();
      stompClient = null;
    }
  };
}
