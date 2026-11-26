import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest';
import { api, ApiError, getToken, setToken } from '../services/apiClient.js';
describe('Cliente HTTP EP3',()=>{
 beforeEach(()=>{ localStorage.clear();vi.stubGlobal('fetch',vi.fn()); });
 afterEach(()=>{vi.unstubAllGlobals();});
 it('adjunta token a petición protegida', async()=>{
   setToken('token-ficticio');
   fetch.mockResolvedValue({ok:true,status:200,json:async()=>[{id:'GA001'}]});
   await expect(api('/products')).resolves.toEqual([{id:'GA001'}]);
   expect(fetch.mock.calls[0][1].headers.Authorization).toBe('Bearer token-ficticio');
 });
 it('no adjunta token al inicio de sesión', async()=>{
   setToken('previo');fetch.mockResolvedValue({ok:true,status:200,json:async()=>({token:'nuevo'})});
   await api('/auth/login',{method:'POST',body:JSON.stringify({email:'demo',password:'demo'}),skipAuth:true});
   expect(fetch.mock.calls[0][1].headers.Authorization).toBeUndefined();
 });
 it('rechaza 403 con mensaje y no confunde autorización con login',async()=>{
   setToken('token');fetch.mockResolvedValue({ok:false,status:403,json:async()=>({error:'Acceso denegado'})});
   await expect(api('/users')).rejects.toMatchObject({status:403,message:'Acceso denegado'});
   expect(getToken()).toBe('token');
 });
 it('elimina token y comunica sesión expirada en 401',async()=>{
   setToken('vencido');fetch.mockResolvedValue({ok:false,status:401,json:async()=>({error:'Caducó'})});
   await expect(api('/auth/me')).rejects.toBeInstanceOf(ApiError);
   expect(getToken()).toBe(null);
 });
});
