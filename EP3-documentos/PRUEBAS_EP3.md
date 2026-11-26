# EP3 · Matriz de trazabilidad y pruebas de aceptación — Sonido Vivo (Forma B)

**Sección:** 005D · **Integrantes:** Johan Codoceo y Gabriel Rogel.  
**Estado de evidencia:** Implementación fuente preparada; las pruebas de ejecución end-to-end NO han sido realizadas ni certificadas en este entorno. Completar columna «Resultado real» en el PC del equipo antes de subir a AVA.

## Correspondencia con la rúbrica del profesor (encargo grupal 40%)

| Indicador | Peso en EP3 | Evidencia implementada | Prueba requerida | Resultado real |
|---|---:|---|---|---|
| IE3.1.1: BD, entidades y negocio | 8% | Backend: `model/{Product,AppUser,PurchaseOrder,OrderLine,StatusEvent}`, `repo/`, `OrderController#create` (`@Transactional`) | Crear pedido y verificar 2 tablas y stock en PostgreSQL | **PENDIENTE** |
| IE3.2.1: CRUD, Swagger | 8% | `ProductController`, `UserController`, OpenApiConfig | GET/POST/PUT/DELETE producto con token admin y Swagger | **PENDIENTE** |
| IE3.2.2: integración REST | 6% | React `services/apiClient.js`, Contexts y pantallas | Registrar/editar y refrescar navegador; verificar cambios en otra sesión | **PENDIENTE** |
| IE3.3.1: JWT y roles backend | 6% | `AuthController`, `JwtService`, `JwtFilter`, `SecurityConfig` y `@PreAuthorize` | LOGIN /me, endpoint admin con JWT cliente da 403 | **PENDIENTE** |
| IE3.3.2: sesiones frontend | 6% | `AuthContext`, clave `sonido-vivo-ep3-jwt`, `/auth/me` | F5 conserva sesión válida; JWT borrado o inválido lleva a login | **PENDIENTE** |
| IE3.3.3: restricciones UI | 6% | `ProtectedRoute`, `AppRoutes`, `SiteHeader` | Cliente sin acceso a panel admin; URL directa restringida | **PENDIENTE** |

> Nota: son porcentajes de la EP3, no de la calificación final de la asignatura. Defensa individual tiene seis indicadores adicionales (12%, 12%, 10%, 10%, 8%, 8%).

## Matriz de pruebas manuales (marcar OK/FALLA y adjuntar captura real)

| ID | Actor | Acción | Respuesta esperada | Real / captura |
|---|---|---|---|---|
| T01 | Todos | Iniciar PostgreSQL, backend y frontend | Front 5173, API 8080, Swagger 8080/swagger-ui.html disponibles | PENDIENTE |
| T02 | Cliente | Inicio correcto con contraseña demo | HTTP 200 + JWT; perfil cliente visible | PENDIENTE |
| T03 | Cliente | Contraseña errónea | HTTP 401, sin acceso | PENDIENTE |
| T04 | Cliente | Consultar `/api/products` con JWT | HTTP 200, 51 productos iniciales | PENDIENTE |
| T05 | Cliente | POST `/api/products` con JWT cliente | HTTP 403, BD sin cambios | PENDIENTE |
| T06 | Vendedor | PATCH stock de GA001 | HTTP 200, nuevo stock persiste tras F5 | PENDIENTE |
| T07 | Vendedor | GET `/api/users` | HTTP 403 | PENDIENTE |
| T08 | Admin | CRUD producto nuevo | 201/200/204, tabla y catálogo reflejan operaciones | PENDIENTE |
| T09 | Admin | Crear usuario con rol vendedor | 201, hash BCrypt BD, password jamás en respuesta | PENDIENTE |
| T10 | Cliente | Registro público con role=administrador enviado | El backend crea siempre rol cliente | PENDIENTE |
| T11 | Cliente | Agregar al carrito y confirmar pedido | HTTP 201, pedido en BD y stock decrece en una transacción | PENDIENTE |
| T12 | Cliente | Comprar más cantidad que el stock | HTTP 409; pedido y stock SIN cambios | PENDIENTE |
| T13 | Vendedor | Avanzar estado preparación → listo-retiro/despachado | Nuevo estado visible y persistido | PENDIENTE |
| T14 | Vendedor | Saltar estado directamente a entregado | HTTP 409 | PENDIENTE |
| T15 | Cliente | Recargar la página | Usuario recuperado desde JWT, sin nuevo login | PENDIENTE |
| T16 | Cliente | Token corrupto/vencido; F5 | HTTP 401; se elimina token local y se muestra login | PENDIENTE |
| T17 | Cliente | URL manual /administracion/usuarios | Navegación a sin acceso; backend también da 403 | PENDIENTE |
| T18 | Admin | Desactivar último admin | HTTP 409, se conserva un administrador activo | PENDIENTE |
| T19 | Equipo | Probar redimensionamiento a 360px / 768px / 1280px | Menú, formularios y catálogo navegables | PENDIENTE |
| T20 | Equipo | Clonar desde GitHub y seguir README | Corre sin secretos en repositorio, configuración `.env` de ejemplo | PENDIENTE |

## Comandos demostrativos reproducibles

**Levantar:** seguir README de `sonido-vivo-backend` y `sonido-vivo-frontend`.

```bash
# Con backend ejecutándose; petición sin token, se espera HTTP 401
curl -i http://localhost:8080/api/products

# Swagger consultable sin permisos
curl -i http://localhost:8080/v3/api-docs

# Probar con una contraseña local propia (NO pegar tokens reales en evidencias)
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"cliente@sonidovivo.example","password":"TU_CLAVE_DEMO_LOCAL"}'
```

**Pruebas automatizadas propuestas:** ejecutar `mvn test` en backend y `npm test` + `npm run build` en frontend. No registrar como aprobadas sin la salida real. No subir `.env`, tokens, contraseñas ni claves. El código heredado de las pruebas EP2 se guarda como referencia en `docs/EP2-tests-historicos/` y no cuenta como prueba EP3 aprobada.
