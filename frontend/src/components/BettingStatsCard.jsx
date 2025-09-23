import React from "react";

export default function BettingStatsCard() {
  const stats = [
    { value: "47", label: "Bets Placed" },
    { value: "£342.50", label: "Total Profit" },
    { value: "63.8%", label: "Win Rate" },
    { value: "£25.80", label: "Average Bet" },
  ];

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
