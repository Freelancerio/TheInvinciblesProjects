import React from "react";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export const ActionButtonsSection = () => {
  // Data for the matches table
  const matchesData = [
    {
      match: "Orlando Pirates vs Mamelodi Sundowns",
      dateTime: "2025-08-16 15:00",
      action: "Bet Now",
    },
    {
      match: "Stellenbosch vs Orlando Pirates",
      dateTime: "2025-08-16 19:30",
      action: "Bet Now",
    },
    {
      match: "Orlando Pirates vs Orbit College",
      dateTime: "2025-08-22 19:00",
      action: "Bet Now",
    },
  ];

  return (
    <section className="flex flex-col items-start px-4 py-3 self-stretch w-full relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      <div className="flex items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] rounded-lg overflow-hidden border border-solid border-[#d6d1e8]">
        <div className="flex flex-col items-start relative flex-1 grow">
          <Table className="w-full">
            <TableHeader>
              <TableRow className="border-none bg-[#1e1e2f] hover:bg-[#1e1e2f]">
                <TableHead className="px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Match
                </TableHead>
                <TableHead className="px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Date/Time
                </TableHead>
                <TableHead className="px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Action
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {matchesData.map((match, index) => (
                <TableRow
                  key={`match-${index}`}
                  className="h-[72px] border-t border-[#e5e8ea] hover:bg-[#252540] transition-colors"
                >
                  <TableCell className="h-[72px] px-4 py-2 text-center">
                    <div className="[font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                      {match.match}
                    </div>
                  </TableCell>
                  <TableCell className="h-[72px] px-4 py-2 text-center">
                    <div className="[font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                      {match.dateTime}
                    </div>
                  </TableCell>
                  <TableCell className="h-[72px] px-4 py-2 text-center">
                    <Button
                      variant="ghost"
                      className="[font-family:'Inter',Helvetica] font-bold text-[#723ae8] text-sm tracking-[0] leading-[21px] h-auto p-0 hover:bg-transparent hover:text-[#723ae8] transition-colors"
                    >
                      {match.action}
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>
    </section>
  );
};