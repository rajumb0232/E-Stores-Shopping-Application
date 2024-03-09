import React, { useEffect, useState } from "react";
import SubmitBtn from "../../Util/SubmitBtn";
import { PiStorefrontDuotone } from "react-icons/pi";
import AxiosPrivateInstance from "../../API/AxiosPrivateInstance";
import { DropDown } from "../../Util/DropDown";
import Input from "../../Util/Input";
import FormHeading from "../../Util/FormHeading";
import { usePrimeCategories } from "../../Hooks/useOptions";
import useStore from "../../Hooks/useStore";

const AddStore = () => {
  const [storeId, setStoreId] = useState("");
  const [storeName, setStoreName] = useState("");
  const [about, setAbout] = useState("");
  const [primeCategory, setPrimeCategory] = useState("");
  const [isSubmited, setIsSubmited] = useState(false);
  const [isPrevPresent, setPrevPresent] = useState(false);
  const [isAnyModified, setAnyModified] = useState(false);
  const { primeCategories } = usePrimeCategories();
  const axiosInstance = AxiosPrivateInstance();
  const { store } = useStore();
  useEffect(() => {
    if (store) {
      setStoreId(store.storeId);
      setStoreName(store.storeName);
      setAbout(store.about);
      setPrimeCategory(store.primeCategory);
      setPrevPresent(true);
    }
  }, [store]);

  // update isModified state if data modified
  useEffect(() => {
    if (isPrevPresent) {
      if (storeName !== store.storeName || about !== store.about) {
        setAnyModified(true);
      }
    }
  }, [storeName, about, primeCategory]);

  // handling the submit event by validating the data submitted
  const submit = (event) => {
    event.preventDefault();
    primeCategory === "" && !store
      ? alert("Category not selected!!")
      : storeName === ""
      ? alert("Store is not defined!!")
      : setIsSubmited(true);
  };

  // handling axios request to post the store data
  const updateStore = async (isNew) => {
    const URL = isNew ? `/stores` : `/stores/${storeId}`;

    const body = {
      storeName: storeName,
      primeCategory: primeCategory.toUpperCase(),
      about: about,
    };
    const config = {
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    };

    const updateCache = async (store) => {
      const cache = await caches.open("user");
      cache.put("/stores", new Response(JSON.stringify(store)));
    };

    // Requesting to add new store
    const add = async () => {
      try {
        const response = await axiosInstance.post(URL, body, config);
        // validating response
        if (response.status === 201) {
          updateCache(response?.data?.data);
          localStorage.setItem("store", "true");
          setStoreId(response?.data?.data?.storeId);
          setIsSubmited(false);
        } else {
          setIsSubmited(false);
          alert(response?.data.message || response?.message);
          console.log(response?.data);
        }
      } catch (error) {
        setIsSubmited(false);
        alert(error?.response?.message);
        console.log(error?.response?.data);
      }
    };

    // Requesting to updating the existing store
    const update = async () => {
      try {
        const response = await axiosInstance.put(URL, body, config);
        // validating response
        if (response.status === 200) {
          updateCache(response?.data?.data);
          localStorage.setItem("store", "true");
          setStoreId(response?.data?.data?.storeId);
          setIsSubmited(false);
        } else {
          setIsSubmited(false);
          alert(response?.data.message || response?.message);
          console.log(response?.data);
        }
      } catch (error) {
        setIsSubmited(false);
        alert(error?.response?.message);
        console.log(error?.response?.data);
      }
    };

    isNew ? add() : update();
  };

  // handling isSubmited changes
  useEffect(() => {
    if (isSubmited) {
      if (isPrevPresent) {
        storeName !== "" && about !== "" && storeId !== "" && isAnyModified
          ? updateStore(false)
          : alert("Invalid Inputs, may be blank!!");
      } else updateStore(true);
    }
  }, [isSubmited]);

  return (
    <div className="flex flex-col justify-center items-center w-full h-full">
      <FormHeading icon={<PiStorefrontDuotone />} text={"Store Details"} />

      <div className="w-full flex flex-col items-center px-5">
        <Input
          isRequired={true}
          placeholderText={"Your store name here:"}
          onChangePerform={setStoreName}
          value={storeName}
        />

        <textarea
          type="text"
          id="about"
          onChange={(event) => setAbout(event.target.value)}
          placeholder="About (optional):"
          className="h-40 w-full overflow-x-clip text-start text-slate-700 bg-cyan-950 bg-opacity-5 hover:border-slate-300 focus:border-slate-300 border-2 border-transparent rounded-md p-2 text-base"
          value={about}
        />
      </div>

      <div className="w-full flex justify-center items-center">
        {!store && (
          <div className="mr-auto my-8 w-full flex">
            <DropDown
              valueType={"Category"}
              setter={setPrimeCategory}
              value={primeCategory}
              warnMessage={
                "You cannot change the category after creating the store. are you sure?"
              }
              DefaultText={"Select Category"}
              options={primeCategories}
            />
          </div>
        )}

        {/* SUBMIT BUTTON */}
        <div className="ml-auto my-8 w-full flex justify-end">
          <SubmitBtn
            submit={submit}
            isSubmited={isSubmited}
            name={isPrevPresent ? "Update" : "Confirm"}
          />
        </div>
      </div>
    </div>
  );
};

export default AddStore;
