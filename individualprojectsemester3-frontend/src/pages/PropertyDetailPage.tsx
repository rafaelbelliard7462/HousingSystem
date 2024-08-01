import React from 'react'
import PropertyDetails from '../comp/PropertyDetails'
import { useParams } from 'react-router-dom';
import { Container, Col, Row } from 'react-bootstrap';
export default function PropertyDetailPage() {
  const { id } = useParams();
  return (

    <Container
      className='ProfilePage'
      style={{marginTop: '75px'}}
    >
      <PropertyDetails propertyId={id}/>
    </Container>
  )
}
