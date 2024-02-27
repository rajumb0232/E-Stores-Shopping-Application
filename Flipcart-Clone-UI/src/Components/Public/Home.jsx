import React from "react";
import { Link, json, useNavigate } from "react-router-dom";
import { useAuth } from "../Context/AuthProvider";
import { useEffect } from "react";

const Home = () => {
  const navigate = useNavigate();
  // const { auth } = useAuth();
  // const { role } = auth;

  useEffect(() => {
    console.log("Working...");
    let role = JSON.parse(localStorage.getItem('user')).role;
    role === "SELLER"
    ? navigate("/seller-dashboard")
    : role === "ADMIN"
    ? navigate("/admin-dashboard")
    : role === "SUPER_ADMIN"
    && navigate("/super-admin-dashboard")
  }, []);

  return (
    <div className="flex items-center justify-center h-svh">
      Home
    </div>
  );
};
export default Home;
