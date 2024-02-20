import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../Context/AuthProvider";
import { useEffect } from "react";

const Home = () => {
  const navigate = useNavigate();
  const { auth } = useAuth();
  const { role } = auth;

  useEffect(() => {
    role === "SELLER"
      ? navigate("/seller-dashboard")
      : role === "ADMIN"
      ? navigate("/admin-dashboard")
      : role === "SUPER_ADMIN"
      ? navigate("/super-admin-dashboard")
      : "";
  }, []);

  return (
    <div>
      <Link
        to={"/seller-dashboard"}
        className="flex items-center justify-center h-svh"
      >
        Seller Dashboard
      </Link>
    </div>
  );
};
export default Home;
