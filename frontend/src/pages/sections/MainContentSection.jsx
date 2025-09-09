import { ChevronDownIcon } from "lucide-react";
import React from "react";
import { Button } from "@/components/ui/button";

export const MainContentSection = () => {
  const filterButtons = [
    { label: "Date Range" },
    { label: "League" },
    { label: "Team" },
    { label: "Status" },
    { label: "Clear Filters" },
  ];

  return (
    <section className="flex flex-wrap items-start gap-3 pl-3 pr-4 py-3 w-full relative translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      {filterButtons.map((button, index) => (
        <Button
          key={button.label}
          variant="secondary"
          className={`h-8 bg-[#723ae8] hover:bg-[#5f2bc4] text-[#b0bec5] hover:text-white font-medium text-sm px-4 py-0 rounded-lg border-0 transition-colors translate-y-[-1rem] animate-fade-in opacity-0`}
          style={{ "--animation-delay": `${400 + index * 100}ms` }}
        >
          <span className="[font-family:'Inter',Helvetica] whitespace-nowrap">
            {button.label}
          </span>
          <ChevronDownIcon className="ml-2 h-4 w-4" />
        </Button>
      ))}
    </section>
  );
};
