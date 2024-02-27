import React from 'react'

const SubmitBtn = ({submit, isSubmited, name}) => {
  return (
    <button onClick={submit} disabled={isSubmited}
    className='bg-blue-500 text-slate-100 font-bold rounded-lg w-max min-w-32 px-4 py-2 ml-auto hover:bg-blue-600'
    >
     { (isSubmited)
      ? <i className="fa-solid fa-circle-notch animate-spin"></i>
      : name}
    </button>
  )
}

export default SubmitBtn