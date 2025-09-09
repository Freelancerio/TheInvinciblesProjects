import React from "react";
import {
    Avatar,
    AvatarFallback,
    AvatarImage,
} from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

export const AddFundsSection = () => {
    const navigationItems = [
        { label: "Home", active: false },
        { label: "Live", active: false },
        { label: "Upcoming", active: false },
        { label: "Results", active: false },
        { label: "My Bets", active: false },
    ];

    return (
        <header className="items-center justify-between px-10 py-3 flex-[0_0_auto] border-b [border-bottom-style:solid] border-[#e5e8ea] flex relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms]">
            <div className="inline-flex items-center flex-[0_0_auto] gap-8 relative">
                <div className="inline-flex items-center gap-4 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                    <img
                        className="relative flex-[0_0_auto]"
                        alt="SmartBet Logo"
                        src="https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-5--frame-0-3.svg"
                    />

                    <div className="inline-flex items-start flex-col relative flex-[0_0_auto]">
                        <h1 className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-lg tracking-[0] leading-[23px] whitespace-nowrap">
                            SmartBet
                        </h1>
                    </div>
                </div>

                <nav className="inline-flex items-center gap-9 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
                    {navigationItems.map((item, index) => (
                        <Button
                            key={item.label}
                            variant="ghost"
                            className="inline-flex items-start flex-col relative flex-[0_0_auto] h-auto p-0 hover:bg-transparent"
                        >
                            <span className="relative self-stretch mt-[-1.00px] [font-family:'Inter',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px] whitespace-nowrap hover:text-white transition-colors">
                                {item.label}
                            </span>
                        </Button>
                    ))}
                </nav>
            </div>

            <div className="flex items-start justify-end flex-1 grow gap-8 relative translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
                <div className="inline-flex flex-col min-w-40 max-w-64 items-start relative flex-[0_0_auto]">
                    <div className="flex items-start relative flex-1 self-stretch w-full grow rounded-lg">
                        <img
                            className="relative self-stretch flex-[0_0_auto]"
                            alt="SearchIcon Icon"
                            src="https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-6--frame-0.svg"
                        />

                        <div className="pl-2 pr-4 py-2 flex-1 self-stretch grow rounded-[0px_8px_8px_0px] flex items-center relative bg-[#eae8f2] overflow-hidden">
                            <Input
                                placeholder="Search"
                                className="border-0 bg-transparent p-0 h-auto text-base text-[#664f96] placeholder:text-[#664f96] [font-family:'Inter',Helvetica] font-normal focus-visible:ring-0 focus-visible:ring-offset-0"
                            />
                        </div>
                    </div>
                </div>

                <img
                    className="max-w-[480px] h-10 relative flex-[0_0_auto]"
                    alt="Action Buttons"
                    src="https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-4--frame-1.svg"
                />

                <Avatar className="relative w-10 h-10 rounded-[20px]">
                    <AvatarImage
                        src="https://c.animaapp.com/mfcfd18iC3JgIz/img/depth-4--frame-2.png"
                        alt="User Avatar"
                        className="object-cover"
                    />
                    <AvatarFallback>U</AvatarFallback>
                </Avatar>
            </div>
        </header>
    );
};
