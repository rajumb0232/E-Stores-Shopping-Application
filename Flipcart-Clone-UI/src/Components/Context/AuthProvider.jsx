import React, { createContext, useContext, useState } from 'react'
import AllRoutes from '../Routes/AllRoutes';

// constext
const AuthContext = createContext({});

export const AuthProvider = () => {
    const [auth, setAuth] = useState({userId:"", username:"", role:"", isAuthenticated:false, fromLocation:""})
  return (
   <AuthContext.Provider value={{auth, setAuth}}>
        {AllRoutes()}
   </AuthContext.Provider>
  )
}

// Custom Hook
export const useAuth = () => useContext(AuthContext);