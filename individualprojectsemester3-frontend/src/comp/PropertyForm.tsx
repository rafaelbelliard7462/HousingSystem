import React, { useEffect, useState } from 'react';
import DatePicker from 'react-datepicker'; // Import the DatePicker component
import 'react-datepicker/dist/react-datepicker.css'; // Import the styles
import { Form, Col, Row, Container, Button } from 'react-bootstrap';
import AddressAutocomplete from './AddressForm'; // Update the import path if needed
// Add this import statement at the top of your component file
import '../App.css';
import TokenManager from '../api/TokenManager';
import UserApi from '../api/UserApi';
import PropertyApi from '../api/PropertyApi';
import LoginApi from '../api/LoginApi';
import { useNavigate } from 'react-router-dom';
import PropertyImageForm from './PropertyImageForm';
import PropertyImagesList from './PropertyImagesList';


export default function AddProperty({propertyId}) {
  const [title, setTitle] = useState ("Add")
  const [edit, SetEdit] = useState(false)
  const [disabledEditButton, SetDisabledEditButton] = useState(false);
  const [disabledSaveButton, SetDisabledSaveButton] = useState(true);
  const [propertyType, setPropertyType] = useState("");
  const [price, setPrice] = useState("");
  const [room, setRoom] = useState("");
  const [available, setAvailable] = useState(new Date()); // Initialize with the current date
  const [description, setDescription] = useState("");
  const [address, setAddress] = useState({
                                        id: 0,
                                        street : "",
                                        city : "",
                                        postCode: ""
                                        });

  const propertyTypeOptions = ["ROOM", "STUDIO", "APARTMENT", "HOUSE"];
  const[propertyImages, setPropertyImages] = useState([]);

  const navigate = useNavigate();
  const [user, setUser] = useState({
    id: 0,
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    email: '',
    password: '',
    role: 0
  });
  
  useEffect(()=>{
    const claims = TokenManager.getClaims();
    if(claims?.userId && claims?.roles.includes('HOMEOWNER') && propertyId !== 0){
      getInfo(propertyId);
      setTitle("Edit")
    }
  }, [propertyId])


  const getInfo = (propertyId) => {
    PropertyApi.getPropertyById(propertyId)
      .then((data) => {
        console.log(data, 'hjk');
        setPropertyImages(data.propertyImages)
        setPropertyType(data.propertyType);
        setPrice(data.price);
        setRoom(data.room);
        setAvailable(new Date(data.available)); // Assuming 'available' is a date string
        setDescription(data.description);
        setAddress({
          id: data.address.id,
          street: data.address.street,
          city: data.address.city,
          postCode: data.address.postCode,
        });
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
          console.error('Error fetching property details:', error);
        }
      });
  };
  

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

  }, [])

  
  const handleAddProperty = () => {
    if (!propertyType || !price || !room || !available || !description || !address.street || !address.city || !address.postCode) {
      alert('Please fill in all the required fields before adding the property.');
      return;
    }
    // Implement the logic to handle adding the property
    const property  = {
        userId : user.id,
        address : address,
        propertyType : propertyType,
        price : price,
        room : room,
        available : available,
        description : description,
        rented : false,
        rentedDate : null
    }

    PropertyApi.postProperty(property)
    .then(response => {console.log(response); alert("success")})
    .catch((error) => {
      if (TokenManager.isAccessTokenExpired()) {
        LoginApi.renew().catch((error) => {
          if (error.message === "Request failed with status code 401") {
            navigate("/loginPage");
          } else {
            console.error(error.message);
          }
        });
      } else {
        console.error(error); alert("fail")
      }
    });
    
    console.log('Property added!', property);
  };

  const handleEdit = (e: any) => {
    e.preventDefault();
    SetEdit(true);
    SetDisabledEditButton(true);
    SetDisabledSaveButton(false);
  };

  const handleUpdate = (e: any) => {
    e.preventDefault();
    if (!propertyType || !price || !room || !available || !description || !address.street || !address.city || !address.postCode) {
      alert('Please fill in all the required fields before saving the changes.');
      return;
    }
    SetEdit(false);
    SetDisabledEditButton(false);
    SetDisabledSaveButton(true);

    const property  = {
      id : propertyId,
      userId : user.id,
      address : address,
      propertyType : propertyType,
      price : price,
      room : room,
      available : available.toISOString().replace(/\.\d{3}Z$/, ''),
      description : description,
      rented : false,
      rentedDate : null
  }
    const token = TokenManager.getAccessToken() !== null;
      const claims = TokenManager.getClaims();

      if(token){
        if (claims?.userId && claims?.roles.includes('HOMEOWNER')) {
          console.log(property)
          PropertyApi.updateProperty(property)
          .catch((error) => {if( error.message === "Network Error"){
            console.log(TokenManager.getRefreshToken())
           LoginApi.renew();  
           window.location.reload();}})
          .then(repsose => {alert("Updated succesfully")})
          
    .catch((error) => {
      if (TokenManager.isAccessTokenExpired()) {
        LoginApi.renew().catch((error) => {
          if (error.message === "Request failed with status code 401") {
            navigate("/loginPage");
          } else {
            console.error(error.message);
          }
        });
      } else {
        alert("Update failed"), console.error(error)
      }
    });
        }
      }
     
      
      
      
    }
    const inputStyle = {
      width: '100%', // Adjust the width as needed
      marginBottom: '10px', // Adjust the marginBottom as needed
      backgroundColor: edit ? 'white' : '#7EB6E0',
      color: edit ? 'black' : 'white',
    };
   const customButtonStyle = {
      
      fontSize: '24px', // Adjust the font size as needed
      padding: '10px 20px', // Adjust the padding as needed
      borderRadius: '20px',
      backgroundColor: '#7EB6E0',
      color: '#fff',
      border: '2px solid #fff', // Set the border color to white
    };
  
    const customButtonHoverStyle = {
      backgroundColor: '#fff',
      color: '#7EB6E0',
    };

  return (
    <Container
      className='AddProperty'
      style={{
        borderRadius: '25px',
        padding: '50px 20px',
        color: 'white',
        maxWidth: '800px',
        width: '800px',
        fontSize: '20px'
      }}
    >
      <Row>
        {/* Property Details Section */}
        <Col md={6}>
          <div style={{ marginBottom: '30px' }}>
            <h3 style={{ color: 'white' }}>Property Details</h3>
            <Form>
              <Form.Group as={Row} controlId="propertyType">
                <Form.Label column lg={5} style={{ color: 'white', marginBottom: '10px', textAlign: 'left' }}>
                  Property Type:
                </Form.Label>
                <Col>
                  <Form.Control
                    as="select"
                    value={propertyType}
                    onChange={(e) => setPropertyType(e.target.value)}
                    readOnly={title === 'Edit'?!edit : false}
                    style={title === 'Edit'? inputStyle : {}}
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

              <Form.Group as={Row} controlId="price">
                <Form.Label column lg={5} style={{ color: 'white', marginBottom: '10px', textAlign: 'left' }}>
                  Price:
                </Form.Label>
                <Col>
                  <Form.Control
                    type="number"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    readOnly={title === 'Edit'?!edit : false}
                    style={title === 'Edit'? inputStyle : {}}
                  />
                </Col>
              </Form.Group>

              <Form.Group as={Row} controlId="room">
                <Form.Label column lg={5} style={{ color: 'white', marginBottom: '10px', textAlign: 'left' }}>
                  Nr. of room:
                </Form.Label>
                <Col>
                  <Form.Control
                    type="number"
                    value={room}
                    onChange={(e) => setRoom(e.target.value)}
                    readOnly={title === 'Edit'?!edit : false}
                    style={title === 'Edit'? inputStyle : {}}
                  />
                </Col>
              </Form.Group>

              <Form.Group as={Row} controlId="available">
                <Form.Label column lg={5} style={{ color: 'white', marginBottom: '10px', textAlign: 'left' }}>
                  Available date:
                </Form.Label>
                <Col>
                <DatePicker
                selected={available}
                onChange={(date) => setAvailable(date)}
                dateFormat="yyyy-MM-dd"
                className={title === 'Add' ? 'custom-datepicker' : `custom-datepicker1${edit ? ' edit' : ''}`}
                readOnly={title === 'Edit'?!edit : false}
                style={inputStyle}
              />
                </Col>
              </Form.Group>

              <Form.Group as={Row} controlId="description">
                <Form.Label column lg={5} style={{ color: 'white', marginBottom: '10px', textAlign: 'left' }}>
                  Description:
                </Form.Label>
                <Col>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    readOnly={title === 'Edit'?!edit : false}
                    style={title === 'Edit'? inputStyle : {}}
                  />
                </Col>
              </Form.Group>
            </Form>
          </div>
        </Col>

        {/* Address Details Section */}
        <Col md={6}>
          <div>
            <h3 style={{ color: 'white' }}>Address Details</h3>
            <Form>
              <Form.Group as={Row} controlId="address">
                <Col>
                  <AddressAutocomplete addressObj={address} setAddress={setAddress} title={title} edit={edit}/>
                </Col>
              </Form.Group>
            </Form>
          </div>
        </Col>
        
      </Row>
      {title === 'Edit' && (
      <Row className='d-flex justify-content-start' style={{textAlign : "left"}}>
        <Col>
        <div>
        <h3 style={{ color: 'white' , marginLeft: "80px"}}>Property Images</h3>
        <PropertyImagesList propertyImages={propertyImages}  getInfo={getInfo}/>
        <PropertyImageForm propertyId={propertyId} getInfo={getInfo}/>
        </div>  
        </Col>
       
      </Row>)}
      
      {/* Add Property Button */}
<Row className="mt-1">
        <Col>
      
        {title === 'Add' && (
        <Button variant='primary' onClick={handleAddProperty}>
          Add Property
        </Button>
      )}
         {title === 'Edit' && (
          
          <Button
          variant='primary'
          onClick={handleEdit}
          disabled={disabledEditButton}
          style={{ ...customButtonStyle, ...(edit && customButtonHoverStyle) }}
        >
          Edit
        </Button>
         )}
         {title === 'Edit' && (
        <Button
          variant='success'
          onClick={handleUpdate}
          disabled={disabledSaveButton}
          style={{ ...customButtonStyle, ...(!edit && customButtonHoverStyle) }}
        >
          Save
        </Button>
         )}
        </Col>
      </Row>
    </Container>
  );
}
