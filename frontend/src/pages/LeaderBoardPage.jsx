import React, { useState, useEffect, useContext } from "react";
import Header from "../components/Header";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api";

const baseUrl = getBaseUrl();

const LeaderboardPage = () => {
  const { user } = useContext(UserContext);
  const [leaderboardData, setLeaderboardData] = useState([]);
  const [currentSeason, setCurrentSeason] = useState("alltime");
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [userData, setUserData] = useState(null);
   const [accuracy, setAccuracy] = useState(null);

  const token = localStorage.getItem("authToken"); // adjust if token comes from context or props
  const currentUser = user.username; 
  const firebaseId = user.firebaseId; 

  // helper to add positions
  const addPositions = (data) =>
    data.map((user, index) => ({
      ...user,
      position: index + 1, // 1-based ranking
    }));

  // Fetch data from backend
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

  // Fetch user accuracy
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
      setAccuracy((acc).toFixed(1) + "%"); // convert to percentage
    } catch (err) {
      console.error(err);
      setAccuracy("N/A");
    }
  };

  useEffect(() => {
    fetchLeaderboard(currentSeason);
    fetchAccuracy();
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3') center/cover no-repeat fixed"
    }}>
      <Header />

      {/* Page Content */}
      <div className="page-content py-8">
        <div className="container mx-auto px-5">
          <h1 className="text-4xl text-center text-secondary mb-3">
            Predictions Leaderboard
          </h1>
          <p className="text-center mb-8 opacity-80">
            Compete with other users and climb to the top!
          </p>

          {/* Stats Summary */}
          {userData && (
            <div className="grid grid-cols-1 md:grid-cols-4 gap-5 mb-8">
              <div className="card-bg rounded-xl p-5 border border-white/10 text-center">
                <div className="text-4xl font-bold text-secondary mb-2">
                  {userData.position}
                </div>
                <div className="text-sm opacity-80">Your Position</div>
              </div>
              <div className="card-bg rounded-xl p-5 border border-white/10 text-center">
                <div className="text-4xl font-bold text-secondary mb-2">
                  {leaderboardData.length}
                </div>
                <div className="text-sm opacity-80">Total Players</div>
              </div>
              <div className="card-bg rounded-xl p-5 border border-white/10 text-center">
                <div className="text-4xl font-bold text-secondary mb-2">
                  {userData.points}
                </div>
                <div className="text-sm opacity-80">Your Points</div>
              </div>
              <div className="card-bg rounded-xl p-5 border border-white/10 text-center">
                <div className="text-4xl font-bold text-secondary mb-2">
                  {accuracy || "N/A"}
                </div>
                <div className="text-sm opacity-80">Accuracy Rate</div>
              </div>
            </div>
          )}

          {/* Filters */}
          <div className="card-bg rounded-xl p-5 border border-white/10 mb-6 flex flex-col md:flex-row justify-between items-center gap-5">
            <div className="flex flex-wrap gap-3 justify-center">
              {["alltime", "2025", "2024", "2023"].map((season) => (
                <button
                  key={season}
                  onClick={() => setCurrentSeason(season)}
                  className={`py-2 px-5 rounded-lg border font-medium transition-all ${
                    currentSeason === season
                      ? "bg-secondary text-primary border-secondary"
                      : "bg-white/10 text-white border-white/20 hover:bg-white/20"
                  }`}
                >
                  {season === "alltime" ? "All Time" : `${season} Season`}
                </button>
              ))}
            </div>
            <div className="flex items-center gap-3">
              <i className="fas fa-search"></i>
              <input
                type="text"
                className="py-2 px-4 rounded-lg border border-white/20 bg-white/10 text-white w-full md:w-64 focus:outline-none focus:border-secondary"
                placeholder="Search users..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          {/* Leaderboard */}
          {loading ? (
            <div className="text-center py-10">
              <div className="loading-spinner border-4 border-white/30 border-t-secondary rounded-full w-10 h-10 mx-auto mb-5 animate-spin"></div>
              <p className="text-lg">Loading leaderboard data...</p>
            </div>
          ) : filteredData.length === 0 ? (
            <div className="text-center py-16 px-5 opacity-70">
              <i className="fas fa-trophy text-5xl mb-5 text-secondary"></i>
              <h3 className="text-xl mb-3">No data available</h3>
              <p className="opacity-80">
                No leaderboard data found for this season
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-1 xl:grid-cols-2 gap-6 mb-8">
              {/* Main Leaderboard */}
              <div className="card-bg rounded-xl p-6 border border-white/10">
                <div className="flex justify-between items-center mb-5 pb-4 border-b border-white/10">
                  <h2 className="text-xl font-semibold text-secondary">
                    {getSeasonDisplayName(currentSeason)}
                  </h2>
                  <div
                    className={`py-1 px-3 rounded-full text-sm font-semibold ${getBadgeClass(
                      currentSeason
                    )}`}
                  >
                    {currentSeason === "alltime" ? "ALL TIME" : currentSeason}
                  </div>
                </div>
                <table className="w-full border-collapse">
                  <thead>
                    <tr>
                      <th className="text-center py-4 px-3 bg-black/20 font-semibold w-16">
                        #
                      </th>
                      <th className="text-left py-4 px-3 bg-black/20 font-semibold">
                        User
                      </th>
                      <th className="text-right py-4 px-3 bg-black/20 font-semibold">
                        Points
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredData.map((user) => (
                      <tr
                        key={user.username} // safer key
                        className={`hover:bg-white/5 ${
                          user.username === currentUser
                            ? "bg-secondary/10 border-l-4 border-l-secondary"
                            : ""
                        }`}
                      >
                        <td
                          className={`py-4 px-3 border-b border-white/10 text-center font-bold ${
                            user.position === 1
                              ? "text-yellow-400 text-lg"
                              : user.position === 2
                              ? "text-gray-300 text-lg"
                              : user.position === 3
                              ? "text-amber-700 text-lg"
                              : ""
                          }`}
                        >
                          {user.position}
                        </td>
                        <td className="py-4 px-3 border-b border-white/10">
                          <div className="flex items-center gap-3">
                            <div className="w-9 h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-sm">
                              {user.avatar || user.username[0]}
                            </div>
                            <div>{user.username}</div>
                          </div>
                        </td>
                        <td className="py-4 px-3 border-b border-white/10 text-right font-semibold text-secondary">
                          {user.points.toLocaleString()}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Top Performers */}
              <div className="card-bg rounded-xl p-6 border border-white/10">
                <div className="flex justify-between items-center mb-5 pb-4 border-b border-white/10">
                  <h2 className="text-xl font-semibold text-secondary">
                    Top Performers
                  </h2>
                  <div
                    className={`py-1 px-3 rounded-full text-sm font-semibold ${getBadgeClass(
                      currentSeason
                    )}`}
                  >
                    TOP 5
                  </div>
                </div>
                <table className="w-full border-collapse">
                  <thead>
                    <tr>
                      <th className="text-center py-4 px-3 bg-black/20 font-semibold w-16">
                        #
                      </th>
                      <th className="text-left py-4 px-3 bg-black/20 font-semibold">
                        User
                      </th>
                      <th className="text-right py-4 px-3 bg-black/20 font-semibold">
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
                            className={`py-4 px-3 border-b border-white/10 text-center font-bold ${
                              user.position === 1
                                ? "text-yellow-400 text-lg"
                                : user.position === 2
                                ? "text-gray-300 text-lg"
                                : user.position === 3
                                ? "text-amber-700 text-lg"
                                : ""
                            }`}
                          >
                            {user.position}
                          </td>
                          <td className="py-4 px-3 border-b border-white/10">
                            <div className="flex items-center gap-3">
                              <div className="w-9 h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-sm">
                                {user.avatar || user.username[0]}
                              </div>
                              <div>{user.username}</div>
                            </div>
                          </td>
                          <td className="py-4 px-3 border-b border-white/10 text-right font-semibold text-secondary">
                            {user.points.toLocaleString()}
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>

                {/* Current User Position */}
                {userData && (
                  <div className="mt-5 pt-4 border-t border-white/10">
                    <div className="flex justify-between items-center">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 rounded-full bg-secondary flex items-center justify-center font-bold text-primary text-sm">
                          {userData.avatar || currentUser[0]}
                        </div>
                        <div>
                          <div className="font-semibold">{currentUser}</div>
                          <div className="text-sm opacity-70">Your Position</div>
                        </div>
                      </div>
                      <div className="font-bold text-secondary text-xl">
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
