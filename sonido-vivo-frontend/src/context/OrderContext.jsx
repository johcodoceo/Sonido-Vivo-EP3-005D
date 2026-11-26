import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { useAuth } from './AuthContext.jsx';
import { useCart } from './CartContext.jsx';
import { useProducts } from './ProductContext.jsx';
import { api } from '../services/apiClient.js';
const OrderContext=createContext(null);
export function OrderProvider({children}){
 const {user}=useAuth();const {lines,replaceAfterOrder}=useCart();const {refreshProducts}=useProducts();
 const [orders,setOrders]=useState([]);const [error,setError]=useState('');
 const refreshOrders=useCallback(async()=>{
   if(!user){setOrders([]);return;}
   try{setOrders(await api('/orders'));setError('');}catch(e){setError(e.message);}
 },[user]);
 useEffect(()=>{refreshOrders();},[refreshOrders]);
 const placeOrder=useCallback(async(form)=>{
   try{
     const order=await api('/orders',{method:'POST',body:JSON.stringify({...form,items:lines.map(({productId,quantity})=>({productId,quantity}))})});
     setOrders((old)=>[order,...old]);replaceAfterOrder();await refreshProducts().catch(()=>{});
     return {error:null,errors:{},order};
   }catch(e){return {error:e.message,errors:e.errors||{}};}
 },[lines,replaceAfterOrder,refreshProducts]);
 const advanceOrder=useCallback(async(id,status)=>{
   try{const changed=await api(`/orders/${encodeURIComponent(id)}/status`,{method:'PATCH',body:JSON.stringify({status})});setOrders((old)=>old.map((o)=>o.id===id?changed:o));return null;}
   catch(e){return e.message;}
 },[]);
 const all=useMemo(()=>[...orders].sort((a,b)=>b.createdAt.localeCompare(a.createdAt)),[orders]);
 const mine=useMemo(()=>all.filter((o)=>o.userId===user?.id),[all,user]);
 const value=useMemo(()=>({orders:all,mine,placeOrder,advanceOrder,error,refreshOrders}),[all,mine,placeOrder,advanceOrder,error,refreshOrders]);
 return <OrderContext.Provider value={value}>{children}</OrderContext.Provider>;
}
export function useOrders(){const ctx=useContext(OrderContext);if(!ctx)throw new Error('useOrders requiere OrderProvider');return ctx;}
