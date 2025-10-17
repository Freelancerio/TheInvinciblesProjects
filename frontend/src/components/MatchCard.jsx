import React, { useState, useContext, useEffect } from "react";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api.js";

const probabilityToDecimal = (prob) =>
  prob && prob > 0 ? (1 / prob).toFixed(2) : "-";

const statusStyles = {
  NS: "bg-green-400 text-purple-900",
  LIVE: "bg-pink-500 text-white",
  FT: "bg-gray-500 text-white",
};

const MatchCard = ({ match }) => {
  const { user, setUser } = useContext(UserContext);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [betAmount, setBetAmount] = useState("");
  const [selectedOutcome, setSelectedOutcome] = useState("");
  const [error, setError] = useState("");
  const [bettingLocked, setBettingLocked] = useState(false);
  const [countdown, setCountdown] = useState(0);

  const baseUrl = getBaseUrl();

  // Countdown effect
  useEffect(() => {
    if (countdown > 0) {
      const timer = setTimeout(() => {
        setCountdown(countdown - 1);
      }, 1000);
      return () => clearTimeout(timer);
    } else if (countdown === 0 && bettingLocked) {
      setBettingLocked(false);
    }
  }, [countdown, bettingLocked]);

  if (!match) return null;

  const {
    matchId,
    homeTeam,
    homeLogo,
    awayTeam,
    awayLogo,
    status,
    matchDate,
    homeWinProbability,
    drawProbability,
    awayWinProbability,
  } = match;

  const formattedDate = matchDate
    ? new Date(matchDate).toLocaleDateString()
    : "TBD";

  const outcomes = [
    { label: homeTeam, key: "homewin", probability: homeWinProbability },
    { label: "Draw", key: "draw", probability: drawProbability },
    { label: awayTeam, key: "awaywin", probability: awayWinProbability },
  ];

  const handlePlaceBet = async () => {
    if (bettingLocked) {
      setError(`Please wait ${countdown} seconds before placing another bet`);
      return;
    }

    const amount = parseFloat(betAmount);
    if (isNaN(amount) || amount <= 0) {
      setError("Enter a valid bet amount");
      return;
    }
    if (amount > user.account_balance) {
      setError("Insufficient balance");
      return;
    }
    if (!selectedOutcome) {
      setError("Select an outcome to bet on");
      return;
    }

    const oddsMap = {
      homewin: homeWinProbability,
      draw: drawProbability,
      awaywin: awayWinProbability,
    };
    const payout = (amount / oddsMap[selectedOutcome]).toFixed(2);

    const payload = {
      userId: user.firebaseId,
      matchId,
      outcome: selectedOutcome,
      betAmount: amount,
      expectedWinAmount: parseFloat(payout),
    };

    try {
      const token = localStorage.getItem("authToken");
      const res = await fetch(`${baseUrl}/api/bets/place`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error(`HTTP error ${res.status}`);

      // Update user context and localStorage
      const newBalance = user.account_balance - amount;
      const updatedUser = { ...user, account_balance: newBalance };
      setUser(updatedUser);
      localStorage.setItem("user-data", JSON.stringify(updatedUser));

      // Lock betting for 7 seconds
      setBettingLocked(true);
      setCountdown(7);

      setIsModalOpen(false);
      setBetAmount("");
      setSelectedOutcome("");
      setError("");
    } catch (err) {
      console.error(err);
      setError("Failed to place bet");
    }
  };

  const calculatePayout = () => {
    if (!selectedOutcome || !betAmount) return "-";
    const odds = outcomes.find((o) => o.key === selectedOutcome)?.probability;
    return odds ? (parseFloat(betAmount) / odds).toFixed(2) : "-";
  };

  return (
    <>
      {/* Card */}
      <div
        className={`bg-white/5 backdrop-blur-md rounded-lg p-4 border border-white/10 hover:shadow-lg hover:scale-105 transition-transform ${bettingLocked ? "cursor-not-allowed opacity-60" : "cursor-pointer"
          }`}
        onClick={() => !bettingLocked && setIsModalOpen(true)}
      >
        <div className="flex justify-between items-center mb-4 border-b border-white/20 pb-2">
          <div className="text-sm opacity-70">{formattedDate}</div>
          <div
            className={`px-3 py-1 rounded-full text-xs font-semibold ${statusStyles[status] || "bg-green-400 text-purple-900"
              }`}
          >
            {status === "NS" ? "Upcoming" : status}
          </div>
        </div>

        {/* Betting Locked Indicator */}
        {bettingLocked && (
          <div className="bg-yellow-500/20 border border-yellow-500 rounded-md p-2 mb-3 text-center">
            <i className="fas fa-lock mr-2"></i>
            <span className="text-sm font-semibold">
              Betting locked for {countdown}s
            </span>
          </div>
        )}

        <div className="flex justify-between items-center mb-4">
          <div className="flex flex-col items-center w-5/12">
            <div className="w-12 h-12 bg-white text-purple-900 rounded-full flex items-center justify-center mb-1 font-bold">
              <img
                src={homeLogo}
                alt={homeLogo}
                className="w-full h-full object-contain"
              />
            </div>
            <div className="text-center font-semibold">{homeTeam}</div>
          </div>
          <div className="text-xl font-bold text-green-400">VS</div>
          <div className="flex flex-col items-center w-5/12">
            <div className="w-12 h-12 bg-white text-purple-900 rounded-full flex items-center justify-center mb-1 font-bold">
              <img
                src={awayLogo}
                alt={awayLogo}
                className="w-full h-full object-contain"
              />
            </div>
            <div className="text-center font-semibold">{awayTeam}</div>
          </div>
        </div>
        <div>
          <div className="text-center text-sm opacity-70 mb-2">
            Win Probabilities
          </div>
          <div className="grid grid-cols-3 gap-2">
            {outcomes.map((o) => (
              <div
                key={o.key}
                className="bg-white/10 p-2 rounded text-center hover:bg-green-400 transition-colors"
              >
                <div className="text-xs opacity-70">{o.label}</div>
                <div className="font-bold text-green-400">
                  {(o.probability * 100).toFixed(1)}%
                </div>
                <div className="text-xs opacity-70">
                  {probabilityToDecimal(o.probability)}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-purple-950 p-6 rounded-lg w-96 relative">
            <button
              className="absolute top-2 right-2 text-white font-bold"
              onClick={() => setIsModalOpen(false)}
            >
              ✕
            </button>
            <h2 className="text-xl font-bold mb-4">Place Your Bet</h2>
            <div className="mb-4">
              <label className="block mb-2">Select Outcome:</label>
              <select
                className="w-full p-2 rounded text-black"
                value={selectedOutcome}
                onChange={(e) => setSelectedOutcome(e.target.value)}
                disabled={bettingLocked}
              >
                <option value="">-- Select --</option>
                {outcomes.map((o) => (
                  <option key={o.key} value={o.key}>
                    {o.label} ({(o.probability * 100).toFixed(1)}%)
                  </option>
                ))}
              </select>
            </div>
            <div className="mb-4">
              <label className="block mb-2">
                Bet Amount (Balance: {user.account_balance})
              </label>
              <input
                type="number"
                className="w-full p-2 rounded text-black"
                value={betAmount}
                onChange={(e) => setBetAmount(e.target.value)}
                disabled={bettingLocked}
              />
            </div>
            {selectedOutcome && betAmount && (
              <div className="mb-4">
                Potential Payout: {calculatePayout()}
              </div>
            )}
            {error && <div className="text-red-500 mb-2">{error}</div>}
            <button
              className={`w-full font-bold py-2 rounded transition-colors ${bettingLocked
                ? "bg-gray-500 cursor-not-allowed"
                : "bg-green-400 text-purple-900 hover:bg-green-500"
                }`}
              onClick={handlePlaceBet}
              disabled={bettingLocked}
            >
              {bettingLocked ? `Wait ${countdown}s` : "Place Bet"}
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default MatchCard;