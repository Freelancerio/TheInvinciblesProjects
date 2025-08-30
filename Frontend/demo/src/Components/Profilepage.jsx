import React,{ useEffect, useState } from "react";
import "./profilepage.css";
import axios from "axios";
import { baseUrl } from "../baseURL";

function ProfilePage() {

  const [user,setUser] = useState(null);

  useEffect(()=> {
        axios.get(`${baseUrl}/user-info`,{withCredentials: true})
        .then(response => {
            setUser(response.data);
        })
        .catch(error => {
            console.error('Error occurred: ',error);
        } )
    },[]);

  return (
    <>
      {/* === Navigation Bar === */}
      <nav className="navbar">
        <div className="navbar-left">
          <span className="brand">SmartBet</span>
        </div>
        <div className="navbar-right">
          <a href="#">Dashboard</a>
          <a href="#">Fixtures</a>
          <a href="#">Promotions</a>
          <button className="icon-btn">⚙️</button>
          <button className="icon-btn">👤</button>
        </div>
      </nav>

      {/* === Profile Page Content === */}
      <div className="profile-container">
        <h1>My Account</h1>
        {user ? (
        <div className="profile-header">
          <div>
            <h2>{user.name}</h2>
            {user.picture && <img src = {user.picture} alt = "User Profile" referrerPolicy="no-referrer"></img>}  
            <p>Joined 2021</p>
            <span>Verified • Level 2</span>    
          </div>
        </div>
        ):(
          <p>Loading user data...</p>
        )}

        {/* === Profile Tabs (static for now) === */}
        <div className="tab-nav">
          <button className="active" id="">Overview</button>
          <button  id="">Personal Info</button>
          <button id="">Account Settings</button>
          <button id="">Betting Preferences</button>
        </div>

        {/* === Personal Info Form === */}
        <div className="info-section">
          <h3>Personal Information</h3>
          <form className="info-form">
            <label>Email</label>
            {/* TODO: Populate this field with user's email (read-only or editable) */}
            {user ? (
            <input type="email" value = {user.email} id="" />
            ):(
              <input type="email" placeholder="Email" id="" />
            )}

            <label>Phone</label>
            {/* TODO: Pre-fill with user.phoneNumber if available */}
            <input type="tel" placeholder="Phone" />

            <label>Address</label>
            {/* TODO: Load and allow editing of user.address if stored in DB */}
            <input type="text" placeholder="Address" />

            {/* TODO: Hook this button to a function that updates user data in your backend or Firebase */}
            <button type="submit" className="update-button" id="">Update Information</button>
          </form>
        </div>
      </div>
    </>
  );
}

export default ProfilePage;
