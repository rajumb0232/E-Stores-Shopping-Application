import React from 'react'
import Home from '../Public/home'
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
import Register from '../Public/Register'
import Login from '../Public/Login'
import VerifyOTP from '../Public/VerifyOTP'


export const navs = [
    /**  ---------------------------------PUBLIC--------------------------------- */
    {
      path:"/", 
      element:<Home/>, 
      isPrivate:false, 
      authorizedTo:["ALL"]
    },

    /** ---------------------------------AUTH--------------------------------- */
    {
      path:"/seller/register", 
      element:<Register role={"SELLER"}/>, 
      isPrivate:false, 
      authorizedTo:["ALL"]
    },
    {
      path:"/customer/register", 
      element:<Register role={"CUSTOMER"}/>, 
      isPrivate:false, 
      authorizedTo:["ALL"]
    },
    {
      path:"/login", 
      element:<Login/>, 
      isPrivate:false, 
      authorizedTo:["ALL"]
    },
    {
      path:"/verify-email", 
      element:<VerifyOTP/>, 
      isPrivate:false, 
      authorizedTo:["ALL"]
    },
  
    /** ---------------------------------ADMIN--------------------------------- */
    {
      path:"/admin-dashboard", 
      element:<AdminDashboard/>, 
      isPrivate:true, 
      authorizedTo:["ADMIN"]
    },
  
    /** ---------------------------------COMMON--------------------------------- */
    {
      path:"/account", 
      element:<Account/>, 
      isPrivate:true, 
      authorizedTo:["ALL"]
    },
    {
      path:"/reset-credentials", 
      element:<ResetCredentials/>, 
      isPrivate:true, 
      authorizedTo:["ALL"]
    },
    {
      path:"/update-profile", 
      element:<UpdateProfile/>, 
      isPrivate:true, 
      authorizedTo:["ALL"]
    },
  
    /** ---------------------------------CUSTOMER--------------------------------- */
    {
      path:"/cart", 
      element:<Cart/>, 
      isPrivate:true, 
      authorizedTo:["CUSTOMER"]
    },
    {
      path:"/wishlist", 
      element:<Wishlist/>, 
      isPrivate:true, 
      authorizedTo:["CUSTOMER"]
    },
  
    /** ---------------------------------SELLER--------------------------------- */
    {
      path:"/add-product", 
      element:<AddUpdateProduct/>, 
      isPrivate:true, 
      authorizedTo:["SELLER"]
    },
    {
      path:"/update-product", 
      element:<AddUpdateProduct/>, 
      isPrivate:true, 
      authorizedTo:["SELLER"]
    },
    {
      path:"/orders", 
      element:<Orders/>, 
      isPrivate:true, 
      authorizedTo:["SELLER"]
    },
    {
      path:"/seller-dashboard", 
      element:<SellerDashboard/>, 
      isPrivate:true, 
      authorizedTo:["SELLER"]
    },
  
    /** ---------------------------------SUPER ADMIN--------------------------------- */
    {
      path:"/super-admin-dashboard", 
      element:<SuperAdminDashboard/>, 
      isPrivate:true, 
      authorizedTo:["SUPER_ADMIN"]
    }
  
  ]
