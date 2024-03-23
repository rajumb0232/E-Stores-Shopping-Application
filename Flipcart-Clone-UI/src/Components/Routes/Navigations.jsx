import React from 'react'
import Home from '../Public/Home'
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
import Register from '../Public/LoginAndRegister'
import VerifyOTP from '../Public/VerifyOTP'
import SetUpStoreAndRelated from '../Private/Seller/SetUpStoreAndRelated'
import Logout from '../Private/Common/Logout'


export const navs = [
    /**  ---------------------------------PUBLIC--------------------------------- */
    {
      path:"/", 
      element:<Home/>, 
      isPrivate:false, 
      isVisibleAfterLogin:true,
      authorizedTo:["ALL"]
    },

    /** ---------------------------------AUTH--------------------------------- */
    {
      path:"/seller/register", 
      element:<Register role={"SELLER"} isLogin={false}/>, 
      isPrivate:false,
      isVisibleAfterLogin:false,
      authorizedTo:["ALL"]
    },
    {
      path:"/customer/register", 
      element:<Register role={"CUSTOMER"} isLogin={false}/>, 
      isPrivate:false,
      isVisibleAfterLogin:false,
      authorizedTo:["ALL"]
    },
    {
      path:"/login", 
      element:<Register role={"ALL"} isLogin={true}/>, 
      isPrivate:false,
      isVisibleAfterLogin:false,
      authorizedTo:["ALL"]
    },
    {
      path:"/verify-email", 
      element:<VerifyOTP/>, 
      isPrivate:false,
      isVisibleAfterLogin:false, 
      authorizedTo:["ALL"]
    },
    {
      path:"/logout", 
      element:<Logout/>, 
      isPrivate:true,
      isVisibleAfterLogin:true, 
      authorizedTo:["ALL"]
    },
  
    /** ---------------------------------ADMIN--------------------------------- */
    {
      path:"/admin-dashboard", 
      element:<AdminDashboard/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["ADMIN"]
    },
  
    /** ---------------------------------COMMON--------------------------------- */
    {
      path:"/account", 
      element:<Account/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["ALL"]
    },
    {
      path:"/reset-credentials", 
      element:<ResetCredentials/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["ALL"]
    },
    {
      path:"/update-profile", 
      element:<UpdateProfile/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["ALL"]
    },
  
    /** ---------------------------------CUSTOMER--------------------------------- */
    {
      path:"/cart", 
      element:<Cart/>, 
      isPrivate:true,
      isVisibleAfterLogin:true, 
      authorizedTo:["CUSTOMER"]
    },
    {
      path:"/wishlist", 
      element:<Wishlist/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["CUSTOMER"]
    },
  
    /** ---------------------------------SELLER--------------------------------- */
    {
      path:"/add-product", 
      element:<AddUpdateProduct/>, 
      isPrivate:true,
      isVisibleAfterLogin:true, 
      authorizedTo:["SELLER"]
    },
    {
      path:"/update-product", 
      element:<AddUpdateProduct/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["SELLER"]
    },
    {
      path:"/orders", 
      element:<Orders/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["SELLER","CUSTOMER"]
    },
    {
      path:"/seller-dashboard", 
      element:<SellerDashboard/>, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["SELLER"]
    },
    {
      path:"/setup-store", 
      element:<SetUpStoreAndRelated />, 
      isPrivate:true,
      isVisibleAfterLogin:true,
      authorizedTo:["ALL"]
    },
  
    /** ---------------------------------SUPER ADMIN--------------------------------- */
    {
      path:"/super-admin-dashboard", 
      element:<SuperAdminDashboard/>, 
      isPrivate:true, 
      isVisibleAfterLogin:true,
      authorizedTo:["SUPER_ADMIN"]
    }
  
  ]
