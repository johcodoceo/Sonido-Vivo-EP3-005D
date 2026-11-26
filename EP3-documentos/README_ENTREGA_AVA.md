# Sonido Vivo · EP3 · Forma B · Sección 005D

**Equipo:** Johan Codoceo y Gabriel Rogel.  
**Entrega de la sección 005:** viernes **27/11/2026** (sesión 2, semana 16).  
**Defensa:** miércoles **02/12/2026** (sesión 1, semana 17).  
El cierre concreto de AVA depende del horario informado por el profesor.

## Paquete preparado

- `sonido-vivo-frontend`: React/Vite, lógica de catálogo/carrito, navegación por rol, sesiones JWT y consumo de REST.
- `sonido-vivo-backend`: Java 21/Spring Boot 3.4.5, Spring Data JPA, PostgreSQL, Spring Security JWT, Swagger OpenAPI, roles/CRUD/pedidos.
- `EP3-documentos`: Manual de usuario (DOCX), integración (DOCX), guion video (DOCX + MD), defensa (DOCX + MD) y matriz de pruebas (MD).

**Sin inventar evidencia:** manual de usuario contiene marcadores para capturas reales. El video no está grabado y los links públicos no están definidos. Las pruebas de integración necesitan ejecutarse en un PC con las dependencias disponibles. No entregar AVA antes de completar esos puntos.

## Cómo levantar ambos proyectos

1. Instalar Docker Desktop con Compose (o PostgreSQL 16), JDK 21, Maven 3.9+, Node 20 y npm.
2. En `sonido-vivo-backend`, crear `.env` desde `.env.example` y reemplazar **cada** marcador por un valor de desarrollo propio. `JWT_SECRET` debe ser aleatorio de 32 bytes o más. Nunca subir `.env`.
3. En terminal backend (PowerShell):

   ```powershell
   Copy-Item .env.example .env
   # Editar .env; luego iniciar PostgreSQL
   docker compose --env-file .env up -d db
   # Cargar variables del archivo al proceso PowerShell
   Get-Content .env | Where-Object { $_ -match '^[A-Za-z_][A-Za-z0-9_]*=' } | ForEach-Object { $k,$v = $_ -split '=',2; [Environment]::SetEnvironmentVariable($k,$v,'Process') }
   mvn spring-boot:run
   ```

4. En otra terminal:

   ```powershell
   cd sonido-vivo-frontend
   Copy-Item .env.example .env
   npm install
   npm run dev
   ```

5. Abrir `http://localhost:5173`, y Swagger en `http://localhost:8080/swagger-ui.html`.
6. Ingresar con correos de demostración listados en README backend y la contraseña propia definida en `DEMO_PASSWORD`. Ningún perfil de administrador puede ingresarse sin contraseña.
7. Realizar y documentar las pruebas propuestas en `PRUEBAS_EP3.md`; ejecutar `mvn test`, `npm test`, `npm run build`, `npm run qa:static`.

## Evidencia AVA que DEBE completar el equipo

- [ ] **GitHub público del frontend**: pegar enlace verdadero (no inventar URL)
- [ ] **GitHub público del backend**: pegar enlace verdadero (no inventar URL)
- [ ] ZIP frontend actualizado con último commit y compilación probada
- [ ] ZIP backend actualizado con último commit y Maven probado
- [ ] Manual con **capturas reales**, sustituyendo todos los marcadores
- [ ] Documento integración con cambios de configuración final revisados
- [ ] Reporte pruebas con resultados y capturas reales
- [ ] **Video grupal real grabado, publicado y comprobado en ventana incógnita**
- [ ] Entrega **individual** de Johan con links, ZIPs, manual, integración y video
- [ ] Entrega **individual** de Gabriel con links, ZIPs, manual, integración y video
- [ ] Ensayo de defensa con ambos estudiantes, código a la vista

## Qué se pudo y no se pudo verificar aquí

**Verificado:** 51 fichas originales tomadas de `src/data/products.js`; análisis sintáctico del código Java y JSX; referencias relativas del frontend; presencia de endpoints, JWT y roles; verificación estática fuente. Docs DOCX generados y renderizados para verificar páginas.

**No verificado:** compilación Maven, tests Spring con PostgreSQL, `npm install`, Vite build o Vitest, uso del navegador con frontend/backend, publicación GitHub, pantallazos y video. En el entorno de creación no se disponía de Maven ni acceso a paquetes npm. No afirmar 100% de rúbrica sin pruebas reales.
