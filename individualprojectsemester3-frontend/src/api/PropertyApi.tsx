import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const PropertyApi = {

    getProperties : () => axios.get("/properties")
    .then(response => response.data.properties),

    getPropertiesByUserId : (userId : number) => axios.get("/properties/findPropertiesByUser/"  + userId, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data.properties),

    postProperty : (newProperty: any) => axios.post("/properties", newProperty,{
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    }),
    
    getPropertyById : (id: number) => axios.get("/properties/id/"+ id)
    .then(response => response.data),

    getUserByEmail : (email: string) => axios.get("/properties/email/"+ email,
    {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),

    updateProperty : (updateProperty: any ) =>axios.put("/properties/" + updateProperty.id, updateProperty, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
        timeout: 5000, // Set a timeout value (in milliseconds)
      })
      

}

export default PropertyApi;



