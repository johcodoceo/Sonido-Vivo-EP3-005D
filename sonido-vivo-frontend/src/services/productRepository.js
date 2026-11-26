import { categories } from '../data/categories.js';

// Utilidades puras del catálogo. Persistencia REST en ProductContext y Spring Boot.
export const formatCLP = (amount) => new Intl.NumberFormat('es-CL', {
  style: 'currency', currency: 'CLP', maximumFractionDigits: 0,
}).format(amount);

export function isProduct(item) {
  return Boolean(item && typeof item.id === 'string' && item.id.length > 0 &&
    typeof item.name === 'string' && item.name.trim().length > 0 &&
    categories.some(({ slug }) => slug === item.categorySlug) &&
    Number.isSafeInteger(item.price) && item.price >= 0 &&
    Number.isSafeInteger(item.stock) && item.stock >= 0 &&
    typeof item.description === 'string' && typeof item.brand === 'string' &&
    typeof item.model === 'string');
}

export function validateProduct(input, products = [], editingId = null) {
  const errors = {};
  const name = String(input.name ?? '').trim();
  const brand = String(input.brand ?? '').trim();
  const model = String(input.model ?? '').trim();
  const description = String(input.description ?? '').trim();
  const categorySlug = String(input.categorySlug ?? '').trim();
  const priceText = String(input.price ?? '').trim();
  const stockText = String(input.stock ?? '').trim();
  const price = Number(priceText);
  const stock = Number(stockText);
  if (name.length < 3 || name.length > 110) errors.name = 'El nombre debe tener entre 3 y 110 caracteres.';
  if (brand.length < 2 || brand.length > 80) errors.brand = 'La marca debe tener entre 2 y 80 caracteres.';
  if (model.length < 1 || model.length > 80) errors.model = 'El modelo debe tener entre 1 y 80 caracteres.';
  if (!categories.some((c) => c.slug === categorySlug)) errors.categorySlug = 'Selecciona una categoría válida.';
  if (description.length < 10 || description.length > 450) errors.description = 'La descripción debe tener entre 10 y 450 caracteres.';
  if (priceText === '' || !Number.isSafeInteger(price) || price <= 0) errors.price = 'Ingresa un precio entero mayor que cero.';
  if (stockText === '' || !Number.isSafeInteger(stock) || stock < 0) errors.stock = 'El stock debe ser un número entero desde cero.';
  if (products.some((p) => p.id !== editingId && p.name.toLowerCase() === name.toLowerCase())) errors.name = 'Ya existe un producto con ese nombre.';
  return { errors, data: { name, brand, model, description, categorySlug, price, stock } };
}

export function filterProducts(products, { query = '', categorySlug = '', availableOnly = false, sort = 'name' } = {}) {
  const normalized = String(query).trim().toLocaleLowerCase('es');
  const result = products.filter((item) =>
    (!normalized || [item.name, item.brand, item.model, item.id, item.description].some((field) => field.toLocaleLowerCase('es').includes(normalized))) &&
    (!categorySlug || item.categorySlug === categorySlug) &&
    (!availableOnly || item.stock > 0));
  return result.sort((a, b) => {
    if (sort === 'price-asc') return a.price - b.price || a.name.localeCompare(b.name, 'es');
    if (sort === 'price-desc') return b.price - a.price || a.name.localeCompare(b.name, 'es');
    if (sort === 'stock-desc') return b.stock - a.stock || a.name.localeCompare(b.name, 'es');
    return a.name.localeCompare(b.name, 'es');
  });
}
