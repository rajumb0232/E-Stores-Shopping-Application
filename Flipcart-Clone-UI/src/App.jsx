import { Outlet } from 'react-router-dom'
import Headers from './Components/Public/Headers'

function App() {

  return (
    <>
      <div className='flex flex-col justify-center items-center bg-slate-100'>
        <Headers/>
        <Outlet/>
      </div>
    </>
  )
}

export default App
