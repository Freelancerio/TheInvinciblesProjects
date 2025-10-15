import React, { useState, useEffect } from "react";
import getBaseUrl from "../api.js";
import Header from "../components/Header.jsx";


const baseUrl = getBaseUrl();

const PredictedStandings = () => {
  const [season, setSeason] = useState(2025);
  const [selectedTags, setSelectedTags] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [predictions, setPredictions] = useState([]);
  const [confidence, setConfidence] = useState(0);

  const allTags = [
    { value: "previousPoints2024", label: "2024 Points (60%)" },
    { value: "previousPoints2023", label: "2023 Points (40%)" },
    { value: "goalDifference2024", label: "2024 Goal Difference (50%)" },
    { value: "goalDifference2023", label: "2023 Goal Difference (30%)" },
    { value: "goalDifferenceCurrent", label: "Current Goal Difference (70%)" },
    { value: "leaguePosition2024", label: "2024 League Position" },
    { value: "leaguePosition2023", label: "2023 League Position" },
    { value: "goalsForCurrent", label: "Goals For (20%)" },
    { value: "goalsAgainstCurrent", label: "Goals Against (20%)" },
  ];

  // update prediction confidence
  useEffect(() => {
    setConfidence(Math.min(95, 60 + selectedTags.length * 2.5));
  }, [selectedTags]);

  // tag toggle
  const toggleTag = (tag) => {
    setSelectedTags((prev) =>
      prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag]
    );
  };

  // select/reset helpers
  const selectAll = () => setSelectedTags(allTags.map((t) => t.value));
  const reset = () => setSelectedTags([]);

  // generate predictions
  const generatePredictions = async () => {
    if (selectedTags.length === 0) {
      setError("Please select at least one prediction factor.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
        const token = localStorage.getItem("authToken");
      const res = await fetch(`${baseUrl}/api/standings/predict`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`,
             "Content-Type": "application/json" },
        body: JSON.stringify({ season: parseInt(season), tags: selectedTags }),
      });

      if (!res.ok) throw new Error("Prediction request failed");
      const data = await res.json();
      setPredictions(data);
    } catch (err) {
      console.error(err);
      setError("Failed to generate predictions. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="text-white min-h-screen bg-[linear-gradient(rgba(56,0,60,0.9),rgba(56,0,60,0.95)),url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3')] bg-cover bg-center bg-fixed">
      <Header/>  
      <main className="container mx-auto px-4 py-10 max-w-7xl fade-in">
        <h1 className="text-4xl font-bold text-secondary text-center mb-4">
          Predicted Standings
        </h1>
        <p className="text-center text-lg opacity-80 mb-8">
          AI-powered predictions based on historical data and current performance
        </p>

        {/* SETTINGS CARD */}
        <div className="card-bg rounded-2xl p-8 border border-white/10 mb-8">
          <h2 className="text-2xl font-semibold text-secondary mb-6">
            Prediction Settings
          </h2>

          {/* Season */}
          <div className="grid md:grid-cols-2 gap-8 mb-8">
            <div>
              <label className="block text-lg font-medium mb-4">
                Select Season
              </label>
              <select
                value={season}
                onChange={(e) => setSeason(e.target.value)}
                className="w-full p-4 rounded-lg border border-white/20 bg-white/10 text-white focus:outline-none focus:border-secondary focus:ring-2 focus:ring-secondary/20"
              >
                <option value="2025">2025 Season</option>
                <option value="2024">2024 Season</option>
                <option value="2023">2023 Season</option>
              </select>
            </div>
          </div>

          {/* TAGS */}
          <div className="mb-8">
            <h4 className="font-semibold text-secondary mb-4">
              Prediction Factors
            </h4>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
              {allTags.map((tag) => (
                <label
                  key={tag.value}
                  className="flex items-center space-x-3 cursor-pointer"
                >
                  <input
                    type="checkbox"
                    checked={selectedTags.includes(tag.value)}
                    onChange={() => toggleTag(tag.value)}
                    className="hidden"
                  />
                  <div
                    className={`w-5 h-5 border-2 rounded flex items-center justify-center transition-all ${
                      selectedTags.includes(tag.value)
                        ? "border-secondary bg-secondary/20"
                        : "border-white/30"
                    }`}
                  >
                    <i
                      className={`fas fa-check text-secondary transition-opacity ${
                        selectedTags.includes(tag.value)
                          ? "opacity-100"
                          : "opacity-0"
                      }`}
                    ></i>
                  </div>
                  <span className="text-sm">{tag.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* BUTTONS */}
          <div className="flex flex-wrap justify-center gap-4">
            <button
              onClick={generatePredictions}
              className="bg-secondary text-primary font-semibold py-3 px-8 rounded-lg hover:bg-[#00d46e] hover:scale-105 transition-all duration-200"
            >
              <i className="fas fa-brain mr-2"></i>Generate Predictions
            </button>
            <button
              onClick={selectAll}
              className="bg-white/10 text-white font-semibold py-3 px-6 rounded-lg border border-white/20 hover:bg-white/20"
            >
              Select All
            </button>
            <button
              onClick={reset}
              className="bg-white/10 text-white font-semibold py-3 px-6 rounded-lg border border-white/20 hover:bg-white/20"
            >
              Reset
            </button>
          </div>
        </div>

        {/* LOADING */}
        {loading && (
          <div className="text-center py-16 fade-in">
            <div className="border-4 border-white/30 border-t-secondary rounded-full w-16 h-16 mx-auto mb-6 animate-spin"></div>
            <p className="text-2xl mb-2">Generating Predictions...</p>
            <p className="text-gray-400">
              Analyzing team data and historical performance
            </p>
          </div>
        )}

        {/* ERROR */}
        {error && !loading && (
          <div className="card-bg rounded-2xl p-8 border border-accent/30 text-center fade-in">
            <i className="fas fa-exclamation-triangle text-accent text-5xl mb-6"></i>
            <h3 className="text-2xl font-semibold mb-4">Prediction Failed</h3>
            <p className="text-gray-300 mb-6 text-lg">{error}</p>
            <button
              onClick={generatePredictions}
              className="bg-accent text-white font-semibold py-3 px-8 rounded-lg hover:bg-[#d80046]"
            >
              Try Again
            </button>
          </div>
        )}

        {/* RESULTS */}
        {!loading && predictions.length > 0 && (
          <div className="fade-in">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
              <div className="card-bg rounded-xl p-6 text-center border border-white/10">
                <div className="text-3xl font-bold text-secondary mb-2">
                  {confidence}%
                </div>
                <div className="text-sm opacity-80">Prediction Confidence</div>
              </div>
              <div className="card-bg rounded-xl p-6 text-center border border-white/10">
                <div className="text-3xl font-bold text-secondary mb-2">
                  {selectedTags.length}
                </div>
                <div className="text-sm opacity-80">Active Factors</div>
              </div>
              <div className="card-bg rounded-xl p-6 text-center border border-white/10">
                <div className="text-3xl font-bold text-secondary mb-2">
                  {predictions.length}
                </div>
                <div className="text-sm opacity-80">Teams Analyzed</div>
              </div>
              <div className="card-bg rounded-xl p-6 text-center border border-white/10">
                <div className="text-3xl font-bold text-secondary mb-2">v2.1</div>
                <div className="text-sm opacity-80">Algorithm Version</div>
              </div>
            </div>

            <div className="card-bg rounded-2xl p-6 border border-white/10 mb-8">
              <h2 className="text-2xl font-semibold text-secondary mb-6">
                Predicted Final Standings
              </h2>
              <table className="w-full">
                <thead>
                  <tr className="border-b border-white/20">
                    <th className="py-3 px-4 text-left">Rank</th>
                    <th className="py-3 px-4 text-left">Team</th>
                    <th className="py-3 px-4 text-right">Predicted Score</th>
                  </tr>
                </thead>
                <tbody>
                  {predictions.map((team, idx) => (
                    <tr
                      key={idx}
                      className="border-b border-white/10 hover:bg-white/5 transition-colors"
                    >
                      <td className="py-3 px-4">{team.predictedRank}</td>
                      <td className="py-3 px-4 flex items-center space-x-3">
                        <div className="w-8 h-8 bg-white text-primary font-bold rounded-full flex items-center justify-center text-xs">
                          
                          <img
                            src={team.teamLogo}
                            alt={team.teamLogo}
                            className="w-full h-full object-contain"
                          />
                        </div>
                        <span>{team.teamName}</span>
                      </td>
                      <td className="py-3 px-4 text-right text-secondary font-semibold">
                        {team.predictedScore?.toFixed(1)}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default PredictedStandings;
