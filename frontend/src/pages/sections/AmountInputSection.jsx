import React from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export const AmountInputSection = () => {
    return (
        <section className="inline-flex flex-wrap max-w-[480px] items-end gap-4 px-4 py-3 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <div className="flex min-w-40 flex-1 grow flex-col items-start relative">
                <div className="flex flex-col items-start pt-0 pb-2 px-0 flex-[0_0_auto] relative self-stretch w-full">
                    <Label className="relative self-stretch mt-[-1.00px] [font-family:'Lexend',Helvetica] font-medium text-[#e8edf5] text-base tracking-[0] leading-6">
                        Available Balance
                    </Label>
                </div>

                <Input
                    defaultValue="R 1300"
                    readOnly
                    className="h-8 items-center p-4 self-stretch w-full bg-[#e8edf4] rounded-lg overflow-hidden flex relative font-medium text-black whitespace-nowrap [font-family:'Lexend',Helvetica] text-sm tracking-[0] leading-[21px] border-0 focus-visible:ring-0 focus-visible:ring-offset-0"
                />
            </div>
        </section>
    );
};
