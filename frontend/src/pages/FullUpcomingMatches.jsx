import React, { useState } from "react";
import Header from "../components/Header";
import UpcomingMatches from "../components/UpcomingMatches";

const FullUpcomingMatches = () => {
  const [season, setSeason] = useState(2025);

  return (
      <div
          className="min-h-screen text-white"
          style={{
            background:
                "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://ocdn.eu/images/pulscms/YmY7MDA_/de3425d352c03819defca8326cc637ce.jpeg') center/cover no-repeat fixed",
          }}
      >
        <Header />

        <div className="container mx-auto px-4 sm:px-6 py-6 sm:py-8">
          {/* Season Selector */}
          <div className="mb-4 sm:mb-6 flex flex-col sm:flex-row sm:items-center gap-2 sm:gap-3">
            <label htmlFor="season" className="text-secondary font-bold text-base sm:text-lg">
              Season:
            </label>
            <select
                id="season"
                value={season}
                onChange={(e) => setSeason(Number(e.target.value))}
                className="bg-[#38003c] text-white px-3 py-2 rounded border border-[#fff3]/20 text-sm sm:text-base"
            >
              <option value={2023}>2023/24</option>
              <option value={2024}>2024/25</option>
              <option value={2025}>2025/26</option>
            </select>
          </div>

          <UpcomingMatches paginated={true} size={20} season={season} />
        </div>
      </div>
  );
};

export default FullUpcomingMatches;