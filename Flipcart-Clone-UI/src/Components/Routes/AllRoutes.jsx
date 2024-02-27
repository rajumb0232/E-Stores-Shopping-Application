import React from "react";
import { Route, Routes } from "react-router-dom";
import App from "../../App";
import { navs } from "./Navigations";
import { useAuth } from "../Context/AuthProvider";
import { Suspense } from "react";

const AllRoutes = () => {
  const { auth } = useAuth();
  const { isAuthenticated, role } = auth;

  return (
    <Suspense fallback={"Loading..."}>
    <Routes>
      <Route path="/" element={<App />}>
        {navs.map((nav, i) => {
          if (isAuthenticated) {
            if (nav.isVisibleAfterLogin) {
              if (nav.authorizedTo.includes("ALL")) {
                return <Route key={i} path={nav.path} element={nav.element} />;
              } else if (nav.authorizedTo.includes(role)) {
                return <Route key={i} path={nav.path} element={nav.element} />;
              }
            }
          } else if (nav.authorizedTo.includes("ALL") && !nav.isPrivate) {
              return <Route key={i} path={nav.path} element={nav.element} />;
            }
        })}
      </Route>
    </Routes>
    </Suspense>
  );
};

export default AllRoutes;
