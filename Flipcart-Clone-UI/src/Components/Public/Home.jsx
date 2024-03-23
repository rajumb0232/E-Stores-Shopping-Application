import React from "react";
import { RiLoader4Fill } from "react-icons/ri";

const Home = () => {

  return (
    <div className="h-screen bg-white flex justify-center items-center text-3xl text-blue-600">
      <div className="animate-spin">
        <RiLoader4Fill />
      </div>
    </div>
  );
};
export default Home;
