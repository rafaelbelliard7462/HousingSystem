import React, { useEffect, useState } from 'react'
import { Form, Button, Col, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
//import './Profile.css'
import UserApi from '../api/UserApi';
import TokenManager from '../api/TokenManager';
import PreferenceApi from '../api/Preference';
import LoginApi from '../api/LoginApi';
import { useNavigate } from 'react-router-dom';
export default function Profile(props : any) {
  const [edit, SetEdit] = useState(false)
  const [disabledEditButton, SetDisabledEditButton] = useState(false);
  const [disabledSaveButton, SetDisabledSaveButton] = useState(true);
  const navigate = useNavigate();
 
  const claims = TokenManager.getClaims();
  
   
   const getInfo = () =>{

    if (claims?.userId) {
      PreferenceApi.getPreferenceByUserId(claims.userId)
        .then(data => {console.log(data);
          setPreferation({
            id: data.id,
            user: {
              id: data.user.id,
              firstName: data.user.firstName,
              lastName: data.user.lastName,
              dateOfBirth: data.user.dateOfBirth,
              email: data.user.email,
              password: data.user.password,
              role: data.user.role
            },
            city: data.city,  // Assuming data contains city information
            propertyType: data.propertyType,  // Assuming data contains propertyType information
            price: data.price,  // Assuming data contains price information
            room: data.room  // Assuming data contains room information
          });
          console.log(data)
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
          } 

          else {
            //console.error( error);
          }
        });
    }
   
   }
 
   useEffect(() =>{
    const claims =TokenManager.getClaims();

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
 getInfo();
  }, [])

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


    console.log(preferation)
      if (preferation.id === 0 ) {
        // If preferation is empty, add a new preference
        const newPreference  = {
          user : user,
          city : preferation.city,
          propertyType : preferation.propertyType,
          price : preferation.price,
          room : preferation.room
      }
  

        PreferenceApi.postPreference(newPreference)
          .then(response => {
            alert("Added successfully");
            //window.location.reload();
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
              alert("Add failed");
            }
          });
      } else {
        // If preferation is not empty, update the existing preference
        PreferenceApi.updatePreference(preferation)
        .catch((error) => {if( error.message === "Network Error"){
          console.log(TokenManager.getRefreshToken())
         LoginApi.renew();  
         window.location.reload();   
        }})
          .then(response => {
            alert("Updated successfully");
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
              alert("Update failed");
              console.error(error);
            }
          });
        }
      
      
    }
    const inputStyle = {
      width: '100%', // Adjust the width as needed
      marginBottom: '10px', // Adjust the marginBottom as needed
      backgroundColor: edit ? 'white' : '#7EB6E0',
      color: edit ? 'black' : 'white',
    };
  
    const labelStyle = {
      fontSize: '20px', // Adjust the font size as needed
      textAlign : 'left'
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
  
    const [preferation, setPreferation] = useState({
      id: 0,
      user:{
        id: 0,
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        email: '',
        password: '',
        role: 0
      },
      city: '',
      propertyType: '',
      price: 0,
      room: 0
    });
    const [user, setUser] = useState({
      id: 0,
      firstName: '',
      lastName: '',
      dateOfBirth: '',
      email: '',
      password: '',
      role: 0
    });
    const propertyTypeOptions = ["ROOM", "STUDIO", "APARTMENT", "HOUSE"];
  return (
    <div className='Profile d-flex align-items-center justify-content-center'>
    <Form>
      <Form.Group controlId='city' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          City:
        </Form.Label>
        <Col>
          <Form.Control
            type='text'
            placeholder='Enter your first name'
            value={preferation.city}
            readOnly={!edit}
            onChange={(e) => setPreferation({ ...preferation, city: e.target.value })}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Form.Group controlId='propertyType' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Property Type:
        </Form.Label>
        <Col>
        <Form.Control
        as="select"
        value={preferation.propertyType}
        readOnly={!edit}
        onChange={(e) => setPreferation({ ...preferation, propertyType: e.target.value })}
        style={inputStyle}
        size='lg'
        >
        <option value="">Select Property Type</option>
        {propertyTypeOptions.map((option) => (
            <option key={option} value={option}>
            {option}
            </option>
        ))}
        </Form.Control>

        </Col>
      </Form.Group>

      <Form.Group controlId='price' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Price:
        </Form.Label>
        <Col>
          <Form.Control
            type="number"
            placeholder='Enter your price'
            value={preferation.price}
            onChange={(e) => setPreferation({ ...preferation, price: e.target.value })}
            readOnly={!edit}
            style={inputStyle}
            size='lg'
          />
        </Col>
      </Form.Group>

      <Form.Group controlId='room' as={Row}>
        <Form.Label column lg={4} style={labelStyle}>
          Nr. of rooms:
        </Form.Label>
        <Col>
          <Form.Control
            type='number'
            placeholder='Enter your room'
            value={preferation.room}
            readOnly={!edit}
            onChange={(e) => setPreferation({ ...preferation, room: e.target.value })}
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
