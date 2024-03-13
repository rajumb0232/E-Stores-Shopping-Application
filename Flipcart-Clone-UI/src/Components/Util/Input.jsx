import React from "react";

const Input = ({value, onChangePerform, isRequired, placeholderText}) => {
  return (
    <input
      type="text"
      onChange={(event) => onChangePerform(event.target.value)}
      required={isRequired}
      placeholder={placeholderText}
      value={value? value : ""}
      className="border-2 border-transparent rounded-md bg-cyan-950 bg-opacity-5 w-full py-2  px-2 text-base hover:border-slate-300 focus:border-slate-300 placeholder:text-slate-500"
    />
  );
};

export default Input;
