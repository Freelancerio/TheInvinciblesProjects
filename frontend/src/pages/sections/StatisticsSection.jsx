import React from "react";
import { Card, CardContent } from "@/components/ui/card";

export const StatisticsSection = () => {
  const statisticsData = [
    {
      label: "Home Wins",
      value: "12",
    },
    {
      label: "Away Wins",
      value: "8",
    },
  ];

  return (
    <div className="flex flex-wrap items-start gap-4 p-4 w-full relative">
      {statisticsData.map((stat, index) => (
        <Card
          key={stat.label}
          className={`flex-1 min-w-[158px] border border-[#e5e8eb] rounded-lg translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:${index * 200}ms]`}
        >
          <CardContent className="flex flex-col items-start gap-2 p-6">
            <div className="flex flex-col items-start w-full">
              <div className="[font-family:'Public_Sans',Helvetica] font-medium text-[#e5e8eb] text-base tracking-[0] leading-6">
                {stat.label}
              </div>
            </div>
            <div className="flex flex-col items-start w-full">
              <div className="[font-family:'Public_Sans',Helvetica] font-bold text-[#e5e8eb] text-2xl tracking-[0] leading-[30px]">
                {stat.value}
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  );
};
