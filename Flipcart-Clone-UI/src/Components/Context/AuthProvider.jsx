import React, { createContext, useContext, useEffect, useState } from 'react'
import AllRoutes from '../Routes/AllRoutes';
import useLoginRefresh from '../Auth/useLoginRefersher';

// constext
const AuthContext = createContext({});

export const AuthProvider = () => {
  const [doRefresh, setDoRefresh] = useState(false);
  const [auth, setAuth] = useState({
    userId: "",
    username: "",
    role: "",
    isAuthenticated: false,
    fromLocation: "",
    accessExpiry: ""
  });

  // refershes if the doRefresh is set to true
  useLoginRefresh(doRefresh);

  // updating the user details to the localStorage
    useEffect(() => {
      if(auth.isAuthenticated === true && !localStorage.getItem('user')){
        console.log("Updating auth state")
        const userData = {
          userId: auth.userId,
          username: auth.username,
          role: auth.role
        }
        localStorage.setItem("user", JSON.stringify(userData));
      }
    }, [auth])

    useEffect(() => {
      console.log("validating if the access is expired")
      const expiry = localStorage.getItem("access_expiry");
      console.log("expiry: "+expiry);
      setDoRefresh(true);
    //   if(expiry){
    //     if(new Date(expiry) > new Date()){
    //       const user = localStorage.getItem("user");
    //       setAuth({...user, isAuthenticated:true, fromLocation:"", accessExpiry:expiry});
    //     }else{
    //      setDoRefresh(true);
    //     }
    //   }
    },[])

  return (
   <AuthContext.Provider value={{auth, setAuth}}>
        <AllRoutes/>
   </AuthContext.Provider>
  )
}

// Custom Hook
export const useAuth = () => useContext(AuthContext);