import React from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { AmountInputSection } from "./sections/AmountInputSection";
import { AvailableBalanceSection } from "./sections/AvailableBalanceSection";
import { WithdrawButtonSection } from "./sections/WithdrawButtonSection";
import { WithdrawalHistorySection } from "./sections/WithdrawalHistorySection";
import { WithdrawalMethodsSection } from "./sections/WithdrawalMethodsSection";
import { WithdrawalStatusSection } from "./sections/WithdrawalStatusSection";

export const Withdrawal = () => {
    return (
        <div
            className="flex flex-col items-start relative bg-[#1e1e2f] translate-y-[-1rem] animate-fade-in opacity-0"
            data-model-id="1:1300"
        >
            <div className="flex flex-col min-h-[800px] items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f]">
                <div className="flex items-start self-stretch w-full flex-col relative flex-[0_0_auto]">
                    <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] w-full">
                        <WithdrawalMethodsSection />
                    </div>

                    <div className="items-start justify-center px-40 py-5 flex-1 grow flex relative self-stretch w-full">
                        <div className="flex flex-col max-w-[960px] w-[960px] items-start px-0 py-5 relative">
                            <div className="flex-wrap items-start justify-around gap-[12px_12px] p-4 self-stretch w-full flex-[0_0_auto] flex relative translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
                                <div className="flex flex-col w-72 items-start relative">
                                    <h1 className="text-[32px] leading-10 relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-bold text-[#1e1e2f] tracking-[0]">
                                        Withdraw
                                    </h1>
                                </div>
                            </div>

                            <div className="flex flex-col h-[47px] items-start pt-4 pb-2 px-4 relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
                                <h2 className="text-[#1e1e2f] relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-bold text-lg tracking-[0] leading-[23px]">
                                    Withdrawal Form
                                </h2>
                            </div>

                            <div className="inline-flex flex-wrap max-w-[480px] items-end gap-[16px_16px] px-4 py-3 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms]">
                                <div className="flex min-w-40 flex-1 grow flex-col items-start relative">
                                    <div className="flex flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] relative self-stretch w-full">
                                        <Label className="text-base leading-6 relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-medium text-[#e8edf5] tracking-[0]">
                                            Amount
                                        </Label>
                                    </div>

                                    <Input className="h-8 bg-[#e8edf4] rounded-lg relative self-stretch w-full border-0" />
                                </div>
                            </div>

                            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1000ms] w-full">
                                <AmountInputSection />
                            </div>

                            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1200ms] w-full">
                                <AvailableBalanceSection />
                            </div>

                            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1400ms] w-full">
                                <WithdrawButtonSection />
                            </div>

                            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1600ms] w-full">
                                <WithdrawalStatusSection />
                            </div>

                            <div className="flex items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:1800ms]">
                                <Button className="flex min-w-[84px] max-w-[480px] items-center justify-center px-4 py-0 relative flex-1 grow bg-[#e8edf5] rounded-lg overflow-hidden h-auto hover:bg-[#d4dae3] transition-colors">
                                    <div className="inline-flex items-center flex-col relative flex-[0_0_auto]">
                                        <span className="text-sm text-center leading-[21px] whitespace-nowrap relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-bold text-[#1e1e2f] tracking-[0]">
                                            Withdraw
                                        </span>
                                    </div>
                                </Button>
                            </div>

                            <div className="flex flex-col h-[47px] items-start pt-4 pb-2 px-4 relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2000ms]">
                                <h2 className="text-[#e8edf5] relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-bold text-lg tracking-[0] leading-[23px]">
                                    Withdrawal Methods
                                </h2>
                            </div>

                            <div className="flex flex-col h-[47px] items-start pt-4 pb-2 px-4 relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2200ms]">
                                <h2 className="text-[#e8edf5] relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-bold text-lg tracking-[0] leading-[23px]">
                                    Withdrawal History
                                </h2>
                            </div>

                            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:2400ms] w-full">
                                <WithdrawalHistorySection />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
