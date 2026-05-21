const SESSION_KEY = "desenrola-session";

export function getSession() {
  const rawValue = window.localStorage.getItem(SESSION_KEY);
  return rawValue ? JSON.parse(rawValue) : null;
}

export function setSession(session) {
  window.localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function clearSession() {
  window.localStorage.removeItem(SESSION_KEY);
}

export function getCurrentUser() {
  return getSession()?.user || null;
}

export function getCurrentUserId() {
  return getCurrentUser()?.id || null;
}
