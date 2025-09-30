import React, { useState, useEffect, useContext } from "react";
import { UserContext } from "../UserContext";

export default function BetHistoryCard() {
  const { user } = useContext(UserContext);
  const [bets, setBets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const statusColors = { won: "#00ff85", lost: "#e90052", pending: "yellow" };
useEffect(() => {
  const fetchBets = async () => {
    try {
      const token = localStorage.getItem("authToken");
      if (!token) throw new Error("No auth token found");

      const res = await fetch(`http://localhost:8080/api/bets/user/${user.firebaseId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error(`HTTP error ${res.status}`);

      const data = await res.json();
      console.log(data);

      // Use DTO fields directly
      setBets(data);
      setLoading(false);
    } catch (err) {
      console.error(err);
      setError(err.message || "Failed to fetch bets");
      setLoading(false);
    }
  };

  if (user && user.firebaseId) {
    fetchBets();
  }
}, [user]);


  if (loading) return <div>Loading betting history...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 col-span-1 lg:col-span-2 overflow-x-auto">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">Recent Betting History</h2>
      </div>
      <table className="w-full min-w-[700px] border-collapse">
        <thead>
          <tr className="bg-white/20">
            {["Date", "Match", "Bet", "Stake", "Odds", "Potential Win", "Status"].map((h, i) => (
              <th key={i} className="text-left py-3 px-4 font-semibold">{h}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {bets.map((bet, idx) => (
            <tr key={idx} className="hover:bg-white/10">
              <td className="py-3 px-4">{bet.date}</td>
              <td className="py-3 px-4">{bet.match}</td>
              <td className="py-3 px-4">{bet.bet}</td>
              <td className="py-3 px-4">{bet.stake}</td>
              <td className="py-3 px-4">{bet.odds}</td>
              <td className="py-3 px-4">{bet.potential}</td>
              <td
                className="py-3 px-4 font-semibold"
                style={{ color: statusColors[bet.status] || "white" }}
              >
                {bet.status.charAt(0).toUpperCase() + bet.status.slice(1).toLowerCase()}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
