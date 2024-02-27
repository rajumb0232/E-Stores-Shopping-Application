import React, { useEffect, useState } from "react";
import AddAddress from "./AddAddress";
import AddStore from "./AddStore";

const SetUpStoreAndRelated = () => {
  const [viewStore, setViewStore] = useState(true);

  useEffect(() => {
    sessionStorage.removeItem("currentView");
  });

  return (
    <div className="w-screen h-screen flex flex-col items-center justify-center bg-slate-100">
      <form className="flex flex-col justify-start items-center w-4/5 h-5/6 px-10 pb-6 mt-16 rounded-lg bg-white">
        {/* SELECTOR */}
        <div className="flex flex-row justify-center items-center w-full font-semibold text-slate-700">
          <button
            className={`w-1/6 py-1 flex justify-center items-center rounded-bl-lg border-r-2 border-white ${
              viewStore
                ? "bg-blue-400 text-slate-50"
                : "bg-slate-200 text-slate-700"
            }`}
            type="button"
            onClick={() => {
              setViewStore(true);
              sessionStorage.setItem("editStoreView", true);
            }}
          >
            Store
          </button>

          <button
            className={`w-1/6 py-1 flex justify-center items-center rounded-br-lg ${
              !viewStore
                ? "bg-blue-400 text-slate-50"
                : "bg-slate-200 text-slate-700"
            }`}
            type="button"
            onClick={() => {
              setViewStore(false);
              sessionStorage.setItem("editStoreView", false);
            }}
          >
            Address
          </button>
        </div>
        {/* FORM */}
        {viewStore ? (
          <AddStore isViewStore={setViewStore} />
        ) : (
          <AddAddress isViewStore={setViewStore} />
        )}
      </form>
    </div>
  );
};

export default SetUpStoreAndRelated;

// function formatToFirstLetterUpperCase(text) {
//   if (!text || text.trim() === "") {
//     return "";
//   }
//   return text[0].toUpperCase() + text.slice(1).toLowerCase();
// }
