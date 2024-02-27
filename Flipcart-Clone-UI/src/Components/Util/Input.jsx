import React from "react";

const Input = ({value, onChangePerform, isRequired, placeholderText}) => {
  return (
    <input
      type="text"
      onChange={(event) => onChangePerform(event.target.value)}
      required={isRequired}
      placeholder={placeholderText}
      value={value}
      className="border-b-2 border-slate-200 w-full mb-4 py-1 text-lg hover:border-slate-300 focus:border-slate-300"
    />
  );
};

export default Input;
