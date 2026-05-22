import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // sockjs-client espera o objeto "global" (Node.js) que nao existe no navegador.
  // Mapeamos para "globalThis" para permitir o uso do WebSocket no frontend.
  define: {
    global: 'globalThis',
  },
})
