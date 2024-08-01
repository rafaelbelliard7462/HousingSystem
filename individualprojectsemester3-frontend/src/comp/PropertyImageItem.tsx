import React, { useState } from 'react'
import PropertyImageApi from '../api/PropertyImageApi';
import { useNavigate } from 'react-router-dom';
import TokenManager from '../api/TokenManager';
import LoginApi from '../api/LoginApi';

export default function PropertyImageItem({propertyImageItem, getInfo}: any) {
    const [isHovered, setIsHovered] = useState(false);;
    const navigate = useNavigate()
    const handleRemove = (e) =>{
        e.preventDefault();
        PropertyImageApi.deletePropertyImage(propertyImageItem.id)
        .then((response)=>{console.log(response);
            getInfo(propertyImageItem.property.id)})
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
    }
    return (
      <div
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        <div>{propertyImageItem.imageName}</div>
        {isHovered && (
          <button className='btn btn-danger' style={{width: "25%", height: "10%", fontSize: "14px"}} onClick={handleRemove}>Remove</button>
        )}
      </div>
    )
}
