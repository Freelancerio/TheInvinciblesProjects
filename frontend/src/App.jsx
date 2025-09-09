import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Profile from "./pages/Profile.jsx";
import Home from "./pages/Home.jsx";
import { LandingPage } from "./pages/LandingPage.jsx";
import { Fixtures } from "./pages/Fixtures.jsx";
import { PlayerStatistics } from "./pages/PlayerStatistics.jsx";
import { TeamStatisticsPage } from "./pages/TeamStatistics.jsx"
import { Withdrawal } from "./pages/Withdrawal.jsx";
import { Deposit } from "./pages/Deposit.jsx";
import { Dashboard } from "./pages/Dashboard.jsx";


export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/fixtures" element={<Fixtures />} />
        <Route path="/playerstats" element={<PlayerStatistics />} />
        <Route path="/teamstats" element={<TeamStatisticsPage />} />
        <Route path="/withdrawal" element={<Withdrawal />} />
        <Route path="/deposit" element={<Deposit />} />
      </Routes>
    </Router>
  );
}
