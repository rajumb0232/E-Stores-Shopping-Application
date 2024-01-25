import { useState } from 'react'
import Home from './Components/Public/home'
import LoginRegister from './Components/Auth/LoginRegister'
import { Outlet } from 'react-router-dom'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div>
        <Outlet/>
      </div>
    </>
  )
}

export default App
