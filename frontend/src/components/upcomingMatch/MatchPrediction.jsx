import { useState, useContext } from 'react';
import { useLocation } from "react-router-dom";
import { UserContext } from "../../UserContext";

const MatchPrediction = () => {
  const location = useLocation();
  const { match } = location.state || {}; // match object passed from previous page
  const [showPrediction, setShowPrediction] = useState(false);
  const [prediction, setPrediction] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [homeScore, setHomeScore] = useState('');
  const [awayScore, setAwayScore] = useState('');

const fetchPrediction = async () => {
  if (!match?.homeTeam || !match?.awayTeam) return;
  setLoading(true);
  setError(null);

  try {
    const token = localStorage.getItem("authToken"); // adjust key to match your login storage

    const params = new URLSearchParams({
      teamA: match.homeTeam,
      teamB: match.awayTeam,
      season: "2025",
    });

    const response = await fetch(`http://localhost:8080/api/matches/predict?${params.toString()}`, {
      headers: {
        "Authorization": `Bearer ${token}`,  
        "Content-Type": "application/json",
      },
    });

    const text = await response.text(); 
    console.log("Prediction raw response:", text);

    if (!response.ok) {
      throw new Error(`HTTP error ${response.status}: ${text}`);
    }

    try {
      const data = JSON.parse(text);
      setPrediction(data);
    } catch (parseErr) {
      console.error("Failed to parse prediction JSON:", parseErr);
      setError("Server did not return valid JSON.");
    }
  } catch (err) {
    console.error("Error fetching prediction:", err);
    setError("Failed to fetch prediction.");
  } finally {
    setLoading(false);
  }
};

  const handlePredictClick = () => {
    if (!showPrediction) {
      fetchPrediction(); // only fetch when first opened
    }
    setShowPrediction(!showPrediction);
  };

  const handleScoreChange = (e, setter) => {
    const value = e.target.value;
    if (value === '' || (Number(value) >= 0 && Number(value) <= 10)) {
      setter(value);
    }
  };

    const { user } = useContext(UserContext); // get firebaseId from context
    const handleSubmitPrediction = async () => {
      if ((homeScore === '' && awayScore === '') || (homeScore === '0' && awayScore === '0')) {
        alert("Please enter a valid score prediction");
        return;
      }

      try {
        const token = localStorage.getItem("authToken");
        const payload = {
          firebaseId: user.firebaseId, // comes from UserContext
          matchId: match.matchId,      // or match.id if that’s the correct field
          season: 2025,
          predHomeScore: parseInt(homeScore || "0"),
          predAwayScore: parseInt(awayScore || "0"),
        };

        const res = await fetch("http://localhost:8080/api/predictions", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(payload),
        });

        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        alert("Prediction submitted successfully!");
        setHomeScore("");
        setAwayScore("");
      } catch (err) {
        console.error("Prediction error:", err);
        alert("Failed to submit prediction");
      }
    };



  const calculateConfidence = (a, b) => {
    const diff = Math.abs(a - b);
    return 60 + diff * 10; // simple formula: closer games => lower confidence
  };

  return (
    <div className="card-bg rounded-xl p-6 mb-6 border border-white/10 col-span-1 lg:col-span-2">
      <div className="card-header flex justify-between items-center mb-5 pb-4 border-b border-white/10">
        <h2 className="card-title text-xl font-semibold text-secondary">Match Prediction</h2>
      </div>

      <button 
        className="prediction-btn bg-secondary text-primary border-none py-4 px-8 rounded-lg text-lg font-semibold cursor-pointer transition-all hover:bg-[#00d46e] hover:-translate-y-1 mb-5"
        onClick={handlePredictClick}
      >
        <i className="fas fa-brain mr-2"></i> 
        {showPrediction ? 'Hide Prediction' : 'Show Predicted Outcome'}
      </button>
      
      {showPrediction && (
        <div className="prediction-result bg-white/10 p-6 rounded-lg my-5">
          {loading && <p>Loading prediction...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {prediction && (
            <>
              <div className="prediction-text text-xl text-secondary mb-4">AI Prediction</div>
              <div className="predicted-score text-4xl font-extrabold my-4">
                {prediction.teamA} {prediction.predictedGoalsA} - {prediction.predictedGoalsB} {prediction.teamB}
              </div>
              <div className="prediction-text text-xl text-secondary mb-2">
                {prediction.predictedGoalsA > prediction.predictedGoalsB
                  ? `${prediction.teamA} to win`
                  : prediction.predictedGoalsB > prediction.predictedGoalsA
                    ? `${prediction.teamB} to win`
                    : "Draw"}
              </div>
              <div className="confidence">
                Confidence: {calculateConfidence(prediction.predictedGoalsA, prediction.predictedGoalsB)}%
              </div>
            </>
          )}
        </div>
      )}
      
      <div className="user-prediction mt-8">
        <h3 className="text-lg mb-4">Make Your Prediction</h3>
        <div className="score-inputs flex justify-center items-center gap-5 my-5 flex-col md:flex-row">
          <input 
            type="number" 
            min="0" 
            max="10" 
            className="score-input py-3 px-4 rounded-lg border border-white/20 bg-white/10 text-white text-xl font-bold text-center" 
            value={homeScore}
            onChange={(e) => handleScoreChange(e, setHomeScore)}
            placeholder="0" 
          />
          <span className="text-xl">:</span>
          <input 
            type="number" 
            min="0" 
            max="10" 
            className="score-input py-3 px-4 rounded-lg border border-white/20 bg-white/10 text-white text-xl font-bold text-center" 
            value={awayScore}
            onChange={(e) => handleScoreChange(e, setAwayScore)}
            placeholder="0" 
          />
        </div>
        <button 
          className="submit-prediction bg-secondary text-primary border-none py-3 px-6 rounded-lg font-semibold cursor-pointer transition-all hover:bg-[#00d46e]"
          onClick={handleSubmitPrediction}
        >
          Submit Prediction
        </button>
      </div>
    </div>
  );
};

export default MatchPrediction;
