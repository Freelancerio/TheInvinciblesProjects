import React from "react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export const FilterOptionsSection = () => {
  const fixtures = [
    {
      date: "2024-07-20 15:00",
      teams: "Marumo Gallants vs Sekhukhune",
      league: "Betway Premiership",
      status: "Scheduled",
      score: "-",
      bet: "Place Bet",
    },
    {
      date: "2024-07-20 17:30",
      teams: "Orbit College vs Sekhukhune",
      league: "Betway Premiership",
      status: "Live",
      score: "1-0",
      bet: "Concluded",
    },
    {
      date: "2024-07-20 20:00",
      teams: "Durban City vs Golden Arrows",
      league: "Betway Premiership",
      status: "Completed",
      score: "2-1",
      bet: "Concluded",
    },
    {
      date: "2024-07-21 14:00",
      teams: "Chippa United vs TS Galaxy",
      league: "Betway Premiership",
      status: "Scheduled",
      score: "-",
      bet: "Place Bet",
    },
    {
      date: "2024-07-21 16:30",
      teams: "Siwelele vs Polokwane City",
      league: "Betway Premiership",
      status: "Live",
      score: "0-0",
      bet: "Place Bet",
    },
    {
      date: "2024-07-21 19:00",
      teams: "Orlando Pirates vs Stellenbosch",
      league: "Betway Premiership",
      status: "Completed",
      score: "3-2",
      bet: "Concluded",
    },
    {
      date: "2024-07-22 15:30",
      teams: "Amazulu vs Marumo Gallants",
      league: "Betway Premiership",
      status: "Scheduled",
      score: "-",
      bet: "Place Bet",
    },
    {
      date: "2024-07-22 18:00",
      teams: "Mamelodi Sundowns vs Magesi",
      league: "Betway Premiership",
      status: "Live",
      score: "2-1",
      bet: "Concluded",
    },
    {
      date: "2024-07-22 20:30",
      teams: "Polokwane Durban City",
      league: "Betway Premiership",
      status: "Completed",
      score: "1-0",
      bet: "Concluded",
    },
    {
      date: "2024-07-23 16:00",
      teams: "Orlando Pirates vs Orbit College",
      league: "Betway Premiership",
      status: "Scheduled",
      score: "-",
      bet: "Place Bet",
    },
  ];

  const getStatusVariant = (status) => {
    switch (status) {
      case "Live":
        return "default";
      case "Completed":
        return "default";
      case "Scheduled":
        return "default";
      default:
        return "default";
    }
  };

  return (
    <section className="flex flex-col items-start px-4 py-3 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      <div className="w-full bg-[#1e1e2f] rounded-lg overflow-hidden border border-solid border-[#d6d1e8]">
        <Table>
          <TableHeader>
            <TableRow className="border-[#1e1e2f] hover:bg-transparent">
              <TableHead className="w-[155px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                Date
              </TableHead>
              <TableHead className="w-[158px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                Teams
              </TableHead>
              <TableHead className="w-[183px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                League
              </TableHead>
              <TableHead className="w-[163px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                Status
              </TableHead>
              <TableHead className="w-[154px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                Score
              </TableHead>
              <TableHead className="w-[113px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                Bet
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {fixtures.map((fixture, index) => (
              <TableRow
                key={index}
                className="h-[72px] border-t border-[#1e1e2f] hover:bg-transparent"
              >
                <TableCell className="w-[155px] px-4 py-2 [font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                  {fixture.date}
                </TableCell>
                <TableCell className="w-[158px] px-4 py-2 [font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                  {fixture.teams}
                </TableCell>
                <TableCell className="w-[183px] px-4 py-2 [font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                  {fixture.league}
                </TableCell>
                <TableCell className="w-[163px] px-4 py-2">
                  <Badge
                    variant={getStatusVariant(fixture.status)}
                    className="min-w-[84px] max-w-[480px] h-8 bg-[#723ae8] text-[#b0bec5] [font-family:'Inter',Helvetica] font-medium text-sm text-center tracking-[0] leading-[21px] hover:bg-[#723ae8]"
                  >
                    {fixture.status}
                  </Badge>
                </TableCell>
                <TableCell className="w-[154px] px-4 py-2 [font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                  {fixture.score}
                </TableCell>
                <TableCell className="w-[113px] px-4 py-2">
                  {fixture.bet === "Place Bet" ? (
                    <Button
                      variant="ghost"
                      className="h-auto p-0 [font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-sm tracking-[0] leading-[21px] hover:bg-transparent hover:text-[#b0bec5]"
                    >
                      {fixture.bet}
                    </Button>
                  ) : (
                    <span className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                      {fixture.bet}
                    </span>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </section>
  );
};
