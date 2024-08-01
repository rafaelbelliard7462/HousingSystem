import React, { useState, useEffect, useRef } from 'react';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import ChatHeader from './ChatHeader';

export default function MessagePanel({ messages, selectedChatId, selectedChatName, fetchChats, setSelectedChatId, setSelectedMessageId, stompClient }: any) {
  

  return (
    <div style={{ width: '30rem' }} >
      {selectedChatId === 0 ? (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
          <div style={{ width: '30rem' }}>Click on a chat to see messages.</div>
        </div>
      ) : (
        <>
          <ChatHeader selectedChatName={selectedChatName} selectedChatId={selectedChatId}  setSelectedChatId={setSelectedChatId} />
          <MessageList messageList={messages} setSelectedMessageId={setSelectedMessageId} stompClient={stompClient}/>
          <MessageInput selectedChatId={selectedChatId} messages={messages} fetchChats={fetchChats} />

          
        </>
      )}
    </div>
  );
}
