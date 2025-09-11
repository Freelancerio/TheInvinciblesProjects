import "../styles/profile.css";
import React, { useState, useEffect } from "react";

export default function ProfileCard() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [joined, setJoined] = useState("");

  // Load user info from backend or localStorage
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const token = localStorage.getItem("authToken");
        const response = await fetch("http://localhost:8080/api/me", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch user");

        const data = await response.json();
        setUsername(data.username);

        const storedEmail = localStorage.getItem("email");
        setEmail(storedEmail);

        const joinedDate = new Date(data.joined);
        const formattedDate = joinedDate.toLocaleDateString("en-US", {
          day: "2-digit",
          month: "long",
          year: "numeric",
        });
        setJoined(formattedDate);
      } catch (err) {
        console.error(err);
      }
    };

    fetchUser();
  }, []);

  const handleSave = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("authToken");
      const response = await fetch("http://localhost:8080/api/me", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ username }),
      });

      if (!response.ok) {
        throw new Error("Failed to update username");
      }

      const updatedUser = await response.json();
      console.log("Updated user:", updatedUser);

      localStorage.setItem("user-name", updatedUser.username);
    } catch (err) {
      console.error("Error updating username:", err);
    }
  };


  const populate = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("authToken");
      const response = await fetch("http://localhost:8080/api/teams/sync", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
       // body: JSON.stringify({ username }),
      });

      if (!response.ok) {
        throw new Error("Failed to update username");
      }

      const updatedUser = await response.json();
      console.log("Updated user:", updatedUser);
    } catch (err) {
      console.error("Error updating username:", err);
    }
  };


  return (
    <section className="profile-card">
      <h1 className="profile-title">My Account</h1>

      <div className="profile-info">
        <label htmlFor="username">Username</label>
        <input
          id="username"
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <p>
          <strong>Email:</strong> {email}
        </p>
        <p>
          <strong>Joined:</strong> {joined}
        </p>

        <button onClick={handleSave} className="btn-primary">
          Save Changes
        </button>

        <button onClick={populate} className="btn-primary">
          populate
        </button>
      </div>
    </section>
  );
}
