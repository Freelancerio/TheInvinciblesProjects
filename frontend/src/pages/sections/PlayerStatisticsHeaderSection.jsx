import { ChevronDownIcon } from "lucide-react";
import React, { useState } from "react";
import { Button } from "@/components/ui/button";

export const PlayerStatisticsHeaderSection = () => {
  const [selectedFilter, setSelectedFilter] = useState("League");

  const filterOptions = [
    { id: "League", label: "League" },
    { id: "Team", label: "Team" },
    { id: "Position", label: "Position" },
    { id: "Top Scorers", label: "Top Scorers" },
    { id: "Most Assists", label: "Most Assists" },
  ];

  return (
    <section className="flex flex-wrap items-start gap-3 pl-3 pr-4 py-3 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      {filterOptions.map((option, index) => (
        <Button
          key={option.id}
          variant="ghost"
          size="sm"
          onClick={() => setSelectedFilter(option.id)}
          className={`h-8 px-4 py-0 gap-2 rounded-lg font-medium text-sm whitespace-nowrap transition-colors ${
            selectedFilter === option.id
              ? "bg-[#49709b] text-[#b0bec5] hover:bg-[#49709b]/90"
              : "bg-[#ededed] text-[#b0bec5] hover:bg-[#ededed]/80"
          } [font-family:'Public_Sans',Helvetica] translate-y-[-1rem] animate-fade-in opacity-0`}
          style={{ "--animation-delay": `${400 + index * 100}ms` }}
        >
          {option.label}
          <ChevronDownIcon className="w-4 h-4" />
        </Button>
      ))}
    </section>
  );
};
