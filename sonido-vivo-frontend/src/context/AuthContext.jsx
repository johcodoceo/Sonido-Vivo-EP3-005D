import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { api, getToken, setToken } from '../services/apiClient.js';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [sessionMessage, setSessionMessage] = useState('');
  const logout = useCallback(() => { setToken(null); setUser(null); }, []);
  useEffect(() => {
    let active = true;
    const expired = () => { logout(); setSessionMessage('La sesión venció o dejó de ser válida. Inicia sesión nuevamente.'); setLoading(false); };
    window.addEventListener('sv:session-expired', expired);
    if (!getToken()) { setLoading(false); }
    else api('/auth/me').then((profile) => { if (active) setUser(profile); })
      .catch(() => { if (active) { logout();setSessionMessage('Inicia sesión para continuar.'); } })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; window.removeEventListener('sv:session-expired', expired); };
  }, [logout]);
  const login = useCallback(async (email, password) => {
    const response = await api('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }), skipAuth: true });
    setToken(response.token); setUser(response.user); setSessionMessage('');
    return response.user;
  }, []);
  const value = useMemo(() => ({ user, loading, sessionMessage, login, logout }), [user, loading, sessionMessage, login, logout]);
  return <AuthContext.Provider value={value}>{loading ? <main className="container py-5" role="status">Validando sesión con el servidor…</main> : children}</AuthContext.Provider>;
}
export function useAuth() {const context=useContext(AuthContext); if (!context) throw new Error('useAuth requiere AuthProvider'); return context;}
