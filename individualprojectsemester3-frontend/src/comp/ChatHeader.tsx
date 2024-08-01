import React, { useRef, useState } from "react";
import { Card, Button, Dropdown } from "react-bootstrap";

export default function ChatHeader({ selectedChatName, setSelectedChatId }: any) {
  const [showContextMenu, setShowContextMenu] = useState(false);
  const [contextMenuPosition, setContextMenuPosition] = useState({ x: 0, y: 0 });
  const contextMenuRef = useRef(null);

  const handleButtonClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setContextMenuPosition({
      x: e.currentTarget.getBoundingClientRect().right,
      y: e.currentTarget.getBoundingClientRect().top,
    });
    setShowContextMenu(true);
  };

  const handleCloseChat = () => {
    setSelectedChatId(0);
    console.log(selectedChatId, 'f');
    console.log('Clearing chat...');
    closeContextMenu();
  };

  const closeContextMenu = () => {
    setShowContextMenu(false);
  };

  return (
    <div>
      <Card
        className="chatHeader-card"
        style={{
          width: "30rem",
          height: "0rem",
          position: "relative",
          right: "22px",
        }}
      >
        <Card.Body style={{ position: "relative", bottom: "25px" }}>
          <Card.Title>{selectedChatName}</Card.Title>
        </Card.Body>
        <div
          style={{
            position: "absolute",
            top: "50%",
            right: "5px",
            transform: "translateY(-50%)",
          }}
        >
          <Button variant="link" className="p-0" onClick={handleButtonClick}>
            <span style={{ fontSize: "20px" }}>&#8942;</span>
          </Button>
        </div>
      </Card>
      {showContextMenu && (
        <div
          ref={contextMenuRef}
          style={{
            position: "fixed",
            top: contextMenuPosition.y,
            left: contextMenuPosition.x,
            zIndex: 1000, // Set a higher zIndex value
            background: "#fff",
            padding: "0.5rem",
            boxShadow: "0 2px 4px rgba(0, 0, 0, 0.2)",
            border: "1px solid black",
            borderRadius: "10px",
          }}
        >
          <Dropdown>
            <Dropdown.Item onClick={handleCloseChat}>Close Chat</Dropdown.Item>
          </Dropdown>
        </div>
      )}
    </div>
  );
}
