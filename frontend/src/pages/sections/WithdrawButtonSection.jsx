import React from "react";
import { Button } from "@/components/ui/button";

export const WithdrawButtonSection = () => {
    return (
        <Button
            variant="ghost"
            className="flex h-[72px] items-center gap-4 px-4 py-2 w-full bg-[#1e1e2f] hover:bg-[#252540] transition-colors justify-start rounded-none translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]"
        >
            <img
                className="w-12 h-12"
                alt="Depth frame"
                src="https://c.animaapp.com/mfcey0msCkDIKF/img/depth-5--frame-0-1.svg"
            />

            <div className="flex flex-col items-start justify-center">
                <div className="flex flex-col items-start">
                    <div className="[font-family:'Lexend',Helvetica] font-medium text-white text-base tracking-[0] leading-6 whitespace-nowrap">
                        E-Wallet
                    </div>
                </div>

                <div className="flex flex-col items-start">
                    <div className="[font-family:'Lexend',Helvetica] font-normal text-[#e8edf5] text-sm tracking-[0] leading-[21px]">
                        Instant
                    </div>
                </div>
            </div>
        </Button>
    );
};
