import React, { useEffect, useState } from 'react';
import { Row, Col } from 'react-bootstrap';
import PropertyApi from '../api/PropertyApi';
import PropertyItem from './PropertyItem';

export default function PropertyList({ properties }) {
  const chunkArray = (array, chunkSize) => {
    const result = [];
    for (let i = 0; i < array.length; i += chunkSize) {
      result.push(array.slice(i, i + chunkSize));
    }
    return result;
  };

  return (
    <div className='PropertyList'>
      {properties.length === 0 ? (
        <h3>No Property found</h3>
      ) : (
        chunkArray(properties, 3).map((row, rowIndex) => (
          <Row className='list' key={rowIndex} style={{ gap: '60px' }}>
            {row.map((property, index) => (
              <Col className='listItem mx-4 mt-4' key={index} md={3}>
                <PropertyItem key={property.id} propertyItem={property} />
              </Col>
            ))}
          </Row>
        ))
      )}
    </div>
  );
}
