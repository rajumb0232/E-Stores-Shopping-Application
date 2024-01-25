import React from 'react'
import { Route } from 'react-router-dom'
import App from '../../App'
import {navs} from './Navigations'


export const AllRoutes = () => {
  const auth = true;
  return (
    <Route path='/' element={<App/>}>
        {
          navs.map((nav, i) => {
            if(nav.authorizedTo[0] === "ALL" && !nav.isPrivate){
                return <Route key={i} path={nav.path} element={nav.element}/>
            }
            else if(nav.isPrivate === auth){
              if(nav.authorizedTo[0] === "ALL")
                return <Route key={i} path={nav.path} element={nav.element}/>
              else if(nav.authorizedTo[0] === "SUPER_ADMIN")
                return <Route key={i} path={nav.path} element={nav.element}/>
              else if(nav.authorizedTo[0] === "ADMIN")
                return <Route key={i} path={nav.path} element={nav.element}/>
              else if(nav.authorizedTo[0] === "SELLER")
                return <Route key={i} path={nav.path} element={nav.element}/>
              else if(nav.authorizedTo[0] === "CUSTOMER")
                return <Route key={i} path={nav.path} element={nav.element}/>
            }
          })
        }
    </Route>
  )
}