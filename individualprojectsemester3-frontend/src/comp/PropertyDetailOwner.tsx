import React, { useState } from 'react'
import "bootstrap/dist/css/bootstrap.min.css"; // Import Bootstrap CSS
import { Row, Col, Button } from "react-bootstrap";
import ContactForm from './ContactForm';

export default function PropertyDetailOwner({property}: any) {
  console.log(property)
  
  const [confirmModal, setConfirmModal] = useState(false);
  return (
    <div>
      <Row
        style={{
          borderRadius: "25px",
          padding: "50px 20px",
          backgroundColor: "#7EB6E0",
          color: "white",
          maxWidth: "800px", // Adjust the maxWidth as needed
          width: "100%", // Ensure the container takes 100% of the available width
          textAlign: "left",
        }}
      >
        <Col md={6}>
          <h6 className="display-6">Homeowner details:</h6>

          <p className="h5">Name: {property.user.firstName}  {property.user.lastName}</p>
          <p className="h5">Email: {property.user.email}</p>
        </Col>
        
        <Row className="justify-content-end ">
          <Col md={2} className="text-right">
            <Button variant="primary" size="lg" onClick={() =>setConfirmModal(true)}>
              Contact
            </Button>
          </Col>
        </Row>
      </Row>
      <ContactForm
      confirmModal={confirmModal}
      setConfirmModal={setConfirmModal}
      property={property}
      />
    </div>
  )
}
