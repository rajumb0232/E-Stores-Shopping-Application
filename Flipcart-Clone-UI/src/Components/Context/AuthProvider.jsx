import React, { createContext, useContext, useEffect, useState } from "react";
import useLoginRefresh from "../Auth/useLoginRefersher";

// constext
const AuthContext = createContext({});

export const AuthProvider = ({children}) => {
  const { handleRefresh } = useLoginRefresh();
  let didRefresh = false;
  const [auth, setAuth] = useState({
    userId: "",
    username: "",
    role: "CUSTOMER",
    isAuthenticated: false,
    fromLocation: "",
    accessExpiry: "",
  });

  const doRefresh = async () => {
    const data = await handleRefresh();
    setAuth({ ...data });
  };

  useEffect(() => {
    if (!didRefresh) {
      doRefresh();
      didRefresh = true;
    }
  }, []);

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom Hook
export const useAuth = () => useContext(AuthContext);
