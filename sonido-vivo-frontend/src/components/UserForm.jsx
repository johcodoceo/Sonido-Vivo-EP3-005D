import { useState } from 'react';
import { ROLES, ROLE_LABELS } from '../utils/roles.js';
import { validateUser } from '../services/userRepository.js';
export const EMPTY_USER = { name:'',email:'',phone:'',role:ROLES.CLIENTE,password:'' };
export default function UserForm({initial=EMPTY_USER,existing=[],editingId=null,publicRegistration=false,onSave,onCancel,submitLabel='Guardar usuario'}){
 const [form,setForm]=useState(()=>({...EMPTY_USER,...initial,password:''}));const [errors,setErrors]=useState({});
 const [generalError,setGeneralError]=useState('');const [saving,setSaving]=useState(false);
 function update(field,value){setForm((current)=>({...current,[field]:value}));setErrors((old)=>({...old,[field]:undefined}));setGeneralError('');}
 async function submit(e){e.preventDefault();if(saving)return;
   const checked=validateUser(form,existing,editingId,{publicRegistration});
   const pass=form.password||'';const errors={...checked.errors};
   if(!editingId&&pass.length<8)errors.password='La contraseña debe tener 8 caracteres o más.';
   if(editingId&&pass&&pass.length<8)errors.password='Usa al menos 8 caracteres o deja en blanco.';
   setErrors(errors);if(Object.keys(errors).length){setGeneralError('Corrige los campos señalados.');return;}
   setSaving(true);
   try{const result=await onSave({...checked.data,password:pass});
    if(result?.error){setGeneralError(result.error);setErrors(result.errors??{});}}
   catch(error){setGeneralError(error.message);}
   finally{setSaving(false);}
 }
 return <form onSubmit={submit} noValidate aria-label={publicRegistration?'Registro de cliente':'Formulario de usuario'}><div className="row g-3">
  <div className="col-md-6"><label htmlFor="user-name" className="form-label fw-semibold">Nombre completo *</label><input id="user-name" className="form-control" value={form.name} onChange={(e)=>update('name',e.target.value)} maxLength={80} required/>{errors.name&&<div className="text-danger small">{errors.name}</div>}</div>
  <div className="col-md-6"><label htmlFor="user-email" className="form-label fw-semibold">Correo electrónico *</label><input id="user-email" className="form-control" type="email" value={form.email} onChange={(e)=>update('email',e.target.value)} maxLength={130} required/>{errors.email&&<div className="text-danger small">{errors.email}</div>}</div>
  <div className="col-md-6"><label htmlFor="user-phone" className="form-label fw-semibold">Teléfono (opcional)</label><input id="user-phone" className="form-control" value={form.phone} onChange={(e)=>update('phone',e.target.value)} maxLength={20}/>{errors.phone&&<div className="text-danger small">{errors.phone}</div>}</div>
  {!publicRegistration&&<div className="col-md-6"><label htmlFor="user-role" className="form-label fw-semibold">Rol *</label><select id="user-role" className="form-select" value={form.role} onChange={(e)=>update('role',e.target.value)}>{Object.values(ROLES).map((role)=><option key={role} value={role}>{ROLE_LABELS[role]}</option>)}</select>{errors.role&&<div className="text-danger small">{errors.role}</div>}</div>}
  <div className="col-md-12"><label htmlFor="user-password" className="form-label fw-semibold">{editingId?'Nueva contraseña (opcional)':'Contraseña *'}</label><input id="user-password" type="password" className="form-control" minLength={8} autoComplete="new-password" value={form.password} onChange={(e)=>update('password',e.target.value)} placeholder={editingId?'En blanco para conservar la actual':'Mínimo 8 caracteres'} required={!editingId}/>{errors.password&&<div className="text-danger small">{errors.password}</div>}</div>
  {publicRegistration&&<p className="col-12 small text-secondary mb-0">El backend asigna el rol Cliente; el navegador no puede decidir otro rol.</p>}
  {generalError&&<div className="col-12"><div role="alert" className="alert alert-danger mb-0">{generalError}</div></div>}
  <div className="col-12 d-flex gap-2"><button className="btn btn-warning fw-semibold" disabled={saving} type="submit">{saving?'Guardando…':submitLabel}</button>{onCancel&&<button type="button" className="btn btn-outline-secondary" onClick={onCancel}>Cancelar</button>}</div>
 </div></form>;
}
