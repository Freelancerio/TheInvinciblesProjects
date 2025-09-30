import React, { useState } from "react";
import Header from "../components/Header";
import getBaseUrl from "../api.js";


const teamLogos = {
  Arsenal: "ARS",
  "Aston Villa": "AVL",
  Bournemouth: "BOU",
  Brentford: "BRE",
  Brighton: "BHA",
  Burnley: "BUR",
  Chelsea: "CHE",
  "Crystal Palace": "CRY",
  Everton: "EVE",
  Fulham: "FUL",
  Liverpool: "LIV",
  Luton: "LUT",
  "Manchester City": "MCI",
  "Manchester United": "MU",
  Newcastle: "NEW",
  "Nottingham Forest": "NFO",
  "Sheffield Utd": "SHU",
  Sunderland: "SUN",
  Tottenham: "TOT",
  "West Ham": "WHU",
  Wolves: "WOL",
};


const getTeamLogo = (teamName) => teamLogos[teamName] || teamName.substring(0, 3).toUpperCase();

const TeamComparison = () => {
  const [team1, setTeam1] = useState("");
  const [season1, setSeason1] = useState("");
  const [team2, setTeam2] = useState("");
  const [season2, setSeason2] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [result, setResult] = useState(null);

  const baseUrl = getBaseUrl();

  const validateForm = () => {
    if (!team1 || !team2 || !season1 || !season2) return "Please select both teams and seasons";
    if (team1 === team2 && season1 === season2) return "Cannot compare the same team from the same season";
    return null;
  };

const handleCompare = async () => {
  const validationError = validateForm();
  if (validationError) {
    setError(validationError);
    return;
  }

  setLoading(true);
  setError("");
  setResult(null);

  try {
    const res = await fetch(
      `${baseUrl}/api/league/compare?team1=${encodeURIComponent(team1)}&season1=${season1}&team2=${encodeURIComponent(team2)}&season2=${season2}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("authToken")}`, // 🔑 include authToken
          "Content-Type": "application/json",
        },
      }
    );

    if (!res.ok) {
      throw new Error("Failed to fetch comparison data");
    }

    const data = await res.json();
    setResult(data); // backend already returns {team1, season1, strength1, team2, season2, strength2, strongerTeam}
  } catch (err) {
    setError(err.message);
  } finally {
    setLoading(false);
  }
};


  const handleReset = () => {
    setTeam1("");
    setSeason1("");
    setTeam2("");
    setSeason2("");
    setResult(null);
    setError("");
  };

  return (
    <div className="min-h-screen text-white" style={{
      background: `linear-gradient(rgba(56,0,60,0.9), rgba(56,0,60,0.95)), url('https://img.allfootballapp.com/www/M00/54/8A/720x-/-/-/CgAGVmbsPTOAFBgPAAJ32NbG4hg678.jpg') center/cover no-repeat fixed`
    }}>
        <Header/>
      

      {/* Main Content */}
      <main className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-[#00ff85] mb-4">Team Comparison</h1>
          <p className="text-xl opacity-80">Compare team performance across different seasons</p>
        </div>

        {/* Form */}
        <div className="bg-white/5 backdrop-blur-xl rounded-2xl p-8 border border-white/10 mb-8">
          <h2 className="text-2xl font-semibold text-[#00ff85] text-center mb-8">Select Teams to Compare</h2>
          <div className="grid md:grid-cols-2 gap-8 mb-8">
            {/* Team 1 */}
            <div className="space-y-6">
              <h3 className="text-lg font-semibold text-center">First Team</h3>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-2">Team</label>
                  <select
                    className="w-full p-3 rounded-lg border border-white/20 bg-white/10 text-[#00ff85] focus:outline-none focus:border-[#00ff85] focus:ring-2 focus:ring-[#00ff85]/20"
                    value={team1}
                    onChange={(e) => setTeam1(e.target.value)}
                  >
                    <option value="">Select a team</option>
                    {Object.keys(teamLogos).map(team => <option key={team} value={team}>{team}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">Season</label>
                  <select
                    className="w-full p-3 rounded-lg border border-white/20 bg-white/10 text-[#00ff85] focus:outline-none focus:border-[#00ff85] focus:ring-2 focus:ring-[#00ff85]/20"
                    value={season1}
                    onChange={(e) => setSeason1(e.target.value)}
                  >
                    <option value="">Select season</option>
                    {[2023, 2024, 2025].map(year => <option key={year} value={year}>{year}</option>)}
                  </select>
                </div>
              </div>
            </div>

            {/* Team 2 */}
            <div className="space-y-6">
              <h3 className="text-lg font-semibold text-center">Second Team</h3>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-2">Team</label>
                  <select
                    className="w-full p-3 rounded-lg border border-white/20 bg-white/10 text-[#00ff85] focus:outline-none focus:border-[#00ff85] focus:ring-2 focus:ring-[#00ff85]/20"
                    value={team2}
                    onChange={(e) => setTeam2(e.target.value)}
                  >
                    <option value="">Select a team</option>
                    {Object.keys(teamLogos).map(team => <option key={team} value={team}>{team}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">Season</label>
                  <select
                    className="w-full p-3 rounded-lg border border-white/20 bg-white/10 text-[#00ff85] focus:outline-none focus:border-[#00ff85] focus:ring-2 focus:ring-[#00ff85]/20"
                    value={season2}
                    onChange={(e) => setSeason2(e.target.value)}
                  >
                    <option value="">Select season</option>
                    {[2023, 2024, 2025].map(year => <option key={year} value={year}>{year}</option>)}
                  </select>
                </div>
              </div>
            </div>
          </div>

          <div className="text-center">
            <button
              onClick={handleCompare}
              className="bg-[#00ff85] text-[#38003c] font-semibold py-3 px-8 rounded-lg hover:bg-[#00d46e] hover:scale-105 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100"
              disabled={loading}
            >
              <i className="fas fa-chart-line mr-2"></i>Compare Teams
            </button>
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className="bg-white/5 backdrop-blur-xl rounded-2xl p-8 border border-[#e90052]/30 text-center mb-8">
            <i className="fas fa-exclamation-triangle text-[#e90052] text-5xl mb-6"></i>
            <h3 className="text-2xl font-semibold mb-4">Comparison Failed</h3>
            <p className="text-gray-300 mb-6 text-lg">{error}</p>
            <button
              onClick={() => setError("")}
              className="bg-[#e90052] text-white font-semibold py-3 px-8 rounded-lg hover:bg-[#d80046] transition-colors"
            >
              Try Again
            </button>
          </div>
        )}

        {/* Loading */}
        {loading && (
          <div className="text-center py-16">
            <div className="border-4 border-white/30 border-t-[#00ff85] rounded-full w-16 h-16 mx-auto mb-6 animate-spin"></div>
            <p className="text-2xl mb-2">Comparing teams...</p>
            <p className="text-gray-400">Analyzing performance data</p>
          </div>
        )}

        {/* Results */}
        {result && !loading && (
          <div className="bg-white/5 backdrop-blur-xl rounded-2xl p-8 border border-white/10 mb-8">
            {/* Teams Display */}
            <div className="grid md:grid-cols-3 gap-8 mb-12">
              {/* Team 1 */}
              <div className="text-center">
                <div className="w-24 h-24 rounded-full bg-white mx-auto mb-6 flex items-center justify-center font-bold text-[#38003c] text-xl">
                  {getTeamLogo(result.team1)}
                </div>
                <h3 className="text-2xl font-semibold mb-2">{result.team1}</h3>
                <div className="text-gray-300 mb-4">Season {result.season1}</div>
                <div className="text-4xl font-bold text-[#00ff85] mb-2">{result.strength1.toFixed(1)}</div>
                <div className="text-sm text-gray-400">Strength Rating</div>
              </div>

              {/* VS */}
              <div className="flex flex-col items-center justify-center space-y-6">
                <div className="bg-[#00ff85] text-[#38003c] text-2xl font-bold py-4 px-8 rounded-full">VS</div>
                <div className="text-center">
                  <div className="text-gray-400 mb-2">Stronger Team</div>
                  <div className="text-xl font-semibold text-[#00ff85]">{result.strongerTeam}</div>
                </div>
              </div>

              {/* Team 2 */}
              <div className="text-center">
                <div className="w-24 h-24 rounded-full bg-white mx-auto mb-6 flex items-center justify-center font-bold text-[#38003c] text-xl">
                  {getTeamLogo(result.team2)}
                </div>
                <h3 className="text-2xl font-semibold mb-2">{result.team2}</h3>
                <div className="text-gray-300 mb-4">Season {result.season2}</div>
                <div className="text-4xl font-bold text-[#00ff85] mb-2">{result.strength2.toFixed(1)}</div>
                <div className="text-sm text-gray-400">Strength Rating</div>
              </div>
            </div>

            {/* Strength Bar */}
            <div className="mb-12">
              <h3 className="text-xl font-semibold text-center mb-6">Strength Comparison</h3>
              <div className="bg-white/10 rounded-full h-10 relative overflow-hidden">
                <div className="absolute inset-0 flex">
                  <div
                    className="h-full bg-[#00ff85] transition-all duration-1000 ease-out"
                    style={{ width: `${(result.strength1 / (result.strength1 + result.strength2)) * 100}%` }}
                  ></div>
                  <div
                    className="h-full bg-[#e90052] transition-all duration-1000 ease-out"
                    style={{ width: `${(result.strength2 / (result.strength1 + result.strength2)) * 100}%` }}
                  ></div>
                </div>
                <div className="absolute inset-0 flex items-center justify-center text-[#38003c] font-semibold">
                  <span>{((result.strength1 / (result.strength1 + result.strength2)) * 100).toFixed(1)}%</span> - 
                  <span>{((result.strength2 / (result.strength1 + result.strength2)) * 100).toFixed(1)}%</span>
                </div>
              </div>
            </div>

            <div className="text-center">
              <button
                onClick={handleReset}
                className="bg-white/10 text-white font-semibold py-3 px-8 rounded-lg border border-white/20 hover:bg-white/20 transition-colors"
              >
                <i className="fas fa-plus mr-2"></i>New Comparison
              </button>
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default TeamComparison;
