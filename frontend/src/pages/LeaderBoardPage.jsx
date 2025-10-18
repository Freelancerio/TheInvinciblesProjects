import React, { useState, useEffect, useContext } from "react";
import Header from "../components/Header";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();

const LeaderboardPage = () => {
  const { user } = useContext(UserContext);
  const [leaderboardData, setLeaderboardData] = useState([]);
  const [currentSeason, setCurrentSeason] = useState("alltime");
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [userData, setUserData] = useState(null);
  const [accuracy, setAccuracy] = useState(null);

  const token = localStorage.getItem("authToken");
  const currentUser = user.username;
  const firebaseId = user.firebaseId;

  const addPositions = (data) =>
      data.map((user, index) => ({
        ...user,
        position: index + 1,
      }));

  const fetchLeaderboard = async (season) => {
    try {
      setLoading(true);
      const res = await fetch(
          `${baseUrl}/api/leaderboard/${season}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      if (!res.ok) {
        throw new Error("Failed to fetch leaderboard");
      }

      const rawData = await res.json();
      const dataWithPositions = addPositions(rawData);

      setLeaderboardData(dataWithPositions);
      setUserData(
          dataWithPositions.find((user) => user.username === currentUser)
      );
    } catch (err) {
      console.error(err);
      setLeaderboardData([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchAccuracy = async () => {
    try {
      const res = await fetch(
          `${baseUrl}/api/leaderboard/accuracy/${firebaseId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
      );
      if (!res.ok) throw new Error("Failed to fetch accuracy");

      const acc = await res.json();
      setAccuracy((acc).toFixed(1) + "%");
    } catch (err) {
      console.error(err);
      setAccuracy("N/A");
    }
  };

  useEffect(() => {
    fetchLeaderboard(currentSeason);
    fetchAccuracy();
  }, [currentSeason]);

  const getBadgeClass = (season) => {
    switch (season) {
      case "2023":
        return "bg-[#e90052] text-white";
      case "2024":
        return "bg-[#00a8ff] text-white";
      case "2025":
        return "bg-[#9c27b0] text-white";
      case "alltime":
        return "bg-secondary text-primary";
      default:
        return "bg-secondary text-primary";
    }
  };

  const getSeasonDisplayName = (season) => {
    switch (season) {
      case "2023":
        return "2023 Season";
      case "2024":
        return "2024 Season";
      case "2025":
        return "2025 Season";
      case "alltime":
        return "All Time Leaderboard";
      default:
        return "All Time Leaderboard";
    }
  };

  const filteredData = leaderboardData.filter((user) =>
      user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
      <div className="min-h-screen text-white" style={{
        background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://s3-alpha.figma.com/hub/file/2222013829005813939/63bf05c3-a41c-490f-b461-93a34d0b8e68-cover.png') center/cover no-repeat fixed"
      }}>
        <Header />

        {/* Page Content */}
        <div className="page-content py-6 sm:py-8">
          <div className="container mx-auto px-4 sm:px-6">
            <h1 className="text-2xl sm:text-4xl text-center text-secondary mb-3">
              Predictions Leaderboard
            </h1>
            <p className="text-center mb-6 sm:mb-8 opacity-80 text-sm sm:text-base">
              Compete with other users and climb to the top!
            </p>

            {/* Stats Summary */}
            {userData && (
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3 sm:gap-5 mb-6 sm:mb-8">
                  <div className="card-bg rounded-lg sm:rounded-xl p-3 sm:p-5 border border-white/10 text-center">
                    <div className="text-xl sm:text-4xl font-bold text-secondary mb-1 sm:mb-2">
                      {userData.position}
                    </div>
                    <div className="text-xs sm:text-sm opacity-80">Your Position</div>
                  </div>
                  <div className="card-bg rounded-lg sm:rounded-xl p-3 sm:p-5 border border-white/10 text-center">
                    <div className="text-xl sm:text-4xl font-bold text-secondary mb-1 sm:mb-2">
                      {leaderboardData.length}
                    </div>
                    <div className="text-xs sm:text-sm opacity-80">Total Players</div>
                  </div>
                  <div className="card-bg rounded-lg sm:rounded-xl p-3 sm:p-5 border border-white/10 text-center">
                    <div className="text-xl sm:text-4xl font-bold text-secondary mb-1 sm:mb-2">
                      {userData.points}
                    </div>
                    <div className="text-xs sm:text-sm opacity-80">Your Points</div>
                  </div>
                  <div className="card-bg rounded-lg sm:rounded-xl p-3 sm:p-5 border border-white/10 text-center">
                    <div className="text-xl sm:text-4xl font-bold text-secondary mb-1 sm:mb-2">
                      {accuracy || "N/A"}
                    </div>
                    <div className="text-xs sm:text-sm opacity-80">Accuracy Rate</div>
                  </div>
                </div>
            )}

            {/* Filters */}
            <div className="card-bg rounded-lg sm:rounded-xl p-4 sm:p-5 border border-white/10 mb-4 sm:mb-6 flex flex-col sm:flex-row justify-between items-center gap-4">
              <div className="flex flex-wrap gap-2 sm:gap-3 justify-center">
                {["alltime", "2025", "2024", "2023"].map((season) => (
                    <button
                        key={season}
                        onClick={() => setCurrentSeason(season)}
                        className={`py-1 sm:py-2 px-3 sm:px-5 rounded-lg border font-medium transition-all text-xs sm:text-sm ${
                            currentSeason === season
                                ? "bg-secondary text-primary border-secondary"
                                : "bg-white/10 text-white border-white/20 hover:bg-white/20"
                        }`}
                    >
                      {season === "alltime" ? "All Time" : `${season} Season`}
                    </button>
                ))}
              </div>
              <div className="flex items-center gap-2 sm:gap-3 w-full sm:w-auto">
                <i className="fas fa-search text-sm"></i>
                <input
                    type="text"
                    className="py-2 px-3 sm:px-4 rounded-lg border border-white/20 bg-white/10 text-white w-full focus:outline-none focus:border-secondary text-sm"
                    placeholder="Search users..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </div>

            {/* Leaderboard */}
            {loading ? (
                <div className="text-center py-8 sm:py-10">
                  <div className="loading-spinner border-4 border-white/30 border-t-secondary rounded-full w-8 h-8 sm:w-10 sm:h-10 mx-auto mb-3 sm:mb-5 animate-spin"></div>
                  <p className="text-base sm:text-lg">Loading leaderboard data...</p>
                </div>
            ) : filteredData.length === 0 ? (
                <div className="text-center py-12 sm:py-16 px-4 sm:px-5 opacity-70">
                  <i className="fas fa-trophy text-3xl sm:text-5xl mb-3 sm:mb-5 text-secondary"></i>
                  <h3 className="text-lg sm:text-xl mb-2 sm:mb-3">No data available</h3>
                  <p className="opacity-80 text-sm sm:text-base">
                    No leaderboard data found for this season
                  </p>
                </div>
            ) : (
                <div className="grid grid-cols-1 xl:grid-cols-2 gap-4 sm:gap-6 mb-6 sm:mb-8">
                  {/* Main Leaderboard */}
                  <div className="card-bg rounded-lg sm:rounded-xl p-4 sm:p-6 border border-white/10">
                    <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2 sm:gap-0 mb-4 sm:mb-5 pb-3 sm:pb-4 border-b border-white/10">
                      <h2 className="text-lg sm:text-xl font-semibold text-secondary">
                        {getSeasonDisplayName(currentSeason)}
                      </h2>
                      <div
                          className={`py-1 px-2 sm:px-3 rounded-full text-xs sm:text-sm font-semibold ${getBadgeClass(
                              currentSeason
                          )}`}
                      >
                        {currentSeason === "alltime" ? "ALL TIME" : currentSeason}
                      </div>
                    </div>
                    <div className="overflow-x-auto">
                      <table className="w-full border-collapse min-w-[300px]">
                        <thead>
                        <tr>
                          <th className="text-center py-3 px-2 bg-black/20 font-semibold w-12 sm:w-16 text-xs sm:text-sm">
                            #
                          </th>
                          <th className="text-left py-3 px-2 bg-black/20 font-semibold text-xs sm:text-sm">
                            User
                          </th>
                          <th className="text-right py-3 px-2 bg-black/20 font-semibold text-xs sm:text-sm">
                            Points
                          </th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredData.map((user) => (
                            <tr
                                key={user.username}
                                className={`hover:bg-white/5 ${
                                    user.username === currentUser
                                        ? "bg-secondary/10 border-l-2 sm:border-l-4 border-l-secondary"
                                        : ""
                                }`}
                            >
                              <td
                                  className={`py-3 px-2 border-b border-white/10 text-center font-bold text-xs sm:text-sm ${
                                      user.position === 1
                                          ? "text-yellow-400"
                                          : user.position === 2
                                              ? "text-gray-300"
                                              : user.position === 3
                                                  ? "text-amber-700"
                                                  : ""
                                  }`}
                              >
                                {user.position}
                              </td>
                              <td className="py-3 px-2 border-b border-white/10">
                                <div className="flex items-center gap-2 sm:gap-3">
                                  <div className="w-6 h-6 sm:w-9 sm:h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-xs sm:text-sm">
                                    {user.avatar || user.username[0]}
                                  </div>
                                  <div className="text-xs sm:text-sm truncate">{user.username}</div>
                                </div>
                              </td>
                              <td className="py-3 px-2 border-b border-white/10 text-right font-semibold text-secondary text-xs sm:text-sm">
                                {user.points.toLocaleString()}
                              </td>
                            </tr>
                        ))}
                        </tbody>
                      </table>
                    </div>
                  </div>

                  {/* Top Performers */}
                  <div className="card-bg rounded-lg sm:rounded-xl p-4 sm:p-6 border border-white/10">
                    <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-2 sm:gap-0 mb-4 sm:mb-5 pb-3 sm:pb-4 border-b border-white/10">
                      <h2 className="text-lg sm:text-xl font-semibold text-secondary">
                        Top Performers
                      </h2>
                      <div
                          className={`py-1 px-2 sm:px-3 rounded-full text-xs sm:text-sm font-semibold ${getBadgeClass(
                              currentSeason
                          )}`}
                      >
                        TOP 5
                      </div>
                    </div>
                    <div className="overflow-x-auto">
                      <table className="w-full border-collapse min-w-[300px]">
                        <thead>
                        <tr>
                          <th className="text-center py-3 px-2 bg-black/20 font-semibold w-12 sm:w-16 text-xs sm:text-sm">
                            #
                          </th>
                          <th className="text-left py-3 px-2 bg-black/20 font-semibold text-xs sm:text-sm">
                            User
                          </th>
                          <th className="text-right py-3 px-2 bg-black/20 font-semibold text-xs sm:text-sm">
                            Points
                          </th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredData
                            .filter((user) => user.position <= 5)
                            .map((user) => (
                                <tr key={user.username} className="hover:bg-white/5">
                                  <td
                                      className={`py-3 px-2 border-b border-white/10 text-center font-bold text-xs sm:text-sm ${
                                          user.position === 1
                                              ? "text-yellow-400"
                                              : user.position === 2
                                                  ? "text-gray-300"
                                                  : user.position === 3
                                                      ? "text-amber-700"
                                                      : ""
                                      }`}
                                  >
                                    {user.position}
                                  </td>
                                  <td className="py-3 px-2 border-b border-white/10">
                                    <div className="flex items-center gap-2 sm:gap-3">
                                      <div className="w-6 h-6 sm:w-9 sm:h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-xs sm:text-sm">
                                        {user.avatar || user.username[0]}
                                      </div>
                                      <div className="text-xs sm:text-sm truncate">{user.username}</div>
                                    </div>
                                  </td>
                                  <td className="py-3 px-2 border-b border-white/10 text-right font-semibold text-secondary text-xs sm:text-sm">
                                    {user.points.toLocaleString()}
                                  </td>
                                </tr>
                            ))}
                        </tbody>
                      </table>
                    </div>

                    {/* Current User Position */}
                    {userData && (
                        <div className="mt-4 sm:mt-5 pt-3 sm:pt-4 border-t border-white/10">
                          <div className="flex justify-between items-center">
                            <div className="flex items-center gap-2 sm:gap-3">
                              <div className="w-6 h-6 sm:w-9 sm:h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-xs sm:text-sm">
                                {userData.avatar || currentUser[0]}
                              </div>
                              <div>
                                <div className="font-semibold text-xs sm:text-sm">{currentUser}</div>
                                <div className="text-xs opacity-70">Your Position</div>
                              </div>
                            </div>
                            <div className="font-bold text-secondary text-lg sm:text-xl">
                              #{userData.position}
                            </div>
                          </div>
                        </div>
                    )}
                  </div>
                </div>
            )}
          </div>
        </div>
      </div>
  );
};

export default LeaderboardPage;