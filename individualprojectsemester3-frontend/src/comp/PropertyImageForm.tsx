import React, { useState, useEffect } from 'react';
import PropertyImageApi from '../api/PropertyImageApi';
import LoginApi from '../api/LoginApi';
import TokenManager from '../api/TokenManager';
import { useNavigate } from 'react-router-dom';

export default function PropertyImageForm({ propertyId, getInfo }: any) {
  const navigate = useNavigate();
  const [selectedImage, setSelectedImage] = useState(null);
  const [propertyImage, setPropertyImage] = useState({
    propertyId: propertyId,
    imageName: '',
  });

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    setSelectedImage(file);
  };

  useEffect(() => {
    if (selectedImage) {
      setPropertyImage((prev) => ({
        ...prev,
        imageName: selectedImage.name,
      }));
    }
  }, [selectedImage]);

  const handleUpload = () => {
    const formData = new FormData();
    
    formData.append(
      'request',
      new Blob([JSON.stringify(propertyImage)], { type: 'application/json' })
    );

    if (selectedImage) {
      formData.append('file', selectedImage);
      console.log('Uploading image:', selectedImage);
      
      PropertyImageApi.postPropertyImage(formData)
        .then((response) => {
          console.log(response, 'fef');
          // Clear the input field
          document.getElementById('imageInput').value = '';
          // Reset the selectedImage state
          setSelectedImage(null);
          getInfo(propertyId)
        })
        .catch((error) => {
          if (TokenManager.isAccessTokenExpired()) {
            LoginApi.renew()
              .catch((error) => {
                if (error.message === 'Request failed with status code 401') {
                  navigate('/loginpage', {
                    state: { from: window.location.pathname },
                  });
                } else {
                  console.error(error.message);
                }
              });
          } else {
            console.error(error);
          }
        });
    } else {
      console.log('No image selected');
    }
  };

  return (
    <div className="my-2"style={{fontSize: "14px"}}>
      <input
        id="imageInput"
        type="file"
        accept="image/*"
        onChange={handleImageChange}
      />
      <button onClick={handleUpload} style={{width : '17%'}}>Upload Image</button>
    </div>
  );
}
