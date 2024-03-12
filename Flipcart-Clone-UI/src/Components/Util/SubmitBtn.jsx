import React from "react";
import { RiLoader4Fill } from "react-icons/ri";

const SubmitBtn = ({ submit, isSubmited, name }) => {
  return (
    <button
      onClick={submit}
      disabled={isSubmited}
      className={` font-bold rounded-lg w-max min-w-32 px-4 py-2  ${
        isSubmited
          ? "bg-transparent hover:bg-transparent bg-prussian_blue"
          : "bg-prussian_blue text-slate-100 hover:bg-blue-600"
      }`}
    >
      {isSubmited ? (
        <div className="flex">
          <div className="text-lg animate-spin">
            <RiLoader4Fill />
          </div>
          <span className="font-medium font-mono text-xs ml-2 text-slate-700">
            please wait...
          </span>
        </div>
      ) : (
        name
      )}
    </button>
  );
};

export default SubmitBtn;
