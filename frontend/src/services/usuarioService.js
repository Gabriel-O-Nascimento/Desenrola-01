import { api } from "./api";

const SESSION_KEY = "desenrola.usuarioLogado";
const REGISTERED_USERS_KEY = "desenrola.usuariosCadastrados";

function formatPhone(phone) {
  const digits = String(phone || "").replace(/\D/g, "");

  if (digits.length !== 11) {
    return phone || "Telefone nao informado";
  }

  return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7)}`;
}

function normalizeProfile(usuario) {
  if (!usuario) {
    return null;
  }

  const accountType = usuario.tipo === "PROFISSIONAL" ? "Profissional" : "Cliente";
  const location = [usuario.endereco, usuario.cidade, usuario.estado]
    .filter(Boolean)
    .join(" - ");

  return {
    id: usuario.id,
    name: usuario.nome,
    email: usuario.email,
    phone: formatPhone(usuario.telefone),
    address: location || "Endereco nao informado",
    accountType,
  };
}

function saveSession(usuario) {
  const profile = normalizeProfile(usuario);

  if (profile) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(profile));
  }

  return profile;
}

function getRegisteredUsers() {
  const rawUsers = localStorage.getItem(REGISTERED_USERS_KEY);

  if (!rawUsers) {
    return [];
  }

  try {
    return JSON.parse(rawUsers);
  } catch {
    localStorage.removeItem(REGISTERED_USERS_KEY);
    return [];
  }
}

function saveRegisteredUser(user) {
  const users = getRegisteredUsers();
  const nextUsers = [
    ...users.filter((currentUser) => currentUser.email !== user.email),
    user,
  ];

  localStorage.setItem(REGISTERED_USERS_KEY, JSON.stringify(nextUsers));
  return user;
}

export const usuarioService = {
  async login({ email, password }) {
    try {
      const usuario = await api.post("/api/usuarios/login", {
        email,
        senha: password,
      });

      return saveSession(usuario);
    } catch (error) {
      const localUser = getRegisteredUsers().find(
        (user) => user.email === email && user.password === password
      );

      if (localUser && error.message === "Erro 404") {
        return this.salvarUsuarioLogado(localUser);
      }

      throw error;
    }
  },

  async buscarPerfil(email) {
    const usuario = await api.get(`/api/usuarios/perfil?email=${encodeURIComponent(email)}`);
    return saveSession(usuario);
  },

  getUsuarioLogado() {
    const rawUser = localStorage.getItem(SESSION_KEY);

    if (!rawUser) {
      return null;
    }

    try {
      return JSON.parse(rawUser);
    } catch {
      localStorage.removeItem(SESSION_KEY);
      return null;
    }
  },

  salvarUsuarioLogado(usuario) {
    if (!usuario) {
      localStorage.removeItem(SESSION_KEY);
      return null;
    }

    localStorage.setItem(SESSION_KEY, JSON.stringify(usuario));
    return usuario;
  },

  registrarUsuarioLocal(formData, accountType = "Cliente") {
    return saveRegisteredUser({
      id: Date.now(),
      name: formData.fullName,
      email: formData.email,
      password: formData.password,
      phone: formData.phone,
      address: [formData.city, formData.state].filter(Boolean).join(" - ") || "Endereco nao informado",
      accountType,
    });
  },

  atualizarTipoContaLocal(email, accountType) {
    const user = getRegisteredUsers().find((currentUser) => currentUser.email === email);

    if (!user) {
      return null;
    }

    const updatedUser = saveRegisteredUser({
      ...user,
      accountType,
    });

    const loggedUser = this.getUsuarioLogado();

    if (loggedUser?.email === email) {
      this.salvarUsuarioLogado({
        ...loggedUser,
        accountType,
      });
    }

    return updatedUser;
  },

  limparSessao() {
    localStorage.removeItem(SESSION_KEY);
  },
};
