import React from "react";
import { Card, CardContent } from "@/components/ui/card";

export const AvailableBalanceSection = () => {
    return (
        <Card className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] bg-[#1e1e2f] border-none">
            <CardContent className="flex h-[72px] items-center gap-4 px-4 py-2 w-full">
                <img
                    className="w-12 h-12 flex-shrink-0"
                    alt="Bank transfer icon"
                    src="https://c.animaapp.com/mfcey0msCkDIKF/img/depth-5--frame-0.svg"
                />

                <div className="flex flex-col justify-center flex-1">
                    <div className="flex flex-col">
                        <h3 className="[font-family:'Lexend',Helvetica] font-medium text-white text-base tracking-[0] leading-6">
                            Bank Transfer
                        </h3>
                    </div>

                    <div className="flex flex-col">
                        <p className="[font-family:'Lexend',Helvetica] font-normal text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                            1-3 business days
                        </p>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};
