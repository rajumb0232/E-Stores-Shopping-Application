import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes } from 'react-router-dom'
import { AllRoutes } from './Components/Routes/AllRoutes.jsx'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        {AllRoutes()}
      </Routes>
    </BrowserRouter>
  </React.StrictMode>,
)
