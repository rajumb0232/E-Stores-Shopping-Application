import { Link } from 'react-router-dom'

const Headers = () => {
  return (
    <header className='shadow-md fixed z-50 top-0 border-b border-blue-400 font-sans w-screen flex justify-center bg-blue-400'>
        <nav className='px-2 py-1 flex flex-row items-center justify-center w-11/12'>
            <div className='mr-auto py-2 text-blue-700 text-xl font-bold flex items-center justify-center'>
                <Link to="/">
                    <img src="/src/Images/flipkart-logo.svg" alt="" />
                </Link>
            </div>

            <div className='rounded-xl h-4/6 w-2/5 flex items-center justify-center bg-blue-50 px-4 py-2'>
                <i className="fa-solid fa-magnifying-glass text-slate-600"></i>
                <input type="text" placeholder='Search for products here..'
                 className='border-0 rounded-xl bg-blue-50 h-full px-4 py-2 w-full'/>
            </div>

            <div className=' text-slate-900 py-2 ml-auto text-lg flex justify-center flex-row'>
                <Link to="/login" className='mx-2 px-4 py-2 hover:bg-blue-100 rounded-md flex justify-center items-center '>
                    <img src="/src/Images/profile-icon.svg" className='mt-0.5 mr-1'/> <p className='px-1'>Login</p>
                </Link>
                <Link to="/seller/register" className='mx-2 px-4 py-2 hover:bg-blue-100 rounded-md flex justify-center items-center '>
                <img src="/src/Images/Store-9eeae2.svg" className='mt-0.5 mr-1'/><p className='px-1'>Become a Seller</p>
                </Link>
                <Link to="/login" className='mx-2 px-4 py-2  rounded-full flex justify-center items-center '>
                <img src="/src/Images/options.svg" className='mt-0.5 mr-1'/>
                </Link>
            </div>
        </nav>
    </header>
  )
}

export default Headers