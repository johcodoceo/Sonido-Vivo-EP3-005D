# Sonido Vivo · Frontend React · EP3

Aplicación React/Vite de la **Forma B — Sonido Vivo**, evolución del proyecto EP2. Conserva catálogo, búsqueda, carrito, pedidos, panel vendedor y panel administrador. **Ahora usa un backend Spring Boot y PostgreSQL** para productos, usuarios, pedidos, JWT y permisos. El carrito mantiene únicamente IDs y cantidades en localStorage por cuenta, pero el precio y la disponibilidad se validan nuevamente en backend al confirmar.

## Requisitos

- Node.js 20+ y npm.
- Backend `sonido-vivo-backend` funcionando en puerto 8080, con PostgreSQL iniciado.
- Copiar `.env.example` a `.env`. La variable Vite `VITE_API_URL` debe apuntar a `http://localhost:8080/api`.

```bash
npm install
npm run dev
```

Abrir `http://localhost:5173`. Para compilar: `npm run build`. Las cuentas demo y contraseñas se configuran en el backend; **no existe acceso sin contraseña**.

## Integración

`src/services/apiClient.js` implementa `fetch`, cabecera `Authorization: Bearer JWT`, manejo centralizado de errores HTTP 401/403 y expiración de sesión. `AuthContext` rehidrata usuario mediante `/api/auth/me` al recargar; `ProductContext`, `UserContext` y `OrderContext` consumen API REST real. La ruta `ProtectedRoute` restringe vistas, pero la validación decisiva está en Spring Security.

**Importante:** las pruebas EP2 que se escribieron para repositorios basados en localStorage deben ser adaptadas a la integración EP3. Antes de entregar se debe ejecutar `npm run build`, revisar manualmente el flujo con backend real y completar las evidencias del documento `PRUEBAS_EP3.md`.
