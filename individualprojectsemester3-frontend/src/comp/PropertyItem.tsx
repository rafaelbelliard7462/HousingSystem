import React, { useState } from 'react';
import { Card, Carousel } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import house from '../assets/images/house.png';
import TokenManager from '../api/TokenManager';

export default function PropertyItem({ propertyItem }) {
  const [isHovered, setIsHovered] = useState(false);
  const navigate = useNavigate();

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', options);
  };

  const cardStyle = {
    width: '25rem',
    cursor: 'pointer',
    height: '30rem',
    transition: 'box-shadow 0.3s ease-in-out',
    textAlign: 'left',
    ...(isHovered && {
      boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)',
    }),
  };

  const handleNavigate = () => {
    const claims = TokenManager.getClaims();
    if (claims?.userId && claims?.roles.includes('HOMEOWNER')) {
      navigate(`/menu/manageProperty/${propertyItem.id}`);
    } else {
      navigate(`/propertyInfo/${propertyItem.id}`);
    }
  };

  return (
    <Card
      style={cardStyle}
      className="property-card"
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={handleNavigate}
    >
      {propertyItem.propertyImages.length > 0 ? (
        <Carousel controls={false} indicators={false}>
          {propertyItem.propertyImages.map((image, index) => (
            <Carousel.Item key={index}>
              <img className="d-block w-100" src={image.imageUrl} alt={`Property Image ${index + 1}`} height={'275rem'} />
            </Carousel.Item>
          ))}
        </Carousel>
      ) : (
        <img src={house} alt="Default Property Image" className="d-block w-100" height={'275rem'} />
      )}

      <Card.Body>
        <Card.Title>{propertyItem.address.street}</Card.Title>
        <Card.Text>
          {propertyItem.address.city} - {propertyItem.propertyType}
        </Card.Text>
        <Card.Text>€{propertyItem.price}</Card.Text>
        <Card.Text>Available: {formatDate(propertyItem.available)}</Card.Text>
      </Card.Body>
    </Card>
  );
}
