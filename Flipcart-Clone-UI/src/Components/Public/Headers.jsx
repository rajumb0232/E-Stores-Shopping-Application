import React from 'react'
import { Link, NavLink } from 'react-router-dom'

const Headers = () => {
  return (
    <header className='shadow sticky z-50 top-0 border-b border-slate-50 font-sans w-screen flex justify-center'>
        <nav className='px-2 py-1 flex flex-row items-center justify-center w-11/12'>
            <div className='mr-auto py-2 text-blue-700 text-xl font-bold flex items-center justify-center'>
                <Link to="/">
                    <img src="/src/Images/flipkart-logo.svg" alt="" />
                </Link>
            </div>
            
            <div className='rounded-xl h-3/4 w-2/5 flex items-center justify-center'>
                <input type="text" placeholder='Search for products here..'
                 className='border-0 rounded-xl bg-slate-200 h-full px-4 py-2 w-full'/>
            </div>

            <div className=' text-slate-900 py-2 ml-auto text-lg flex justify-center flex-row'>
                <Link to="/login" className='mx-2 px-4 py-2 hover:bg-blue-100 rounded-full flex justify-center items-center '>
                    <img src="/src/Images/profile-icon.svg" className='mt-0.5'/> <p className='px-1'>Login</p>
                </Link>
                <Link to="/register" className='mx-2 px-4 py-2 rounded-full font-medium text-blue-600 hover:bg-blue-100 border-2 border-blue-100 flex justify-center items-center '>
                <img src="" className='mt-0.5'/> <p className='px-1'>Sign Up</p>
                </Link>
            </div>
        </nav>
    </header>
  )
}

export default Headers