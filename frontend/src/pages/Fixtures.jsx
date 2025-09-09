import React from "react";
import { BetTableSection } from "./sections/BetTableSection";
import { CalendarViewSection } from "./sections/CalenderViewSection.jsx";
import { FilterOptionsSection } from "./sections/FilterOptionsSection.jsx";
import { FootballFixturesSection } from "./sections/FootballFixturesSection.jsx";
import { MainContentSection } from "./sections/MainContentSection.jsx";

export const Fixtures = () => {
  return (
    <div
      className="flex flex-col items-start relative bg-white"
      data-model-id="1:788"
    >
      <div className="flex flex-col min-h-[800px] items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f]">
        <div className="flex flex-col items-start self-stretch w-full relative flex-[0_0_auto]">
          <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms] w-full">
            <FootballFixturesSection />
          </div>

          <div className="items-start justify-center px-40 py-5 flex-1 grow flex relative self-stretch w-full">
            <div className="flex flex-col max-w-[960px] items-start relative flex-1 grow mb-[-1.00px]">
              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] w-full">
                <MainContentSection />
              </div>

              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms] w-full">
                <FilterOptionsSection />
              </div>

              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms] w-full">
                <BetTableSection />
              </div>

              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms] w-full">
                <CalendarViewSection />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
