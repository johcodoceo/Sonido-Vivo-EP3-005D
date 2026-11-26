# Sonido Vivo — Evaluación Parcial 3 (EP3)

**Asignatura:** Desarrollo Fullstack II (DSY1104)  
**Caso:** Forma B — Tienda Sonido Vivo  
**Sección:** 005D  
**Integrantes:** Johan Codoceo y Gabriel Rogel  
**Repositorio público conjunto:** https://github.com/johcodoceo/Sonido-Vivo-EP3-005D

## Estructura del repositorio

- `sonido-vivo-backend/`: API REST con Java, Spring Boot, Spring Security, JWT, Spring Data JPA y PostgreSQL.
- `sonido-vivo-frontend/`: cliente React + Vite, autenticación, catálogo, carrito y paneles por rol.
- `ZIPs_AVA_EP3_005D/`: ZIP del backend y ZIP del frontend, preparados para la entrega.
- `EP3-documentos/`: material de apoyo, manual, informe de integración y defensa. **Para AVA utilizar las versiones finales de los documentos**.

## Requisitos para ejecutar

Docker Desktop con Docker Compose, Java 21 o versión compatible con el proyecto, Maven, Node.js y npm.

### 1. Iniciar PostgreSQL y backend

Desde `sonido-vivo-backend/`, copiar `.env.example` como `.env` y reemplazar sus valores de ejemplo por credenciales locales. El `.env` contiene secretos y **nunca debe publicarse**.

**Puerto del entorno Docker entregado:** el contenedor escucha internamente en 5432 y el host se conecta por **55432**. Por eso `DB_URL` debe apuntar a `jdbc:postgresql://127.0.0.1:55432/sonido_vivo` cuando se use `docker-compose.yml` del repositorio. Si se utiliza otro PostgreSQL local, adaptar la URL.

```powershell
cd sonido-vivo-backend
Copy-Item .env.example .env
# Editar .env antes de continuar
# DB_URL=jdbc:postgresql://127.0.0.1:55432/sonido_vivo

docker compose --env-file .env up -d db

Get-Content .env | Where-Object { $_ -match '^[A-Za-z_][A-Za-z0-9_]*=' } | ForEach-Object {
    $k,$v = $_ -split '=',2
    [Environment]::SetEnvironmentVariable($k,$v,'Process')
}

mvn spring-boot:run
```

Backend: http://localhost:8080  
Swagger: http://localhost:8080/swagger-ui.html

### 2. Iniciar el frontend

En otra terminal:

```powershell
cd sonido-vivo-frontend
Copy-Item .env.example .env
# Ajustar VITE_API_URL=http://localhost:8080/api si es necesario
npm install
npm run dev
```

Frontend: http://localhost:5173

### 3. Usuarios de demostración

La base de datos inicial incluye roles `cliente`, `vendedor` y `administrador`. Los correos de demostración están documentados en `sonido-vivo-backend/README.md`. La contraseña se define **localmente** mediante `DEMO_PASSWORD` en `.env`; no se publica en este repositorio.

## Funcionalidades implementadas

- API REST documentada en Swagger con CRUD de productos y administración de usuarios.
- Autenticación JWT y autorización en backend; rutas y acciones restringidas en frontend.
- Persistencia de sesión tras recargar, rechazo de token inválido y cierre de sesión.
- Gestión de inventario por Vendedor y flujo de pedidos de Cliente, con descuento de existencias y estados del pedido.
- Persistencia de productos, usuarios, stock y pedidos en PostgreSQL.

## Validación realizada por el equipo

Durante la verificación manual del proyecto, el equipo reportó resultados correctos en CRUD de productos (GET 200, POST 201, PUT 200 y DELETE 204), permisos de roles, conservación y cierre de sesión, stock (10→15), pedido de dos unidades (15→13), cambios de estado de pedidos y administración de usuarios.

El equipo también reportó ejecución satisfactoria de:

```powershell
# Backend
mvn clean test
mvn clean package

# Frontend
npm test
npm run qa:static
npm run build
```

Estos resultados corresponden a comprobaciones hechas en el equipo de desarrollo; para reproducirlos, ejecutar los comandos y verificar sus salidas localmente.

## Archivos de entrega y video

Según las instrucciones EP3, AVA requiere **un ZIP por componente** (frontend y backend), manual de usuario, documento de integración, repositorios accesibles y video demostrativo grupal. El repositorio conjunto es válido identificando claramente ambas carpetas.

> El manual preparado por el grupo no contiene capturas. La pauta docente las solicita expresamente; por tanto, el equipo debe consultar si el video puede sustituirlas o asumir ese incumplimiento. No declarar que el manual incorpora capturas si no las tiene.

> El enlace final del video debe añadirse en la entrega individual de AVA una vez publicado y comprobado en una ventana privada. No incluir ni exponer contraseñas, tokens o claves.

**Fechas sección 005:** entrega viernes 27/11/2026 durante la sesión y horario de cierre comunicado en AVA; defensa individual miércoles 02/12/2026.

Cada integrante debe completar **su propio envío en AVA**, incluyendo los mismos artefactos grupales y la identificación de ambos participantes.
