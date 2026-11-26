import { useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import Brand from '../components/Brand.jsx';
const profiles=[
 {label:'Cliente',email:'cliente@sonidovivo.example',icon:'🎧'},
 {label:'Vendedor',email:'vendedor@sonidovivo.example',icon:'🎛️'},
 {label:'Administrador',email:'admin@sonidovivo.example',icon:'🎼'},
];
export default function LoginPage(){
 const {user,login,sessionMessage}=useAuth();
 const navigate=useNavigate();const location=useLocation();
 const [email,setEmail]=useState('');const [password,setPassword]=useState('');
 const [error,setError]=useState('');const [loading,setLoading]=useState(false);
 if(user)return <Navigate to="/inicio" replace/>;
 async function submit(event){event.preventDefault();setError('');setLoading(true);
   try{await login(email,password);const from=location.state?.from?.pathname;
     navigate(from&&['/inicio','/catalogo','/ubicaciones'].includes(from)?from:'/inicio',{replace:true});}
   catch(e){setError(e.message);}finally{setLoading(false);}
 }
 return <main className="login-screen min-vh-100 d-flex align-items-center py-5"><div className="container"><div className="row g-5 align-items-center">
   <div className="col-lg-6 login-info"><Brand light to="/ingresar"/><div className="eyebrow mt-5 mb-3">TIENDA MUSICAL · VIÑA DEL MAR</div><h1 className="display-4 fw-bold mb-3">Que tu próxima idea <span className="gradient-text">suene en grande.</span></h1><p className="fs-5 login-description mb-4">Sonido Vivo: instrumentos, equipos de sonido y accesorios para músicos.</p><div className="login-points"><span>Instrumentos</span><span>Audio</span><span>Accesorios</span></div></div>
   <div className="col-lg-6"><section className="login-panel"><div className="eyebrow mb-2">EP3 · ACCESO SEGURO CON JWT</div><h2 className="h3 fw-bold mb-3">Iniciar sesión</h2>
   <p className="text-secondary">Ahora el acceso se valida en Spring Boot mediante contraseña y roles. Selecciona una cuenta de demostración y escribe la contraseña configurada en el backend.</p>
   {sessionMessage&&<div className="alert alert-warning" role="status">{sessionMessage}</div>}
   {location.state?.registered&&<div className="alert alert-success">Cuenta creada. Ingresa con tu correo y contraseña.</div>}
   {error&&<div className="alert alert-danger" role="alert">{error}</div>}
   <form onSubmit={submit} className="d-grid gap-3"><div><label htmlFor="login-email" className="form-label fw-semibold">Correo electrónico</label><input id="login-email" className="form-control" type="email" autoComplete="username" required value={email} onChange={(e)=>setEmail(e.target.value)}/></div>
    <div><label htmlFor="login-password" className="form-label fw-semibold">Contraseña</label><input id="login-password" className="form-control" type="password" autoComplete="current-password" required value={password} onChange={(e)=>setPassword(e.target.value)}/></div>
    <button type="submit" disabled={loading} className="btn btn-warning fw-bold">{loading?'Verificando…':'Entrar a Sonido Vivo'}</button></form>
   <div className="mt-4 pt-3 border-top"><strong className="small d-block mb-2">Cuentas de ejemplo (selecciona el correo)</strong><div className="d-flex flex-wrap gap-2">{profiles.map((p)=><button type="button" className="btn btn-outline-dark btn-sm" key={p.email} onClick={()=>{setEmail(p.email);setError('');}}>{p.icon} {p.label}</button>)}</div><p className="text-secondary small mt-2">Estas opciones solo completan el correo; no evitan la autenticación.</p></div>
   <div className="small text-secondary mt-4">¿Eres cliente nuevo? <Link to="/registro">Crear cuenta</Link></div>
   </section></div></div></div></main>;
}
