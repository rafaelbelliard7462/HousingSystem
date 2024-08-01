import ChatItem from "./ChatItem";

export default function ChatList({ chatList, setSelectedChatId, setMessages, setSelectedChatName }: any) {
  console.log(chatList)
  return (
    <div className="ChatList">
      <div
        
        style={{
          position: "relative",
          height: "400px",
          overflowY: "scroll",
        }}
      >
        {chatList.map((chat: any, index: number) => (
          <div className="listItem mx-4 mt-4" key={index}>
            <ChatItem key={chat.id} chatItem={chat} setSelectedChatId={setSelectedChatId}  setMessages={setMessages} setSelectedChatName={setSelectedChatName}/>
          </div>
        ))}
      </div>
    </div>
  );
}
