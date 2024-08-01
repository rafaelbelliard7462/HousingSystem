import React, { useState } from "react";
import PlacesAutocomplete from "react-places-autocomplete";
import { Form, Row, Col } from "react-bootstrap";
import { faL } from "@fortawesome/free-solid-svg-icons";
import TokenManager from "../api/TokenManager";

export default function HomepageSearchAndFilter({ onFiltersChange }) {
  const [city, setCity] = useState("");
  const [maxPrice, setMaxPrice] = useState(null);
  const [minSize, setMinSize] = useState(null);
  const [propertyType, setPropertyType] = useState(null);
  const [rented, setRented] = useState('false');
  const propertyTypeOptions = ["ROOM", "STUDIO", "APARTMENT", "HOUSE"];
const claims = TokenManager.getClaims();
  const rentedOptions = [
    { label: "Rented", value: 'true' },
    { label: "Not Rented", value: 'false' },
  ];

  const maxPriceOptions = Array.from(
    { length: (6500 - 250) / 100 + 1 },
    (_, index) => {
      const value = 250 + index * 100;
      return { label: `€${value}`, value };
    }
  );

  const minSizeOptions = Array.from(
    { length: (100 - 6) / 2 + 1 },
    (_, index) => {
      const value = 6 + index * 2;
      return { label: `${value} m²`, value };
    }
  );

  const handleSelect = (address) => {
    setCity(address.split(",")[0]);
    onFiltersChange({ city: address.split(",")[0] });
  };

  const searchOptions = {
    types: ["(cities)"],
    componentRestrictions: { country: "nl" },
  };

  const handleFilterChange = (filterName, filterValue) => {
    switch (filterName) {
      case "maxPrice":
        setMaxPrice(filterValue === "" ? null : filterValue);
        break;
      case "minSize":
        setMinSize(filterValue === "" ? null : filterValue);
        break;
      case "propertyType":
        setPropertyType(filterValue);
        break;
        case "rented":
        setRented(filterValue);
        break;
      default:
        break;
    }
    onFiltersChange({ [filterName]: filterValue === "" ? null : filterValue });
  };

  return (
    <div>
      <Row className="mb-2 align-items-center">
        <Col>
          <Form.Label className="mb-0">Search:</Form.Label>
        </Col>
        <Col>
          <PlacesAutocomplete
            value={city}
            onChange={setCity}
            onSelect={handleSelect}
            searchOptions={searchOptions}
          >
            {({
              getInputProps,
              suggestions,
              getSuggestionItemProps,
              loading,
            }) => (
              <div
                className="autocomplete-container"
                style={{ position: "relative" }}
              >
                <input
                  {...getInputProps({
                    placeholder: "Type a city",
                    className:
                      "location-search-input form-control form-control-sm",
                  })}
                />
                <div
                  className="autocomplete-dropdown-container"
                  style={{
                    position: "absolute",
                    zIndex: 1000,
                    backgroundColor: "white",
                    textAlign: "left",
                    width: "100%",
                  }}
                >
                  {loading && <div>Loading...</div>}
                  {suggestions.map((suggestion) => {
                    const className = suggestion.active
                      ? "suggestion-item--active"
                      : "suggestion-item";
                    return (
                      <div
                        {...getSuggestionItemProps(suggestion, { className })}
                      >
                        <span>{suggestion.description.split(",")[0]}</span>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </PlacesAutocomplete>
        </Col>
        <Col>
          <Form.Label className="mb-0">Max Price:</Form.Label>
        </Col>
        <Col>
          <Form.Control
            as="select"
            value={maxPrice || ""}
            onChange={(e) => handleFilterChange("maxPrice", e.target.value)}
            size="sm"
            style={{ width: "8rem" }}
          >
            <option value="">Select max price</option>
            {maxPriceOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </Form.Control>
        </Col>
        <Col>
          <Form.Label className="mb-0">Min Size:</Form.Label>
        </Col>
        <Col>
          <Form.Control
            as="select"
            value={minSize || ""}
            onChange={(e) => handleFilterChange("minSize", e.target.value)}
            size="sm"
          >
            <option value="">Select min size</option>
            {minSizeOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </Form.Control>
        </Col>
        <Col>
          <Form.Label className="mb-0">Property Type:</Form.Label>
        </Col>
        <Col>
          <Form.Control
            as="select"
            value={propertyType || ""}
            onChange={(e) => handleFilterChange("propertyType", e.target.value)}
            size="sm"
            style={{ width: "10rem" }}
          >
            <option value="">Select Property Type</option>
            {propertyTypeOptions.map((option) => (
              <option key={option} value={option}>
                {option}
              </option>
            ))}
          </Form.Control>
        </Col>
        {claims?.roles.includes("HOMEOWNER") && (
        <>
          <Col>
            <Form.Label className="mb-0">Rented:</Form.Label>
          </Col>
          <Col>
            <Form.Control
              as="select"
              value={rented}
              onChange={(e) => handleFilterChange("rented", e.target.value)}
              size="sm"
            >
              {rentedOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </Form.Control>
          </Col>
        </>
      )}
      </Row>
    </div>
  );
}
