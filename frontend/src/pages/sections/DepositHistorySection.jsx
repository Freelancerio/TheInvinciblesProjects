import React from "react";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

const paymentMethods = [
    {
        name: "Visa",
        icon: "https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-5--frame-0-2.svg",
        details: "Processing Time: Instant | Fee: None",
    },
    {
        name: "MasterCard",
        icon: "https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-5--frame-0.svg",
        details: "Processing Time: Instant | Fee: None",
    },
    {
        name: "PayPal",
        icon: "https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-5--frame-0.svg",
        details: "Processing Time: Instant | Fee: None",
    },
];

const depositHistory = [
    {
        date: "2024-07-20",
        amount: "R5000",
        status: "Completed",
        transactionId: "TXN123456",
    },
    {
        date: "2024-07-15",
        amount: "R100",
        status: "Completed",
        transactionId: "TXN789012",
    },
    {
        date: "2024-07-10",
        amount: "R20",
        status: "Failed",
        transactionId: "TXN345678",
    },
    {
        date: "2024-07-05",
        amount: "R75",
        status: "Completed",
        transactionId: "TXN901234",
    },
    {
        date: "2024-07-01",
        amount: "R31000",
        status: "Completed",
        transactionId: "TXN567890",
    },
];

export const DepositHistorySection = () => {
    return (
        <div className="flex flex-col w-full items-start relative">
            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms] flex flex-col items-start pt-5 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <h2 className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-[22px] tracking-[0] leading-7">
                    Payment Methods
                </h2>
            </div>

            {paymentMethods.map((method, index) => (
                <div
                    key={method.name}
                    className={`translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:${200 + index * 100}ms] flex h-[72px] items-center gap-4 px-4 py-2 relative self-stretch w-full bg-[#1e1e2f] hover:bg-[#252540] transition-colors cursor-pointer`}
                >
                    <img
                        className="relative w-10 h-6"
                        alt={`${method.name} icon`}
                        src={method.icon}
                    />

                    <div className="inline-flex flex-col items-start justify-center relative flex-[0_0_auto]">
                        <div className="flex flex-col w-[243px] items-start relative flex-[0_0_auto]">
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-base tracking-[0] leading-6">
                                {method.name}
                            </div>
                        </div>

                        <div className="inline-flex items-start flex-col relative flex-[0_0_auto]">
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px] whitespace-nowrap">
                                {method.details}
                            </div>
                        </div>
                    </div>
                </div>
            ))}

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms] flex flex-col items-start pt-5 pb-3 px-4 relative self-stretch w-full flex-[0_0_auto]">
                <h2 className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-[22px] tracking-[0] leading-7">
                    Deposit History
                </h2>
            </div>

            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms] flex flex-col items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto]">
                <Card className="flex items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] rounded-lg overflow-hidden border border-solid border-[#d6d1e8]">
                    <CardContent className="flex flex-col items-start relative flex-1 grow p-0">
                        <Table>
                            <TableHeader>
                                <TableRow className="bg-[#1e1e2f] border-none hover:bg-[#1e1e2f]">
                                    <TableHead className="w-[73px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                                        Date
                                    </TableHead>
                                    <TableHead className="w-[84px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                                        Amount
                                    </TableHead>
                                    <TableHead className="w-[137px] px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                                        Status
                                    </TableHead>
                                    <TableHead className="w-28 px-4 py-3 [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                                        Transaction ID
                                    </TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {depositHistory.map((transaction, index) => (
                                    <TableRow
                                        key={transaction.transactionId}
                                        className="h-[72px] border-t border-[#e5e8ea] hover:bg-[#252540] transition-colors"
                                    >
                                        <TableCell className="w-[73px] px-4 py-2 text-center [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                                            {transaction.date}
                                        </TableCell>
                                        <TableCell className="w-[84px] px-4 py-2 text-center [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                                            {transaction.amount}
                                        </TableCell>
                                        <TableCell className="w-[137px] px-4 py-2 text-center">
                                            <Badge
                                                variant="secondary"
                                                className={`min-w-[84px] max-w-[480px] h-8 px-4 py-0 bg-[#eae8f2] rounded-lg [font-family:'Inter',Helvetica] font-medium text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap ${transaction.status === "Failed"
                                                        ? "text-[#e8edf5]"
                                                        : "text-[#e8edf5]"
                                                    }`}
                                            >
                                                {transaction.status}
                                            </Badge>
                                        </TableCell>
                                        <TableCell className="w-28 px-4 py-2 text-center [font-family:'Inter',Helvetica] font-normal text-[#664f96] text-sm tracking-[0] leading-[21px]">
                                            {transaction.transactionId}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
};
