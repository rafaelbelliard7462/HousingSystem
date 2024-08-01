import React, { useEffect, useState } from 'react'
import { Form, Button, Col, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
//import './Profile.css'
import UserApi from '../api/UserApi';
import TokenManager from '../api/TokenManager';
import LoginApi from '../api/LoginApi';
import { useNavigate } from 'react-router-dom';
export default function Profile(props : any) {
  const [edit, SetEdit] = useState(false)
  const [disabledEditButton, SetDisabledEditButton] = useState(false);
  const [disabledSaveButton, SetDisabledSaveButton] = useState(true);
const navigate = useNavigate();
 
  const claims = TokenManager.getClaims();
   useEffect(()=>{
    getInfo()
   }, [claims.userId])
   
   const getInfo = () =>{

    if (claims?.userId) {
      UserApi.getUserById(claims.userId)
      .then(data => { setUser({id: data.id,
                              firstName: data.firstName,
                              lastName: data.lastName,
                              dateOfBirth: data.dateOfBirth,
                              email: data.email,
                              password: data.password,
                              role: data.role})
                           })
                           .catch((error) => {
                            if (TokenManager.isAccessTokenExpired()) {
                              LoginApi.renew().catch((error) => {
                                if (error.message === "Request failed with status code 401") {
                                  navigate("/loginpage", { state: { from: window.location.pathname } });
                                } else {
                                  console.error(error.message);
                                }
                              });
                            } else {
                              console.error( error);
                            }
                          });
    }
   
   }
 

    //const [role, setRole] = useState(1);
    
const getAge = (dateOfBirth :string) =>{
  // The birthdate in the format: YYYY-MM-DD
var birthdateString = dateOfBirth;

// Parse the birthdate string to a Date object
var birthdate = new Date(birthdateString);

// Get the current date
var currentDate = new Date();

// Calculate the difference in years
var age = currentDate.getFullYear() - birthdate.getFullYear();

// Check if the birthday has occurred this year
if (
  currentDate.getMonth() < birthdate.getMonth() ||
  (currentDate.getMonth() === birthdate.getMonth() &&
    currentDate.getDate() < birthdate.getDate())
) {
  age--;
  
}
return age;
}
    const handleEdit = (e: any) => {
    e.preventDefault();
    SetEdit(true);
    SetDisabledEditButton(true);
    SetDisabledSaveButton(false);
  };

  const handleSave = (e: any) => {
    e.preventDefault();
    SetEdit(false);
    SetDisabledEditButton(false);
    SetDisabledSaveButton(true);

      const claims = TokenManager.getClaims();
      if (claims?.userId) {
        UserApi.updateUser(user)
        .then(repsose => {alert("Updated succesfully")})
        .catch((error) => {
          if (TokenManager.isAccessTokenExpired()) {
            LoginApi.renew().catch((error) => {
              if (error.message === "Request failed with status code 401") {
                navigate("/loginpage", { state: { from: window.location.pathname } });
              } else {
                console.error(error.message);
              }
            });
          } else {alert("Update failed"), console.error(error)
          }
        });
      }
      
      
      
    }
    const inputStyle = {
      width: '125%', // Adjust the width as needed
      marginBottom: '10px', // Adjust the marginBottom as needed
      backgroundColor: edit ? 'white' : '#7EB6E0',
      color: edit ? 'black' : 'white',
    };
  
    const labelStyle = {
      fontSize: '20px', // Adjust the font size as needed
    };
  
    const customButtonStyle = {
      fontSize: '24px', // Adjust the font size as needed
      padding: '10px 20px', // Adjust the padding as needed
      borderRadius: '20px',
      marginRight: '10px',
      backgroundColor: '#7EB6E0',
      color: '#fff',
      border: '2px solid #fff', // Set the border color to white
    };
  
    const customButtonHoverStyle = {
      backgroundColor: '#fff',
      color: '#7EB6E0',
    };
  
    const [user, setUser] = useState({
      id: 0,
      firstName: '',
      lastName: '',
      dateOfBirth: '',
      email: '',
      password: '',
      role: 0
    });
  return (
    <div className='Profile d-flex align-items-center justify-content-center'>
    <Form>
      <Form.Group controlId='firstName' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          First name:
        </Form.Label>
        <Col>
          <Form.Control
            type='text'
            placeholder='Enter your first name'
            value={user.firstName}
            readOnly={!edit}
            onChange={(e) => setUser({ ...user, firstName: e.target.value })}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Form.Group controlId='lastName' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Last name:
        </Form.Label>
        <Col>
          <Form.Control
            type='text'
            placeholder='Enter your last name'
            value={user.lastName}
            readOnly={!edit}
            onChange={(e) => setUser({ ...user, lastName: e.target.value })}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Form.Group controlId='age' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Age:
        </Form.Label>
        <Col>
          <Form.Control
            type='text'
            placeholder='Enter your age'
            value={getAge(user.dateOfBirth)}
            readOnly={true}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Form.Group controlId='email' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Email:
        </Form.Label>
        <Col>
          <Form.Control
            type='text'
            placeholder='Enter your email'
            value={user.email}
            readOnly={!edit}
            onChange={(e) => setUser({ ...user, email: e.target.value })}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Button
          variant='primary'
          onClick={handleEdit}
          disabled={disabledEditButton}
          style={{ ...customButtonStyle, ...(edit && customButtonHoverStyle) }}
        >
          Edit
        </Button>
        <Button
          variant='success'
          onClick={handleSave}
          disabled={disabledSaveButton}
          style={{ ...customButtonStyle, ...(!edit && customButtonHoverStyle) }}
        >
          Save
        </Button>
    </Form>
  </div>
  
  )
}
