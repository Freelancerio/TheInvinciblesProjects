// TeamStatistics.jsx
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import getBaseUrl from "../../api.js";

const TeamStatistics = () => {
  const location = useLocation();
  const { match } = location.state || {};

  const [stats, setStats] = useState([]);
  const [loading, setLoading] = useState(true);
  const baseUrl = getBaseUrl();

  useEffect(() => {
    if (!match) return;

    const fetchTeamStats = async () => {
      try {
        setLoading(true);

        const idToken = localStorage.getItem("authToken");
        const url = `${baseUrl}/api/statistics/season-stats?teamA=${match.homeTeam}&teamB=${match.awayTeam}&season=2025`;

        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch team statistics");

        const data = await response.json();

        const transformed = [
          { value: data.teamA.avgGoalsScored.toFixed(1), label: `${data.teamA.teamName} Avg Goals` },
          { value: data.teamB.avgGoalsScored.toFixed(1), label: `${data.teamB.teamName} Avg Goals` },
          { value: data.teamA.avgGoalsConceded.toFixed(1), label: `${data.teamA.teamName} Goals Conceded` },
          { value: data.teamB.avgGoalsConceded.toFixed(1), label: `${data.teamB.teamName} Goals Conceded` },
          { value: `${data.teamA.avgPossession.toFixed(0)}%`, label: `${data.teamA.teamName} Avg Possession` },
          { value: `${data.teamB.avgPossession.toFixed(0)}%`, label: `${data.teamB.teamName} Avg Possession` },
        ];

        setStats(transformed);
      } catch (error) {
        console.error(error);
        setStats([]);
      } finally {
        setLoading(false);
      }
    };

    fetchTeamStats();
  }, [match]);

  if (loading) return <div>Loading team statistics...</div>;
  if (!stats.length) return <div>No statistics available</div>;

  return (
    <div className="card-bg rounded-xl p-6 mb-6 border border-white/10">
      <div className="card-header flex justify-between items-center mb-5 pb-4 border-b border-white/10">
        <h2 className="card-title text-xl font-semibold text-secondary">Team Statistics</h2>
      </div>
      <div className="stats-grid grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {stats.map((stat, index) => (
          <div key={index} className="stat-item bg-white/10 p-5 rounded-lg text-center">
            <div className="stat-value text-3xl font-bold text-secondary mb-1">{stat.value}</div>
            <div className="stat-label text-sm opacity-80">{stat.label}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TeamStatistics;
