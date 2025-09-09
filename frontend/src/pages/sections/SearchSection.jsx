import { SearchIcon } from "lucide-react";
import React from "react";
import {
    Avatar,
    AvatarFallback,
    AvatarImage,
} from "@/components/ui/avatar";
import { Input } from "@/components/ui/input";

export const SearchSection = () => {
    const navigationItems = [
        "Home",
        "Teams",
        "Players",
        "Fixtures",
        "Results",
        "Standings",
    ];

    return (
        <header className="items-center justify-between px-10 py-3 flex-[0_0_auto] border-b [border-bottom-style:solid] border-[#e5e8ea] flex relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0">
            <div className="inline-flex items-center flex-[0_0_auto] gap-8 relative">
                <div className="inline-flex items-center gap-4 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:100ms]">
                    <img
                        className="relative flex-[0_0_auto]"
                        alt="SmartBet logo"
                        src="https://c.animaapp.com/mfbgq1fnIyf8v2/img/depth-4--frame-0.svg"
                    />

                    <div className="inline-flex flex-col items-start relative flex-[0_0_auto]">
                        <div className="relative self-stretch mt-[-1.00px] [font-family:'Public_Sans',Helvetica] font-bold text-[#b0bec5] text-lg tracking-[0] leading-[23px] whitespace-nowrap">
                            SmartBet
                        </div>
                    </div>
                </div>

                <nav className="inline-flex items-center gap-9 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                    {navigationItems.map((item, index) => (
                        <div
                            key={item}
                            className="inline-flex flex-col items-start relative flex-[0_0_auto]"
                        >
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] whitespace-nowrap cursor-pointer hover:text-[#90a4ae] transition-colors">
                                {item}
                            </div>
                        </div>
                    ))}
                </nav>
            </div>

            <div className="flex items-start justify-end flex-1 grow gap-8 relative translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:300ms]">
                <div className="inline-flex flex-col min-w-40 max-w-64 items-start relative flex-[0_0_auto]">
                    <div className="flex items-start relative flex-1 self-stretch w-full grow rounded-lg">
                        <div className="relative self-stretch flex-[0_0_auto] flex items-center justify-center bg-[#ededed] rounded-[8px_0px_0px_8px] px-2">
                            <SearchIcon className="w-4 h-4 text-[#727272]" />
                        </div>

                        <div className="flex items-center pl-2 pr-4 py-2 relative flex-1 self-stretch grow bg-[#ededed] rounded-[0px_8px_8px_0px] overflow-hidden">
                            <Input
                                className="border-0 bg-transparent p-0 h-auto text-base text-[#727272] placeholder:text-[#727272] focus-visible:ring-0 focus-visible:ring-offset-0 [font-family:'Public_Sans',Helvetica] font-normal"
                                placeholder="Search"
                                defaultValue=""
                            />
                        </div>
                    </div>
                </div>

                <img
                    className="relative max-w-[480px] flex-[0_0_auto] h-10"
                    alt="Additional controls"
                    src="https://c.animaapp.com/mfbgq1fnIyf8v2/img/depth-4--frame-1.svg"
                />

                <Avatar className="w-10 h-10">
                    <AvatarImage
                        src="https://c.animaapp.com/mfbgq1fnIyf8v2/img/depth-4--frame-2.png"
                        alt="User avatar"
                    />
                    <AvatarFallback className="bg-[#d9d9d9]">U</AvatarFallback>
                </Avatar>
            </div>
        </header>
    );
};
