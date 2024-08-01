import React, { useEffect, useState } from "react";
import LoginApi from "../api/LoginApi";
import PropertyApi from "../api/PropertyApi";
import TokenManager from "../api/TokenManager";
import UserApi from "../api/UserApi";
import ApplicationApi from "../api/ApplicationApi";
import ApplicationList from "../comp/ApplicationList";
import ApplicationFilter from "../comp/ApplicationFilter";
import { Container, Pagination, Row } from "react-bootstrap";
import Search from "../comp/Search";
import { useNavigate } from "react-router-dom";
import SearchAndFilterApi from "../api/SearchAndFilterApi";

export default function ApplicationPage() {
  const [applications, setApplications] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isDataReady, setIsDataReady] = useState(false);
  const [filteredApplications, setFilteredApplication] = useState([]);
  const [status, setStatus] = useState("PENDING");
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();
  useEffect(() => {
    refreshPropertyList();
  }, [currentPage, status, searchQuery]);

  const refreshPropertyList = () => {
    const claims = TokenManager.getClaims();
    let allApplications = [];

    if (claims?.userId && claims?.roles.includes("HOMEOWNER")) {
      if (!isDataReady) {
        SearchAndFilterApi.getAplicationsPageByHomeowner(
          claims.userId,
          currentPage,
          4,
          status
        )
          .then((data) => {
            setApplications(data.content);
            setTotalPages(data.totalPages);
          })
          .catch((error) => {
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
      } else {
        SearchAndFilterApi.searchAplicationsPageByHomeowner(
          claims.userId,
          currentPage,
          4,
          status,
          searchQuery
        )
          .then((data) => {
            setFilteredApplication(data.content);
            setTotalPages(data.totalPages);
          })
          .catch((error) => {
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

      // UserApi.getUserById(claims.userId)
      //   .then((userData) => {
      //     console.log(userData.properties);

      //     // Use flatMap to flatten the list of applications for all properties
      //     const allApplications = userData.properties.flatMap(property =>
      //       property.applications.map(application => ({ ...application, propertyId: property.id }))
      //     );

      //     console.log(allApplications, "ghj");

      //     // Update the setApplications logic to handle both properties and applications
      //     setApplications(allApplications);

      //   })
      //   .catch((error) => {
      //     if (TokenManager.isAccessTokenExpired()) {
      //       LoginApi.renew();
      //       window.location.reload();
      //     } else {
      //       console.error(error);
      //     }
      //   });
    } else if (claims?.userId && claims?.roles.includes("HOME_SEEKER")) {
      UserApi.getUserById(claims.userId)
        .then((data) => {
          setApplications(data.applications);
          console.log(data);
        })
        .catch((error) => {
          if (TokenManager.isAccessTokenExpired()) {
            LoginApi.renew();
            window.location.reload();
          } else {
            console.error(error);
          }
        });
    }
    // else {
    //   console.log('ert')
    //   PropertyApi.getProperties()
    //     .then((data) => setProperties(data))
    //     .catch((error) => console.log(error));
    // }
  };
  // style={{ position: "relative", top: "-15px", left: "350px", zIndex: 1000 }}
  const handlePageChange = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };
  return (
    <Container>
      <Row className="my-4">
        <div className="col-md-6">
          <Search
            applications={applications}
            setFilteredApplication={setFilteredApplication}
            setIsDataReady={setIsDataReady}
            setSearchQuery={setSearchQuery}
            refreshPropertyList={refreshPropertyList}
          />
        </div>
        <div className="col-md-6">
          <ApplicationFilter setStatus={setStatus} />
        </div>
      </Row>
      <Row
        style={{
          borderRadius: "25px",
          padding: "50px 20px",
          border: "2px solid #7EB6E0",
          color: "white",
          width: "45rem",
          height: "40rem",
        }}
      >
        <ApplicationList
          applications={
            isDataReady === true ? filteredApplications : applications
          }
          refreshPropertyList={refreshPropertyList}
          status={status}
        />
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
