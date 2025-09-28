import React from "react";

export default function BetHistoryCard() {
  const bets = [
    { date: "12/05/23", match: "Arsenal vs Brentford", bet: "Arsenal to win", stake: "£20.00", odds: "1.75", potential: "£35.00", status: "won" },
    { date: "11/05/23", match: "Chelsea vs Fulham", bet: "Under 2.5 goals", stake: "£15.00", odds: "1.90", potential: "£28.50", status: "won" },
    { date: "10/05/23", match: "Liverpool vs Wolves", bet: "Liverpool -1.5", stake: "£25.00", odds: "2.10", potential: "£52.50", status: "lost" },
    { date: "09/05/23", match: "Man City vs Everton", bet: "Man City to win", stake: "£30.00", odds: "1.45", potential: "£43.50", status: "pending" },
  ];

  const statusColors = { won: "#00ff85", lost: "#e90052", pending: "yellow" };

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
              <td className="py-3 px-4 font-semibold" style={{ color: statusColors[bet.status] || "white" }}>{bet.status.charAt(0).toUpperCase() + bet.status.slice(1)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
