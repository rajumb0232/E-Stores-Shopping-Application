import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RxDashboard } from "react-icons/rx";
import { BsBoxArrowInDown } from "react-icons/bs";
import { BsBoxes } from "react-icons/bs";
import { PiStorefrontDuotone } from "react-icons/pi";
import PerformanceRecord from "./PerformanceRecord";
import InDisplayNavBtn from "./InDisplayNavBtn";
import AddUpdateProduct from "./AddUpdateProduct";
import EditStore from "./EditStore";
import Orders from "./Orders";
import { useAuth } from "../../Context/AuthProvider";
import AxiosPrivateInstance from "../../API/AxiosPrivateInstance";

const SellerDashboard = () => {
  const [currentViewName, setCurrentViewName] = useState("dashboard");
  const navigate = useNavigate();
  const axiosInstance = AxiosPrivateInstance();
  const { auth } = useAuth();
  const { userId } = auth;

  let isChecked = false;
  const checkForStore = async () => {
    if (!isChecked) {
      isChecked = true;
      const response = await axiosInstance.get("/stores-exist", {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });

      if (response.status === 200) {
        if (response.data === true) {
          localStorage.setItem("store", "true");
          sessionStorage.setItem("currentView", currentViewName);
        } else navigate("/setup-store");
      } else alert("Something went wrong!!");
    }
  };

  useEffect(() => {
    if (localStorage.getItem("store")) {
      const currentInSessionName = sessionStorage.getItem("currentView");
      if (currentInSessionName) {
        setCurrentViewName(currentInSessionName);
      } else {
        sessionStorage.setItem("currentView", currentViewName);
      }
    } else checkForStore();
  }, [currentViewName]);

  return (
    <div className="w-full border-2 border-transparent h-svh flex justify-center items-start bg-gray-200 ">
      {/* MANAGEMENT BLOCK */}
      <div className="w-3/12 ml-2 px-1 h-max mt-18 flex justify-center items-start rounded-sm rounded-t-lg">
        <div className="w-full h-fit bg-stone-50 hover:bg-white rounded-sm flex flex-col justify-start items-center font-semibold text-lg rounded-t-lg">
          <div className="w-full flex flex-col items-center justify-center rounded-t-lg">
            <div className="w-full flex items-center justify-center rounded-t-lg bg-gradient-to-r border-b-2">
              <p className="text-lg text-slate-700 font-semibold py-1 px-4">
                My Store
              </p>
            </div>
            {/* GROSS REVENUE */}
            <div className="rounded-sm w-full text-slate-700 text-base rounded-t-none p-2 h-fit">
              <p>Gross Revenue</p>
              <div className="h-36 text-slate-300 flex justify-center items-center">
                no data to show
              </div>
            </div>
          </div>
          {/* NAVIGATION LINKS */}
          <div className="flex flex-wrap justify-center items-center w-full text-xs p-1 border-t-2">
            {/* DASHBOARD */}
            <InDisplayNavBtn
              icon={<RxDashboard />}
              displayName={"View Dashboard"}
              view={"dashboard"}
              setCurrent={setCurrentViewName}
            />
            {/* ADD PRODUCT */}
            <InDisplayNavBtn
              icon={<BsBoxArrowInDown />}
              displayName={"Add Product"}
              view={"addProduct"}
              setCurrent={setCurrentViewName}
            />
            {/* MANAGE ORDERS */}
            <InDisplayNavBtn
              icon={<BsBoxes />}
              displayName={"Manage Orders"}
              view={"manageOrders"}
              setCurrent={setCurrentViewName}
            />
            {/* UPDATE STORE ADDRESS */}
            <InDisplayNavBtn
              icon={<PiStorefrontDuotone />}
              displayName={"Edit Store Info"}
              view={"editStore"}
              setCurrent={setCurrentViewName}
            />
          </div>
        </div>
      </div>

      {/* DASHBOARD ANALYSIS VIEW */}
      <div className="w-9/12 mr-2 h-full bg-gray-200 flex justify-center items-start rounded-sm overflow-scroll">
        <div className="w-full h-max bg-gray-200 rounded-sm">
          {currentViewName === "dashboard" ? (
            <PerformanceRecord />
          ) : currentViewName === "addProduct" ? (
            <AddUpdateProduct />
          ) : currentViewName === "manageOrders" ? (
            <Orders />
          ) : currentViewName === "editStore" ? (
            navigate('/setup-store')
          ) : (
            ""
          )}
        </div>
      </div>
    </div>
  );
};

export default SellerDashboard;
