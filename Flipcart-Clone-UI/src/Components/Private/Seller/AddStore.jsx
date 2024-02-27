import React, { useEffect, useState } from "react";
import SubmitBtn from "../../Public/SubmitBtn";
import { PiStorefrontDuotone } from "react-icons/pi";
import AxiosPrivateInstance from "../../API/AxiosPrivateInstance";
import { DropDown } from "../../Util/DropDown";
import Input from "../../Util/Input";
import FormHeading from "../../Util/FormHeading";
import { usePrimeCategories } from "../../Hooks/useOptions";
import useStore from "../../Hooks/useStore";

const AddStore = ({ isViewStore }) => {
  const [storeName, setStoreName] = useState("");
  const [about, setAbout] = useState("");
  const [primeCategory, setPrimeCategory] = useState("");
  const [isSubmited, setIsSubmited] = useState(false);
  const [store, setStore] = useState({});
  const [isPrevPresent, setPrevPresent] = useState(false);
  const [isAnyModified, setAnyModified] = useState(false);

  const { primeCategories } = usePrimeCategories();
  const { getPrevStore } = useStore({ setStore });

  const axiosInstance = AxiosPrivateInstance();

  useEffect(() => {
    if (store && isViewStore) {
      setStoreName(store.storeName);
      setAbout(store.about);
    }
  }, [store]);

  // fetching prime categories and updating categories state.
  useEffect(() => {
    const getPrevStoreData = async () => {
      const isPresent = await getPrevStore(false);
      setPrevPresent(isPresent);
    };
    getPrevStoreData();
  }, []);

  // update isModified state if data modified
  useEffect(() => {
    if (isPrevPresent) {
      if (
        storeName !== store.storeName ||
        about !== store.about ||
        primeCategory !== store.primeCategory
      ) {
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
  const handleAddStore = async () => {
    const cache = await caches.open("user");
    const body = {
      storeName: storeName,
      primeCategory: primeCategory.toUpperCase(),
      about: about,
    };
    const config = {
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    };
    // Requesting
    try {
      const response = await axiosInstance.post("/stores", body, config);
      // validating response
      if (response.status === 201) {
        cache.put("/stores", new Response(JSON.stringify(response.data.data)));
        localStorage.setItem("store", "true");
        isViewStore(false);
      } else {
        setIsSubmited(false);
        alert("Something went wrong!!");
      }
    } catch (error) {
      setIsSubmited(false);
      console.log(error);
    }
  };

  // If any prevPresent and isModified do update or else continue...
  const handleUpdateStore = () => {
    console.log("updating store...");
    setIsSubmited(false);
    isViewStore(false);
  };

  // handling changes on isSubmitted changes
  useEffect(() => {
    if (isSubmited) {
      if (isPrevPresent) handleUpdateStore();
      else handleAddStore();
    }
  }, [isSubmited]);

  return (
    <div className="flex flex-col justify-center w-10/12 h-full">
      <FormHeading icon={<PiStorefrontDuotone />} text={"Create Store"} />

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
        className="h-40 overflow-x-clip text-start text-slate-700 focus:bg-slate-50 hover:bg-slate-50 hover:border-slate-300 focus:border-slate-300 border-2 rounded-md p-2 text-base"
        value={about}
      />

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
        <div className="ml-auto my-8 w-full flex">
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
