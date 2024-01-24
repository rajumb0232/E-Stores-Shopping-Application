import React from 'react'
import { Route } from 'react-router-dom'
import App from '../../App'
import Home from '../Public/home'
import LoginRegister from '../Public/LoginRegister'
import AdminDashboard from '../Private/Admin/AdminDashboard'
import Account from '../private/Common/Account'
import ResetCredentials from '../Private/Common/ResetCredentials'
import UpdateProfile from '../Private/Common/UpdateProfile'
import Cart from '../Private/Customer/Cart'
import Wishlist from '../Private/Customer/Wishlist'
import AddUpdateProduct from '../Private/Seller/AddUpdateProduct'
import Orders from '../Private/Seller/Orders'
import SellerDashboard from '../Private/Seller/SellerDashboard'
import SuperAdminDashboard from '../Private/SuperAdmin/SuperAdminDashboard'

export const AllRoutes = () => {
  return (
    <Route path='/' element={<App/>}>
        <Route path='/' element={<Home/>}/>
        <Route path='/login' element={<LoginRegister isLogin={true}/>}/>
        <Route path='/register' element={<LoginRegister isLogin={false}/>}/>

        <Route path='/admin-dashboard' element={<AdminDashboard/>}/>

        <Route path='/account' element={<Account/>}/>\
        <Route path='/reset-credentials' element={<ResetCredentials/>}/>
        <Route path='/update-profile' element={<UpdateProfile/>}/>

        <Route path='/cart' element={<Cart/>}/>
        <Route path='/wishlist' element={<Wishlist/>}/>

        <Route path='/add-product' element={<AddUpdateProduct/>}/>
        <Route path='/update-product' element={<AddUpdateProduct/>}/>
        <Route path='/orders' element={<Orders/>}/>
        <Route path='/seller-dashboard' element={<SellerDashboard/>}/>

        <Route path='/super-admin-dashboard' element={<SuperAdminDashboard/>}/>
    </Route>
  )
}