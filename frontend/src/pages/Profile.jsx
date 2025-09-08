// src/pages/Profile.jsx

import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/profile.css";

export default function Profile() {
  const navigate = useNavigate();

  return (
    <main className="profile-wrap">
      {/* Top navigation */}
      <header className="profile-nav">
        <nav className="nav-links" aria-label="Profile Navigation">
          <button className="btn btn-ghost" onClick={() => navigate("/home")}>
            Homepage
          </button>
          <button className="btn btn-ghost" onClick={() => navigate("/profile")}>
            Profile
          </button>
          <button className="btn btn-ghost" onClick={() => navigate("/")}>
            Logout
          </button>
        </nav>
      </header>

      {/* Profile card */}
      <section className="profile-card">
        <h1 className="profile-title">My Account</h1>

        {/* User Info */}
        <div className="profile-header">
          <div className="avatar"></div>
          <div className="user-info">
            <h2>Luvo Zulu</h2>
            <p>Joined 2021</p>
            <p>Verified • Level 2</p>
          </div>
        </div>

        {/* Personal Info Form */}
        <form className="profile-form">
          <label htmlFor="email">Email</label>
          <input id="email" type="email" placeholder="Enter your email" />

          <label htmlFor="phone">Phone</label>
          <input id="phone" type="tel" placeholder="Enter your phone number" />

          <label htmlFor="address">Address</label>
          <input id="address" type="text" placeholder="Enter your address" />

          <button type="submit" className="btn-primary">
            Update Information
          </button>
        </form>
      </section>
    </main>
  );
}
