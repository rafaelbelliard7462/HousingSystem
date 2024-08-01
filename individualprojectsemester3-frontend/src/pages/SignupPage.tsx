import React from 'react';
import './SignupPage.css';
import SignUp from '../comp/SignUp';
import { Container } from 'react-bootstrap'
export default function SignupPage() {


  return (
    <Container
    className='SignupPage'
    style={{
      borderRadius: '25px',
      padding: '50px 20px',
      backgroundColor: '#7EB6E0',
      color: 'white',
      maxWidth: '800px', // Adjust the maxWidth as needed
      width: '800px', // Ensure the container takes 100% of the available width
    }}
  >
    <h1>SignUp Page</h1>
        <SignUp />
  </Container>
 
    
     
  )
}
