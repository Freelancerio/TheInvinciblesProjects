import React from "react";
import { PlayerStatisticsHeaderSection } from "./sections/PlayerStatisticsHeaderSection.jsx";
import { PlayerStatsContainerSection } from "./sections/PlayerStatsContainerSection.jsx";
import { PlayerStatsTableSection } from "./sections/PlayerStatsTableSection.jsx";
import { SearchSection } from "./sections/SearchSection.jsx";

export const PlayerStatistics = () => {
  return (
    <div
      className="flex flex-col items-start relative bg-white"
      data-model-id="47:205"
    >
      <div className="flex flex-col min-h-[800px] items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f]">
        <div className="flex flex-col items-start relative self-stretch w-full flex-[0_0_auto]">
          <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms]">
            <SearchSection />
          </div>

          <div className="items-start justify-center px-40 py-5 flex-1 grow flex relative self-stretch w-full">
            <div className="flex flex-col max-w-[960px] items-start relative flex-1 grow">
              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] w-full">
                <PlayerStatisticsHeaderSection />
              </div>

              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms] w-full">
                <PlayerStatsContainerSection />
              </div>

              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms] w-full">
                <PlayerStatsTableSection />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
