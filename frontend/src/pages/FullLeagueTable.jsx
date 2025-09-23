import React, { useState } from "react";
import LeagueTable from "../components/LeagueTable";
import Header from "../components/Header";

const FullLeagueTable = () => {
  const [season, setSeason] = useState(2025); // default season

  return (
    <div className="min-h-screen text-white" style={{
          background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3') center/cover no-repeat fixed"
        }}>
          <Header />
        <div className="container mx-auto px-5 py-8">
          {/* Season Selector */}
          <div className="mb-6 flex items-center gap-3">
            <label htmlFor="season" className="text-secondary font-semibold text-lg">Season:</label>
            <select
              id="season"
              value={season}
              onChange={(e) => setSeason(Number(e.target.value))}
             className="bg-[rgba(255,255,255,0.1)] backdrop-blur-md border border-[rgba(255,255,255,0.2)] 
               text-white px-4 py-2 rounded-lg focus:outline-none focus:ring-2 
               focus:ring-secondary"
            >
              <option value={2023}>2023/24</option>
              <option value={2024}>2024/25</option>
              <option value={2025}>2025/26</option>
            </select>
          </div>

          <LeagueTable topN={null} season={season} />
        </div>
    </div>
  );
};

export default FullLeagueTable;

