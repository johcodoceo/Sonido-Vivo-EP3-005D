import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from 'react';
import { useAuth } from './AuthContext.jsx';
import { api } from '../services/apiClient.js';
const ProductContext = createContext(null);
export function ProductProvider({ children }) {
 const { user } = useAuth();
 const [products, setProducts] = useState([]);
 const [loading, setLoading] = useState(Boolean(user));
 const [loadedFor, setLoadedFor] = useState(null);
 const loadedForRef = useRef(null);
 const [storageError, setStorageError] = useState('');
 const refreshProducts = useCallback(async () => {
   if (!user) {setProducts([]);setLoadedFor(null);loadedForRef.current=null;setLoading(false);return;}
   if (loadedForRef.current !== user.id) setLoading(true);
   try {const next = await api('/products');setProducts(next);loadedForRef.current=user.id;setLoadedFor(user.id);setStorageError('');}
   catch (error) {setStorageError(error.message);throw error;}
   finally {setLoading(false);}
 }, [user]);
 useEffect(() => {refreshProducts().catch(() => {});}, [refreshProducts]);
 const addProduct = useCallback(async (data) => {
   try {const item=await api('/products',{method:'POST',body:JSON.stringify(data)});setProducts((old)=>[...old,item]);setStorageError('');return {};}
   catch(error){return {...error.errors,general:error.message};}
 }, []);
 const editProduct = useCallback(async (id,data) => {
   try {const item=await api(`/products/${encodeURIComponent(id)}`,{method:'PUT',body:JSON.stringify(data)});setProducts((old)=>old.map((p)=>p.id===id?item:p));return {};}
   catch(error){return {...error.errors,general:error.message};}
 }, []);
 const removeProduct = useCallback(async (id) => {
   try {await api(`/products/${encodeURIComponent(id)}`,{method:'DELETE'});setProducts((old)=>old.filter((p)=>p.id!==id));return null;}
   catch(error){return error.message;}
 }, []);
 const setStock = useCallback(async (id,stock) => {
   try {const item=await api(`/products/${encodeURIComponent(id)}/stock`,{method:'PATCH',body:JSON.stringify({stock})});setProducts((old)=>old.map((p)=>p.id===id?item:p));return null;}
   catch(error){return error.message;}
 }, []);
 const value=useMemo(()=>({products,loading,loadedFor,storageError,refreshProducts,addProduct,editProduct,removeProduct,setStock}),[products,loading,loadedFor,storageError,refreshProducts,addProduct,editProduct,removeProduct,setStock]);
 return <ProductContext.Provider value={value}>{children}</ProductContext.Provider>;
}
export function useProducts(){const ctx=useContext(ProductContext);if(!ctx)throw new Error('useProducts requiere ProductProvider');return ctx;}
