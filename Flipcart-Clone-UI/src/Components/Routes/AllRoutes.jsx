import React from 'react'
import { Route } from 'react-router-dom'
import App from '../../App'
import {navs} from './Navigations'


export const AllRoutes = () => {
  const auth = false;
  return (
    <Route path='/' element={<App/>}>
        {
          navs.map(nav => {
            if(nav.authorizedTo[0]==="ALL" && !nav.isPrivate){
              return <Route path={nav.path} element={nav.element}/>
            }
            else if(nav.authorizedTo[0]==="ALL"  && nav.isPrivate===auth){
              return <Route path={nav.path} element={nav.element}/>
            }
            else if(nav.authorizedTo[0]==="SUPER_ADMIN" && nav.isPrivate===auth){
              return <Route path={nav.path} element={nav.element}/>
            }
            else if(nav.authorizedTo[0]==="ADMIN" && nav.isPrivate===auth){
              return <Route path={nav.path} element={nav.element}/>
            }
            else if(nav.authorizedTo[0]==="SELLER" && nav.isPrivate===auth){
              return <Route path={nav.path} element={nav.element}/>
            }
            else if(nav.authorizedTo[0]==="CUSTOMER" && nav.isPrivate===auth){
              return <Route path={nav.path} element={nav.element}/>
            }
          })
        }
    </Route>
  )
}