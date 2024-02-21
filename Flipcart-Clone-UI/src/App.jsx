import { Outlet } from "react-router-dom";
import Headers from "./Components/Util/Headers";
import { useEffect, useRef, useState } from "react";
import Footer from "./Components/Util/Footer";

function App() {
  const headerRef = useRef(null);
  const [headerHeight, setHeaderHeight] = useState();

  useEffect(() => {
    if (headerRef.current) {
      setHeaderHeight(headerRef.current.getBoundingClientRect());
      console.log(headerHeight);
    }
  }, []);
  return (
    <>
        <Headers />
        <Outlet />
        <Footer/>
    </>
  );
}

export default App;
