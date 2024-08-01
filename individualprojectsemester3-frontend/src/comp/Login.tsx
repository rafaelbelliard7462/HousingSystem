import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import './Login.css';
import UserApi from '../api/UserApi';

export default function Login(props: any) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  const handleLogin = () => {
    // Check if email or password is empty
    if (!email || !password) {
      alert('Please enter both email and password before attempting to log in.');
      return;
    }

    // Proceed with login logic
    props.onLogin(email, password);
  };

  return (
    <div className='Login'>
      <div className="label-input-container">
        <label htmlFor="email">Email:</label>
        <input
          type="text"
          id="email"
          placeholder="Enter your email"
          style={{ marginLeft: '44px' }}
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>

      <div className="label-input-container">
        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          placeholder="Enter your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>

      <button onClick={handleLogin}>Login</button>

      <NavLink to={"/signupPage"}> No account? Create a new account!</NavLink>
    </div>
  );
}
