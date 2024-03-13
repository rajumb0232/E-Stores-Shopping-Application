import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../Context/AuthProvider";
import { useEffect } from "react";
import { RiLoader4Fill } from "react-icons/ri";

const Home = () => {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const { role } = auth;

  useEffect(() => {
    role === "SELLER"
      ? navigate("/seller-dashboard")
      : role === "ADMIN"
      ? navigate("/admin-dashboard")
      : role === "SUPER_ADMIN"? navigate("/super-admin-dashboard")
      : role === "CUSTOMER" && navigate("/explore")
  }, [auth]);

  return (
    <div className="h-screen bg-white flex justify-center items-center text-3xl text-blue-600">
      <div className="animate-spin">
        <RiLoader4Fill />
      </div>
    </div>
  );
};
export default Home;
