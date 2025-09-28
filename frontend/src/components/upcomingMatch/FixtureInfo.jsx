// FixtureInfo.jsx
import { useLocation } from "react-router-dom";

const FixtureInfo = () => {
  const location = useLocation();
  const { match } = location.state || {};

  const formatDate = (dateString) => {
    if (!dateString) return "TBD";
    const date = new Date(dateString);
    return new Intl.DateTimeFormat("en-GB", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    }).format(date);
  };

  const formatTime = (dateString) => {
    if (!dateString) return "TBD";
    const date = new Date(dateString);
    return (
      new Intl.DateTimeFormat("en-GB", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
        timeZone: "GMT",
      }).format(date) + " GMT"
    );
  };

  if (!match) {
    return <div>No match data provided</div>;
  }

  return (
    <div className="card-bg rounded-xl p-6 mb-6 border border-white/10 col-span-1 lg:col-span-2">
      {/* League info */}
      <div className="league-info flex items-center justify-center gap-2 mb-5">
        <div className="league-logo rounded-full bg-white flex items-center justify-center font-bold text-primary">
          <img
          src="https://media.api-sports.io/football/leagues/39.png"
          className="w-12 h-12 object-contain"
          />
        </div>
        <h2 className="text-xl">
          Premier League - {match.round || "Matchweek"}
        </h2>
      </div>

      {/* Match details */}
      <div className="match-details flex justify-between items-center my-8 flex-col md:flex-row gap-8 md:gap-0">
        {/* Home team */}
        <div className="team flex flex-col items-center gap-4 w-full md:w-2/5">
          <div className="team-logo rounded-full bg-white flex items-center justify-center font-bold text-primary text-xl">
            {match.homeLogo ? (
              <img
                src={match.homeLogo}
                alt={match.homeTeam}
                className="w-12 h-12 object-contain"
              />
            ) : (
              match.homeTeam.slice(0, 3).toUpperCase()
            )}
          </div>
          <div className="team-name text-xl font-semibold text-center">
            {match.homeTeam}
          </div>
        </div>

        <div className="vs text-2xl font-bold text-secondary">VS</div>

        {/* Away team */}
        <div className="team flex flex-col items-center gap-4 w-full md:w-2/5">
          <div className="team-logo rounded-full bg-white flex items-center justify-center font-bold text-primary text-xl">
            {match.awayLogo ? (
              <img
                src={match.awayLogo}
                alt={match.awayTeam}
                className="w-12 h-12 object-contain"
              />
            ) : (
              match.awayTeam.slice(0, 3).toUpperCase()
            )}
          </div>
          <div className="team-name text-xl font-semibold text-center">
            {match.awayTeam}
          </div>
        </div>
      </div>

      {/* Fixture metadata */}
      <div className="fixture-meta flex justify-center gap-8 mt-5 flex-wrap">
        <div className="meta-item flex flex-col items-center gap-1">
          <div className="meta-label text-sm opacity-80">Date</div>
          <div className="meta-value font-semibold">
            {formatDate(match.dateTime)}
          </div>
        </div>

        <div className="meta-item flex flex-col items-center gap-1">
          <div className="meta-label text-sm opacity-80">Time</div>
          <div className="meta-value font-semibold">
            {formatTime(match.dateTime)}
          </div>
        </div>

        <div className="meta-item flex flex-col items-center gap-1">
          <div className="meta-label text-sm opacity-80">Venue</div>
          <div className="meta-value font-semibold">
            {match.venue || "Stadium TBD"}
          </div>
        </div>
      </div>
    </div>
  );
};

export default FixtureInfo;
