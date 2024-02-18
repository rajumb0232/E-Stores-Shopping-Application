import React from "react";

const InDisplayNavBtn = ({ icon, displayName, view, setCurrent}) => {
  return (
    <button
      className="rounded-sm w-1/2 py-4 px-2 flex flex-col justify-center items-center hover:bg-blue-100"
      onClick={() => {
        sessionStorage.removeItem("currentView");
        setCurrent(view);
      }}
    >
      <p className="text-4xl">{icon}</p>
      <p className="py-1">{displayName}</p>
    </button>
  );
};

export default InDisplayNavBtn;
