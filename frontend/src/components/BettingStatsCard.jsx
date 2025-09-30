import React, { useContext, useEffect, useState } from "react";
import { UserContext } from "../UserContext";

export default function BettingStatsCard() {
  const { user } = useContext(UserContext);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchStats = async () => {
      if (!user?.firebaseId) return;

      try {
        const token = localStorage.getItem("authToken");
        const res = await fetch(
          `http://localhost:8080/api/bets/stats/${user.firebaseId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }
        );

        if (!res.ok) {
          throw new Error(`HTTP error ${res.status}`);
        }

        const data = await res.json();
        setStats([
          { value: data.totalBetsPlaced, label: "Bets Placed" },
          { value: `R${data.totalProfit}`, label: "Total Profit" },
          { value: `${data.winRate}%`, label: "Win Rate" },
          { value: `R${data.averageBet}`, label: "Average Bet" },
        ]);
      } catch (err) {
        console.error(err);
        setError("Failed to load betting stats");
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [user]);

  if (loading) return <div>Loading stats...</div>;
  if (error) return <div className="text-red-500">{error}</div>;
  if (!stats) return null;

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">Betting Statistics</h2>
      </div>
      <div className="grid grid-cols-2 gap-4">
        {stats.map((stat, idx) => (
          <div key={idx} className="bg-white/10 rounded-lg p-5 text-center">
            <div className="text-2xl font-bold text-[#00ff85] mb-1">{stat.value}</div>
            <div className="opacity-80 text-sm">{stat.label}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
