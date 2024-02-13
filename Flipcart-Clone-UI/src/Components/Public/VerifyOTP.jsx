import { useEffect, useRef, useState} from 'react'
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/AuthProvider';
import AxiosPrivateInstance from '../API/AxiosPrivateInstance';

const VerifyOTP= () => {
    const navigate = useNavigate();
    const {auth, setAuth} = useAuth();
    const [isSubmited, setIsSubmited] = useState(false);
    const inputs = Array.from({length:6}, () => useRef(null))
    const [otp, setOtp] = useState(0);
    const [hiddenEmail, setHiddenEmail] = useState("");
    const [incorrectOTP, setIncorrectOTP] = useState("");
    const axiosInstance = AxiosPrivateInstance();

    useEffect(() => {
    (auth.fromLocation !== "register") ? navigate("/") : setAuth({...auth, fromLocation:""});
    setHiddenEmail(auth.username.substring(auth.username.lastIndexOf("@gmail.com") - 4));
    }, []);
  

    useEffect(()=> {
       if (otp!==0){
         // requesting to verify OTP
            try{
                const response = axiosInstance.post("/verify-email", {userId:auth.userId, otp:otp});
                if(response.status === 417 || response.status === 400){
                console.log(response.data)
                setIncorrectOTP(response.data.rootCause);
                } 
                if(response.status === 200){
                    console.log(response.data)
                    setIncorrectOTP("");
                    navigate("/login")
                }
            }catch(error){
                console.log(error)
            }
        }
    }, [otp])

    const handleChange = (index, event) => {
        if(event.target.value && index < 5) inputs[index+1].current.focus();
    }

    const handleKeyDown = (index, event) => {
        // Allow only numeric characters and control keys like Backspace, Delete, Arrow keys, etc.
        if (!/^\d$/.test(event.key) && !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight'].includes(event.key)) event.preventDefault();
        if(event.key === 'Backspace' && index > 0 && !event.target.value) inputs[index-1].current.focus();
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        // generating OTP from all the input fields
        let inputOtp = '';
        inputs.map(input => {
            inputOtp += input.current.value.toString();
        });
        setIsSubmited(true);
        setOtp(parseInt(inputOtp));
        
    }

    return (
        <div className='w-svw h-svh flex flex-row justify-center items-center'>

            <div className='w-10/12 h-5/6 mt-16 bg-white flex flex-row justify-center items-center'>

                <div className='w-5/12 h-full flex flex-col justify-center items-center'>

                    <img src="/src/Images/otp.jpg" alt="" className='bg-cover w-10/12 ml-auto'/>
                    <p className='text-slate-400 text-xs'><a href="https://www.freepik.com/free-photo/3d-mobile-phone-with-security-code-padlock_34131312.htm#query=otp&position=0&from_view=search&track=sph&uuid=b9e6347b-597a-4e14-999d-10ed574d59e5" className='text-slate-400'>Image by upklyak</a> on Freepik</p>

                </div>

                <div className='h-full w-7/12 flex flex-col justify-center items-center mr-auto'>

                    <div className='h-full w-10/12 flex flex-col justify-center items-center mr-auto'>
                        <h1 className='text-4xl text-slate-700 font-semibold my-6 p-2'>Verify OTP</h1>
                        <p className='text-lg text-slate-700'
                        >Please enter the otp sent on the Mail id *****{hiddenEmail}</p>

                        <div className='h-max w-full flex justify-center items-center'>
                            {[...Array(6)].map((_, i) => (
                                <input
                                    key={i}
                                    type='text'
                                    maxLength={1}
                                    ref={inputs[i]}
                                    onChange={(e) => handleChange(i, e)}
                                    onKeyDown={(e) => handleKeyDown(i, e)}
                                    className='bg-white border-2 border-slate-500 rounded-md m-2 w-12 h-12 text-center text-2xl font-semibold text-indigo-600'
                                />
                            ))}
                        </div>
                        <p className='text-xs text-red-400 font-mono font-semibold min-h-3 mb-4' 
                        >{incorrectOTP}
                        </p>

                        <button onClick={handleSubmit} disabled={isSubmited}
                        className='rounded-lg bg-blue-500 text-slate-200 w-3/12 py-2 px-4 my-6 font-bold text-center text-lg'
                        >
                            { (isSubmited)
                                ? <i className="fa-solid fa-circle-notch animate-spin"></i>
                                : "Confirm"}
                        </button>
                    </div>

                </div>

            </div>

        </div>
    );
}

export default VerifyOTP;