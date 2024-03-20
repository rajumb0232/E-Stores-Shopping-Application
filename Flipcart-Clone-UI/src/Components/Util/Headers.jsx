import React from "react";
import { Link, NavLink } from "react-router-dom";
import { useAuth } from "../Context/AuthProvider";
import { useState } from "react";
import { VscListSelection } from "react-icons/vsc";
import { PiStorefront, PiUserCircle } from "react-icons/pi";
import { MdKeyboardArrowDown, MdKeyboardArrowUp } from "react-icons/md";
import { IoSearchOutline } from "react-icons/io5";
import Logout from "../Private/Common/Logout";

const Headers = () => {
  const { auth } = useAuth();
  const { isAuthenticated, role, username } = auth;
  const [loginHovered, setLoginHovered] = useState(false);
  const [doLogout, setDoLogout] = useState(false);

  return (
    <header className="border-b-1 fixed z-50 top-0 font-sans w-screen flex justify-center bg-white">
      <nav className="px-2 flex flex-row items-center justify-center w-11/12 max-w-7xl">
        {/* LOGO */}
        <div className="mr-auto flex items-center justify-center">
          <Link
            to={
              role === "SELLER"
                ? "/seller-dashboard"
                : role === "CUSTOMER"
                ? "/explore"
                : "/"
            }
          >
            <img src="/src/Images/flipkart-logo.svg" alt="" className="w-36" />
          </Link>
        </div>

        {/* SEARCH BAR */}
        <div className="rounded-md h-4/6 w-2/5 text-lg flex items-center justify-center bg-blue-50 px-2 py-1">
          <div className="text-2xl text-slate-500">
            <IoSearchOutline />
          </div>
          <input
            type="text"
            placeholder="Search for products here.."
            className="border-0 rounded-xl bg-blue-50 placeholder:text-slate-500 hover:placeholder:text-slate-400 h-full px-2 py-4 w-full text-gray-700"
          />
        </div>

        {doLogout && <Logout doAppear={setDoLogout} />}

        {/* LOGIN AND ACCOUNT */}
        <div className=" text-slate-900 ml-auto text-md flex justify-center items-center">
          <div
            className="flex justify-start items-center"
            onMouseEnter={() => setLoginHovered(true)}
            onMouseLeave={() => setLoginHovered(false)}
          >
            <Link
              to={isAuthenticated ? "/account" : "/login"}
              className={`mx-2 px-4 py-2 rounded-md flex justify-start items-center ${
                loginHovered
                  ? "bg-prussian_blue text-white"
                  : "bg-transparent text-slate-700"
              }`}
            >
              {/* <img src="/src/Images/profile-icon.svg" className="mt-0.5 mr-1 hover:text-white" /> */}
              <div className="mt-0.5 mr-1 hover:text-white text-2xl">
                <PiUserCircle />
              </div>
              <div className="px-1 flex justify-center items-center">
                {isAuthenticated ? username : "Login"}
                <div className="ml-1">
                  {loginHovered ? (
                    <MdKeyboardArrowUp />
                  ) : (
                    <MdKeyboardArrowDown />
                  )}
                </div>
              </div>
            </Link>
            {/* ON HOVER DISPLAY CARD */}
            {loginHovered ? (
              <div className="shadow-lg shadow-slate-300 bg-white rounded-sm h-max absolute top-14 w-1/5 translate-x-2 -translate-y-0.5 flex flex-col justify-center transition-all duration-300">
                <div className="flex justify-between items-center w-full border-b-2 border-slate-300 p-2">
                  <p className="text-slate-700 ">
                    {isAuthenticated ? "Need break?" : "New customer?"}
                  </p>
                  <button
                    className="text-prussian_blue font-semibold rounded-sm px-2"
                    onClick={() => setDoLogout(true)}
                  >
                    {isAuthenticated ? "Logout" : "Register"}
                  </button>
                </div>

                <NavLink
                  to={isAuthenticated ? "/account" : "/login"}
                  className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                >
                  <i className="fa-regular fa-circle-user px-2"></i>
                  My Profile
                </NavLink>
                {!role || (role && role === "CUSTOMER") ? (
                  <NavLink
                    to={isAuthenticated ? "/wishlist" : "/login"}
                    className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                  >
                    <i className="fa-regular fa-heart px-2"></i>
                    Wishlist
                  </NavLink>
                ) : (
                  ""
                )}
                {!role ||
                (role && (role === "CUSTOMER" || role === "SELLER")) ? (
                  <NavLink
                    to={isAuthenticated ? "/orders" : "/login"}
                    className="text-slate-700 w-full hover:bg-slate-100 text-base py-2"
                  >
                    <i className="fa-solid fa-box-open px-2"></i>
                    Orders
                  </NavLink>
                ) : (
                  ""
                )}
              </div>
            ) : (
              ""
            )}
          </div>

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
            className="mx-2 px-4 py-2 hover:bg-prussian_blue hover:text-white rounded-md flex justify-center items-center "
          >
            <div className="mt-0.5 mr-1 hover:text-white text-2xl">
              <PiStorefront />
            </div>
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
            className="mx-2 px-4 py-2 hover:bg-prussian_blue hover:text-white rounded-md flex justify-center items-center "
          >
            <div className="text-xl mt-1 hover:text-white">
              <VscListSelection />
            </div>
          </Link>
        </div>
      </nav>
    </header>
  );
};

export default Headers;
