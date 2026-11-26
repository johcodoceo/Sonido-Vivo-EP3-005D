import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { useAuth } from './AuthContext.jsx';
import { api } from '../services/apiClient.js';
const UserContext = createContext(null);
export function UserProvider({ children }) {
  const { user } = useAuth();
  const [users,setUsers]=useState([]);
  const [storageError,setStorageError]=useState('');
  const refreshUsers=useCallback(async()=>{
    if(user?.role!=='administrador'){setUsers([]);return;}
    try{setUsers(await api('/users'));setStorageError('');}catch(e){setStorageError(e.message);}
  },[user]);
  useEffect(()=>{refreshUsers();},[refreshUsers]);
  const registerClient=useCallback(async(values)=>{
    try{const created=await api('/auth/register',{method:'POST',body:JSON.stringify({...values,role:'cliente'}),skipAuth:true});return {error:null,errors:{},user:created};}
    catch(e){return {error:e.message,errors:e.errors||{}};}
  },[]);
  const createAccount=useCallback(async(values)=>{
    try{const created=await api('/users',{method:'POST',body:JSON.stringify(values)});setUsers((old)=>[...old,created]);return {error:null,errors:{},user:created};}
    catch(e){return {error:e.message,errors:e.errors||{}};}
  },[]);
  const updateAccount=useCallback(async(id,values)=>{
    try{const changed=await api(`/users/${encodeURIComponent(id)}`,{method:'PUT',body:JSON.stringify(values)});setUsers((old)=>old.map((u)=>u.id===id?changed:u));return {error:null,errors:{},user:changed};}
    catch(e){return {error:e.message,errors:e.errors||{}};}
  },[]);
  const toggleAccount=useCallback(async(id)=>{
    try{const changed=await api(`/users/${encodeURIComponent(id)}/toggle`,{method:'PATCH'});setUsers((old)=>old.map((u)=>u.id===id?changed:u));return {error:null,errors:{},user:changed};}
    catch(e){return {error:e.message,errors:e.errors||{}};}
  },[]);
  const value=useMemo(()=>({users,storageError,refreshUsers,registerClient,createAccount,updateAccount,toggleAccount}),[users,storageError,refreshUsers,registerClient,createAccount,updateAccount,toggleAccount]);
  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
}
export function useUsers(){const ctx=useContext(UserContext);if(!ctx)throw new Error('useUsers requiere UserProvider');return ctx;}
