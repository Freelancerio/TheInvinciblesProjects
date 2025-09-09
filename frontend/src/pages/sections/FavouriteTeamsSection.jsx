import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

export const FavoriteTeamsSection = () => {
  const teamsData = [
    {
      rank: 1,
      team: "Siwelele FC",
      points: 85,
      goalDifference: "+50",
    },
    {
      rank: 2,
      team: "Marumo Gallants",
      points: 82,
      goalDifference: "+45",
    },
    {
      rank: 3,
      team: "Kaizer Chiefs",
      points: 78,
      goalDifference: "+40",
    },
    {
      rank: 4,
      team: "Mamelodi Sundowns",
      points: 75,
      goalDifference: "+35",
    },
    {
      rank: 5,
      team: "Amazulu FC",
      points: 70,
      goalDifference: "+30",
    },
  ];

  return (
    <section className="flex flex-col items-start px-4 py-3 self-stretch w-full relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
      <div className="flex items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] rounded-lg overflow-hidden border border-solid border-[#d6d1e8]">
        <div className="flex flex-col items-start relative flex-1 grow">
          <Table className="w-full">
            <TableHeader>
              <TableRow className="border-none bg-[#1e1e2f] hover:bg-[#1e1e2f]">
                <TableHead className="w-[221px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Rank
                </TableHead>
                <TableHead className="w-60 px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Team
                </TableHead>
                <TableHead className="w-[225px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Points
                </TableHead>
                <TableHead className="w-60 px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] text-left">
                  Goal Difference
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {teamsData.map((team, index) => (
                <TableRow
                  key={`team-${index}`}
                  className="h-[72px] border-t border-[#e5e8ea] hover:bg-[#252540] transition-colors"
                >
                  <TableCell className="w-[221px] h-[72px] px-4 py-2 text-center">
                    <div className="font-normal text-sm leading-[21px] [font-family:'Inter',Helvetica] text-[#b0bec5] tracking-[0]">
                      {team.rank}
                    </div>
                  </TableCell>
                  <TableCell className="w-60 h-[72px] px-4 py-2 text-center">
                    <div className="[font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                      {team.team}
                    </div>
                  </TableCell>
                  <TableCell className="w-[225px] h-[72px] px-4 py-2 text-center">
                    <div className="font-normal text-sm leading-[21px] [font-family:'Inter',Helvetica] text-[#b0bec5] tracking-[0]">
                      {team.points}
                    </div>
                  </TableCell>
                  <TableCell className="w-60 h-[72px] px-4 py-2 text-center">
                    <div className="[font-family:'Inter',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                      {team.goalDifference}
                    </div>
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
