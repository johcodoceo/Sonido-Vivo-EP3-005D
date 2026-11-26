# Preparación de defensa técnica individual — EP3 Sonido Vivo

**Johan Codoceo / Gabriel Rogel · sección 005D** · **Miércoles 02/12/2026** (primera sesión de la semana 17).

La defensa es **individual (60% de la EP3)**. El docente puede hacer preguntas distintas a cada integrante según el código efectivamente entregado. El video es evidencia obligatoria y la ausencia limita cada indicador individual a nivel aceptable (60%). Los pesos son 12%, 12%, 10%, 10%, 8% y 8%.

## Seis áreas; código preciso que debemos abrir

**IE3.1.2 — Backend, persistencia, modelos y negocio (12%)**
- Abrir `backend/src/main/java/cl/duoc/sonidovivo/model/Product.java`, `PurchaseOrder.java` y `repo/ProductRepository.java`.
- Explicar JPA: `@Entity` mapea un objeto a tabla, `@Id` establece clave, repositorios simplifican persistencia.
- Abrir `api/OrderController.java` método `create`: `@Transactional`, bloqueo pesimista por ID, valida stock y descuenta, registra líneas e historial; si falla, rollback. Mostrar PostgreSQL.
- Pregunta posible: «¿Por qué no calcular el total en React?». Respuesta: porque cliente puede manipular el navegador; el servidor calcula totales desde la BD.

**IE3.2.3 — API REST, CRUD y Swagger (12%)**
- Abrir `api/ProductController.java`, `security/OpenApiConfig.java`; mostrar `http://localhost:8080/swagger-ui.html`.
- Verbos: GET leer, POST crear (201), PUT reemplazar datos (200), DELETE borrar (204), PATCH modificar stock/estado (200).
- Errores: 400 validación, 401 no autenticado/token inválido, 403 sin permisos, 404 recurso ausente, 409 conflicto de stock/reglas.
- Demostrar el código HTTP y la respuesta concreta en Swagger con autorización.

**IE3.2.4 — Integración frontend/backend (10%)**
- Abrir `frontend/src/services/apiClient.js`, `context/ProductContext.jsx` y `pages/ProductManagementPage.jsx`.
- Flujo real: click Crear → `ProductForm` valida → `ProductContext.addProduct` → `api('/products', POST)` → Spring Security → controlador → repositorio → PostgreSQL → JSON → `setProducts()` → React vuelve a mostrar producto.
- Mostrar Network y confirmar persistencia con F5 / otra sesión.
- Diferenciar `useState`, `useEffect` y `useCallback`.

**IE3.3.4 — Autenticación, JWT y roles (10%)**
- Abrir `api/AuthController.java` (login + register), `security/JwtService.java` (firma, emisión, vencimiento), `security/JwtFilter.java` (validación), `security/SecurityConfig.java` (SecurityFilterChain) y `api/UserController.java` (`@PreAuthorize`).
- Contraseñas se verifican con BCrypt; el token enviado en `Authorization: Bearer` contiene identificador de usuario y expiración. `JwtFilter` verifica firma/vigencia, carga usuario activo desde BD y sus roles.
- Cliente no puede crear productos; vendedor puede actualizar stock; admin gestiona usuarios. No mostrar el token completo en el video.

**IE3.3.5 — Sesión y persistencia (8%)**
- Abrir `frontend/src/context/AuthContext.jsx` y `services/apiClient.js`.
- Token guardado en `localStorage` en esta implementación académica, por ello continúa tras F5. `AuthContext` ejecuta `/auth/me` y restaura el perfil; HTTP 401 elimina token y obliga a login.
- Limitación justificada: `localStorage` es expuesto frente a XSS. Para producción preferiríamos sesiones mediante cookies HttpOnly/Secure + estrategia CSRF/refresh y endurecimiento de frontend.

**IE3.3.6 — Restricciones de interfaces y acciones (8%)**
- Abrir `routes/AppRoutes.jsx`, `components/ProtectedRoute.jsx`, `components/SiteHeader.jsx` y controladores Spring.
- Se ocultan rutas/botones según rol por experiencia de usuario, pero **esto no reemplaza seguridad real**; el backend tiene `@PreAuthorize` sobre los endpoints.
- Demostrar entrar con cliente → ruta `/administracion/usuarios` bloqueada; llamar API directamente → HTTP 403.

## Cinco preguntas difíciles que podrían hacer

1. «¿Qué ocurre si dos clientes compran el último artículo a la vez?». Se obtiene bloqueo pesimista por producto dentro de una transacción; solo una compra puede descontar el último stock. **Debe comprobarse en ejecución**.
2. «¿Por qué JWT no basta para verificar el rol?» La identidad/rol puede cambiar. El filtro vuelve a consultar a la BD en cada petición para cargar rol y estado activo actualizado.
3. «¿Qué diferencia hay entre 401 y 403?» 401: falta sesión autenticada válida. 403: usuario autenticado, pero rol insuficiente.
4. «¿Un vendedor puede cambiar una ruta en React para ser administrador?» Puede manipular el cliente, pero Spring bloquea la API; el backend es la autoridad.
5. «¿Cuál es el riesgo de `ddl-auto:update`?» Es útil para prototipo; en producción usar migraciones versionadas como Flyway/Liquibase y backups, no cambios de esquema automáticos.

## Ensayo conjunto y revisión individual

Cada integrante debe poder **levantar el proyecto**, mostrar un CRUD y los 3 roles, identificar exactamente qué archivos intervienen, explicar 401/403, demostrar refresh JWT y justificar riesgos. No basta memorizar estas respuestas: el profesor puede pedir modificar/navegar código y la demostración debe corresponder a lo efectivamente probado.
