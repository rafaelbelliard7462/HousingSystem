import React, { useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { Form, Button } from "react-bootstrap";
import TokenManager from '../api/TokenManager';
import ChatAndMessageApi from '../api/ChatAndMessageApi';
import LoginApi from '../api/LoginApi';
import { useNavigate } from 'react-router-dom';

export default function MessageInput({ selectedChatId, messages, fetchChats }: any) {
  const [message, setMessage] = useState("");
  const [isInputEmpty, setIsInputEmpty] = useState(true);
  const navigate = useNavigate();
  const claims = TokenManager.getClaims();

  const handleSendMessage = () => {
    if (claims?.userId && !isInputEmpty) {
      const newMessage = {
        senderId: claims.userId,
        chatId: selectedChatId,
        content: message,
      };
      ChatAndMessageApi.postMessage(newMessage)
        .then((response) => {
          setMessage(""); // Clear the input after sending the message
          fetchChats(claims.userId);
          console.log(response, 'he');
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
          }
        });
    }
  };

  return (
    <div className="text-muted d-flex justify-content-start align-items-center pe-3 pt-3 mt-2">
      <Form.Control
        type="text"
        className="form-control form-control-lg"
        id="exampleFormControlInput2"
        placeholder="Type message"
        value={message}
        onChange={(e) => {
          setMessage(e.target.value);
          setIsInputEmpty(e.target.value === ""); // Update the state based on input emptiness
        }}
      />
      <Button variant="link" className="ms-3" onClick={handleSendMessage} disabled={isInputEmpty}>
        <FontAwesomeIcon icon={faPaperPlane} />
      </Button>
    </div>
  );
}
