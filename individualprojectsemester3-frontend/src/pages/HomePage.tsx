import React, { useEffect, useState } from "react";
import axios from 'axios';
import NavBar from "../comp/NavBar";
import PropertyList from "../comp/PropertyList";
import TokenManager from "../api/TokenManager";
import LoginApi from "../api/LoginApi";
import UserApi from "../api/UserApi";
import { useNavigate } from "react-router-dom";
import HomepageSearchAndFilter from "../comp/HomepageSearchAndFilter";
import { Container, Pagination, Row } from "react-bootstrap";

export default function HomePage() {
 const [properties, setProperties] = useState([]);
 const [currentPage, setCurrentPage] = useState(0);
 const [totalPages, setTotalPages] = useState(0);
 const [searchFilters, setSearchFilters] = useState({
  city: null,
  maxPrice: 0,
  minSize: null,
  propertyType: null,
  rented: null
 });
 const claims = TokenManager.getClaims();

 const navigate = useNavigate();
 useEffect(() => {
  refreshPropertyList();
 }, [searchFilters, currentPage]);

 const refreshPropertyList = () => {
  const { city, maxPrice, minSize, propertyType, rented } = searchFilters;
  const queryParams = [];

  if (city !== null && city !== "") queryParams.push(`city=${city}`);
  if (maxPrice !== null || maxPrice === null) queryParams.push(`maxPrice=${maxPrice !== null ? maxPrice : 0}`);
  if (minSize !== null || minSize === null) queryParams.push(`minSize=${minSize !== null ? minSize : 0}`);
  if (propertyType !== null) queryParams.push(`propertyType=${propertyType}`);
  if (rented !== null) queryParams.push(`rented=${rented}`);

  if(claims?.roles.includes("HOMEOWNER")){
    axios.get(`/searchAndFilter/searchHomeowner/${claims.userId}?${queryParams.join('&')}&pageNum=${currentPage}&itemsNum=6`,{
      headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
  })
    .then((response) => {
      console.log(response);
      setProperties(response.data.content); // Assuming the API returns a 'content' array
      setTotalPages(response.data.totalPages); // Assuming the API returns 'totalPages'
    }).catch((error) => {
      if (TokenManager.isAccessTokenExpired()) {
        LoginApi.renew().catch((error) => {
          if (error.message === "Request failed with status code 401") {
            navigate("/loginpage", { state: { from: window.location.pathname } });
          } else {
            console.error(error.message);
          }
        });
      } else {
        console.error(error.message);
      }
    });
  }
  else{
    axios.get(`/searchAndFilter/search?${queryParams.join('&')}&pageNum=${currentPage}&itemsNum=6`)
    .then((response) => {
      console.log(response);
      setProperties(response.data.content); // Assuming the API returns a 'content' array
      setTotalPages(response.data.totalPages); // Assuming the API returns 'totalPages'
    })
    .catch((error) => console.error(error));
};
  }
  

 const handleSearchFiltersChange = (newFilters) => {
  setSearchFilters((prevFilters) => ({
    ...prevFilters,
    ...newFilters,
  }));
 };

 const handlePageChange = (pageNumber) => {
  setCurrentPage(pageNumber);
 };

 const containerStyle = {
 borderRadius: '25px',
 padding: '15px 10px',
 backgroundColor: '#7EB6E0',
 marginLeft: '35px',
 width: '100%'
};

 return (
  <Container>
    <Row className="my-5" style={containerStyle}>
      <HomepageSearchAndFilter onFiltersChange={handleSearchFiltersChange} />
    </Row>
    <Row style={{marginRight : "15px",  height: '65rem' }}>
      <PropertyList properties={properties} />
    </Row>
    <Pagination className=" d-flex justify-content-center mt-2">
      <Pagination.Prev
        onClick={() => handlePageChange(currentPage - 1)}
        disabled={currentPage === 0}
      />

      {/* Render pagination items dynamically based on the total number of pages */}
      {Array.from({ length: totalPages }, (_, index) => (
        <Pagination.Item
          key={index}
          active={index === currentPage}
          onClick={() => handlePageChange(index)}
        >
          {index + 1}
        </Pagination.Item>
      ))}

      <Pagination.Next
        onClick={() => handlePageChange(currentPage + 1)}
        disabled={currentPage === totalPages - 1}
      />
    </Pagination>
  </Container>
 );
}
