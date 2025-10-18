import React, { useEffect, useState } from "react";
import MatchCard from "../components/MatchCard";
import Header from "../components/Header";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();

const PAGE_SIZE = 20;

const MatchOddsPage = () => {
  const [matches, setMatches] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [search, setSearch] = useState("");
  const [filterDate, setFilterDate] = useState("");

  const fetchMatches = async (page = 0) => {
    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        throw new Error("No auth token found");
      }

      const params = new URLSearchParams({ page, size: PAGE_SIZE });
      const response = await fetch(
          `${baseUrl}/api/matches/match-odds?${params}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setMatches(data.content || []);
      setCurrentPage(data.number);
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error fetching matches:", error);
    }
  };

  useEffect(() => {
    fetchMatches(currentPage);
  }, [currentPage]);

  const filteredMatches = matches
      .filter((m) => {
        const home = m.homeTeam?.toLowerCase() || "";
        const away = m.awayTeam?.toLowerCase() || "";
        return (
            home.includes(search.toLowerCase()) ||
            away.includes(search.toLowerCase())
        );
      })
      .filter((m) => {
        if (!filterDate) return true;
        if (!m.matchDate) return false;
        const matchDateOnly = new Date(m.matchDate)
            .toISOString()
            .split("T")[0];
        return matchDateOnly === filterDate;
      });

  return (
      <div style={{ background: "linear-gradient(rgba(56,0,60,0.9), rgba(56,0,60,0.95)), url('https://www.arsenal.com/sites/default/files/styles/desktop_16x9/public/images/champions_2004.jpg?h=6dff888f&auto=webp&itok=fV_V3M5o') center/cover no-repeat fixed" }} className="min-h-screen text-white">
        <Header />
        <div className="container mx-auto px-4 py-6 sm:py-8">
          {/* Page Header */}
          <h1 className="text-2xl sm:text-3xl font-bold text-center text-green-400 mb-2">
            Match Odds
          </h1>
          <p className="text-center mb-6 opacity-70 text-sm sm:text-base">
            Upcoming Premier League matches with win probabilities
          </p>

          {/* Filters */}
          <div className="flex flex-col sm:flex-row justify-between items-center bg-white/10 p-4 rounded-lg mb-6 gap-4">
            <div className="flex items-center gap-2 w-full sm:w-auto">
              <i className="fas fa-search"></i>
              <input
                  type="text"
                  placeholder="Search matches..."
                  className="px-3 py-2 rounded-md bg-white/10 focus:bg-white/20 focus:outline-none text-white placeholder-white/70 w-full sm:w-64"
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
              />
            </div>
            <div className="flex items-center gap-2 w-full sm:w-auto">
              <label className="whitespace-nowrap">Filter by date:</label>
              <input
                  type="date"
                  className="px-3 py-2 rounded-md bg-white/10 focus:bg-white/20 focus:outline-none text-white w-full sm:w-auto"
                  value={filterDate}
                  onChange={(e) => setFilterDate(e.target.value)}
              />
            </div>
          </div>

          {/* Matches Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
            {filteredMatches.length > 0 ? (
                filteredMatches.map((match) => (
                    <MatchCard key={match.matchId} match={match} />
                ))
            ) : (
                <div className="col-span-full text-center opacity-70 py-12">
                  <i className="fas fa-futbol fa-2x mb-2"></i>
                  <h3>No matches found</h3>
                </div>
            )}
          </div>

          {/* Pagination */}
          <div className="flex justify-center items-center gap-3 mt-6 sm:mt-8">
            <button
                className={`px-3 sm:px-4 py-2 rounded-md bg-white/10 hover:bg-green-400 text-white text-sm sm:text-base ${
                    currentPage === 0 && "opacity-50 cursor-not-allowed"
                }`}
                disabled={currentPage === 0}
                onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 0))}
            >
              Previous
            </button>
            <span className="text-sm sm:text-base">
            Page {currentPage + 1} of {totalPages}
          </span>
            <button
                className={`px-3 sm:px-4 py-2 rounded-md bg-white/10 hover:bg-green-400 text-white text-sm sm:text-base ${
                    currentPage + 1 === totalPages &&
                    "opacity-50 cursor-not-allowed"
                }`}
                disabled={currentPage + 1 === totalPages}
                onClick={() =>
                    setCurrentPage((prev) => Math.min(prev + 1, totalPages - 1))
                }
            >
              Next
            </button>
          </div>
        </div>
      </div>
  );
};

export default MatchOddsPage;