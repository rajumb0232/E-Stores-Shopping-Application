import React, { createContext, useContext, useEffect, useState } from "react";
import useLoginRefresh from "../Auth/useLoginRefersher";
import { useNavigate } from "react-router-dom";

// constext
const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const { user } = useLoginRefresh();
  const [auth, setAuth] = useState({
    userId: "",
    username: "",
    role: "CUSTOMER",
    isAuthenticated: false,
    fromLocation: "",
    accessExpiry: "",
  });

  useEffect(() => {
    if (user?.userId) {
      setAuth(user);
    }
  }, [user]);

  useEffect(() => {
    const role = auth?.role;
    role === "SELLER"
      ? navigate("/seller-dashboard")
      : role === "ADMIN"
      ? navigate("/admin-dashboard")
      : role === "SUPER_ADMIN"? navigate("/super-admin-dashboard")
      : role === "CUSTOMER" && navigate("/explore")
  }, [auth])

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom Hook
export const useAuth = () => useContext(AuthContext);
