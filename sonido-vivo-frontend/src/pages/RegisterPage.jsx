import { Link, useNavigate } from 'react-router-dom';
import Brand from '../components/Brand.jsx';
import UserForm from '../components/UserForm.jsx';
import { useUsers } from '../context/UserContext.jsx';
export default function RegisterPage(){
 const {registerClient}=useUsers();const navigate=useNavigate();
 async function save(form){const result=await registerClient(form);if(!result.error)navigate('/ingresar',{replace:true,state:{registered:result.user.id}});return result;}
 return <main className="login-screen min-vh-100 d-flex align-items-center py-5"><div className="container" style={{maxWidth:760}}><div className="mb-4"><Brand light to="/ingresar"/></div><section className="login-panel">
  <div className="eyebrow mb-2">EP3 · REGISTRO REAL</div><h1 className="h2 fw-bold">Crea tu cuenta Cliente</h1><p className="text-secondary">El registro se almacena en PostgreSQL. La contraseña se cifra con BCrypt en el backend. Las cuentas públicas solo tienen rol Cliente.</p><div className="demo-alert mb-4" role="note">Para la evaluación utiliza correos y datos de demostración, no datos privados reales.</div>
  <UserForm publicRegistration onSave={save} submitLabel="Registrar cuenta Cliente"/>
  <p className="small text-secondary mt-4 mb-0">¿Ya tienes una cuenta? <Link to="/ingresar">Ir al acceso</Link></p>
 </section></div></main>;
}
