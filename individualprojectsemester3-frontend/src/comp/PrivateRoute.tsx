import React from "react";
import TokenManager from "../api/TokenManager";
import { Navigate, useNavigate } from "react-router-dom";
import LoginApi from "../api/LoginApi";

const PrivateRoute = ({ roles, children }) => {
  console.log(children);
  const claims = TokenManager.getClaims();
      
  const navigate = useNavigate();
  // console.log(claims.refreshToken)
  if (
    claims?.roles?.some((role) => roles.includes(role)) &&
    TokenManager.isAccessTokenExpired()
  ) {
    console.log(claims.refreshToken);
    LoginApi.renew()
      .then((reponse) => {
        console.log(reponse);
        window.location.reload();
        return children;
      })
      .catch((error) => {
        if (TokenManager.isAccessTokenExpired()) {
          LoginApi.renew().catch((error) => {
            if (error.message === "Request failed with status code 401") {
              navigate("/loginpage", {
                state: { from: window.location.pathname },
              });
            } else {
              console.error(error.message);
            }
          });
        } else {
          console.error(error);
        }
      });
  } else if (
    claims?.roles?.some((role) => roles.includes(role)) &&
    !TokenManager.isAccessTokenExpired()
  ) {
    return children;
  } else {
    return <Navigate to={"/loginPage"} />;
  }
};
export default PrivateRoute;
