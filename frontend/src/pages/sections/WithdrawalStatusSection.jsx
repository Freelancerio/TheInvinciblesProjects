import React from "react";
import { Card, CardContent } from "@/components/ui/card";

export const WithdrawalStatusSection = () => {
    return (
        <Card className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] h-[72px] bg-[#1e1e2f] border-0 rounded-none">
            <CardContent className="flex items-center gap-4 px-4 py-2 h-full">
                <img
                    className="relative w-12 h-12"
                    alt="Depth frame"
                    src="https://c.animaapp.com/mfcey0msCkDIKF/img/depth-5--frame-0-2.svg"
                />

                <div className="inline-flex justify-center flex-[0_0_auto] flex-col items-start relative">
                    <div className="inline-flex items-start flex-col relative flex-[0_0_auto]">
                        <div className="relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-medium text-[#1e1e2f] text-base tracking-[0] leading-6 whitespace-nowrap">
                            Credit/Debit Card
                        </div>
                    </div>

                    <div className="flex-col w-[142px] items-start flex-[0_0_auto] flex relative">
                        <div className="relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-normal text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                            1-5 business days
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};
