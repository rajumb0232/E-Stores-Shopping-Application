import AxiosPrivateInstance from "../API/AxiosPrivateInstance";

const useStore = ({ setStore }) => {
  const axiosInstance = AxiosPrivateInstance();

  const updateCacheAndState = (data, cache) => {
    cache.put("/stores", new Response(JSON.stringify(data)));
    setStore(data);
  };

  const fetch = async (cache) => {
    try {
      const response = await axiosInstance.get("/stores");
      if (response.status === 302) {
        console.log(response.data.data);
        updateCacheAndState(response.data.data, cache);
        return true;
      }
    } catch (error) {
      if (error.response.data.status === 302) {
        updateCacheAndState(error.response.data.data, cache);
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
          return true;
        });
      } else return await fetch(cache);
    } else return await fetch(cache);
  };

  return { getPrevStore };
};

export default useStore;
