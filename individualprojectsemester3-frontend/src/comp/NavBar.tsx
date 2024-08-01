import React, { useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
//import"./NavBar.css"
import user from '../assets/images/user.png';
import chat from '../assets/images/comment (1).png'
import logo from '../assets/images/logo.png'
import TokenManager from '../api/TokenManager';
import { Nav, NavItem, Navbar } from 'react-bootstrap';
import LoginApi from '../api/LoginApi';
export default function NavBar() {
  const navigate = useNavigate();
  const claims = TokenManager.getClaims();
    const navLinks = [ 
        {
            id: 1,
            path: "/",
            text: claims?.roles.includes("HOMEOWNER") ? "Manage property" : "Search for properties"
        },
        {
            id: 2,
            path: "/loginPage",
            text: "Login"
        }

    ]
    //const navigate = useNavigate();
    
    const accessToken = TokenManager.getAccessToken() != null;
    console.log(accessToken)
    if (accessToken) {
      // const claims = TokenManager.getClaims();
      // // Check if the token is expired
      // const isTokenExpired = claims && claims.exp && Date.now() >= claims.exp * 1000;
      
      if (!TokenManager.isAccessTokenExpired()) {

        console.log('efrg')
        // Find the link with id 2 and update its path and text
        const linkIndex = navLinks.findIndex(link => link.id === 2);
        if (linkIndex !== -1) {
          navLinks[linkIndex] = {
            ...navLinks[linkIndex],
            path: "/menu",
          };
        }
    }
    else{
      LoginApi.renew().catch((error) => {
        if (error.message === "Request failed with status code 401") {
          navigate("/loginpage", { state: { from: window.location.pathname } });
        } else {
          console.error(error.message);
        }
      });
    }
    
 
  }
   
       
    
  return (
    <Navbar expand="md" style={{ backgroundColor: '#7EB6E0', height: '50px' }} variant="dark" fixed="top" className="NavBar p-2">
    <Navbar.Brand href="/">
      <img
        alt="Logo"
        src={logo}
        className="d-inline-block align-top"
        style={{ width: '135px', height: '48px' }}

      />
    </Navbar.Brand>
    <Navbar.Toggle aria-controls="responsive-navbar-nav" />
    <Navbar.Collapse id="responsive-navbar-nav">
      <Nav className="ml-auto d-flex justify-content-center">
        {navLinks.map(link => link.id !== 2 && (
          <NavLink key={link.id} to={link.path} className="nav-link text-white ml-2" style={{ marginRight: '15px', fontSize: '25px'}}>
            {link.text}
          </NavLink>
        ))}
      </Nav>
    </Navbar.Collapse>
    {accessToken && !TokenManager.isAccessTokenExpired()&&
     <NavLink key={3} to={"/chat"} className="nav-link text-white ml-2" style={{ marginRight: '25px', fontSize: '25px' }}>
     { <img src={chat} alt="chat" className="img-fluid" style={{ width: '45px', height: '45px' }} />}
   </NavLink>
    }
   
    {/* NavLink with id: 2 will be rendered at the end of the Navbar */}
    {navLinks.map(link => link.id === 2 && (
      <NavLink key={link.id} to={link.path} className="nav-link text-white ml-2" style={{ marginRight: '40px', fontSize: '25px' }}>
        {link.path === "/menu" ? <img src={user} alt="User" className="img-fluid" style={{ width: '45px', height: '45px' }} /> : link.text}
      </NavLink>
    ))}
  </Navbar>

  )
}
