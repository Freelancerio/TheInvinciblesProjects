import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Header from "../components/Header";
import getBaseUrl from "../api.js";

const TeamStrength = () => {
  const { teamName } = useParams();
  const [strength, setStrength] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const baseUrl = getBaseUrl();

  useEffect(() => {
    const fetchStrength = async () => {
      try {
        const idToken = localStorage.getItem("authToken");
        const response = await fetch(`${baseUrl}/api/teamStrength/strength/${teamName}`, {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch team strength");

        const data = await response.json();
        setStrength(data);
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchStrength();
  }, [teamName]);

  if (loading) return <p>Loading team strength...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!strength) return <p>No strength data found.</p>;

  return (

    <div className="min-h-screen text-white" style={{
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://s3-alpha.figma.com/hub/file/2222013829005813939/63bf05c3-a41c-490f-b461-93a34d0b8e68-cover.png') center/cover no-repeat fixed"
    }}>
    <Header/>
    <div className="fixture-content py-8">
      <div className="container mx-auto px-5">
        <h1 className="page-title text-4xl text-secondary mb-8 text-center">Team Strength</h1>
        
        <div className="fixture-grid grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="card-bg backdrop-blur-md rounded-[10px] p-5 border border-[rgba(255,255,255,0.1)]">
                <h2 className="text-xl text-secondary font-semibold mb-4">{strength.teamName} Strength</h2>
                <div className="flex flex-col gap-3">
                    <p><strong>Attack Strength:</strong> {strength.attackStrength.toFixed(2)}</p>
                    <p><strong>Midfield Strength:</strong> {strength.midfieldStrength.toFixed(2)}</p>
                    <p><strong>Defense Strength:</strong> {strength.defenseStrength.toFixed(2)}</p>
                    <p><strong>Squad Strength:</strong> {strength.squadStrength.toFixed(2)}</p>
                </div>
            </div>
        </div>
      </div>
    </div>
    </div>



   
  );
};

export default TeamStrength;
