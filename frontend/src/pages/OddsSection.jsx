import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

export const OddsSection = () => {
    const matchesData = [
        {
            match: "Orlando Pirate vs Kaizer Chiefs",
            dateTime: "2024-07-20 20:00",
            odds: "2.10 / 3.20 / 3.50",
        },
        {
            match: "Sekhukhune FC vs Orbit College FC",
            dateTime: "2024-07-21 17:30",
            odds: "2.30 / 3.10 / 3.30",
        },
        {
            match: "Stellenbosch vs Mamelodi Sundowns",
            dateTime: "2024-07-22 19:45",
            odds: "1.90 / 3.40 / 4.00",
        },
        {
            match: "Siwelele vs Richard's Bay",
            dateTime: "2024-07-23 21:00",
            odds: "2.50 / 3.00 / 3.10",
        },
        {
            match: "Supersport United vs Cape Town Spurs",
            dateTime: "2024-07-24 18:15",
            odds: "2.00 / 3.30 / 3.70",
        },
    ];

    return (
        <section className="flex flex-col items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto]">
            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] flex items-start self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] border border-solid border-white relative rounded-lg overflow-hidden">
                <div className="flex flex-col items-start relative flex-1 grow">
                    <Table className="w-full">
                        <TableHeader>
                            <TableRow className="border-none hover:bg-transparent">
                                <TableHead className="w-[317px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Match
                                </TableHead>
                                <TableHead className="w-[305px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Date & Time
                                </TableHead>
                                <TableHead className="w-[304px] px-4 py-3 [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] h-auto">
                                    Odds
                                </TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {matchesData.map((match, index) => (
                                <TableRow
                                    key={index}
                                    className="h-[72px] border-t border-solid border-[#e5e8ea] hover:bg-[#2a2a3f] transition-colors"
                                >
                                    <TableCell className="w-[317px] px-4 py-2 [font-family:'Space_Grotesk',Helvetica] font-normal text-white text-sm tracking-[0] leading-[21px] h-[72px]">
                                        {match.match}
                                    </TableCell>
                                    <TableCell className="w-[305px] px-4 py-2 [font-family:'Space_Grotesk',Helvetica] font-normal text-white text-sm tracking-[0] leading-[21px] h-[72px]">
                                        {match.dateTime}
                                    </TableCell>
                                    <TableCell className="w-[304px] px-4 py-2 [font-family:'Space_Grotesk',Helvetica] font-normal text-white text-sm tracking-[0] leading-[21px] h-[72px]">
                                        {match.odds}
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
