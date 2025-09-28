import React from "react";

export default function LeaderboardCard() {
  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 text-center">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">Predictions Leaderboard</h2>
      </div>
      <div className="py-8">
        <div className="text-5xl font-extrabold text-[#00ff85] mb-2">27</div>
        <div className="text-lg mb-4">Your Current Position</div>
        <a href="#" className="text-[#00ff85] font-semibold inline-flex items-center gap-2 hover:underline" onClick={e => { e.preventDefault(); alert("Redirecting to leaderboard page"); }}>
          View Full Leaderboard <i className="fas fa-arrow-right"></i>
        </a>
      </div>
    </div>
  );
}
