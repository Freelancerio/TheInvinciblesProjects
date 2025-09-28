import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

const HeadToHead = () => {
  const location = useLocation();
  const { match } = location.state || {};
  
  const [headToHeadData, setHeadToHeadData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!match?.homeTeam || !match?.awayTeam) return;

    const fetchHeadToHead = async () => {
      try {
        setLoading(true);
        const idToken = localStorage.getItem("authToken");
        const params = new URLSearchParams({
          teamA: match.homeTeam,
          teamB: match.awayTeam,
        });

        const response = await fetch(`http://localhost:8080/api/matches/between/completed?${params}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch head-to-head data");

        const data = await response.json();

        const formattedData = data.map((m) => ({
          homeTeam: m.homeTeam,
          homeLogo: m.homeLogo || m.homeTeam.slice(0, 3).toUpperCase(),
          score: `${m.homeScore} - ${m.awayScore}`,
          awayLogo: m.awayLogo || m.awayTeam.slice(0, 3).toUpperCase(),
          awayTeam: m.awayTeam,
          date: new Date(m.dateTime).toLocaleDateString("en-GB", {
            day: "numeric",
            month: "short",
            year: "numeric",
          }),
        }));

        setHeadToHeadData(formattedData);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchHeadToHead();
  }, [match]);

  if (loading) return <div>Loading head-to-head matches...</div>;
  if (error) return <div className="text-red-500">Error: {error}</div>;
  if (headToHeadData.length === 0) return <div>No head-to-head matches found.</div>;

  return (
    <div className="card-bg rounded-xl p-6 mb-6 border border-white/10">
      <div className="card-header flex justify-between items-center mb-5 pb-4 border-b border-white/10">
        <h2 className="card-title text-xl font-semibold text-secondary">Head to Head</h2>
      </div>
      <div className="h2h-list">
        {headToHeadData.map((match, index) => (
          <div key={index} className="h2h-item flex justify-between items-center py-4 border-b border-white/10 last:border-b-0">
            <div className="h2h-teams flex items-center gap-4 flex-1">
              <div className="h2h-team h2h-team-home flex items-center gap-2 w-2/5 justify-end">
                <span>{match.homeTeam}</span>
                <div className="team-logo-small rounded-full bg-white flex items-center justify-center font-bold text-primary text-xs">

                  <img
                    src={match.homeLogo}
                    alt={match.homeTeam}
                    className="w-12 h-12 object-contain"
                  />
                  
                </div>
              </div>
              <div className="h2h-score bg-white/10 px-3 py-1 rounded font-bold min-w-[50px] text-center">
                {match.score}
              </div>
              <div className="h2h-team h2h-team-away flex items-center gap-2 w-2/5">
                <div className="team-logo-small rounded-full bg-white flex items-center justify-center font-bold text-primary text-xs">
                  
                   <img
                  src={match.awayLogo}
                  alt={match.awayTeam}
                    className="w-12 h-12 object-contain"
                  />
                </div>
                <span>{match.awayTeam}</span>
              </div>
            </div>
            <div className="h2h-date text-white/70 text-sm min-w-[90px] text-right">
              {match.date}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default HeadToHead;
