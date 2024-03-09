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
      setStoreName(store.storeName);
      setAbout(store.about);
    }
  }, [store]);

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
  };

  // handling changes on isSubmitted changes
  useEffect(() => {
    if (isSubmited) {
      if (isPrevPresent) handleUpdateStore();
      else handleAddStore();
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
