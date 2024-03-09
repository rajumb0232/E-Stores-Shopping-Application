import React from 'react'

const SubmitBtn = ({submit, isSubmited, name}) => {
  return (
    <button onClick={submit} disabled={isSubmited}
    className={` font-bold rounded-lg w-max min-w-32 px-4 py-2  ${isSubmited ? "bg-transparent hover:bg-transparent text-blue-600" : "bg-blue-600 text-slate-100 hover:bg-blue-500"}`}
    >
     { (isSubmited)
      ? <p className='flex'><i className="fa-solid fa-circle-notch animate-spin"></i><span className='font-medium font-mono text-xs ml-2 text-slate-700'>please wait...</span></p>
      : name}
    </button>
  )
}

export default SubmitBtn