import React from 'react';
import userImage from '../assets/images/user (2).png';
import addImage from '../assets/images/add (2).png';
import propertyInsuranceImage from '../assets/images/property-insurance.png';
import applicationImage from '../assets/images/application.png';
import priceImage from '../assets/images/price.png';
import settingImage from '../assets/images/settings (1).png';
import houseSparkeImage from '../assets/images/houseSparkle.png';
import searchHouse from '../assets/images/search-house.png';
import logoutImage from '../assets/images/log-out.png';
import { NavLink, useNavigate } from 'react-router-dom';
import { HashLink } from 'react-router-hash-link';

import TokenManager from '../api/TokenManager';

export default function MenuItem(props: any) {
  const { menuItem } = props;
  const navigate = useNavigate();
  const claims = TokenManager.getClaims();
  const imagesList = {
    user: userImage,
    add: addImage,
    propertyInsurance: propertyInsuranceImage,
    application: applicationImage,
    price: priceImage,
    setting: settingImage,
    houseSparke: houseSparkeImage,
    search: searchHouse,
    logout: logoutImage
    // Add mappings for other images...
  };

  const imagePath = imagesList[menuItem.image];

  const handleClick = () => {
    const sectionId = menuItem.text.toLowerCase();
    const anchorLink = `${sectionId}Section`;

    const targetElement = document.getElementById(anchorLink);

    if (menuItem.text === 'Logout') {
      TokenManager.clear();
      navigate('/');
      alert()
    } else if (targetElement) {
      targetElement.scrollIntoView({ behavior: 'smooth' });
      navigate(menuItem.path);
    }
  };

  return (
    <HashLink to={menuItem.path} style={{ color: '#7EB6E0' }} onClick={handleClick}>
      <img
        src={imagePath}
        style={{ width: '250px', height: '250px',  position : 'relative',left: imagePath === logoutImage ? '30px' : '0' }}
        alt={menuItem.text}
      />
      <p style={claims?.roles?.includes('HOME_SEEKER') ? { marginLeft: '50px' } : {}}>
        {menuItem.text}
      </p>
    </HashLink>
  );
  
}
