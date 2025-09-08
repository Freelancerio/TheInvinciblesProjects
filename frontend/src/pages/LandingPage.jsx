import React from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { LiveUpdatesSection } from "./LiveUpdatesSection";
import { MatchUpdatesSection } from "./MatchUpdatesSection";
import { OddsSection } from "./OddsSection";
import { UpcomingMatchesSection } from "./UpcomingMatchesSection";

export const LandingPage = () => {
  const navigationButtons = [];

  return (
    <main className="bg-[#0f0f1a] min-h-screen w-full" data-model-id="59:28">
      {/* full width wrapper with responsive padding */}
      <div className="w-full min-h-screen px-4 sm:px-8 lg:px-16">
        <div className="relative min-h-screen flex flex-col">
          <section className="flex flex-col w-full flex-grow bg-[#1e1e2f] rounded-xl shadow-lg p-4 sm:p-6 lg:p-10">
            <div className="flex flex-col w-full">
              {/* Upcoming Matches */}
              <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms]">
                <UpcomingMatchesSection />
              </div>

              {/* Card Wrapper */}
              <div className="flex justify-center py-5 w-full">
                <Card className="w-full bg-transparent border-0 shadow-[0px_18px_56px_#0000005b,0px_10px_18px_#0000002d,0px_4px_6px_#00000024]">
                  <CardContent className="p-0 space-y-0">
                    <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                      <LiveUpdatesSection />
                    </div>

                    <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
                      <OddsSection />
                    </div>

                    <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1000ms]">
                      <MatchUpdatesSection />
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          </section>

          {/* navigation  */}
          <nav className="absolute top-[97px] left-1/2 transform -translate-x-1/2 flex gap-4">
            {navigationButtons.map((button, index) => (
              <Button
                key={`nav-button-${index}`}
                className="min-w-[120px] h-10 px-4 bg-[#b0bec5] hover:bg-[#9e9e9e] rounded-lg text-[#1e1e2f] font-bold text-sm whitespace-nowrap transition-colors"
                variant="secondary"
              >
                {button.text}
              </Button>
            ))}
          </nav>
        </div>
      </div>
    </main>
  );
};
