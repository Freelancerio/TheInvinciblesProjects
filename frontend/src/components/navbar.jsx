import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { auth } from "../firebase";
import "../styles/navbar.css"; // import CSS

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await auth.signOut();
      localStorage.removeItem("authToken");
      localStorage.removeItem("user-name");
      navigate("/");
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span className="brand-mark">▦</span>
        <span className="brand-name">SmartBet</span>
      </div>

      <ul className="nav-links">
        <li><Link to="/home">Home</Link></li>
        <li><Link to="/profile">Profile</Link></li>
        <li><Link to="/bets">My Bets</Link></li>
        <li><Link to="/leaderboard">Leaderboard</Link></li>
      </ul>

      <button className="logout-btn" onClick={handleLogout}>Logout</button>
    </nav>
  );
}
