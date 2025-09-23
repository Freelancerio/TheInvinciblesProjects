import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";

const LeagueTable = ({ topN = 5, season = 2025 }) => {
  const [standings, setStandings] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchStandings = async () => {
      try {
        const idToken = localStorage.getItem("authToken");
        const url = topN
          ? `http://localhost:8080/api/standings/top${topN}?season=${season}`
          : `http://localhost:8080/api/standings?season=${season}`;

        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch league standings");

        const data = await response.json();
        setStandings(data);
      } catch (err) {
        console.error(err);
      }
    };

    fetchStandings();
  }, [topN, season]);

  return (
    <div className="card-bg backdrop-blur-md rounded-[10px] p-5 mb-[25px] border border-[rgba(255,255,255,0.1)]">
      <div className="card-header flex justify-between items-center mb-5 pb-[10px] border-b border-[rgba(255,255,255,0.1)]">
        <h3 className="card-title text-[1.3rem] font-semibold text-secondary">
          Premier League Standings - {season}/{season + 1}
        </h3>
        <Link
          to="/league-table"
          className="view-all text-secondary no-underline text-[0.9rem] font-medium"
        >
          Full Table
        </Link>
      </div>

      <div className="table-responsive overflow-x-auto">
        <table className="league-table w-full border-collapse">
          <thead>
            <tr>
              <th className="text-left py-3 px-4 bg-[rgba(0,0,0,0.2)] font-semibold">#</th>
              <th className="text-left py-3 px-4 bg-[rgba(0,0,0,0.2)] font-semibold">Team</th>
              <th className="text-left py-3 px-4 bg-[rgba(0,0,0,0.2)] font-semibold">P</th>
              <th className="text-left py-3 px-4 bg-[rgba(0,0,0,0.2)] font-semibold">GD</th>
              <th className="text-left py-3 px-4 bg-[rgba(0,0,0,0.2)] font-semibold">Pts</th>
            </tr>
          </thead>

          <tbody>
            {standings.map((team) => (
              <tr
                key={team.rank}
                className="hover:bg-[rgba(255,255,255,0.05)] cursor-pointer"
                onClick={() => navigate(`/teamStats/${team.teamName}`)} // Navigate to team page
              >
                <td className="team-position py-3 px-4 font-bold text-center w-[25px]">
                  {team.rank}
                </td>
                <td className="py-3 px-4">
                  <div className="team-info flex items-center gap-[8px]">
                    <div className="team-logo bg-white rounded-full flex items-center justify-center overflow-hidden w-8 h-8">
                      {team.teamLogo ? (
                        <img
                          src={team.teamLogo}
                          alt={team.teamName}
                          className="w-full h-full object-contain"
                        />
                      ) : (
                        <span className="text-xs font-bold">{team.teamName.slice(0, 3).toUpperCase()}</span>
                      )}
                    </div>
                    <span className="team-name font-medium">{team.teamName}</span>
                  </div>
                </td>
                <td className="py-3 px-4">{team.matchesPlayed}</td>
                <td className="py-3 px-4">{team.goalDifference >= 0 ? `+${team.goalDifference}` : team.goalDifference}</td>
                <td className="py-3 px-4">{team.points}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default LeagueTable;
