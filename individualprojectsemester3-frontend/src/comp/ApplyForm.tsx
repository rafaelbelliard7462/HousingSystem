import React, { useEffect, useState } from "react";
import CustomModal from "./CustomModal";
import { Col, Form, Row } from "react-bootstrap";
import ApplicationApi from "../api/ApplicationApi";
import LoginApi from "../api/LoginApi";
import TokenManager from "../api/TokenManager";
import UserApi from "../api/UserApi";
import { useNavigate } from "react-router-dom";

export default function ApplyForm({
  confirmModal,
  setConfirmModal,
  property,
}: any) {
  const [description, setDescription] = useState("");
  const navigate = useNavigate();
  // const [application, setApplication] = useState({
  //     id: 0,
  //     status: 'PENDING',
  //     description: '',
  //     email: '',
  //     role: 0
  //     password: '',
  //   });
  const [user, setUser] = useState({
    id: 0,
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    email: "",
    password: "",
    role: 0,
  });
  useEffect(() => {
    const claims = TokenManager.getClaims();

    if (claims?.userId) {
      UserApi.getUserById(claims.userId)
        .then((data) => {
          setUser({
            id: data.id,
            firstName: data.firstName,
            lastName: data.lastName,
            dateOfBirth: data.dateOfBirth,
            email: data.email,
            password: data.password,
            role: data.role,
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
            console.error( error);
          }
        });
    }
  }, []);

  const handleAddApplication = () => {
    if (!description) {
      alert('Please provide a reason for applying before submitting.');
      return;
    }
    const newApplication = {
      user: user,
      property: property,
      appliedDate: new Date().toISOString(),
      status: "PENDING",
      description: description,
    };
    ApplicationApi.postApplication(newApplication)
      .then((reponse) => {
        alert("Application sent!");
        setConfirmModal(false);
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
          alert("Failed to send application!");
        }
      });
  };

  return (
    <div>
      <CustomModal
        title={"Apply for property"}
        isOpen={confirmModal}
        toggle={() => {
          setConfirmModal(false);
        }}
        onCancel={() => {
          setConfirmModal(false);
        }}
        cancelText="Cancel"
        onSubmit={handleAddApplication}
        submitText="OK"
      >
        Do you want to apply for this property?
        <Form as={Row} controlId="description ">
          <Form.Label className="mt-3" column lg={3}>
            Reason why:
          </Form.Label>
          <Col>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              style={{ height: "20rem", width: "29rem" }}
            />
          </Col>
        </Form>
      </CustomModal>
    </div>
  );
}
