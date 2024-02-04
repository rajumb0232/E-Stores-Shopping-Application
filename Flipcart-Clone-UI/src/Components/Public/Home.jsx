import React, { useLayoutEffect, useState } from 'react'
import { useAuth } from '../Context/AuthProvider';
import { useCookies } from 'react-cookie';


const Home = () => {
  const [value, setValue] = useState('');
  const {auth} = useAuth();
  const [cookies, setCookie, removeCookie] = useCookies(['demo']);

  useLayoutEffect(() => {
    console.log(auth);
    setCookie("demo", "secured data", {
      secure: true,
      sameSite: 'strict',
      expires: new Date(Date.now() + 1000*60*15)
    });
  },[])

  const get = () => {
    console.log(cookies);
    setValue(cookies)
    removeCookie('demo');
  }


  return (
    <div className='w-svw h-svh flex justify-center items-center' >
      <button onClick={get}>getCookie</button>
      <p>{value!==null? value.demo : "null"}</p>
    </div>
  )
}
export default Home;