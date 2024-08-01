import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Form, Button } from "react-bootstrap";
import "bootstrap-icons/font/bootstrap-icons.css";
import ChatList from "../comp/ChatList";
import MessagePanel from "../comp/MessagePanel";
import { Client } from "@stomp/stompjs";
import TokenManager from "../api/TokenManager";
import axios from "axios";
import ChatAndMessageApi from "../api/ChatAndMessageApi";
import LoginApi from "../api/LoginApi";
import { useNavigate } from "react-router-dom";
function ChatPage() {
  const [chats, setChats] = useState([]);
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState();
  const [selectedChatId, setSelectedChatId] = useState(0);
  const [selectedMessageId, setSelectedMessageId] = useState(0);
  const [selectedChatName, setSelectedChatName] = useState('');
  const navigate = useNavigate();
  const claims = TokenManager.getClaims();
  useEffect(() => {
    setup();
  }, [selectedChatId, selectedMessageId]);

 
  
  const setup = () => {
    if (claims?.sub) {
      const newStompClient = new Client({
        brokerURL: "ws://localhost:8080/ws",
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });
   
      newStompClient.onConnect = () => {

        console.log(selectedMessageId, "fghj");
        newStompClient.subscribe(`/user/${selectedMessageId.toString()}/queue/messages/delete`, (data) => {
          const messageId = data.body;
          console.log(messageId, "fghj");
          setMessages((prevMessages) => {
            // Use filter to remove the message with the matching id
            const updatedMessages = prevMessages.filter((m) => m.id !== messageId);
            return updatedMessages;
          });
        });

        newStompClient.subscribe(`/user/${claims.sub}/queue/chat`, (data) => {
          const chat = JSON.parse(data.body);
          console.log(chat, "44");
          setChats((prevChats) => {
            if (!prevChats.some((c) => c.id === chat.id)) {
              return [...prevChats, chat];
            }
            setSelectedChatId(chat.id);
            return prevChats;
          });
        });
        console.log(selectedChatId);
        newStompClient.subscribe(
          `/user/${selectedChatId.toString()}/queue/messages`,
          (data) => {
            const message = JSON.parse(data.body);
            console.log(message, "12");
            setMessages((prevMessages) => {
              if (!prevMessages.some((m) => m.id === message.id)) {
                return [...prevMessages, message];
              }
            });
          }
        );
      };
   
      newStompClient.onStompError = (frame) => {
        console.log("Broker reported error: ", frame.headers["message"]);
        console.log("Additional details: ", frame.body);
      };
   
      newStompClient.activate();
   
      // Subscribe to the delete topic here
      
   
      setStompClient(() => newStompClient);
      if (claims?.userId) {
        fetchChats(claims.userId);
      }
    }
   };
   
  // useEffect(() => {
  //   console.log(selectedMessageId, 'ghjkl');
  //   if (claims?.sub) {
  //     const newStompClient = new Client({
  //       brokerURL: "ws://localhost:8080/ws",
  //       reconnectDelay: 5000,
  //       heartbeatIncoming: 4000,
  //       heartbeatOutgoing: 4000,
  //     });
  //     newStompClient.onConnect = () => {
  //       console.log(selectedMessageId, 'ghjkl');
       
  //     };
  //     newStompClient.activate();
  //   }
  // }, [selectedMessageId]);
  const fetchChats =  (userId) => {
    if (claims?.roles.includes("HOMEOWNER")) {
       ChatAndMessageApi.getChatsByHomeownerId(userId)
        .then((response) => {
          setChats(response);
          //setMessages(response.messages)
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
            console.error(error.message);
          }
        });
    }
    if (claims?.roles.includes("HOME_SEEKER")) {
       ChatAndMessageApi.getChatsByHomeSeekerId(userId)
        .then((response) => {
          setChats(response);
          console.log(response, "ii");
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
            console.error(error.message);
          }
        });
    }
  };

  return (
    <Container className="py-5">
      <Row>
        <Col md={12}>
          <Card id="chat3" style={{ borderRadius: "15px" }}>
            <Card.Body>
              <Row>
                <Col md={6} lg={5} xl={4} className="mb-4 mb-md-0">
                  <div className="p-3">
                    <Form.Group className="rounded mb-3">
                      <Form.Control
                        type="search"
                        className="form-control rounded"
                        placeholder="Search"
                        aria-label="Search"
                        aria-describedby="search-addon"
                      />
                      <span
                        className="input-group-text border-0"
                        id="search-addon"
                      >
                        <i className="fas fa-search"></i>
                      </span>
                    </Form.Group>
                    <ChatList
                      chatList={chats}
                      setSelectedChatId={setSelectedChatId}
                      setSelectedChatName={setSelectedChatName}
                      setMessages={setMessages}
                    />
                  </div>
                </Col>
                <Col md={6} lg={7} xl={8} className="pt-3 pe-3" >
                  <MessagePanel
                    messages={messages}
                    setMessages={setMessages}
                    selectedChatId={selectedChatId}
                    fetchChats={fetchChats}
                    selectedChatName={selectedChatName}
                    setSelectedChatId={setSelectedChatId}
                    setSelectedMessageId={setSelectedMessageId}
                    stompClient={stompClient}
                    
                  />
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default ChatPage;
