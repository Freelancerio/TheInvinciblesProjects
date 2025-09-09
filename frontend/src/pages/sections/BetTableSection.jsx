import { ChevronLeftIcon, ChevronRightIcon } from "lucide-react";
import React, { useState } from "react";
import { Button } from "@/components/ui/button";

export const BetTableSection = () => {
    const [activePage, setActivePage] = useState(1);

    const pageNumbers = [
        { number: 1, isActive: true },
        { number: 2, isActive: false },
        { number: 3, isActive: false },
        { number: 4, isActive: false },
        { number: 5, isActive: false },
    ];

    const handlePageClick = (pageNumber) => {
        setActivePage(pageNumber);
    };

    return (
        <div className="flex items-center justify-center p-4 relative self-stretch w-full flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <Button
                variant="ghost"
                size="icon"
                className="w-10 h-10 rounded-[20px] hover:bg-accent transition-colors"
                onClick={() => activePage > 1 && handlePageClick(activePage - 1)}
                disabled={activePage === 1}
            >
                <ChevronLeftIcon className="w-5 h-5 text-[#b0bec5]" />
            </Button>

            {pageNumbers.map((page, index) => (
                <Button
                    key={page.number}
                    variant="ghost"
                    size="icon"
                    className={`w-10 h-10 rounded-[20px] transition-colors hover:bg-accent ${activePage === page.number ? "bg-[#723ae8]" : ""
                        }`}
                    onClick={() => handlePageClick(page.number)}
                >
                    <span
                        className={`font-${activePage === page.number ? "bold" : "normal"} text-[#b0bec5] [font-family:'Inter',Helvetica] text-sm tracking-[0] leading-[21px] whitespace-nowrap`}
                    >
                        {page.number}
                    </span>
                </Button>
            ))}

            <Button
                variant="ghost"
                size="icon"
                className="w-10 h-10 rounded-[20px] hover:bg-accent transition-colors"
                onClick={() => activePage < 5 && handlePageClick(activePage + 1)}
                disabled={activePage === 5}
            >
                <ChevronRightIcon className="w-5 h-5 text-[#b0bec5]" />
            </Button>
        </div>
    );
};
