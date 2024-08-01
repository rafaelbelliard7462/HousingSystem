import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const PropertyImageApi = {
 
  postPropertyImage: (newPropertyImage: any) =>
    axios.post("/propertyImages", newPropertyImage, {
      headers: { "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    }),
    deletePropertyImage: (propertyImageId: any) =>
    axios.delete("/propertyImages/" + propertyImageId, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    })

};


export default PropertyImageApi;
