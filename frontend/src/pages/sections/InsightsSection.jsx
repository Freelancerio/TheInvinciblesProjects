import React from "react";
import { Button } from "@/components/ui/button";

export const InsightsSection = () => {
  const insightButtons = [
    {
      label: "Premier League Fixtures",
      delay: "0ms",
    },
    {
      label: "Premier League Top Scorers",
      delay: "200ms",
    },
  ];

  return (
    <section className="flex items-start justify-around self-stretch w-full relative flex-[0_0_auto]">
      <div className="flex flex-wrap items-start justify-between gap-[12px_12px] px-4 py-3 relative flex-1 grow">
        {insightButtons.map((button, index) => (
          <Button
            key={`insight-${index}`}
            className={`translate-y-[-1rem] animate-fade-in opacity-0 min-w-[84px] max-w-[480px] h-10 px-4 py-0 bg-[#723ae8] hover:bg-[#5f2bc4] rounded-lg [font-family:'Inter',Helvetica] font-bold text-[#1e1e2f] text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap transition-colors`}
            style={{ "--animation-delay": button.delay }}
            variant="default"
          >
            {button.label}
          </Button>
        ))}
      </div>
    </section>
  );
};
