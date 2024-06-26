import React, { useEffect, useState } from "react";
import AddStore from "./AddStore";
import AddAddress from "../Common/AddAddress";
import ContactForm from "../Common/ContactForm";

const SetUpStoreAndRelated = () => {
  const [view, setView] = useState("");
  useEffect(() => {
    sessionStorage.removeItem("currentView");
  });

  useEffect(() => {
    const currentView = sessionStorage.getItem("esView");
    if(currentView) setView(currentView)
    else setView("Store")
  }, []);

  return (
    <div className="w-screen min-h-screen h-max flex flex-col items-center justify-start">
      <form className="flex justify-start items-start w-11/12 h-max px-10 pb-6 mt-20 rounded-lg">
        {/* SELECTOR */}
        <div className=" w-1/12 h-4/6 p-2 top-24 border-r-1 fixed flex flex-col justify-start items-start font-semibold text-slate-700 text-md">
          <Tab
            tabName={"Store"}
            activeTabName={view}
            onClick={() => {
              setView("Store");
              sessionStorage.setItem("esView", "Store");
            }}
          />
          <Tab
            tabName={"Address"}
            activeTabName={view}
            onClick={() => {
              setView("Address");
              sessionStorage.setItem("esView", "Address");
            }}
          />
          <Tab
            tabName={"Contacts"}
            activeTabName={view}
            onClick={() => {
              setView("Contacts");
              sessionStorage.setItem("esView", "Contacts");
            }}
          />
        </div>
        {/* FORM */}
        <div className="w-10/12 h-full ml-auto flex justify-end px-2">
          {view === "Store" ? (
            <AddStore />
          ) : view === "Address" ? (
            <AddAddress />
          ) : (
            view === "Contacts" && <ContactForm />
          )}
        </div>
      </form>
    </div>
  );
};

export default SetUpStoreAndRelated;

export const Tab = ({ tabName, onClick, activeTabName }) => {
  return (
    <button
      className={`w-max py-1 my-2 flex justify-center items-center border-b-2 ${
        tabName === activeTabName ? "border-blue-400 " : "border-transparent"
      }`}
      type="button"
      onClick={onClick}
    >
      {tabName}
    </button>
  );
};
