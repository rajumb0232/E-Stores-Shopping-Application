import React from 'react'
import { Route, Routes } from 'react-router-dom'
import App from '../../App'
import {navs} from './Navigations'


const AllRoutes = () => {
  const auth = false;
  return (
    <Routes>
      <Route path='/' element={<App/>}>
          {
            navs.map((nav, i) => {
              if(nav.authorizedTo.includes("ALL") && !nav.isPrivate)
                  return <Route key={i} path={nav.path} element={nav.element}/>
              else if(nav.isPrivate === auth){
                if(nav.authorizedTo.includes("ALL"))
                  return <Route key={i} path={nav.path} element={nav.element}/>
                else if(nav.authorizedTo.includes("SUPER_ADMIN"))
                  return <Route key={i} path={nav.path} element={nav.element}/>
                else if(nav.authorizedTo.includes("ADMIN"))
                  return <Route key={i} path={nav.path} element={nav.element}/>
                else if(nav.authorizedTo.includes("SELLER"))
                  return <Route key={i} path={nav.path} element={nav.element}/>
                else if(nav.authorizedTo.includes("CUSTOMER"))
                  return <Route key={i} path={nav.path} element={nav.element}/>
              }
            })
          }
      </Route>
    </Routes>
  )
}

export default AllRoutes;