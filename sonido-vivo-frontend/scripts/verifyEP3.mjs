import fs from 'node:fs';
import path from 'node:path';
const checks={
  'JWT endpoint': ['src/context/AuthContext.jsx', '/auth/me'],
  'Token HTTP': ['src/services/apiClient.js', 'Bearer ${token}'],
  'REST productos': ['src/context/ProductContext.jsx', '/products'],
  'REST pedidos': ['src/context/OrderContext.jsx', '/orders'],
  'REST usuarios': ['src/context/UserContext.jsx', '/users'],
  'Rutas protegidas': ['src/routes/AppRoutes.jsx', 'ProtectedRoute'],
};
let fail=0;
for(const [label,[filename,text]] of Object.entries(checks)){
 const present=fs.readFileSync(path.resolve(filename),'utf8').includes(text);
 console.log(`${present?'OK':'FALTA'} — ${label}`);
 if(!present)fail++;
}
process.exitCode=fail?1:0;
