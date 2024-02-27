import { useState, useEffect } from "react";
import AxiosPrivateInstance from "../API/AxiosPrivateInstance";

export const useStates = () => {
  const [states, setStates] = useState([]);
  const axiosInstance = AxiosPrivateInstance();

  const updateStates = async () => {
    try {
      const statesCache = await caches.open("user");
      const cachedResponse = await statesCache.match("/states");

      if (cachedResponse) {
        const cachedStates = await cachedResponse.json();
        setStates(cachedStates);
      } else {
        const response = await axiosInstance.get("/states");

        if (response.status === 200) {
          const responseData = response.data;
          setStates(responseData);
          statesCache.put(
            "/states",
            new Response(JSON.stringify(responseData))
          );
        } else {
          console.log("Something went wrong");
        }
      }
    } catch (error) {
      console.error("Error fetching states:", error);
    }
  };

  useEffect(() => {
    updateStates();
  }, []);

  return { states };
};

export const usePrimeCategories = () => {
  const [primeCategories, setPrimeCategories] = useState([]);
  const axiosInstance = AxiosPrivateInstance();

  const getPrimeCategories = async () => {
    const primeCategoriesCache = await caches.open("user");
    const primeCategories = await primeCategoriesCache.match(
      "/prime-categories"
    );
    let list = [];
    if (primeCategories) {
      list = await primeCategories.json();
      setPrimeCategories(list);
    } else {
      const response = await axiosInstance.get("/prime-categories");
      if (response.status === 200) {
        list = response.data.map((category) => {
          return category.charAt(0) + category.slice(1).toLowerCase();
        });
        setPrimeCategories(list);
        primeCategoriesCache.put(
          "/prime-categories",
          new Response(JSON.stringify(list))
        );
      } else alert("Something went wront!!");
    }
  };

  useEffect(() => {
    getPrimeCategories();
  }, []);

  return { primeCategories };
};
