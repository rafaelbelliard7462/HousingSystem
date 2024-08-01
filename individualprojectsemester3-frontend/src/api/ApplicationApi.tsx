import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const PropertyApi = {
  getApplications: () =>
    axios.get("/applications").then((response) => response.data.applications),

  getAplicationsByUserId: (userId: number) =>
    axios
      .get("/applications/findApplicationsByUser/" + userId, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
      })
      .then((response) => response.data.applications),
  getAplicationsByPropertyId: (propertyId: number) =>
    axios
      .get("/applications/findApplicationsByProperty/" + propertyId, {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
      })
      .then((response) => response.data.applications),

  postApplication: (newApplication: any) =>
    axios.post("/applications", newApplication, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    }),

  getApplicationById: (id: number) =>
    axios.get("/applications/id/" + id).then((response) => response.data),

  updateApplication: (updateApplication: any) =>
    axios.put("/applications/" + updateApplication.id, updateApplication, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    }),

  deleteApplication: (applicationId: any) =>
    axios.delete("/applications/" + applicationId, {
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
    })
};


export default PropertyApi;
