import React, { useState } from "react";
import Header from "../components/Header";
import RecentMatches from "../components/RecentMatches";

const FullRecentMatches = () => {
  const [season, setSeason] = useState(2025); // default season

  return (
    <div
      className="min-h-screen text-white"
      style={{
        background:
          "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://s3-alpha.figma.com/hub/file/2222013829005813939/63bf05c3-a41c-490f-b461-93a34d0b8e68-cover.png') center/cover no-repeat fixed",
      }}
    >
      <Header />

      <div className="container mx-auto px-5 py-8">
        {/* Season Selector */}
        <div className="mb-6 flex items-center gap-3">
          <label htmlFor="season" className="text-secondary font-bold text-lg">
            Season:
          </label>
          <select
            id="season"
            value={season}
            onChange={(e) => setSeason(Number(e.target.value))}
            className="bg-[#38003c] text-white px-3 py-2 rounded border border-[#fff3]/20"
          >
            <option value={2023}>2023/24</option>
            <option value={2024}>2024/25</option>
            <option value={2025}>2025/26</option>
          </select>
        </div>

        <RecentMatches paginated={true} season={season} size={20} />

      </div>
    </div>
  );
};

export default FullRecentMatches;
//complete
