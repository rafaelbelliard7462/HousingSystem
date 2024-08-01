import React from "react";
import AddProperty from "../comp/PropertyForm";
import { Container } from "react-bootstrap";

export default function AddPropertyPage() {
  return (
    <Container
      className="AddPropertyPage"
      style={{
        borderRadius: "25px",
        padding: "50px 20px",
        backgroundColor: "#7EB6E0",
        color: "white",
        width: "100%", 
        marginTop: "50px",
      }}
    >
      <h1>Add Property</h1>
      <AddProperty propertyId={0} />
    </Container>
  );
}
