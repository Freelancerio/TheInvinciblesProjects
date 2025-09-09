import React from "react";
import { Card, CardContent } from "@/components/ui/card";

export const QuickActionsSection = () => {
  const metricsData = [
    {
      title: "Total Bets Placed",
      value: "125",
    },
    {
      title: "Win Rate",
      value: "68%",
    },
    {
      title: "Total Winnings",
      value: "R 15,250",
    },
    {
      title: "Available Balance",
      value: "R 1,300",
    },
  ];

  return (
    <section className="flex flex-wrap items-start gap-4 p-4 w-full">
      {metricsData.map((metric, index) => (
        <Card
          key={index}
          className={`flex-1 min-w-[158px] border-[#d6d1e8] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:${index * 200}ms]`}
        >
          <CardContent className="p-6 space-y-2">
            <div className="flex flex-col">
              <div className="[font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-base leading-6">
                {metric.title}
              </div>
            </div>
            <div className="flex flex-col">
              <div className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-2xl leading-[30px]">
                {metric.value}
              </div>
            </div>
          </CardContent>
        </Card>
      ))}
    </section>
  );
};
