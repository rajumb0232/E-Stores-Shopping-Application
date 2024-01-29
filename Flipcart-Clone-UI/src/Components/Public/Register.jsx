import axios from 'axios';
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';

const Register = ({role}) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const register = async (event) => {
    event.preventDefault();

  try{
    axios.post("http://localhost:7000/api/fcv1/users/register",
    {email, password, userRole:role},
    {
      headers:{
        "Content-Type":"application/json",
        "withCredentials":"true"
      }
    }
    ).then(response => {
      navigate("/")
      alert(response.data.message + ", " +response.data.data)
      console.log(response)
    }).catch(error => {
      alert(error.rootCause)
      console.log(error)
    })
  }catch(error){
    alert(error)
  }
   
  }
  return (
    <div className='w-screen h-screen flex flex-col items-center justify-center bg-slate-100'>
      <form className='flex flex-row justify-center items-center w-4/5 h-5/6 px-10 py-6  mt-16 rounded-lg bg-white'>

        {(role=="SELLER")
        ? 
        <div className='w-6/12 h-full'>
          <img src="/src/Images/registerSeller.jpg" alt="Seller JPG" className='w-fit' />
          <p className='text-slate-300 text-sm'>Image by <a href="https://www.freepik.com/free-vector/support-local-business-concept_9010726.htm#fromView=search&term=seller&track=sph&regularType=vector&page=1&position=1&uuid=dc030a6f-b617-4a88-9a41-1a2b8f4f865f" className='text-slate-400'>Freepik</a></p></div>
        : 
        <div className='w-6/12 h-full'>
        <img src="/src/Images/registerCustomer.jpg" alt="Seller JPG" className='w-fit' />
        <p className='text-slate-300 text-sm'><a href="https://www.freepik.com/free-vector/seasonal-sale-discounts-presents-purchase-visiting-boutiques-luxury-shopping-price-reduction-promotional-coupons-special-holiday-offers-vector-isolated-concept-metaphor-illustration_12083346.htm#fromView=search&term=shopping&track=sph&regularType=vector&page=1&position=0&uuid=8e4c697b-0e19-4ca8-b235-d2bf59d415c3" className='text-slate-400'>Image by vectorjuice</a> on Freepik</p></div>
        }

        <div className='flex flex-col justify-center w-6/12 h-full'>
          <h1 className='text-slate-700 font-semibold text-4xl my-8'>Register</h1>

          <label htmlFor="email" 
          className='text-slate-700 text-xl'
          >Email: </label>
          <input type="email" id='email' onChange={(event) => setEmail(event.target.value)}
          className='border-b-2 border-slate-400 mb-4'
          />

          <label htmlFor="password" 
          className='text-slate-700 text-xl my-2'
          >Password: </label>
          <input type="password" id='password' onChange={(event) => setPassword(event.target.value)}
          className='border-b-2 border-slate-400 mb-4'
          />

          <button onClick={register}
          className='bg-blue-500 text-slate-100 font-bold rounded-lg w-1/5 px-4 py-2 my-8 ml-auto'
          >Submit</button>
        </div>
      </form>
    </div>
  )
}
export default Register;