import React, { useEffect, useState } from "react";
import SubmitBtn from "../../Public/SubmitBtn";
import { PiUserCirclePlusDuotone } from "react-icons/pi";
import AxiosPrivateInstance from "../../API/AxiosPrivateInstance";
import { DropDown } from "../../Util/DropDown";
import Input from "../../Util/Input";
import FormHeading from "../../Util/FormHeading";
import { RiUserLocationLine } from "react-icons/ri";
import RadioBtn from "../../Util/RadioBtn";
import { FiEdit3 } from "react-icons/fi";
import { useStates } from "../../Hooks/useOptions";
import useStore from "../../Hooks/useStore";

const AddAddress = ({ isViewStore }) => {
  const [addressLine1, setAddressLine1] = useState("");
  const [addressLine2, setAddressLine2] = useState("");
  const [areaVillage, setAreaVillage] = useState("");
  const [cityDistrict, setCityDistrict] = useState("");
  const [pincode, setPinCode] = useState("");
  const [state, setState] = useState("");

  const [districts, setDistricts] = useState([]);
  const { states } = useStates();

  const [addContact, setAddContact] = useState(false);
  const [contacts, addToContacts] = useState([]);

  const [contactName, setContactName] = useState("");
  const [contactNumber, setContactNumber] = useState("");
  const [contactPriority, setContactPriority] = useState("");
  const [contactPrimary, setContactPrimary] = useState(false);

  const [store, setStore] = useState({});
  const [address, setAddress] = useState({});
  const [contact, setContact] = useState({});

  const [isPrevAddressPresent, setIsPrevAddressPresent] = useState(false);
  const [isPrevContactPresent, setIsPrevContactPresent] = useState(false);

  const [isSubmited, setIsSubmited] = useState(false);
  const axiosInstance = AxiosPrivateInstance();
  const { getPrevStore } = useStore({ setStore });

  // updating store state
  useEffect(() => {
    const getStoreData = async () => {
      getPrevStore(false).then((result) => {
        if (result && !store.address) getPrevStore(true);
      });
    };
    getStoreData();
  }, []);

  // Updating address if store exists
  useEffect(() => {
    if (store.address) {
      setIsPrevAddressPresent(true);
      setAddress(store.address);
    } else setIsPrevAddressPresent(false);
  }, [store]);

  // Fetching the States information and updating the state
  let fired2 = false;
  useEffect(() => {
    const getDistricts = async () => {
      const response = await axiosInstance.get(
        "/states/" + state + "/districts"
      );
      response.status === 200
        ? setDistricts(response.data)
        : console.log("Something went wrong");
    };

    if (state && state !== "" && !fired2) {
      setCityDistrict("");
      fired2 = true;
      getDistricts();
    }
  }, [state]);

  // mapping data from address to the respective states.
  useEffect(() => {
    if (address) {
      setAddressLine1(address.addressLine1);
      setAddressLine2(address.addressLine2);
      setAreaVillage(address.areaVillage);
      setCityDistrict(address.cityDistrict);
      setState(address.state);
      setPinCode(address.pincode);
    }
  }, [address]);

  // executes POST or PUt request ot the address resource.
  const updateAddress = async () => {
    try {
      const response = await axiosInstance.post(
        "/stores/" + store.storeId + "/addresses",
        {
          addressLine1,
          addressLine2,
          areaVillage,
          cityDistrict,
          state,
          pincode,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );
      if (response.status === 201) {
        cache.put(
          "/stores/" + store.storeId + "/addresses",
          new Response(JSON.stringify(response.data.data))
        );
      } else {
        setIsSubmited(false);
        console.log(response);
      }
    } catch (error) {
      alert(error.response.message);
      console.log(error);
    }
  };

  // validating the submit and invoking appropriate handler function
  useEffect(() => {
    if (isSubmited) {
      console.log(store.storeId);
      if (store.storeId) updateAddress();
      else {
        setIsSubmited(false);
        alert(
          "You have not created store yet! please create it to continue..."
        );
        isViewStore(true);
      }
    }
  }, [isSubmited]);

  // Handling onClick event
  const handleSubmit = (event) => {
    event.preventDefault();
    setIsSubmited(true);
  };

  return (
    <div className="flex flex-col justify-center items-center w-10/12 h-full">
      <div
        className={`w-full flex flex-col justify-center items-center ${
          addContact ? "-z-10" : "z-10"
        }`}
      >
        <FormHeading
          icon={<RiUserLocationLine />}
          text={"Address & Contact Details"}
        />
        <div className="w-full px-4 py-2 h-full">
          <Input
            isRequired={true}
            onChangePerform={setAddressLine1}
            placeholderText={"Address Line 1: "}
            value={addressLine1}
          />
          <Input
            isRequired={true}
            onChangePerform={setAddressLine2}
            placeholderText={"Address Line 2 (optional): "}
            value={addressLine2}
          />
          <div className="flex justify-center items-center w-full">
            <Input
              isRequired={true}
              onChangePerform={setAreaVillage}
              value={areaVillage}
              placeholderText={"Area/Village: "}
            />
            <div className="w-full flex justify-start items-center">
              <div
                className={`mx-1 min-w-max mb-3 hover:shadow-md shadow-blackrounded-sm ${
                  state !== "" && state ? "bg-amber-400" : "bg-gray-200"
                }`}
              >
                <DropDown
                  valueType={"State"}
                  value={state}
                  setter={setState}
                  DefaultText={"Select State"}
                  options={states}
                />
              </div>
              <div
                className={`mx-1 min-w-max mb-3 hover:shadow-md rounded-sm shadow-black ${
                  cityDistrict !== "" && cityDistrict
                    ? "bg-amber-400"
                    : "bg-gray-200"
                }`}
              >
                <DropDown
                  valueType={"District"}
                  value={cityDistrict}
                  setter={setCityDistrict}
                  DefaultText={"Select District"}
                  options={districts}
                />
              </div>
            </div>
          </div>
          <div className="w-1/6">
            <Input
              isRequired={true}
              onChangePerform={setPinCode}
              placeholderText={"pincode: "}
              value={pincode}
            />
          </div>
          <div className="w-full flex justify-center items-center">
            {/* CONTACTS DISPLAY */}
            <div className="w-full flex justify-start items-center">
              {(contacts || contacts.length > 0) && (
                <div className="py-4 ">
                  {contacts.map((c, i) => {
                    return (
                      <div className="py-1 flex" key={i}>
                        <button
                          className="text-slate-700 hover:text-blue-400 flex justify-center items-center font-bold"
                          type="button"
                        >
                          <FiEdit3 />
                          <p className="font-semibold hover:text-blue-400 text-slate-700 ml-2">
                            Contact {i + 1}:{" "}
                          </p>
                        </button>
                        <p className="ml-2 text-slate-500">{c.contactName}</p>
                        <p className="ml-2 text-slate-500">{c.contactNumber}</p>
                      </div>
                    );
                  })}
                </div>
              )}
              {/*   ADD CONTACT Btn */}
              <div>
                {(!contacts || contacts.length < 2) && (
                  <button
                    className="w-max text-slate-700 rounded-md font-semibold border-2 border-transparent hover:text-amber-400 hover:bg-transparent"
                    onClick={() => {
                      setAddContact(true);
                      sessionStorage.setItem("addContactView", true);
                    }}
                    type="button"
                  >
                    AddContact
                  </button>
                )}
              </div>
            </div>
            {/* SUBMIT Btn */}
            <div className="w-full ml-auto flex mt-auto">
              <SubmitBtn
                isSubmited={isSubmited}
                name={"Update"}
                submit={handleSubmit}
              />
            </div>
          </div>
        </div>
      </div>

      {/* ADD CONTACT CARD | DISPLAY ONLY ON addContact = true*/}
      {addContact && (
        <div
          className={`w-2/4 h-max px-6 py-4 bottom-32 absolute bg-white ${
            addContact ? "z-10" : "-z-10"
          }`}
        >
          <FormHeading
            icon={<PiUserCirclePlusDuotone />}
            text={"Add Contact"}
          />
          <Input
            isRequired={true}
            onChangePerform={setContactName}
            placeholderText={"Contact name: "}
          />
          <Input
            isRequired={true}
            onChangePerform={setContactNumber}
            placeholderText={"Contact number: "}
          />
          <RadioBtn
            value={"Set to primary"}
            setter={setContactPrimary}
            state={contactPrimary}
          />
          <div className="w-full ml-auto flex py-8">
            <SubmitBtn
              isSubmited={isSubmited}
              name={"Add"}
              submit={handleSubmit}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default AddAddress;
