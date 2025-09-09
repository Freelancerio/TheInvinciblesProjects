import React from "react";
import { SearchSection } from "./sections/TeamSearchSection";
import { StatisticsSection } from "./sections/StatisticsSection";
import { TeamInfoSection } from "./sections/TeamInfoSection";
import { UpcomingMatchesSection } from "./sections/UpcomingMatchesSection";
import { WinsSection } from "./sections/WinsSection";

export const TeamStatisticsPage = () => {
    return (
        <div
            className="flex flex-col items-start bg-[#1e1e2f] translate-y-[-1rem] animate-fade-in opacity-0"
            data-model-id="46:2"
        >
            <div className="flex flex-col items-start w-full bg-[#1e1e2f]">
                <div className="flex flex-col items-start w-full">
                    <div className="w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                        <SearchSection />
                    </div>

                    <div className="flex items-start justify-center px-40 py-5 w-full">
                        <div className="flex flex-col max-w-[960px] items-start w-full">
                            <div className="flex flex-col items-start pt-5 pb-3 px-4 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
                                <div className="[font-family:'Public_Sans',Helvetica] font-bold text-[#1e1e2f] text-[22px] tracking-[0] leading-7 w-full">
                                    Team Overview
                                </div>
                            </div>

                            <div className="w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
                                <TeamInfoSection />
                            </div>

                            <div className="flex flex-col items-start pt-5 pb-3 px-4 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms]">
                                <div className="[font-family:'Public_Sans',Helvetica] font-bold text-[#1e1e2f] text-[22px] tracking-[0] leading-7 w-full">
                                    Season Statistics
                                </div>
                            </div>

                            <div className="w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1000ms]">
                                <WinsSection />
                            </div>

                            <div className="flex flex-col items-start pt-5 pb-3 px-4 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1200ms]">
                                <div className="[font-family:'Public_Sans',Helvetica] font-bold text-[#1e1e2f] text-[22px] tracking-[0] leading-7 w-full">
                                    Home vs. Away Performance
                                </div>
                            </div>

                            <div className="w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1400ms]">
                                <StatisticsSection />
                            </div>

                            <div className="flex flex-col items-start pt-5 pb-3 px-4 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1600ms]">
                                <div className="[font-family:'Public_Sans',Helvetica] font-bold text-[#1e1e2f] text-[22px] tracking-[0] leading-7 w-full">
                                    Upcoming Fixtures
                                </div>
                            </div>

                            <div className="w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1800ms]">
                                <UpcomingMatchesSection />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
