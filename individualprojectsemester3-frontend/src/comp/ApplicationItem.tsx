import React, { useEffect, useState } from "react";
import { Card } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import TokenManager from "../api/TokenManager";
import ViewApplicationForm from "./ViewApplicationForm";
import { Button } from "reactstrap";
import ApplicationApi from "../api/ApplicationApi";
import LoginApi from "../api/LoginApi";
import UserApi from "../api/UserApi";
import ConfirmationCustomModel from "./ConfirmationCustomModel";

export default function ApplicationItem({
  applicationItem,
  refreshPropertyList,
}: any) {
  const [isHovered, setIsHovered] = useState(false);

  const [confirmModal, setConfirmModal] = useState(false);
  const [delteConfirmModal, setDeleteConfirmModal] = useState(false);
  const claims = TokenManager.getClaims();
  const navigate = useNavigate();

  const cardStyle = {
    width: "40rem",
    cursor: "pointer",
    height: "8rem",
    transition: "box-shadow 0.3s ease-in-out",
    textAlign: "left",
    ...(isHovered && {
      boxShadow: "0 8px 16px rgba(0, 0, 0, 0.2)",
    }),
  };
  const handleNavigate = () => {
    //   const claims = TokenManager.getClaims();
    //   if(claims?.userId && claims?.roles.includes('HOMEOWNER')){
    //       navigate(`/menu/manageProperty/${propertyItem.id}`)
    //   }
    //   else{
    //       navigate(`/propertInfo/${propertyItem.id}`)
    //   }
  };
  const handleButtonClick = (e) => {
   // e.stopPropagation(); // Stop the event from propagating to the card
    ApplicationApi.deleteApplication(applicationItem.id)
      .then((reponse) => {
        alert("Application deleted!");
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
          console.error( error);
          alert("Failed to delete application!");
        }
      });
    // setConfirmModal(false);
    // Add any other button click logic here
  };

  return (
    <div>
      <Card
        style={cardStyle}
        className="property-card"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
        onClick={() => setConfirmModal(true)}
      >
        {/* <Card.Img variant="top" src={house} height={'275rem'} /> */}
        <Card.Body style={{ position: "relative", top: "-35px" }}>
          <Card.Title>
            {applicationItem.property.address.street}{" "}
            {applicationItem.property.address.city} -{" "}
            {applicationItem.property.propertyType}
          </Card.Title>
          <Card.Text>
            Status: {applicationItem.status}
            {isHovered && applicationItem.status === "DECLINED" && (
              <Button
                color="danger"
                onClick={(e) => {setDeleteConfirmModal(true)
                                e.stopPropagation(); }}
                style={{ position: "relative", right: "-350px" }}
              >
                {" "}
                Remove{" "}
              </Button>
            )}
            {isHovered && applicationItem.status ===  "PENDING" && claims?.roles.includes("HOME_SEEKER") && (
              <Button
                color="danger"
                onClick={(e) => {setDeleteConfirmModal(true)
                                e.stopPropagation(); }}
                style={{ position: "relative", right: "-350px" }}
              >
                {" "}
                Remove{" "}
              </Button>
            )}
          </Card.Text>
          <Card.Text>
            {claims?.userId && claims.roles.includes("HOME_SEEKER")
              ? `Homeoner name: ${applicationItem.property.user.firstName} ${applicationItem.property.user.lastName} `
              : `Applicant name: ${applicationItem.user.firstName} ${applicationItem.user.lastName}`}
          </Card.Text>
        </Card.Body>
      </Card>
      <ViewApplicationForm
        confirmModal={confirmModal}
        setConfirmModal={setConfirmModal}
        application={applicationItem}
        refreshPropertyList={refreshPropertyList}
      />
     <ConfirmationCustomModel
     title={'Delete Application'}
     isOpen={delteConfirmModal}
     toggle={() => {
       setDeleteConfirmModal(false);
     }}
     onCancel={() => {
       setDeleteConfirmModal(false);
     }}
     cancelText="Cancel"
     onDelete={handleButtonClick}
     deleteText="Delete"
     >
      Are you sure you want to delete this application?
     </ConfirmationCustomModel>
    </div>
  );
}
