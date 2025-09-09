import React from "react";
import { Badge } from "@/components/ui/badge";



import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

export const MatchUpdatesSection = () => {
    const matchData = [
        {
            match: "Amazulu vs Durban City",
            score: "1-1",
            status: "Live",
        },
        {
            match: "Ts Galaxy ft Golden Arrows",
            score: "0-2",
            status: "Live",
        },
        {
            match: "Polokwane City ft Chippa United",
            score: "2-0",
            status: "Live",
        },
    ];

    return (
        <section className="flex flex-col items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
            <div className="flex items-start self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] border border-solid border-white relative rounded-lg overflow-hidden">
                <div className="flex flex-col items-start relative flex-1 grow">
                    <Table className="w-full">
                        <TableHeader>
                            <TableRow className="border-none hover:bg-transparent">
                                <TableHead className="w-[356px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Match
                                </TableHead>
                                <TableHead className="w-[349px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Score
                                </TableHead>
                                <TableHead className="w-[221px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Status
                                </TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {matchData.map((match, index) => (
                                <TableRow
                                    key={`match-${index}`}
                                    className="h-[72px] border-t border-[#e5e8ea] hover:bg-transparent"
                                >
                                    <TableCell className="w-[356px] h-[72px] items-center justify-center px-4 py-2 [font-family:'Space_Grotesk',Helvetica] font-normal text-white text-sm tracking-[0] leading-[21px]">
                                        {match.match}
                                    </TableCell>
                                    <TableCell className="w-[349px] h-[72px] items-center justify-center px-4 py-2 [font-family:'Space_Grotesk',Helvetica] font-normal text-white text-sm tracking-[0] leading-[21px]">
                                        {match.score}
                                    </TableCell>
                                    <TableCell className="w-[221px] h-[72px] items-center justify-center px-4 py-2">
                                        <Badge className="min-w-[84px] max-w-[480px] h-8 items-center justify-center px-4 py-0 bg-[#f4f6fc] hover:bg-[#f4f6fc] rounded-lg [font-family:'Space_Grotesk',Helvetica] font-medium text-[#1e1e2f] text-sm text-center tracking-[0] leading-[21px]">
                                            {match.status}
                                        </Badge>
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
