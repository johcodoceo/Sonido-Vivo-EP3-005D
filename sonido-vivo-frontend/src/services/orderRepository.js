// Funciones puras para la interfaz; pedidos persistentes en Spring Boot.
export const FULFILLMENT = Object.freeze({ DELIVERY: 'despacho', PICKUP: 'retiro' });
export const ORDER_STATUS = Object.freeze({ PREPARING: 'preparacion', SHIPPED: 'despachado', READY: 'listo-retiro', DELIVERED: 'entregado' });
export const STATUS_NAMES = Object.freeze({
  [ORDER_STATUS.PREPARING]: 'En preparación',
  [ORDER_STATUS.SHIPPED]: 'Despachado',
  [ORDER_STATUS.READY]: 'Listo para retiro',
  [ORDER_STATUS.DELIVERED]: 'Entregado',
});
export const FULFILLMENT_NAMES = Object.freeze({ despacho: 'Despacho a domicilio', retiro: 'Retiro en tienda' });

export function validateCheckout(form) {
  const errors = {};
  const name = String(form?.name ?? '').trim();
  const email = String(form?.email ?? '').trim();
  const fulfillment = form?.fulfillment;
  const city = String(form?.city ?? '').trim();
  const address = String(form?.address ?? '').trim();
  const notes = String(form?.notes ?? '').trim();
  if (name.length < 3 || name.length > 80) errors.name = 'Ingresa un nombre de 3 a 80 caracteres.';
  if (email.length > 130 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) errors.email = 'Ingresa un correo válido.';
  if (![FULFILLMENT.DELIVERY, FULFILLMENT.PICKUP].includes(fulfillment)) errors.fulfillment = 'Selecciona despacho o retiro.';
  if (fulfillment === FULFILLMENT.DELIVERY) {
    if (city.length < 2 || city.length > 80) errors.city = 'Ingresa una ciudad de 2 a 80 caracteres.';
    if (address.length < 8 || address.length > 150) errors.address = 'Ingresa una dirección de 8 a 150 caracteres.';
  }
  if (notes.length > 250) errors.notes = 'El comentario no puede superar los 250 caracteres.';
  return { errors, data: { name, email, fulfillment, city: fulfillment === 'despacho' ? city : '', address: fulfillment === 'despacho' ? address : '', notes } };
}

export function nextStatuses(order) {
  if (!order) return [];
  if (order.status === ORDER_STATUS.PREPARING) return [order.fulfillment === FULFILLMENT.DELIVERY ? ORDER_STATUS.SHIPPED : ORDER_STATUS.READY];
  if (order.status === ORDER_STATUS.SHIPPED || order.status === ORDER_STATUS.READY) return [ORDER_STATUS.DELIVERED];
  return [];
}

