# Desenrola - Versão Web

Versão web responsiva da plataforma Desenrola, criada a partir do design mobile existente.

## 📱 Sobre o Projeto

O Desenrola é uma plataforma que conecta clientes a profissionais qualificados para serviços residenciais e comerciais. Esta versão web mantém a identidade visual e experiência do usuário do aplicativo mobile.

## 🎨 Identidade Visual

### Cores Principais
- **Amarelo Primário**: `#F4C430` - Cor principal da marca
- **Preto**: `#000000` - Textos e elementos principais
- **Cinzas**: Escala de cinzas para textos secundários e backgrounds
- **Cores de Status**:
  - Sucesso: `#10B981` (Verde)
  - Aviso: `#F59E0B` (Laranja)
  - Erro: `#EF4444` (Vermelho)

### Tipografia
- **Fonte**: Inter Sans Serif
- **Pesos**: 300, 400, 500, 600, 700

### Componentes
- Cards com bordas arredondadas
- Badges de status (Verificado, Aguardando, Aprovado, etc.)
- Botões com estados hover e transições suaves
- Ícones minimalistas

## 📄 Páginas

### 1. `index.html` - Página Principal
Contém:
- **Header**: Logo, navegação e botões de ação
- **Hero Section**: Apresentação da plataforma com CTA
- **Categorias de Serviços**: Grid com principais categorias
- **Profissionais em Destaque**: Cards de profissionais verificados
- **Como Funciona**: Passo a passo do processo
- **CTA Section**: Chamada para ação final
- **Footer**: Links e informações da empresa

### 2. `cadastro.html` - Cadastro e Login
Contém:
- **Tabs**: Alternância entre cadastro de usuário e profissional
- **Formulário de Usuário**: Login com email/senha e opções sociais
- **Formulário de Profissional**: Cadastro completo com:
  - Dados pessoais
  - Localização
  - Informações do serviço
  - Documentação

## 🚀 Como Usar

### Visualização Local
1. Abra o arquivo `index.html` diretamente no navegador
2. Ou use um servidor local:
   ```bash
   # Com Python 3
   python -m http.server 8000
   
   # Com Node.js (http-server)
   npx http-server
   ```
3. Acesse `http://localhost:8000`

### Estrutura de Arquivos
```
versao-web/
├── index.html              # Página principal
├── cadastro.html           # Página de cadastro/login
├── styles.css              # Estilos principais
├── cadastro-styles.css     # Estilos específicos do cadastro
└── README.md              # Esta documentação
```

## 📱 Responsividade

A versão web é totalmente responsiva com breakpoints:
- **Desktop**: > 1024px
- **Tablet**: 768px - 1024px
- **Mobile**: < 768px

### Adaptações Mobile
- Menu de navegação oculto em telas pequenas
- Grid de serviços e profissionais adaptativo
- Botões e formulários otimizados para toque
- Espaçamentos ajustados

## 🎯 Funcionalidades Implementadas

### Página Principal
- ✅ Header fixo com navegação
- ✅ Hero section com apresentação
- ✅ Grid de categorias de serviços
- ✅ Cards de profissionais com estatísticas
- ✅ Seção "Como Funciona"
- ✅ Call-to-action
- ✅ Footer completo

### Página de Cadastro
- ✅ Sistema de tabs (Usuário/Profissional)
- ✅ Formulário de login
- ✅ Opções de login social (Google/Apple)
- ✅ Formulário completo de cadastro profissional
- ✅ Validação de campos obrigatórios
- ✅ Máscaras de input (telefone, CPF, CEP)

## 🔄 Próximos Passos

### Funcionalidades a Implementar
- [ ] Integração com backend (API REST)
- [ ] Sistema de autenticação real
- [ ] Busca e filtros de profissionais
- [ ] Sistema de mensagens/chat
- [ ] Painel do profissional
- [ ] Painel do cliente
- [ ] Sistema de avaliações
- [ ] Pagamentos integrados
- [ ] Notificações em tempo real
- [ ] Upload de fotos de trabalhos realizados
- [ ] Geolocalização para busca por proximidade

### Melhorias de UX/UI
- [ ] Animações e micro-interações
- [ ] Loading states
- [ ] Feedback visual de ações
- [ ] Modo escuro (dark mode)
- [ ] Acessibilidade (WCAG)
- [ ] Otimização de performance

## 🛠️ Tecnologias Utilizadas

- **HTML5**: Estrutura semântica
- **CSS3**: Estilização com variáveis CSS e Grid/Flexbox
- **JavaScript**: Interatividade básica (tabs, validações)
- **Google Fonts**: Fonte Inter

## 📊 Compatibilidade

- ✅ Chrome/Edge (últimas versões)
- ✅ Firefox (últimas versões)
- ✅ Safari (últimas versões)
- ✅ Mobile browsers (iOS Safari, Chrome Mobile)

## 📝 Notas de Desenvolvimento

### Design System
O projeto segue um design system consistente com:
- Variáveis CSS para cores, espaçamentos e tipografia
- Componentes reutilizáveis
- Nomenclatura BEM-like para classes CSS
- Mobile-first approach

### Boas Práticas
- Código semântico e acessível
- Performance otimizada (fontes, imagens)
- SEO-friendly
- Responsivo e adaptativo

## 📞 Contato

Para dúvidas ou sugestões sobre o projeto Desenrola, entre em contato através dos canais oficiais da plataforma.

---

**Versão**: 1.0.0  
**Data**: Abril 2026  
**Status**: Em Desenvolvimento
