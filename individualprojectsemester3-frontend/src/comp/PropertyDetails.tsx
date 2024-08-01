import React, { useEffect, useState } from "react";
import house from "../assets/images/house.png";
import "bootstrap/dist/css/bootstrap.min.css"; // Import Bootstrap CSS
import { Row, Col, Button, Carousel } from "react-bootstrap";
import PropertyApi from "../api/PropertyApi";
import PropertyDetailsMap from "./PropertyDetailsMap";
import LoginApi from "../api/LoginApi";
import TokenManager from "../api/TokenManager";
import ApplyForm from "./ApplyForm";
import { useNavigate } from "react-router-dom";
import PropertyDetailOwner from "./PropertyDetailOwner";

export default function PropertyDetails({ propertyId }: any) {
  const claims = TokenManager.getClaims();
  const navigate = useNavigate();
  const [confirmModal, setConfirmModal] = useState(false);
  const [property, setProperty] = useState({
    id: 0,
    user: {
      id: 0,
      firstName: "",
      lastName: "",
      dateOfBirth: "",
      email: "",
      password: "",
      role: 0,
    },
    propertyType: "",
    price: "",
    room: "",
    available: "",
    description: "",
    address: {
      id: 0,
      street: "",
      city: "",
      postCode: "",
    },
    propertyImages: [],
  });

  useEffect(() => {
    getInfo(propertyId);
  }, []);

  const getInfo = (propertyId: number) => {
    PropertyApi.getPropertyById(propertyId)
      .then((data) => {
        setProperty({
          id: data.id,
          user: {
            id: data.user.id,
            firstName: data.user.firstName,
            lastName: data.user.lastName,
            dateOfBirth: data.user.dateOfBirth,
            email: data.user.email,
            password: data.user.password,
            role: data.user.role,
          },
          propertyType: data.propertyType,
          price: data.price,
          room: data.room,
          available: data.available,
          description: data.description,
          address: {
            id: data.address.id,
            street: data.address.street,
            city: data.address.city,
            postCode: data.address.postCode,
          },
          propertyImages: data.propertyImages,
        });
      })
      .catch((error) => {
        if (TokenManager.isAccessTokenExpired()) {
          LoginApi.renew().catch((error) => {
            if (error.message === "Request failed with status code 401") {
              navigate("/loginpage", {
                state: { from: window.location.pathname },
              });
            } else {
              console.error(error.message);
            }
          });
        } else {
          console.error(error);
        }
      });
  };

  console.log(property);
  const formatDate = (dateString: string) => {
    const options = { year: "numeric", month: "long", day: "numeric" };
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", options);
  };
  const handleApplyClick = () => {
    if (claims?.userId) {
      console.log(claims.id);
      setConfirmModal(true);
    } else {
      // Pass the current location to the login page
      navigate("/loginpage", { state: { from: window.location.pathname } });
    }
  };

  return (
    <div style={{ width: "150%", position: "relative", right: "100px" }}>
      <Row
        style={{
          borderRadius: "25px",
          padding: "50px 20px",
          backgroundColor: "#7EB6E0",
          color: "white",
          width: "100%", // Ensure the container takes 100% of the available width
          textAlign: "left",
        }}
      >
        <Col md={6}>
          <h6 className="display-5">{property.propertyType} for rent</h6>
          <h4 className="display-4">{property.address.street}</h4>
          <h6 className="display-5 mb-3">in {property.address.city}</h6>

          <p className="h5">Price: {property.price}</p>
          <p className="h5">Nr. of room: {property.room}</p>
          <p className="h5">Available: {formatDate(property.available)}</p>
          <p className="h5">Description: {property.description}</p>
        </Col>
        <Col md={6}>
          <div style={{ position: "absolute", zIndex: 1000 }}>
            <Row>
              <Col className="text-right mx-5">
                <Carousel controls={true} indicators={true}>
                  {property.propertyImages.length > 0 ? (
                    property.propertyImages.map((image, index) => (
                      <Carousel.Item key={index}>
                        <img
                          src={image.imageUrl}
                          alt={`Property Image ${index + 1}`}
                          className="img-fluid"
                          style={{ height: "250px", width: "250px" }}
                        />
                      </Carousel.Item>
                    ))
                  ) : (
                    <Carousel.Item>
                      <img
                        src={house} // Use the default image
                        alt="Default Property Image"
                        className="img-fluid"
                        style={{ height: "250px", width: "250px" }}
                      />
                    </Carousel.Item>
                  )}
                </Carousel>
              </Col>
            </Row>
          </div>
        </Col>
        <Row className="justify-content-end mt-3">
          <Col md={2} className="text-right">
            <Button variant="primary" size="lg" onClick={handleApplyClick}>
              Apply
            </Button>
          </Col>
        </Row>
      </Row>

      <ApplyForm
        confirmModal={confirmModal}
        setConfirmModal={setConfirmModal}
        property={property}
      />
      <Row style={{ marginTop: "75px", textAlign: "left" }}>
        <Col>
          <h4>Location:</h4>
        </Col>
      </Row>
      <Row>
        <Col>
          <PropertyDetailsMap address={property.address} />
        </Col>
      </Row>

      {claims?.userId && (
        <Row className="mt-5">
          <Col>
            <PropertyDetailOwner property={property} />
          </Col>
        </Row>
      )}
    </div>
  );
}
