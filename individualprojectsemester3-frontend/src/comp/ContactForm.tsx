import { useState } from "react";
import CustomModal from "./CustomModal";
import { Col, Form, Row } from "react-bootstrap";
import TokenManager from "../api/TokenManager";
import ChatAndMessageApi from "../api/ChatAndMessageApi";
import LoginApi from "../api/LoginApi";
import { useNavigate } from "react-router-dom";

export default function ContactForm({
  confirmModal,
  setConfirmModal,
  property,
}: any) {
  const [message, setMessage] = useState("");
  const claims = TokenManager.getClaims();
  const navigate = useNavigate();

  const createChat = () => {
    const newChat = {
      homeSeekerId: claims.userId,
      homeownerId: property.user.id,
    };
    let chatIdFromServer = null;

    if (newChat) {
      return ChatAndMessageApi.postChat(newChat)
        .then((response) => {
          chatIdFromServer = response.data.chatId;
          console.log(response, "ee");
          //setChatId(chatIdFromServer);
          return chatIdFromServer;
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
    }
  };

  const handleSendMessage = async () => {
    if (claims?.userId) {
      const chatIdFromServer = await createChat();

      let address =
        property.address.street +
        ", " +
        property.address.city +
        ", " +
        property.address.postCode;
      const newMessage = {
        senderId: claims.userId,
        chatId: chatIdFromServer,
        content: message + `\nFor property ${address}`,
      };

      ChatAndMessageApi.postMessage(newMessage)
        .then((response) => {
          console.log(response, "he");
          alert("Message sent!");
          setConfirmModal(false);

          // console.log(chatId);
        })
        .catch((error) => console.error(error));
    }
  };

  return (
    <div>
      <CustomModal
        title={"Contact Form"}
        isOpen={confirmModal}
        toggle={() => {
          setConfirmModal(false);
        }}
        onCancel={() => {
          setConfirmModal(false);
        }}
        cancelText="Cancel"
        onSubmit={handleSendMessage}
        submitText="Send message"
      >
        Do you want to contact {property.user.firstName}{" "}
        {property.user.lastName}?
        <Form as={Row} controlId="Message ">
          <Form.Label className="mt-3" column lg={3}>
            Message:
          </Form.Label>
          <Col>
            <Form.Control
              as="textarea"
              rows={3}
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              style={{ height: "20rem", width: "29rem" }}
            />
          </Col>
        </Form>
      </CustomModal>
    </div>
  );
}
