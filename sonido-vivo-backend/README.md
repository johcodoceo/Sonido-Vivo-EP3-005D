# Sonido Vivo · API Spring Boot · EP3

Backend real de la tienda Sonido Vivo, Forma B. Persistencia PostgreSQL mediante Spring Data JPA, autenticación BCrypt + JWT, roles cliente/vendedor/administrador, API REST y Swagger.

## Requisitos

- JDK 21 y Apache Maven 3.9+.
- PostgreSQL 16, o Docker Desktop con Compose.
- `JWT_SECRET` aleatorio de **32 bytes o más**; `DB_PASSWORD` y `DEMO_PASSWORD` propios.

## Inicio local (terminal 1)

1. Copia `.env.example` como `.env` y configura variables. En PowerShell: `Copy-Item .env.example .env`.
2. Genera el secreto JWT, por ejemplo con Python: `python -c "import secrets; print(secrets.token_urlsafe(48))"`.
3. Inicia PostgreSQL: `docker compose --env-file .env up -d db`. Si no tienes Docker, crea base `sonido_vivo` y usuario en PostgreSQL local.
4. Configura las variables de `.env` en tu terminal. En PowerShell:
   ```powershell
   Get-Content .env | Where-Object { $_ -match '^[A-Za-z_][A-Za-z0-9_]*=' } | ForEach-Object { $k,$v = $_ -split '=',2; [Environment]::SetEnvironmentVariable($k,$v,'Process') }
   mvn spring-boot:run
   ```
5. API disponible en `http://localhost:8080/api`, Swagger `http://localhost:8080/swagger-ui.html`.

**Precaución:** `.env` y las contraseñas no se deben subir a GitHub; el `.env.example` solo contiene marcadores. Los 3 usuarios se crean al iniciar una BD vacía con la misma contraseña de demostración definida en `DEMO_PASSWORD`. Cambia esa contraseña cuando no sea un entorno de prueba.

| Perfil | Correo de demostración | Permisos |
|---|---|---|
| Cliente | cliente@sonidovivo.example | Consulta catálogo y registra pedidos |
| Vendedor | vendedor@sonidovivo.example | Inventario y gestión de estados |
| Administrador | admin@sonidovivo.example | Catálogo CRUD, usuarios y pedidos |

### REST

| Método y ruta | Rol |
|---|---|
| POST `/api/auth/login`, POST `/api/auth/register` | Público |
| GET `/api/auth/me`, GET `/api/products`, GET `/api/products/{id}` | Autenticado |
| POST `/api/products`, PUT `/api/products/{id}`, DELETE `/api/products/{id}` | Administrador |
| PATCH `/api/products/{id}/stock` | Vendedor, administrador |
| GET `/api/users`, POST `/api/users`, PUT `/api/users/{id}`, PATCH `/api/users/{id}/toggle` | Administrador |
| GET `/api/orders` | Clientes: solo propios; vendedor/admin: todos |
| POST `/api/orders` | Cliente, administrador |
| PATCH `/api/orders/{id}/status` | Vendedor, administrador |

Swagger expone los contratos. Selecciona **Authorize** e introduce el JWT obtenido en `/api/auth/login` para probar endpoints protegidos. Las operaciones sensibles necesitan autorización real en backend, no solo ocultar botones.

### Persistencia y consistencia

- Entidades JPA `Product`, `AppUser`, `PurchaseOrder`, `OrderLine` y `StatusEvent`. Productos iniciales: 51 fichas exactas del catálogo Forma B.
- Pedido `@Transactional`: obtiene los productos con bloqueo pesimista por ID, verifica stock, calcula totales en el servidor, descuenta existencias y guarda líneas e historial en una única transacción; ante error se revierte.
- No se procesa ningún pago real. La persistencia usa `ddl-auto:update` para la demostración académica, no es una estrategia de migraciones para producción.
- Passwords con BCrypt; JWT firmado y con vencimiento; se verifica el usuario activo contra BD en cada petición.

### Pruebas

`mvn test` para tests backend. Consulta `docs/PRUEBAS_EP3.md` en el paquete principal para pruebas manuales de 200/201/400/401/403/409 y sesión expirada.
