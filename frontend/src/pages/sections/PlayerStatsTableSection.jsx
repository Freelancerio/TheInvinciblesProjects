import { ChevronLeftIcon, ChevronRightIcon } from "lucide-react";
import React from "react";
import { Button } from "@/components/ui/button";

export const PlayerStatsTableSection = () => {
    const pageNumbers = [
        { number: 1, isActive: true },
        { number: 2, isActive: false },
        { number: 3, isActive: false },
        { number: 4, isActive: false },
        { number: 5, isActive: false },
    ];

    return (
        <div className="flex items-center justify-center p-4 relative self-stretch w-full flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <Button
                variant="ghost"
                size="icon"
                className="w-10 h-10 rounded-[20px] hover:bg-accent transition-colors"
            >
                <ChevronLeftIcon className="w-5 h-5 text-[#b0bec5]" />
            </Button>

            {pageNumbers.map((page) => (
                <Button
                    key={page.number}
                    variant="ghost"
                    size="icon"
                    className={`w-10 h-10 rounded-[20px] transition-colors ${page.isActive
                            ? "bg-[#ededed] hover:bg-[#e0e0e0]"
                            : "hover:bg-accent"
                        }`}
                >
                    <span
                        className={`[font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px] whitespace-nowrap ${page.isActive
                                ? "font-bold text-[#b0bec5]"
                                : "font-normal text-[#b0bec5]"
                            }`}
                    >
                        {page.number}
                    </span>
                </Button>
            ))}

            <Button
                variant="ghost"
                size="icon"
                className="w-10 h-10 rounded-[20px] hover:bg-accent transition-colors"
            >
                <ChevronRightIcon className="w-5 h-5 text-[#b0bec5]" />
            </Button>
        </div>
    );
};
