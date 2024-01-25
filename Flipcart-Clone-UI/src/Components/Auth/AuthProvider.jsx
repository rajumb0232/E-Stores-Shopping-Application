import React, { createContext, useState } from 'react'
import { AllRoutes } from '../Routes/AllRoutes';

// constext
const AuthContext = createContext({});

export const AuthProvider = () => {
    const [auth, setAuth] = useState({username:"", role:"", isAuthenticated:false})
  return (
   <AuthContext.Provider value={{auth, setAuth}}>
        {AllRoutes()}
   </AuthContext.Provider>
  )
}

// Custom Hook
export const useAuth = () => useContext(AuthContext);