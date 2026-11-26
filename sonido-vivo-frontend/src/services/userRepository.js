import { ROLES, isValidRole } from '../utils/roles.js';

// Validación del cliente; el backend vuelve a validar todos los campos.
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
export function validateUser(values, users = [], excludeId = null, { publicRegistration = false } = {}) {
  const name = String(values?.name ?? '').trim().replace(/\s+/g, ' ');
  const email = String(values?.email ?? '').trim().toLowerCase();
  const phone = String(values?.phone ?? '').trim();
  const role = publicRegistration ? ROLES.CLIENTE : values?.role;
  const errors = {};
  if (name.length < 3 || name.length > 80) errors.name = 'El nombre debe tener entre 3 y 80 caracteres.';
  if (email.length > 130 || !emailPattern.test(email)) errors.email = 'Ingresa un correo electrónico válido.';
  else if (users.some((person) => person.id !== excludeId && person.email.toLowerCase() === email)) errors.email = 'Ese correo ya está registrado.';
  if (phone && !/^\+?[0-9\s-]{8,20}$/.test(phone)) errors.phone = 'Usa entre 8 y 20 caracteres: números, espacios, + o guiones.';
  if (!isValidRole(role)) errors.role = 'Selecciona un rol válido.';
  return { errors, data: { name, email, phone, role } };
}

