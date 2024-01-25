import React, { createContext, useState } from 'react'

// constext
const AuthContext = createContext({});

export const AuthProvider = ({child}) => {
    const [auth, setAuth] = useState({username:"", role:"", isAuthenticated:false})
  return (
   <AuthContext.Provider value={{auth, setAuth}}>
        {child}
   </AuthContext.Provider>
  )
}

// Custom Hook
export const useAuth = () => useContext(AuthContext);