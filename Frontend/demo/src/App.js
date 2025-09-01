import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Loginfunction from "./Components/loginpage";
import Home from "./Pages/Home";
import UserDashboard from "./Pages/UserDashboard";
import ProfilePage from "./Components/Profilepage";
import TeamStatisticsPage from "./Pages/TeamStatistics";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Loginfunction />} />
        <Route path="/home" element={<Home />} />
        <Route path="/userDashboard" element={<UserDashboard />} />
        <Route path="/dashboard" element={<UserDashboard />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/TeamStatistics" element={<TeamStatisticsPage />} />
        <Route path="/team-statistics" element={<TeamStatisticsPage />} />
      </Routes>
    </Router>
  );
}

export default App;