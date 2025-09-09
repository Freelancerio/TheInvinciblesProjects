import React from "react";
import { Button } from "@/components/ui/button";

const actionButtons = [
  {
    text: "Deposit Funds",
    className:
      "translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms]",
  },
  {
    text: "View Predictions",
    className:
      "translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]",
  },
];

export const StandingsSection = () => {
  return (
    <section className="flex items-start justify-around self-stretch w-full relative flex-[0_0_auto]">
      <div className="flex flex-wrap items-start justify-between gap-3 px-4 py-3 relative flex-1 grow">
        {actionButtons.map((button, index) => (
          <Button
            key={`action-button-${index}`}
            className={`min-w-[84px] max-w-[480px] h-10 px-4 py-0 bg-[#723ae8] rounded-lg text-[#1e1e2f] text-sm font-bold [font-family:'Inter',Helvetica] tracking-[0] leading-[21px] hover:bg-[#5f2bc4] transition-colors ${button.className}`}
          >
            {button.text}
          </Button>
        ))}
      </div>
    </section>
  );
};
