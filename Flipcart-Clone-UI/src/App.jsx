import { Outlet } from 'react-router-dom'
import Headers from './Components/Public/Headers'

function App() {

  return (
    <>
      <div>
        <Headers/>
        <Outlet/>
      </div>
    </>
  )
}

export default App
