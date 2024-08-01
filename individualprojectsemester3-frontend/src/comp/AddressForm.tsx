import React, { useEffect, useState } from 'react';
import PlacesAutocomplete, {
  geocodeByAddress,
  getLatLng,
} from 'react-places-autocomplete';
import { Form, Row, Col } from 'react-bootstrap';

const AddressAutocomplete = ({ addressObj, setAddress, title, edit }: any) => {
  const [address, setAddressInternal] = useState('');
  const [street, setStreet] = useState('');
  const [city, setCity] = useState('');
  const [postalCode, setPostalCode] = useState('');

useEffect(()=>{
  if(title === "Edit" && addressObj && addressObj.street && addressObj.city && addressObj.postCode){
    setStreet(addressObj.street);
    setCity(addressObj.city);
    setPostalCode(addressObj.postCode)
  }
}, [addressObj])
  const handleChange = (newAddress : string) => {
    setAddressInternal(newAddress);
  };

  const handleSelect = (newAddress : string) => {
    geocodeByAddress(newAddress)
      .then((results) => {
        const addressComponents = results[0].address_components;

        const streetName = getAddressComponent(addressComponents, 'route');
        const houseNumber = getAddressComponent(addressComponents, 'street_number');

        const formattedStreet = `${streetName} ${houseNumber}`;
        setStreet(formattedStreet);
        setCity(getAddressComponent(addressComponents, 'locality'));
        setPostalCode(getAddressComponent(addressComponents, 'postal_code'));

        setAddress({
          ...addressObj,
          street: formattedStreet,
          city: getAddressComponent(addressComponents, 'locality'),
          postCode: getAddressComponent(addressComponents, 'postal_code'),
        }); // Pass the selected address to the parent component

        return getLatLng(results[0]);
      })
      .then((latLng) => console.log('Success', latLng))
      .catch((error) => console.error('Error', error));
  };

  const getAddressComponent = (addressComponents : any, type : any) => {
    const component = addressComponents.find(
      (component :any) => component.types.indexOf(type) !== -1
    );

    return component ? component.long_name : '';
  };

  const searchOptions = {
    componentRestrictions: { country: 'nl' }, // Restrict to Netherlands
  };
  const handleClear = () => {
    // Implement the logic to clear the input fields
    setAddressInternal('');
    setStreet('');
    setCity('');
    setPostalCode('');
  };
  const inputStyle = {
    width: '100%', // Adjust the width as needed
    marginBottom: '10px', // Adjust the marginBottom as needed
    backgroundColor: edit ? 'white' : '#7EB6E0',
    color: edit ? 'black' : 'white',
  };
  return (
    <PlacesAutocomplete
    value={address}
    onChange={handleChange}
    onSelect={handleSelect}
    googleCallbackName="initMap"
    searchOptions={searchOptions}
  >
    {({ getInputProps, suggestions, getSuggestionItemProps, loading }) => (
      <div>
        <Row className="mb-3">
          <Col>
            <Form.Label className="mb-0">
              Search:
            </Form.Label>
          </Col>
          <Col>
            <Form.Control
              {...getInputProps({
                id: 'searchInput',
                placeholder: 'Search Places ...',
                className: 'location-search-input',
              })}
              style={title === 'Edit'? inputStyle : {}}
            />
          </Col>
        </Row>
        <div className="autocomplete-dropdown-container" style={{ position: 'absolute', zIndex: 1, border: '1px solid #ddd', borderRadius: '5px', textAlign: 'left', marginBottom: '15px' }}>
          {loading && <div>Loading...</div>}
          {suggestions.map((suggestion) => {
            const className = suggestion.active
              ? 'suggestion-item--active'
              : 'suggestion-item';
            const style = suggestion.active
              ? { backgroundColor: '#fafafa', cursor: 'pointer', color: '#7EB6E0', padding: '8px', borderBottom: '1px solid #ddd' }
              : { backgroundColor: '#ffffff', cursor: 'pointer', color: 'black', padding: '8px', borderBottom: '1px solid #ddd' };
            return (
              <div
                {...getSuggestionItemProps(suggestion, {
                  className,
                  style,
                })}
              >
                <span>{suggestion.description}</span>
              </div>
            );
          })}
        </div>
        <Row className="mb-2">
          <Col>
            <Form.Label className="mb-0">Street:</Form.Label>
          </Col>
          <Col>
            <Form.Control type="text" value={street} readOnly size="sm" style={title === 'Edit'? inputStyle : { fontSize: '16px', marginBottom: '10px' }} />
          </Col>
        </Row>
        <Row className="mb-2">
          <Col>
            <Form.Label className="mb-0">City:</Form.Label>
          </Col>
          <Col>
            <Form.Control type="text" value={city} readOnly size="sm"  style={title === 'Edit'? inputStyle : { fontSize: '16px', marginBottom: '10px' }}/>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col>
            <Form.Label className="mb-0">Postal Code:</Form.Label>
          </Col>
          <Col>
            <Form.Control type="text" value={postalCode} readOnly size="sm"  style={title === 'Edit'? inputStyle : { fontSize: '16px', marginBottom: '10px' }} />
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="d-flex justify-content-end">
            <button className="btn btn-secondary" onClick={handleClear}>
              Clear
            </button>
          </Col>
        </Row>
      </div>
    )}
  </PlacesAutocomplete>
  );
};

export default AddressAutocomplete;
