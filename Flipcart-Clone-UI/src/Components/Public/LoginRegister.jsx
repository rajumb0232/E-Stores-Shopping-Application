import React from 'react'

const LoginRegister = ({isLogin}) => {
  return (
    <h1 className='bg-slate-700 text-slate-200 text-center'>
        {isLogin? "Login" : "Register"}
    </h1>
  )
}

export default LoginRegister;