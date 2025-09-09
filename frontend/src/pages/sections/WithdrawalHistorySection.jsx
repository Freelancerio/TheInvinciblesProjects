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

export const WithdrawalHistorySection = () => {
    const withdrawalData = [
        {
            date: "05/15/2024",
            method: "Bank Transfer",
            amount: "R500",
            status: "Completed",
        },
        {
            date: "04/20/2024",
            method: "E-Wallet",
            amount: "R200",
            status: "Completed",
        },
        {
            date: "03/10/2024",
            method: "Credit/Debit Card",
            amount: "R100",
            status: "Completed",
        },
    ];

    return (
        <section className="flex flex-col items-start px-4 py-3 w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <div className="w-full bg-[#1e1e2f] rounded-lg border border-[#e8edf5] overflow-hidden">
                <Table>
                    <TableHeader>
                        <TableRow className="border-b-0 hover:bg-transparent">
                            <TableHead className="w-[250px] px-4 py-3 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-medium text-sm">
                                Date
                            </TableHead>
                            <TableHead className="w-[253px] px-4 py-3 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-medium text-sm">
                                Method
                            </TableHead>
                            <TableHead className="w-[236px] px-4 py-3 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-medium text-sm">
                                Amount
                            </TableHead>
                            <TableHead className="w-[187px] px-4 py-3 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-medium text-sm">
                                Status
                            </TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {withdrawalData.map((withdrawal, index) => (
                            <TableRow
                                key={index}
                                className="h-[72px] border-t border-[#e5e8ea] hover:bg-transparent"
                            >
                                <TableCell className="px-4 py-2 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-normal text-sm">
                                    {withdrawal.date}
                                </TableCell>
                                <TableCell className="px-4 py-2 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-normal text-sm">
                                    {withdrawal.method}
                                </TableCell>
                                <TableCell className="px-4 py-2 text-[#e8edf5] [font-family:'Lexend',Helvetica] font-normal text-sm">
                                    {withdrawal.amount}
                                </TableCell>
                                <TableCell className="px-4 py-2">
                                    <Badge
                                        variant="secondary"
                                        className="h-8 min-w-[84px] bg-[#e8edf4] text-[#1e1e2f] [font-family:'Lexend',Helvetica] font-medium text-sm hover:bg-[#e8edf4]"
                                    >
                                        {withdrawal.status}
                                    </Badge>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </section>
    );
};
