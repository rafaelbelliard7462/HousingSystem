import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { NavLink, useNavigate } from 'react-router-dom';
import { Form, Button, Col, Row } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import UserApi from '../api/UserApi';
import '../App.css';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
export default function SignUp() {
  const [user, setUser] = useState({
    id: 0,
    firstName: '',
    lastName: '',
    dateOfBirth: null, // Change the initial value to null
    email: '',
    password: '',
    role: 0,
  });

  const [rePassword, setRePassword] = useState('');
  const [homeSeekerHovered, setHomeSeekerHovered] = useState(false);
  const [homeOwnerHovered, setHomeOwnerHovered] = useState(false);
  const [createAccountHovered, setCreateAccountHovered] = useState(false);
  const navigate = useNavigate();
  const handleCreateUser = (e) => {
    e.preventDefault();
  
    // Check if any required field is empty
    if (!user.firstName || !user.lastName || !user.dateOfBirth || !user.email || !user.password || !rePassword) {
      toast.error('Please fill in all the required fields before creating an account.');
      return;
    }
  
    // Check if passwords match
    if (user.password === rePassword) {
      UserApi.postUser(user)
        .then(() => {
          toast.success('Account created successfully!');
          navigate('/loginPage');
        })
        .catch((error) => {
          if (error.response.status === 400) {
            toast.error('Sorry, email already exists:(');
          } else {
            toast.error('Sorry, something went wrong');
          }
        });
    } else {
      toast.error('Password and the Re-entered password do not match:(. Try again!');
    }
  };
  
  

  const inputStyle = {
    width: '100%',
    marginBottom: '10px',
    backgroundColor: '#E5E4E2',
    color: 'black',
    border: '2px solid #E5E4E2',
    borderRadius: '20px',
    padding: '8px',
    boxSizing: 'border-box',
  };

  const customButtonStyle = {
    borderRadius: '25px',
    cursor: 'pointer',
    marginTop: '10px',
  };

  const homeSeekerStyle = {
    ...customButtonStyle,
    borderRadius: '25px 0px 0px 25px',
    backgroundColor: homeSeekerHovered ? '#7EB6E0' : '#E5E4E2',
    color: user.role === 1 ? '#7EB6E0' : homeSeekerHovered ? '#E5E4E2' : 'black',
    border: '2px solid #E5E4E2',
  };

  const homeOwnerStyle = {
    ...customButtonStyle,
    borderRadius: '0px 25px 25px 0px',
    backgroundColor: homeOwnerHovered ? '#7EB6E0' : '#E5E4E2',
    color: user.role === 0 ? '#7EB6E0' : homeOwnerHovered ? '#E5E4E2' : 'black',
    border: '2px solid #E5E4E2',
  };

  const createAccountStyle = {
    ...customButtonStyle,
    width: '50%',
    backgroundColor: createAccountHovered ? '#7EB6E0' : '#E5E4E2',
    color: createAccountHovered ? '#E5E4E2' : 'black',
    border: '2px solid #E5E4E2',
  };

  const reducedGapInputStyle = {
    ...inputStyle,
    marginBottom: '10px',
  };
  const maxDate = new Date(2006, 11, 31); 

  return (
    <div className="SignUp">
      <ToastContainer position="top-center" 
        autoClose={5000} 
        hideProgressBar={false} 
        closeOnClick
        pauseOnHover />
      <div className="roleButton-container" style={{ marginBottom: '10px' }}>
        <Button
          type="button"
          className={user.role === 1 ? 'HomeSeekerButton color' : 'HomeSeekerButton'}
          onClick={() => setUser({ ...user, role: 1 })}
          onMouseEnter={() => setHomeSeekerHovered(true)}
          onMouseLeave={() => setHomeSeekerHovered(false)}
          style={homeSeekerStyle}
        >
          Home seeker
        </Button>
        <Button
          type="button"
          className={user.role === 0 ? 'HomeOwnerButton color' : 'HomeOwnerButton'}
          onClick={() => setUser({ ...user, role: 0 })}
          onMouseEnter={() => setHomeOwnerHovered(true)}
          onMouseLeave={() => setHomeOwnerHovered(false)}
          style={homeOwnerStyle}
        >
          Homeowner
        </Button>
      </div>
      <div className="SignUp d-flex align-items-center justify-content-center">
        <Form>
          <Form.Group controlId="firstName" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              First name:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <Form.Control
                type="text"
                placeholder="Enter your first name"
                value={user.firstName}
                onChange={(e) => setUser({ ...user, firstName: e.target.value })}
                style={reducedGapInputStyle}
              />
            </Col>
          </Form.Group>

          <Form.Group controlId="lastName" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              Last name:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <Form.Control
                type="text"
                placeholder="Enter your last name"
                value={user.lastName}
                onChange={(e) => setUser({ ...user, lastName: e.target.value })}
                style={reducedGapInputStyle}
              />
            </Col>
          </Form.Group>

          <Form.Group controlId="age" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              Date of birth:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <DatePicker
                selected={user.dateOfBirth}
                className={'custom-datepicker2'}
                onChange={(date) => setUser({ ...user, dateOfBirth: date })}
                dateFormat="yyyy-MM-dd"
                placeholderText="Select your date of birth"
                maxDate={maxDate} 
                showYearDropdown // Show the year dropdown
                yearDropdownItemNumber={100} // Number of years to show in the dropdown
                showMonthDropdown // Show the month dropdown
                scrollableYearDropdown 
              />
            </Col>
          </Form.Group>

          <Form.Group controlId="email" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              Email:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <Form.Control
                type="text"
                placeholder="Enter your email"
                value={user.email}
                onChange={(e) => setUser({ ...user, email: e.target.value })}
                style={reducedGapInputStyle}
              />
            </Col>
          </Form.Group>

          <Form.Group controlId="password" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              Password:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <Form.Control
                type="password"
                placeholder="Enter your password"
                value={user.password}
                onChange={(e) => setUser({ ...user, password: e.target.value })}
                style={reducedGapInputStyle}
              />
            </Col>
          </Form.Group>

          <Form.Group controlId="rePassword" as={Row}>
            <Form.Label column lg={4} style={{ textAlign: 'left' }}>
              Re-enter password:
            </Form.Label>
            <Col style={{ display: 'flex', justifyContent: 'flex-start' }}>
              <Form.Control
                type="password"
                placeholder="Re-enter your password"
                value={rePassword}
                onChange={(e) => setRePassword(e.target.value)}
                style={reducedGapInputStyle}
              />
            </Col>
          </Form.Group>

          <Button
            type="button"
            onClick={handleCreateUser}
            onMouseEnter={() => setCreateAccountHovered(true)}
            onMouseLeave={() => setCreateAccountHovered(false)}
            style={createAccountStyle}
          >
            Create account
          </Button>
        </Form>
      </div>
    </div>
  );
}
