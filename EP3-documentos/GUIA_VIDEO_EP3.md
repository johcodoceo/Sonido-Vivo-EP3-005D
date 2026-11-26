# Guion de video demostrativo obligatorio — EP3 Sonido Vivo

**Sección 005D** · **Integrantes:** Johan Codoceo y Gabriel Rogel · **Duración orientativa:** 8 a 12 minutos (recomendación, no requisito oficial). Debe grabarse **funcionamiento real**, no solo diapositivas o capturas. La ausencia del video limita al 60% el máximo por cada indicador de la defensa técnica individual.

## Distribución sugerida (aprox. 10 minutos)

| Tiempo | Pantalla / acción verdadera | Narración sugerida |
|---|---|---|
| 0:00–0:45 | Portada breve + interfaz Sonido Vivo | «Somos Johan Codoceo y Gabriel Rogel, sección 005D. Presentamos la EP3 de Sonido Vivo, tienda musical de la Forma B. Conservamos el frontend React e integramos Spring Boot y PostgreSQL». |
| 0:45–2:00 | VS Code: carpetas `frontend`/`backend`, backend levantado, pgAdmin/SQL o consola | «React envía solicitudes HTTP a Spring Boot. El backend valida los datos y persiste productos, usuarios y pedidos mediante JPA en PostgreSQL». Mostrar 51 productos iniciales importados del Excel. |
| 2:00–3:20 | `http://localhost:8080/swagger-ui.html`: GET/POST/PUT/DELETE producto con token de admin | «Swagger documenta endpoints, parámetros y respuestas. Como administrador podemos realizar el CRUD; las operaciones requieren JWT y se validan en el backend». Mostrar códigos 200/201/204. |
| 3:20–5:00 | Navegador React, catálogo, edición stock o creación producto + pestaña Network | «Esta acción dispara una petición REST real. La API responde, el Context actualiza el estado React y la BD conserva el cambio tras recargar». |
| 5:00–6:40 | Login de cliente, luego vendedor y admin en sesiones separadas | «Spring Security verifica la contraseña BCrypt y el JWT. Cada rol tiene permisos distintos». Mostrar intento de cliente contra endpoint admin **403**, sin publicar token completo. |
| 6:40–8:00 | Carrito → checkout simulado → pedidos, vista vendedor cambia estado | «Confirmar pedido valida stock, calcula el precio y descuenta inventario dentro de una transacción. No se cobran pagos reales». |
| 8:00–9:15 | F5, token inválido en navegador de prueba, ruta restringida | «La sesión se rehidrata con /auth/me. Si el token expira o es inválido, vuelve a iniciar sesión. El frontend restringe vistas y el backend valida permisos». |
| 9:15–10:00 | README, docs, repos públicos (links definitivos) y cierre | «La entrega incluye dos repositorios o carpeta diferenciada, sus ZIPs, manual, arquitectura, pruebas y video. La defensa verificará estas implementaciones». |

## Instrucciones de grabación

1. Levanten y prueben PostgreSQL, backend y frontend **antes** de grabar.
2. Preparar usuarios **ficticios** y datos de ejemplo. Ocultar contraseñas, `.env`, clave de firma, tokens completos y datos reales.
3. Usar grabador de pantalla con voz de ambos integrantes, letra/zoom legibles, consola sin secretos.
4. Mostrar **una operación real** por indicador, preferentemente en una misma sesión continua.
5. Publicar el video con un enlace accesible al docente sin solicitar permisos, comprobarlo en ventana privada.
6. **Cada integrante** incorpora el enlace de video en su entrega individual AVA, junto con ZIPs, repos y documentos.

## Lista final para el video

- [ ] React, Spring Boot y PostgreSQL **funcionando**
- [ ] Swagger CRUD visible
- [ ] Petición frontend → API → BD → actualización de pantalla
- [ ] Login, JWT (oculto), 2 o más roles, endpoint denegado
- [ ] Sesión tras F5, sesión inválida y cierre
- [ ] Código y narración comprensibles
- [ ] Enlace verificado sin permisos adicionales

**Estado actual:** guion preparado, grabación y publicación pendientes porque requieren ejecutar la aplicación en el equipo y aportar capturas/video reales.
