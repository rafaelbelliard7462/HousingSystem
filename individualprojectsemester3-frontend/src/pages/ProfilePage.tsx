import React from 'react';
import Profile from '../comp/Profile';
import Preference from '../comp/Preference';
import TokenManager from '../api/TokenManager';
import { Container } from 'react-bootstrap';

export default function ProfilePage(props: any) {
  const claims = TokenManager.getClaims();

  // Common style object
  const containerStyle = {
    borderRadius: '25px',
    padding: '50px 20px',
    backgroundColor: '#7EB6E0',
    color: 'white',
    maxWidth: '800px',
    width: '700px', // Ensure the container takes 100% of the available width
    marginTop: '100px',
    marginBottom: '100px',
  };

  return (
    <>
      <Container
          className='Profile'
          style={containerStyle}>
          <h1>Profile</h1>
          <Profile id={props.id} />
        </Container>
      {claims?.roles.includes('HOME_SEEKER') && (
        <Container
        id="preferenceSection"
        className='Preference'
        style={containerStyle}>
          <h1>Preference</h1>
          <Preference />
        </Container>
        
      )}

      
    </>
  );
}
