import React, { useState, useEffect } from "react";
import "../styles/home.css";
import Navbar from "../components/navbar";
import StatsCard from "../components/statsCard";

export default function Home() {
  const [username, setUsername] = useState("");

  useEffect(() => {
    const storedUsername = localStorage.getItem("user-name") || "User";
    setUsername(storedUsername);
  }, []);

  return (
    <main className="home-wrap">
      {/* Top nav */}
      <Navbar/>

      {/* Page container */}
      <section className="home-card">
        <h1 className="title">Welcome back, {username}</h1>

        {/* Stats */} 
        <StatsCard/>
      

        {/* Upcoming Matches (3 columns) */}
        <section className="block">
          <h2 className="block-title">Favorite Teams – Upcoming Matches</h2>
          <div className="table cols-3">
            <div className="t-head">
              <span>Match</span>
              <span>Date/Time</span>
              <span className="right">Action</span>
            </div>
            {[
              ["Orlando Pirates vs Mamelodi Sundowns", "2025-08-16 15:00"],
              ["Stellenbosch vs Orlando Pirates", "2025-08-16 19:30"],
              ["Orlando Pirates vs Orbit College", "2025-08-22 19:00"],
            ].map((row, i) => (
              <div className="t-row" key={i}>
                <span>{row[0]}</span>
                <span>{row[1]}</span>
                <span className="right">
                  <button className="btn btn-link">Bet Now</button>
                </span>
              </div>
            ))}
          </div>
        </section>

        {/* Standings (4 columns) */}
        <section className="block">
          <h2 className="block-title">Followed Leagues – Standings</h2>
          <div className="table cols-4">
            <div className="t-head">
              <span>Rank</span>
              <span>Team</span>
              <span>Points</span>
              <span className="right">Goal Difference</span>
            </div>

            {[
              [1, "Siwelele FC", 85, "+50"],
              [2, "Marumo Gallants", 82, "+45"],
              [3, "Kaizer Chiefs", 78, "+40"],
              [4, "Mamelodi Sundowns", 75, "+35"],
              [5, "AmaZulu FC", 70, "+30"],
            ].map((r) => (
              <div className="t-row" key={r[0]}>
                <span>{r[0]}</span>
                <span>{r[1]}</span>
                <span>{r[2]}</span>
                <span className="right">{r[3]}</span>
              </div>
            ))}
          </div>

          <div className="row-right">
            <button className="btn btn-soft sm">View More Standings</button>
          </div>
        </section>

        {/* Insights & Quick Actions */}
        <section className="grid-2">
          <div>
            <h3 className="subhead">League Insights</h3>
            <div className="chips">
              <button className="chip">Premier League Fixtures</button>
              <button className="chip">Premier League Top Scorers</button>
              <button className="chip">Premier League Top Goalkeepers</button>
              <button className="chip">Premier League Discipline</button>
            </div>
          </div>

          <div>
            <h3 className="subhead">Quick Actions</h3>
            <div className="chips">
              <button className="chip">Place New Bet</button>
              <button className="chip">Deposit Funds</button>
              <button className="chip">View Fixtures</button>
              <button className="chip">View Predictions</button>
            </div>
          </div>
        </section>
      </section>
    </main>
  );
}
