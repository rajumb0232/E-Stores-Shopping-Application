import { Outlet } from "react-router-dom";
import Headers from "./Components/Public/Headers";
import { useEffect, useRef, useState } from "react";

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
    </>
  );
}

export default App;
