// Única capa HTTP del frontend. Los datos persistidos (productos/usuarios/pedidos)
// siempre se consultan en Spring Boot; nunca se sustituyen por los mocks EP2.
const API_BASE = (import.meta.env.VITE_API_URL || 'http://localhost:8080/api').replace(/\/$/, '');
const TOKEN_KEY = 'sonido-vivo-ep3-jwt';
export const getToken = () => { try { return localStorage.getItem(TOKEN_KEY); } catch { return null; } };
export const setToken = (token) => { try { if (token) localStorage.setItem(TOKEN_KEY, token); else localStorage.removeItem(TOKEN_KEY); } catch { /* almacenamiento bloqueado */ } };
export class ApiError extends Error {
  constructor(message, status, errors = {}) { super(message); this.name = 'ApiError'; this.status = status; this.errors = errors; }
}
export async function api(path, options = {}) {
  const { skipAuth = false, ...rest } = options;
  const token = skipAuth ? null : getToken();
  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, {
      ...rest,
      headers: { ...(rest.body ? { 'Content-Type': 'application/json' } : {}), ...(token ? { Authorization: `Bearer ${token}` } : {}), ...rest.headers },
    });
  } catch {
    throw new ApiError('No se pudo conectar al servidor. Comprueba que Spring Boot y PostgreSQL estén iniciados.', 0);
  }
  let body = null;
  if (response.status !== 204) {
    try { body = await response.json(); } catch { /* error sin JSON */ }
  }
  if (!response.ok) {
    if (response.status === 401 && !skipAuth && token) {
      setToken(null);
      window.dispatchEvent(new Event('sv:session-expired'));
    }
    throw new ApiError(body?.error || `Error HTTP ${response.status}`, response.status, body?.errors || {});
  }
  return body;
}
