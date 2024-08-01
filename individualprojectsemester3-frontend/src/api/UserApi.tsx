import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const UserApi = {

    getUsers : () => axios.get("/users", {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data.users),
    postUser : (newUser: any) => axios.post("/users", newUser),
    getUserById : (id: number) => axios.get("/users/id/"+ id,
    {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),
    getUserByEmail : (email: string) => axios.get("/users/email/"+ email,
    {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })
    .then(response => response.data),
    updateUser : (updatedUser: any ) =>axios.put("/users/"+ updatedUser.id , updatedUser,
    {
        headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
    })

}

export default UserApi;



