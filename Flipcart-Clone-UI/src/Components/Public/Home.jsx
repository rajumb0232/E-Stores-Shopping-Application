import React, { useEffect } from "react";
import { RiLoader4Fill } from "react-icons/ri";
import { useAuth } from "../Context/AuthProvider";
import { useNavigate } from "react-router-dom";

const Home = () => {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    console.log(auth?.auth?.role);
    const role = auth?.auth?.role;
    role === "SELLER"
      ? navigate("/seller-dashboard")
      : role === "ADMIN"
      ? navigate("/admin-dashboard")
      : role === "SUPER_ADMIN"? navigate("/super-admin-dashboard")
      : role === "CUSTOMER" && navigate("/explore")
  }, [auth])
  
  return (
    <div className="h-screen bg-white flex justify-center items-center text-3xl text-blue-600">
      <div className="animate-spin">
        <RiLoader4Fill />
      </div>
    </div>
  );
};
export default Home;
