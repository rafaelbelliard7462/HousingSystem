import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const PreferenceApi = {


    getPreferenceByUserId : (userId : number) => axios.get("/preference/id/"  + userId, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),

    postPreference : (newPreference: any) => axios.post("/preference", newPreference,{
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    

    updatePreference : (updatePreference: any ) =>axios.put("/preference/" + updatePreference.id, updatePreference, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
        timeout: 5000, // Set a timeout value (in milliseconds)
      })
      

}

export default PreferenceApi;



