import React, { useState } from "react";
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from "reactstrap";
//import DeleteConfirmation from "./DeleteConfirmation";

export default function CustomModal({
  title = "Title",
  isOpen,
  toggle,
  onCancel,
  cancelText,
  onDelete,
  deleteText,
  children,
}) {
  
  return (
    <div>
<Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>{title}</ModalHeader>
      <ModalBody>{children}</ModalBody>
      <ModalFooter>
        
        {onCancel && (
          <Button color="secondary" onClick={onCancel}>
            {cancelText || "Cancel"}
          </Button>
        )}
        {onDelete && (
            deleteText === "Delete" ?
          <Button  color="danger" onClick={() => {
            onDelete();
          }} >
            { deleteText || "Delete"}
          </Button>
          :
          <Button  color="primary" onClick={() => {
            onDelete();
          }} >
            { deleteText || "Delete"}
          </Button>
        )}
      </ModalFooter>
    </Modal>
    </div>
    
  );
}
