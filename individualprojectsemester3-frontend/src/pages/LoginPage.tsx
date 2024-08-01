import { useEffect } from 'react';
import Login from '../comp/Login';
import './LoginPage.css';
import LoginApi from '../api/LoginApi';
import { useNavigate, useLocation } from 'react-router-dom';
import TokenManager from '../api/TokenManager';

export default function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const accessToken = TokenManager.getAccessToken();

    if ( accessToken !== null) {
      if(!TokenManager.isAccessTokenExpired()){
        if (location.state && location.state.from) {
          // Redirect to the location stored in the state
          navigate(location.state.from);
        } else {
          // No stored location, navigate to '/'
          navigate('/');
        }
      }
      // Token is not expired
     
    }
  }, [navigate, location.state]);

  const handleLogin = (username: string, password: string) => {
    LoginApi.login(username, password)
      .then((response) => {
        console.log(response);
        if (location.state && location.state.from) {
          // Redirect to the location stored in the state after successful login
          navigate(location.state.from);
        } else {
          // No stored location, navigate to '/'
          navigate('/');
        }
        // Optional: You may choose to reload the entire page after successful login
        window.location.reload();
      })
      .catch(() => alert('Login failed!'))
      .catch((error) => console.error(error));
  };

  return (
    <div>
      <div className="LoginPage">
        <h1>LoginPage</h1>
        <Login onLogin={handleLogin} />
      </div>
    </div>
  );
}
