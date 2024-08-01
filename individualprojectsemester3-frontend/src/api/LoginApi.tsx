import axios from "axios";
import TokenManager from "./TokenManager";

let token;
const AuthAPI = {
    login: (email : string, password : string) => axios.post('http://localhost:8080/tokens', { email, password })
        .then(response => response.data)
        .then(data => {TokenManager.setAccessToken(data.accessToken)
                       TokenManager.setRefreshToken(data.refreshToken)}),
    renew:() => axios.post('http://localhost:8080/tokens/renew',  token={refreshToken: TokenManager.getRefreshToken()})
    .then(response => response.data)
    .then(data => {TokenManager.setAccessToken(data.accessToken)
                   TokenManager.setRefreshToken(data.refreshToken)
                   window.location.reload()})
}

export default AuthAPI;