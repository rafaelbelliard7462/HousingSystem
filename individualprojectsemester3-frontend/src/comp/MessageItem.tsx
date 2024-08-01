import React, { useState, useRef, useEffect } from "react";
import TokenManager from "../api/TokenManager";
import ChatAndMessageApi from "../api/ChatAndMessageApi";
import LoginApi from "../api/LoginApi";
import { useNavigate } from "react-router-dom";

export default function MessageItem({ messageItem, setSelectedMessageId, stompClient }: any) {
  const claims = TokenManager.getClaims();
  const isCurrentUser = claims.userId === messageItem.sender.id;

  


  


  return (
    <div>
      <div className={`d-flex flex-row justify-content-${isCurrentUser ? "end" : "start"}`}>
        <div>
          <p
            className={`small p-2 ${isCurrentUser ? "me-3 text-white bg-primary" : "ms-3"} mb-1 rounded-3`}
            style={{ backgroundColor: isCurrentUser ? "transparent" : "#f5f6f7" }}
          >
            {messageItem.content}
          </p>
        </div>
      </div>

    </div>
  );
}
