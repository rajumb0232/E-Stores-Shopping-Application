import { useEffect, useState } from "react";
import AxiosPrivateInstance from "../API/AxiosPrivateInstance";

const useStore = () => {
  const [store, setStore] = useState({});
  const [prevAddress, setPrevAddress] = useState({});
  const [prevContacts, setPrevContacts] = useState([]);
  const axiosInstance = AxiosPrivateInstance();

  // Updating address if store exists
  let checked = false;
  useEffect(() => {
    if (!checked) {
      checked = true;
      if (store.address) {
        setPrevAddress(store.address);
      }
    }
  }, [store]);

  const fetch = async (cache) => {
    try {
      const response = await axiosInstance.get("/stores");
      if (response.status === 302) {
        cache.put("/stores", new Response(JSON.stringify(response.data.data)));
        setStore(response.data.data);
        return true;
      }
    } catch (error) {
      if (error.response.data.status === 302) {
        cache.put("/stores", new Response(JSON.stringify(error.response.data.data)));
        setStore(error.response.data.data);
      } else {
        console.log(error.stack);
        return false;
      }
    }
  };

  const getPrevStore = async (doForce) => {
    const cache = await caches.open("user");
    if (!doForce) {
      const storeCache = await cache.match("/stores");
      if (storeCache) {
        return storeCache.json().then((data) => {
          setStore(data);
        });
      } else fetch(cache);
    } else fetch(cache);
  };

  // update contact list if the address if updated
  useEffect(() => {
    if (prevAddress) {
      setPrevContacts(prevAddress.contacts ? prevAddress.contacts : []);
    } else {
      getPrevStore(true);
    }
  }, [prevAddress]);

  // begin
  useEffect(() => {
    getPrevStore();
  }, []);

  return { store, prevAddress, prevContacts };
};

export default useStore;
