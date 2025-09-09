import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

export const WinsSection = () => {
    const statisticsData = [
        { statistic: "Matches Played", value: "30" },
        { statistic: "Wins", value: "20" },
        { statistic: "Draws", value: "5" },
        { statistic: "Losses", value: "5" },
        { statistic: "Goals For", value: "50" },
        { statistic: "Goals Against", value: "20" },
        { statistic: "Goal Difference", value: "+30" },
        { statistic: "Points", value: "65" },
    ];

    return (
        <section className="flex flex-col items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto]">
            <div className="self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] overflow-hidden flex items-start relative rounded-lg border border-solid border-[#e5e8eb] translate-y-[-1rem] animate-fade-in opacity-0">
                <div className="flex flex-col items-start relative flex-1 grow">
                    <Table className="w-full">
                        <TableHeader className="bg-[#eae8f2]">
                            <TableRow className="border-none">
                                <TableHead className="flex flex-col items-start px-4 py-3 relative [font-family:'Public_Sans',Helvetica] font-medium text-[#141414] text-sm tracking-[0] leading-[21px] h-auto">
                                    Statistic
                                </TableHead>
                                <TableHead className="flex flex-col items-start px-4 py-3 relative [font-family:'Public_Sans',Helvetica] font-medium text-[#141414] text-sm tracking-[0] leading-[21px] h-auto">
                                    Value
                                </TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {statisticsData.map((row, index) => (
                                <TableRow
                                    key={`stat-${index}`}
                                    className="border-t border-solid border-[#e5e8ea] h-[72px] translate-y-[-1rem] animate-fade-in opacity-0"
                                    style={{ "--animation-delay": `${(index + 1) * 100}ms` }}
                                >
                                    <TableCell className="flex flex-col items-center justify-center px-4 py-2 relative [font-family:'Public_Sans',Helvetica] font-normal text-[#e5e8eb] text-sm tracking-[0] leading-[21px] h-[72px]">
                                        {row.statistic}
                                    </TableCell>
                                    <TableCell className="flex flex-col items-center justify-center px-4 py-2 relative [font-family:'Public_Sans',Helvetica] font-normal text-[#e5e8eb] text-sm tracking-[0] leading-[21px] h-[72px]">
                                        {row.value}
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
