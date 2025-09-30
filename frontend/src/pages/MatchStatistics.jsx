import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Header from "../components/Header";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();


const MatchStatistics = () => {
  const { matchId } = useParams();
  const [stats, setStats] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const idToken = localStorage.getItem("authToken");
        const url = `${baseUrl}/api/statistics/finished/${matchId}`;

        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch match statistics");

        const data = await response.json();
        console.log(data);
        setStats(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [matchId]);

  return (
      <div className="min-h-screen text-white" style={{
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://library.sportingnews.com/styles/crop_style_16_9_desktop_webp/s3/2021-08/alex-ferguson_1k1npgdzt9khx1lq5xe9mvgozr.jpg.webp?itok=WWMdpycw') center/cover no-repeat fixed"
    }}>
    <Header/>
    <div className="fixture-content py-8">
      <div className="container mx-auto px-5">
        <h1 className="page-title text-4xl text-secondary mb-8 text-center">Fixture Details</h1>
        
        <div className="fixture-grid grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="card-bg backdrop-blur-md rounded-[10px] p-5 border border-[rgba(255,255,255,0.1)] text-white">
      {loading ? (
        <div className="text-center py-10">Loading match statistics...</div>
      ) : (
        <>
          <h2 className="text-xl font-bold mb-4">Match Statistics</h2>
          {stats.length === 0 ? (
            <p>No statistics available for this match.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {stats.map((team) => (
                <div
                  key={team.teamId}
                  className="bg-[rgba(255,255,255,0.05)] p-4 rounded-lg shadow"
                >
                  <div className="flex items-center gap-3 mb-4">
                    <img
                      src={team.teamLogo}
                      alt={team.teamName}
                      className="w-10 h-10 rounded-full"
                    />
                    <h3 className="text-lg font-semibold">{team.teamName}</h3>
                  </div>
                  <ul className="space-y-1 text-sm">
                    <li>Shots on Goal: {team.shotsOnGoal}</li>
                    <li>Shots off Goal: {team.shotsOffGoal}</li>
                    <li>Total Shots: {team.totalShots}</li>
                    <li>Blocked Shots: {team.blockedShots}</li>
                    <li>Fouls: {team.fouls}</li>
                    <li>Corner Kicks: {team.cornerKicks}</li>
                    <li>Offsides: {team.offsides}</li>
                    <li>Ball Possession: {team.ballPossession}</li>
                    <li>Yellow Cards: {team.yellowCards}</li>
                    <li>Red Cards: {team.redCards}</li>
                    <li>Goalkeeper Saves: {team.goalkeeperSaves}</li>
                    <li>Total Passes: {team.totalPasses}</li>
                    <li>Passes Accurate: {team.passesAccurate}</li>
                    <li>Passes %: {team.passesPercentage}</li>
                    <li>Expected Goals (xG): {team.expectedGoals}</li>
                    <li>Goals Prevented: {team.goalsPrevented}</li>
                  </ul>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>


        </div>
      </div>
    </div>
    </div>



    
  );
};

export default MatchStatistics;
