import React from "react";
import { Badge } from "@/components/ui/badge";

export const UserGreetingSection = () => {
  const badges = [
    {
      text: "Premier League Top Goalkeepers",
      delay: "0ms",
    },
    {
      text: "Premier League Discipline",
      delay: "200ms",
    },
  ];

  return (
    <section className="flex items-start justify-around self-stretch w-full relative flex-[0_0_auto]">
      <div className="flex flex-wrap items-start justify-between gap-3 px-4 py-3 relative flex-1 grow">
        {badges.map((badge, index) => (
          <div
            key={index}
            className="translate-y-[-1rem] animate-fade-in opacity-0"
            style={{ "--animation-delay": badge.delay }}
          >
            <Badge className="inline-flex min-w-[84px] max-w-[480px] h-10 items-center justify-center px-4 py-0 bg-[#723ae8] rounded-lg overflow-hidden cursor-pointer hover:bg-[#6332d4] transition-colors">
              <span className="[font-family:'Inter',Helvetica] font-bold text-[#1e1e2f] text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap">
                {badge.text}
              </span>
            </Badge>
          </div>
        ))}
      </div>
    </section>
  );
};
