import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";


const UpcomingMatches = ({
  page = 0,
  size = 5,
  season = 2025,
  paginated = false, // true for full page, false for homepage
}) => {
  const [matches, setMatches] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(page);
  const [loading, setLoading] = useState(true); // loading state

  // Reset page to 0 when season changes
  useEffect(() => {
    setCurrentPage(0);
  }, [season]);

  // Fetch matches
  useEffect(() => {
    const fetchMatches = async () => {
      try {
        setLoading(true); // start loading
        const idToken = localStorage.getItem("authToken");
        const url = `http://localhost:8080/api/matches/upcoming?page=${currentPage}&size=${size}&season=${season}`;

        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${idToken}`,
          },
        });

        if (!response.ok) throw new Error("Failed to fetch upcoming matches");

        const data = await response.json();

        setMatches(data.content); // update matches
        setTotalPages(data.totalPages); // update total pages
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false); // stop loading
      }
    };

    fetchMatches();
  }, [currentPage, size, season]);

  return (
    <div className="card-bg backdrop-blur-md rounded-[10px] p-5 mb-[25px] border border-[rgba(255,255,255,0.1)]">
      <div className="card-header flex justify-between items-center mb-5 pb-[10px] border-b border-[rgba(255,255,255,0.1)]">
        <h3 className="card-title text-[1.3rem] font-semibold text-secondary">
          Upcoming Fixtures
        </h3>
        <a href="/upcoming" className="view-all text-secondary no-underline text-[0.9rem] font-medium">
          View All
        </a>
      </div>

      {loading ? (
        <div className="text-center py-10 text-white">Loading matches...</div>
      ) : (
        <>
          <ul className="match-list">
          {matches.map((match) => (
            <Link
              key={match.matchId}
              to={`/match/${match.matchId}`}
              className="no-underline w-full"
            >
              <li className="match-item flex justify-between items-center py-[15px] border-b border-[rgba(255,255,255,0.1)] cursor-pointer hover:bg-[rgba(255,255,255,0.05)]">
                <div className="match-teams flex items-center gap-[15px] flex-1">
                  {/* Home */}
                  <div className="team team-home flex items-center gap-[8px] w-[40%] justify-end font-medium">
                    <span>{match.homeTeam}</span>
                    <div className="team-logo bg-white rounded-full flex items-center justify-center font-bold w-8 h-8 overflow-hidden">
                      <img
                        src={match.homeLogo}
                        alt={match.homeTeam}
                        className="w-full h-full object-contain"
                      />
                    </div>
                  </div>

                  {/* Time */}
                  <div className="match-time flex-none bg-secondary text-primary py-2 px-[10px] rounded-[5px] text-[0.9rem] leading-none font-semibold min-w-[70px] text-center whitespace-nowrap">
                    {new Date(match.dateTime).toLocaleString("en-GB", {
                      weekday: "short",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>

                  {/* Away */}
                  <div className="team team-away flex items-center gap-[8px] w-[40%] justify-start font-medium">
                    <div className="team-logo bg-white rounded-full flex items-center justify-center font-bold w-8 h-8 overflow-hidden">
                      <img
                        src={match.awayLogo}
                        alt={match.awayTeam}
                        className="w-full h-full object-contain"
                      />
                    </div>
                    <span>{match.awayTeam}</span>
                  </div>
                </div>

                <div className="match-date text-[rgba(255,255,255,0.7)] text-[0.9rem]">
                  {new Date(match.dateTime).toLocaleDateString()}
                </div>
              </li>
            </Link>
          ))}
        </ul>


          {/* Pagination for full page only */}
          {!loading && paginated && totalPages > 1 && (
            <div className="flex justify-center mt-4 gap-2">
              <button
                onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
                disabled={currentPage === 0}
                className="px-3 py-1 bg-secondary text-primary rounded disabled:opacity-50"
              >
                Prev
              </button>
              <span className="px-3 py-1">
                Page {currentPage + 1} of {totalPages}
              </span>
              <button
                onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))}
                disabled={currentPage === totalPages - 1}
                className="px-3 py-1 bg-secondary text-primary rounded disabled:opacity-50"
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default UpcomingMatches;
