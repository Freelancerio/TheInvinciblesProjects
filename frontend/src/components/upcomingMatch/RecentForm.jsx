// RecentForm.jsx
import { useLocation } from "react-router-dom";
import { useState, useEffect } from "react";

const RecentForm = () => {
  const location = useLocation();
  const { match } = location.state || {};
  const [teamForms, setTeamForms] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!match) return;

    const fetchHeadToHead = async () => {
      try {
        setLoading(true);
        const idToken = localStorage.getItem("authToken");
        const url = `http://localhost:8080/api/matches/head-to-head?teamA=${match.homeTeam}&teamB=${match.awayTeam}&season=2025`;

        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch head-to-head data");

        const data = await response.json();

        // Transform backend data to the shape used by your component
        const transformed = [
          {
            team: data.teamA.teamName,
            logo: match.homeLogo, // if you want team logo, you need to include it in DTO
            form: data.teamA.form ? data.teamA.form.split("") : [],
            matches: data.teamA.last5Matches?.map((m) => ({
              home: m.homeTeam,
              score: `${m.homeScore ?? "-"} - ${m.awayScore ?? "-"}`,
              away: m.awayTeam,
            })) || [],
          },
          {
            team: data.teamB.teamName,
            logo: match.awayLogo,
            form: data.teamB.form ? data.teamB.form.split("") : [],
            matches: data.teamB.last5Matches?.map((m) => ({
              home: m.homeTeam,
              score: `${m.homeScore ?? "-"} - ${m.awayScore ?? "-"}`,
              away: m.awayTeam,
            })) || [],
          },
        ];


        setTeamForms(transformed);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchHeadToHead();
  }, [match]);

  const getFormIndicatorClass = (result) => {
    switch (result) {
      case "W":
        return "bg-secondary text-primary";
      case "D":
        return "bg-[#ffcc00] text-dark";
      case "L":
        return "bg-accent text-white";
      default:
        return "bg-gray-500 text-white";
    }
  };

  if (loading) return <div>Loading...</div>;
  if (!teamForms.length) return <div>No data available</div>;

  return (
    <div className="card-bg rounded-xl p-6 mb-6 border border-white/10">
      <div className="card-header flex justify-between items-center mb-5 pb-4 border-b border-white/10">
        <h2 className="card-title text-xl font-semibold text-secondary">
          Recent Form
        </h2>
      </div>
      <div className="form-section grid grid-cols-1 md:grid-cols-2 gap-5">
        {teamForms.map((team, index) => (
          <div key={index} className="team-form bg-white/10 p-5 rounded-lg">
            <div className="form-header flex items-center gap-2 mb-4">
              <div className="team-logo-small rounded-full bg-white flex items-center justify-center font-bold text-primary text-xs">
                
                <img
                src={team.logo}
                alt={team.logo}
                className="w-12 h-12 object-contain"
              />
              </div>
              <div className="team-name">{team.team}</div>
            </div>
            <div className="form-indicators flex gap-2 mb-5">
              {team.form.map((result, i) => (
                <div
                  key={i}
                  className={`form-indicator rounded-full flex items-center justify-center text-xs font-bold ${getFormIndicatorClass(
                    result
                  )}`}
                  style={{ width: "25px", height: "25px" }}
                >
                  {result}
                </div>
              ))}
            </div>
            <ul className="recent-matches">
              {team.matches.map((match, i) => (
                <li
                  key={i}
                  className="recent-match flex justify-between items-center py-3 border-b border-white/10 last:border-b-0"
                >
                  <div className="match-teams flex items-center gap-2 flex-1">
                    <div>{match.home}</div>
                    <div className="h2h-score bg-white/10 px-3 py-1 rounded font-bold">
                      {match.score}
                    </div>
                    <div>{match.away}</div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RecentForm;

