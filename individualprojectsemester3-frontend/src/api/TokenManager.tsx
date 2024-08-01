import  { jwtDecode } from 'jwt-decode'

const TokenManager = {
    getAccessToken: () => sessionStorage.getItem("accessToken"),
    getRefreshToken : () => sessionStorage.getItem("refreshToken"),
    getClaims: () => {

        const claims = sessionStorage.getItem("claims");

        if (!claims) {
            return undefined;
        }
        return JSON.parse(claims);
    },
    setAccessToken: (token:any) => {
        sessionStorage.setItem("accessToken", token);
        const claims = jwtDecode(token);
        sessionStorage.setItem("claims", JSON.stringify(claims));
        return claims;
    },
    setRefreshToken: (token: any) =>{
        sessionStorage.setItem("refreshToken", token)
    },
    clear: () => {
        sessionStorage.removeItem("accessToken");
        sessionStorage.removeItem("refreshToken");
        sessionStorage.removeItem("claims");
    },
    isAccessTokenExpired: () =>{
        const claims = TokenManager.getClaims();
        
        return claims && claims.exp && Date.now() >= claims.exp * 1000
    }
}

export default TokenManager;