import React, { useRef, useEffect } from 'react';
import MessageItem from './MessageItem';

export default function MessageList({ messageList,setSelectedMessageId, stompClient }: any) {
  const scrollableRef = useRef(null);

  useEffect(() => {
    // Scroll to the bottom when the component mounts
    if (scrollableRef.current) {
      scrollableRef.current.scrollTop = scrollableRef.current.scrollHeight;
    }
  }, [messageList]);

  return (
    <div className="MessageList">
      <div
        ref={scrollableRef}
        data-mdb-perfect-scrollbar="true"
        style={{
          position: "relative",
          height: "400px",
          overflowY: "scroll",
        }}
      >
        {messageList.map((message, index) => (
          <div className="listItem mx-4 mt-4" key={index}>
            <MessageItem key={message.id} messageItem={message} setSelectedMessageId={setSelectedMessageId} stompClient={stompClient}/>
          </div>
        ))}
      </div>
    </div>
  );
}
