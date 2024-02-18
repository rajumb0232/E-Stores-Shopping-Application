import React from "react";

const DataCartBtn = ({name, data}) => {
  return (
    <div className="pt-2 flex flex-col justify-center items-center w-full hover:bg-stone-50">
      <p>{name}</p>
      <p className="text-2xl py-4 text-slate-500">{data}</p>
    </div>
  );
};

export default DataCartBtn;
