
import {
    Badge,
    Button,
  } from "react-bootstrap";
import TokenManager from "../api/TokenManager";
import { useState } from "react";

export default function ChatItem({ chatItem, setSelectedChatId, setMessages, setSelectedChatName }: any) {
  console.log(chatItem.messages, 'GHJ')
  
  const [isHovered, setIsHovered] = useState(false);
  const claims = TokenManager.getClaims();
  const handleSelectChat = () =>{
    setSelectedChatId(chatItem.id)
    setMessages(chatItem.messages)
    if(claims?.roles.includes("HOMEOWNER")){
      const name = `${chatItem.homeSeeker.firstName} ${chatItem.homeSeeker.lastName}`
      setSelectedChatName(`${chatItem.homeSeeker.firstName} ${chatItem.homeSeeker.lastName}`)
    }
    if(claims?.roles.includes("HOME_SEEKER")){
      setSelectedChatName(`${chatItem.homeowner.firstName} ${chatItem.homeowner.lastName}`)
    }
   
    
  }
  const cardStyle = {
    textAlign : 'left', 
    color : "black", 
    width : '12.5rem',
    position : 'relative',
    right : '24px',
    border : '0px solid black',
    ...(isHovered && {
      boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)', // Adjust size and color
    }),
    // Add any additional styles you need
  };
  return (
    <div>
      <Button  
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      className="d-flex justify-content-between bg-white" style={cardStyle} onClick={handleSelectChat}>
        <div className="d-flex flex-row">
          <div>
            <Badge pill bg="success"></Badge>
          </div>
          <div className="pt-1">

            {claims?.roles.includes("HOMEOWNER") && 
            <p className="fw-bold mb-0">{chatItem.homeSeeker.firstName} {chatItem.homeSeeker.lastName}</p>}
            {claims?.roles.includes("HOME_SEEKER") && 
            <p className="fw-bold mb-0">{chatItem.homeowner.firstName} {chatItem.homeowner.lastName}</p>}
            
          </div>
        </div>
        {/* <div className="pt-1">
                                <p className="small text-muted mb-1">Just now</p>
                                <Badge pill bg="danger" className="float-end">3</Badge>
                              </div> */}
      </Button>
    </div>
  );
}
