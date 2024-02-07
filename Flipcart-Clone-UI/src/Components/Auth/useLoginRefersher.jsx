import AxiosPrivateInstance from "../API/AxiosPrivateInstance";
import { useAuth } from "../Context/AuthProvider";

const useLoginRefresh = async ({doRefresh}) => {
    const {auth, setAuth} = useAuth();
    const axiosInstance = AxiosPrivateInstance();

    console.log("refreshing..., doRefresh: "+doRefresh);

    if(doRefresh === true){
        try{
            const response = await axiosInstance.post("/login/refresh");
            if(response.status === 200){
                console.log(response.data)
                setAuth({
                  ...auth,
                  userId: response.data.data.userId,
                  username: response.data.data.username,
                  role: response.data.data.role,
                  isAuthenticated: response.data.data.authenticated
                })
                localStorage.setItem("access_expiry", response.data.data.accessExpiration);
            }else console.log(response.data);
        }catch(error){
            console.log(error);
        }
    }
  
}

export default useLoginRefresh;