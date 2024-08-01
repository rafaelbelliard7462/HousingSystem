import { Profiler, useEffect, useState } from "react";

import "./App.css";
import { Route, Routes, BrowserRouter as Router } from "react-router-dom";
import HomePage from "./pages/HomePage";
import NavBar from "./comp/NavBar";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import ProfilePage from "./pages/ProfilePage";
import MenuPage from "./pages/MenuPage";
import TokenManager from "./api/TokenManager";
import AddPropertyPage from "./pages/AddPropertyPage";
import PrivateRoute from "./comp/PrivateRoute";
import PropertyDetailPage from "./pages/PropertyDetailPage";
import ManagePropertyPage from "./pages/ManagePropertyPage";
import ApplicationPage from "./pages/ApplicationPage";
import ChatPage from "./pages/ChatPage";
import StatisticsPage from "./pages/StatisticsPage";

function App() {
  const [id, setId] = useState(0);

  //useEffect(() =>{})

  return (
    <div className="App">
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/loginPage" element={<LoginPage />} />
          <Route path="/signupPage" element={<SignupPage />} />
          <Route
            path="/menu"
            element={
              <PrivateRoute roles={["HOMEOWNER", "HOME_SEEKER"]}>
                <MenuPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/menu/profilePage"
            element={
              <PrivateRoute roles={["HOMEOWNER", "HOME_SEEKER"]}>
                <ProfilePage id={id} />
              </PrivateRoute>
            }
          />
          <Route
            path="/menu/addProperty"
            element={
              <PrivateRoute roles={["HOMEOWNER"]}>
                <AddPropertyPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/menu/manageProperty/:id"
            element={
              <PrivateRoute roles={["HOMEOWNER"]}>
                <ManagePropertyPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/menu/viewApplications"
            element={
              <PrivateRoute roles={["HOMEOWNER", "HOME_SEEKER"]}>
                <ApplicationPage />
              </PrivateRoute>
            }
          />
          <Route path="/propertyInfo/:id" element={<PropertyDetailPage />} />

          <Route
            path="/chat"
            element={
              <PrivateRoute roles={["HOMEOWNER", "HOME_SEEKER"]}>
                <ChatPage />
              </PrivateRoute>
            }
          />
          <Route
            path="/menu/stats"
            element={
              <PrivateRoute roles={["HOMEOWNER"]}>
                <StatisticsPage />
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
