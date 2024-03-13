import React, { useEffect, useState } from "react";
import AddStore from "./AddStore";
import AddAddress from "../Common/AddAddress";
import ContactForm from "../Common/ContactForm";

const SetUpStoreAndRelated = () => {
  const [view, setView] = useState("store");
  useEffect(() => {
    sessionStorage.removeItem("currentView");
  });

  return (
    <div className="w-screen min-h-screen h-max flex flex-col items-center justify-start">
      <form className="flex justify-start items-start w-11/12 h-max px-10 pb-6 mt-20 rounded-lg">
        {/* SELECTOR */}
        <div className=" w-1/12 h-4/6 p-2 top-24 border-r-1 fixed flex flex-col justify-start items-start font-semibold text-slate-700 text-md">
          <button
            className={`w-max py-1 my-2 flex justify-center items-center border-r-2 border-b-2 border-r-white ${
              view === "store" ? "border-blue-400 " : "border-transparent"
            }`}
            type="button"
            onClick={() => {
              setView("store");
              sessionStorage.setItem("editStoreView", true);
            }}
          >
            Store
          </button>

          <button
            className={`w-max py-1 my-2 flex justify-center items-center border-b-2 ${
              view === "address" ? "border-blue-400 " : "border-transparent"
            }`}
            type="button"
            onClick={() => {
              setView("address");
              sessionStorage.setItem("editStoreView", false);
            }}
          >
            Address
          </button>
          <button
            className={`w-max py-1 my-2 flex justify-center items-center border-b-2 ${
              view === "contacts" ? "border-blue-400 " : "border-transparent"
            }`}
            type="button"
            onClick={() => {
              setView("contacts");
              sessionStorage.setItem("editStoreView", false);
            }}
          >
            Contacts
          </button>
        </div>
        {/* FORM */}
        <div className="w-10/12 h-full ml-auto flex justify-end px-2">
          {view === "store" ? (
            <AddStore />
          ) : view === "address" ? (
            <AddAddress />
          ) : (
            view === "contacts" && <ContactForm />
          )}
        </div>
      </form>
    </div>
  );
};

export default SetUpStoreAndRelated;
