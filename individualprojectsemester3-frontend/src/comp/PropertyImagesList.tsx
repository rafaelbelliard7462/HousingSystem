import React from "react";
import PropertyImageItem from "./PropertyImageItem";

export default function PropertyImagesList({ propertyImages, getInfo }) {
  return (
    <div
      className="ApplicationList"
      data-mdb-perfect-scrollbar="true"
      style={{
        position: "relative",
        height: "175px",
        overflowY: "scroll",
        width: "50%",
        border: "1px solid white",
      }}
    >
      {propertyImages.map((propertyImage, index) => (
        <div className="listItem mx-4 mt-4" key={index}>
          <PropertyImageItem
            key={propertyImage.id}
            propertyImageItem={propertyImage}
            getInfo={getInfo}
          />
        </div>
      ))}
     
    </div>
  );
}
