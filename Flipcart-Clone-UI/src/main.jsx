import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes } from 'react-router-dom'
import { AllRoutes } from './Components/Routes/AllRoutes.jsx'
import { AuthProvider } from './Components/Auth/AuthProvider.jsx'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {AllRoutes()}
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>,
)
