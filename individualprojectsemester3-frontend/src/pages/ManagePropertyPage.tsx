import React, { useEffect, useState } from 'react'
import { Container } from 'react-bootstrap';
import AddProperty from '../comp/PropertyForm';
import { useParams } from 'react-router-dom';

export default function ManagePropertyPage() {
    const { id } = useParams();
//     const [properties, setProperties] = useState([]);

//   useEffect(() => {
//     refreshPropertyList();
//   }, []);

//   const refreshPropertyList = () => {
//     const claims = TokenManager.getClaims();
//     if(claims?.userId){
//         PropertyApi.getPropertiesByUserId(claims.userId)
//         .then((data) => setProperties(data))
//         .catch((error) => console.log(error));
//     };

//     }
   
  return (
    <Container
    className='AddPropertyPage'
    style={{
      borderRadius: '25px',
      padding: '50px 20px',
      backgroundColor: '#7EB6E0',
      color: 'white',
      width: '100%', 
      margin: '0 auto',
      marginTop: "50px"
    }}
  >
    <h1>Manage Property</h1>
    <AddProperty propertyId={id}/>
  </Container>
  )
}
