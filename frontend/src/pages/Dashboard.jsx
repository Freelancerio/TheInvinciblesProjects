import React from "react";
import { Button } from "@/components/ui/button";
import { ActionButtonsSection } from "./sections/ActionButtonsSection.jsx";
import { FavoriteTeamsSection } from "./sections/FavouriteTeamsSection.jsx";
import { InsightsSection } from "./sections/InsightsSection.jsx";
import { LeagueInsightsSection } from "./sections/LeagueInsightsSection.jsx";
import { QuickActionsSection } from "./sections/QuickActionsSection.jsx";
import { StandingsSection } from "./sections/StandingsSection.jsx";
import { UserGreetingSection } from "./sections/UserGreetingSection.jsx";

export const Dashboard = () => {
  return (
    <div className="flex flex-col items-start relative bg-[#0f0f1a] min-h-screen">
      {/* Header Section */}
      <div className="w-full">
        <LeagueInsightsSection />
      </div>

      {/* Main Content */}
      <div className="flex-1 w-full px-4 sm:px-8 lg:px-16 py-8">
        <div className="max-w-6xl mx-auto">
          {/* Welcome Section */}
          <div className="mb-8">
            <h1 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[32px] tracking-[0] leading-10 mb-6">
              Welcome back, Luvo
            </h1>

            {/* Stats Cards */}
            <QuickActionsSection />
          </div>

          {/* Favorite Teams Section */}
          <div className="mb-8">
            <h2 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[22px] tracking-[0] leading-7 mb-4">
              Favorite Teams - Upcoming Matches
            </h2>
            <ActionButtonsSection />
          </div>

          {/* Followed Leagues Section */}
          <div className="mb-8">
            <h2 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[22px] tracking-[0] leading-7 mb-4">
              Followed Leagues - Standings
            </h2>
            <FavoriteTeamsSection />

            {/* View More Standings Button */}
            <div className="flex justify-end mt-4">
              <Button className="min-w-[84px] max-w-[480px] h-auto bg-[#eae8f2] hover:bg-[#d5d3dd] text-[#49709b] font-bold text-sm px-4 py-2 rounded-lg transition-colors">
                View More Standings
              </Button>
            </div>
          </div>

          {/* League Insights Section */}
          <div className="mb-8">
            <h2 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[22px] tracking-[0] leading-7 mb-4">
              League Insights
            </h2>
            <div className="flex flex-wrap gap-3 mb-6">
              <InsightsSection />
            </div>
            <div className="flex flex-wrap gap-3">
              <UserGreetingSection />
            </div>
          </div>

          {/* Quick Actions Section */}
          <div className="mb-8">
            <h2 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-[22px] tracking-[0] leading-7 mb-4">
              Quick Actions
            </h2>
            <StandingsSection />
          </div>
        </div>
      </div>
    </div>
  );
};