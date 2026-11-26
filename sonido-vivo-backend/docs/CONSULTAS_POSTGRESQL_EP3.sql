-- Evidencia académica: ejecutar después de levantar PostgreSQL y el backend.
-- Ocultar datos personales en capturas. No publicar contraseñas, hash ni secretos.
SELECT COUNT(*) AS productos_iniciales FROM products;
SELECT id, name, stock, price FROM products WHERE id = 'GA001';
SELECT id, user_id, status, fulfillment, total_products, created_at
FROM purchase_orders ORDER BY created_at DESC LIMIT 10;
-- Los nombres de columnas se crean por Hibernate (estrategia física snake_case).
-- El detalle e historial pueden consultarse por su código de pedido:
SELECT * FROM order_lines LIMIT 10;
SELECT * FROM order_history LIMIT 10;
