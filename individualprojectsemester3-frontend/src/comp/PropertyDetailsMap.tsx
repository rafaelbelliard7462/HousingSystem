import React, { useEffect, useState } from 'react';
import { GoogleMap, Circle, Marker } from '@react-google-maps/api';
import axios from 'axios';

const mapContainerStyle = {
  width: '100%',
  height: '300px',
};

const PropertyDetailsMap = ({ address }) => {
  const [lat, setLat] = useState(0);
  const [lng, setLng] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (address && address.street && address.city && address.postCode) {
      setLoading(true);
      getInfo(address);
    }
  }, [address]);

  const getInfo = (address) => {
    axios
      .get(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${address.street},${address.city},${address.postCode}&key=AIzaSyCEEl2sRTv-mbRjbpq17CfwolsNo_iATtg`
      )
      .then((response) => {
        const { lat, lng } = response.data.results[0].geometry.location;

        console.log('Coordinates:', lat, lng);

        setLat(lat);
        setLng(lng);
      })
      .catch((error) => {
        console.error('Error fetching coordinates:', error);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <GoogleMap mapContainerStyle={mapContainerStyle} center={{ lat, lng }} zoom={15} >
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <Circle
            center={{ lat, lng }}
            radius={50} // radius in meters
            options={{
              fillColor: '#7EB6E0',
              fillOpacity: 0.3,
              strokeWeight: 2,
              strokeColor: '#7EB6E0',
              zIndex: 1,
            }}
          />
          
          <Marker
            position={{ lat, lng }}
          />
        </>
      )}
    </GoogleMap>
  );
};

export default PropertyDetailsMap;
