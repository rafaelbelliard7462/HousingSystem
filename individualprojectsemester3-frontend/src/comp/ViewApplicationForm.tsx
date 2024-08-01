import React, { useEffect, useState } from "react";
import CustomModal from "./CustomModal";
import { Card, Col, Form, FormGroup, Row } from "react-bootstrap";
import house from "../assets/images/house.png";
import ApplicationApi from "../api/ApplicationApi";
import LoginApi from "../api/LoginApi";
import TokenManager from "../api/TokenManager";
import UserApi from "../api/UserApi";
import { useNavigate } from "react-router-dom";
import { Label, Input } from "reactstrap";

export default function ViewApplicationForm({
  confirmModal,
  setConfirmModal,
  application,
  refreshPropertyList,
}: any) {

  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();
const claims = TokenManager.getClaims();
  const formatDate = (dateString) => {
    const options = { year: "numeric", month: "long", day: "numeric" };
    const date = new Date(dateString);
    return date.toLocaleDateString("en-US", options);
  };


  const handleAcceptApplication = () => {
    const updateApplication = {
      id: application.id,
      appliedDate: application.appliedDate,
      status: "ACCEPTED",
      description: application.description,
    };

    ApplicationApi.updateApplication(updateApplication)
      .then((reponse) => {
        alert("Application accepted!");
        setConfirmModal(false);
        refreshPropertyList();
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
          console.error(error);
          alert("fail");
        }
      });
  };

  const handleDeclineApplication = () => {
    const updateApplication = {
      id: application.id,
      appliedDate: application.appliedDate,
      status: "DECLINED",
      description: application.description,
    };

    ApplicationApi.updateApplication(updateApplication)
      .then((reponse) => {
        alert("Application declined!");
        setConfirmModal(false);
        refreshPropertyList();
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
          console.error(error);
          alert("fail");
        }
      });
      
  };

  return (
    <div>
      <CustomModal
        title={"View Application Form"}
        isOpen={confirmModal}
        toggle={() => {
          setConfirmModal(false);
        }}
        onCancel={() => {
          setConfirmModal(false);
        }}
        cancelText="Cancel"
        onSubmit={claims?.roles.includes("HOMEOWNER") && handleAcceptApplication}
        submitText="Accept"
        onDelete={claims?.roles.includes("HOMEOWNER") && handleDeclineApplication}
        deleteText={"Decline"}
        
      >
        <Form
          className="property-form"
          onMouseEnter={() => setIsHovered(true)}
          onMouseLeave={() => setIsHovered(false)}
        >
          {/* Property Details Header */}
          <h5>Property Details</h5>

          <FormGroup>
            <Label>Street Address</Label>
            <Input
              type="text"
              value={application.property.address.street}
              readOnly
            />
          </FormGroup>

          <FormGroup>
            <Label>City and Property Type</Label>
            <Input
              type="text"
              value={`${application.property.address.city} - ${application.property.propertyType}`}
              readOnly
            />
          </FormGroup>

          <FormGroup>
            <Label>Price (€)</Label>
            <Input
              type="text"
              value={`€${application.property.price}`}
              readOnly
            />
          </FormGroup>

          <FormGroup>
            <Label>Available Date</Label>
            <Input
              type="text"
              value={`Available: ${formatDate(application.property.available)}`}
              readOnly
            />
          </FormGroup>

          {/* Applicant Details Header */}
          {claims?.roles.includes('HOME_SEEKER') ? (
  <>
    <h5>Homeowner Details</h5>

    <FormGroup>
      <Label>Homeowner Name</Label>
      <Input
        type="text"
        value={`${application.property.user.firstName} ${application.property.user.lastName}`}
        readOnly
      />
    </FormGroup>

    <FormGroup>
      <Label>Homeowner Email</Label>
      <Input type="text" value={application.property.user.email} readOnly />
    </FormGroup>
  </>
) : (
  <>
    <h5>Applicant Details</h5>

    <FormGroup>
      <Label>Applicant Name</Label>
      <Input
        type="text"
        value={`${application.user.firstName} ${application.user.lastName}`}
        readOnly
      />
    </FormGroup>

    <FormGroup>
      <Label>Applicant Email</Label>
      <Input type="text" value={application.user.email} readOnly />
    </FormGroup>

    <FormGroup>
      <Label>Applicant Description</Label>
      <Input
        type="textarea"
        value={application.description}
        readOnly
        style={{ height: "20rem", width: "29rem" }}
      />
    </FormGroup>
  </>
)}

        </Form>
      </CustomModal>
    </div>
  );
}
