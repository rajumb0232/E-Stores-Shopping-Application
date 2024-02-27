import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../Context/AuthProvider";
import { useState } from "react";
import { VscListSelection } from "react-icons/vsc";

const Headers = () => {
  const { auth } = useAuth();
  const { isAuthenticated, role, username } = auth;
  const [loginHovered, setLoginHovered] = useState(false);

  return (
    <header className=" shadow-md shadow-slate-300 fixed z-50 top-0 font-sans w-screen flex justify-center bg-blue-400 border-b-2 border-blue-400">
      <nav className="px-2 py-1 flex flex-row items-center justify-center w-11/12 max-w-7xl">
        {/* LOGO */}
        <div className="mr-auto flex items-center justify-center">
          <Link to="/">
            <img src="/src/Images/flipkart-logo.svg" alt="" className="w-36" />
          </Link>
        </div>

        {/* SEARCH BAR */}
        <div className="rounded-xl h-4/6 w-2/5 text-lg flex items-center justify-center bg-blue-50 px-2">
          <i className="fa-solid fa-magnifying-glass text-gray-400"></i>
          <input
            type="text"
            placeholder="Search for products here.."
            className="border-0 rounded-xl bg-blue-50 h-full px-4 py-4 w-full text-gray-700"
          />
        </div>

        {/* LOGIN AND ACCOUNT */}
        <div className=" text-slate-900 ml-auto text-lg flex justify-center items-center">
          <Link
            to={isAuthenticated ? "/account" : "/login"}
            className="mx-2 px-4 py-2 hover:bg-blue-100 rounded-md flex justify-start items-center"
            onMouseEnter={() => setLoginHovered(true)}
            onMouseLeave={() => setLoginHovered(false)}
          >
            <img src="/src/Images/profile-icon.svg" className="mt-0.5 mr-1" />
            <p className="px-1 flex justify-center items-center">
              {isAuthenticated ? username : "Login"}
              <i className="fa-solid fa-angle-down px-2 text-sm"></i>
            </p>
            {/* ON HOVER DISPLAY CARD */}
            {loginHovered ? (
              <div className="shadow-lg shadow-slate-300 bg-white rounded-sm h-max absolute top-14 w-1/5 -translate-x-4 flex flex-col justify-center">
                <div className="flex justify-between items-center w-full border-b-2 border-slate-300 p-2">
                  <p>{isAuthenticated ? "Need break?" : "New customer?"}</p>
                  <Link
                    to={isAuthenticated ? "/logout" : "/customer/register"}
                    className="text-blue-600 font-semibold rounded-sm px-2"
                  >
                    {isAuthenticated ? "Logout" : "Register"}
                  </Link>
                </div>

                <Link
                  to={isAuthenticated ? "/account" : "/login"}
                  className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                >
                  <i className="fa-regular fa-circle-user px-2"></i>
                  My Profile
                </Link>
                {!role || (role && role === "CUSTOMER") ? (
                  <Link
                    to={isAuthenticated ? "/wishlist" : "/login"}
                    className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                  >
                    <i className="fa-regular fa-heart px-2"></i>
                    Wishlist
                  </Link>
                ) : (
                  ""
                )}
                {!role ||
                (role && (role === "CUSTOMER" || role === "SELLER")) ? (
                  <Link
                    to={isAuthenticated ? "/orders" : "/login"}
                    className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                  >
                    <i className="fa-solid fa-box-open px-2"></i>
                    Orders
                  </Link>
                ) : (
                  ""
                )}
              </div>
            ) : (
              ""
            )}
          </Link>

          {/* CORE MODULE LINK */}
          <Link
            to={
              !isAuthenticated
                ? "/seller/register"
                : role === "SELLER"
                ? "/orders"
                : role === "CUSTOMER"
                ? "/cart"
                : role === "ADMIN"
                ? ""
                : ""
            }
            className="mx-2 px-4 py-2 hover:bg-blue-100 rounded-md flex justify-center items-center "
          >
            <img src="/src/Images/Store-9eeae2.svg" className="mt-0.5 mr-1" />
            <p className="px-1">
              {!isAuthenticated
                ? "Become a Seller"
                : role === "SELLER"
                ? "Orders"
                : role === "CUSTOMER"
                ? "Cart"
                : role === "ADMIN"
                ? ""
                : ""}
            </p>
          </Link>

          {/* OPTIONS */}
          <Link
            to="/login"
            className="mx-2 px-4 py-2 hover:bg-blue-100 rounded-md flex justify-center items-center "
          >
            <div className="text-xl mt-1">
              <VscListSelection />
            </div>
          </Link>
        </div>
      </nav>
    </header>
  );
};

export default Headers;
