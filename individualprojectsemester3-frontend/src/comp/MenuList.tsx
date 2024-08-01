import React from 'react';
import MenuItem from './MenuItem';

import { Row, Col } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import TokenManager from '../api/TokenManager';
//import './MenuList.css';
 export default function MenuList(props) {
  const { menuItems } = props;
  const isEven = props.menuList.length % 2 === 0;

  const claims = TokenManager.getClaims();
  // Function to chunk the array into rows or halves based on size
  const chunkArray = (array, size) => {
    const chunkedArray = [];
    if (size === 2) {
      // Split into halves
      const halfSize = array.length / 2;
      chunkedArray.push(array.slice(0, halfSize), array.slice(halfSize));
    } else {
      // Split into rows of 'size' length
      for (let i = 0; i < array.length; i += size) {
        chunkedArray.push(array.slice(i, i + size));
      }
    }
    return chunkedArray;
  };

  // Determine the size based on whether the length is even or odd
  const size = isEven ? 2 : 3;
  
  // Chunk the menuItems based on the size
  const chunks = chunkArray(props.menuList, size);

  return (
    <div className='MenuList'>
    {chunks.map((chunk, chunkIndex) => (
      <Row className='list' key={chunkIndex} style={claims?.roles?.includes('HOME_SEEKER')? {gap: '100px'}:{}}>
        {chunk.map((menuItem, index) => (
          <Col className='listItem' key={index} xs={12} md={4}>
            <MenuItem key={menuItem.id} menuItem={menuItem} />
          </Col>
        ))}
      </Row>
    ))}
  </div>
  );
}

