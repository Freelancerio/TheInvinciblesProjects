import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Profile from "./pages/Profile.jsx";
import Home from "./pages/Home.jsx";
import LandingPage from "./pages/LandingPage.jsx";
import Signup from "./pages/SignUp.jsx";
import FullLeagueTable from "./pages/FullLeagueTable.jsx";
import FullUpcomingMatches from "./pages/FullUpcomingMatches.jsx";
import FullRecentMatches from "./pages/FullRecentMatches.jsx";
import MatchDetails from "./pages/MatchDetails.jsx";
import MatchStatistics from "./pages/MatchStatistics.jsx";
import TeamStrength from "./pages/TeamStats.jsx";
import MatchOddsPage from "./pages/MatchOddsPage.jsx";
import LeaderboardPage from "./pages/LeaderBoardPage.jsx";
import TeamComparison from "./pages/TeamComparison.jsx";
import { UserProvider } from "./UserContext.js";

export default function App() {
  return (
    <UserProvider>
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/home" element={<Home/>} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/league-table" element={<FullLeagueTable/>} />
        <Route path="/upcoming" element={<FullUpcomingMatches/>} />
        <Route path="/recent" element={<FullRecentMatches/>} />
        <Route path="/match/:id" element={<MatchDetails />} />
        <Route path="/matchStatistics/:matchId" element={<MatchStatistics />} />
        <Route path="/teamStats/:teamName" element={<TeamStrength />} />
        <Route path="/betting" element={<MatchOddsPage/>}/>
        <Route path="/leaderboards" element={<LeaderboardPage/>}/>
        <Route path="/comparison" element={<TeamComparison/>}/>

      </Routes>
    </Router>
    </UserProvider>
  );
}
