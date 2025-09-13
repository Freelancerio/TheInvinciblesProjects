import React from "react";
import { useNavigate } from "react-router-dom";
import {
    Avatar,
    AvatarFallback,
    AvatarImage,
} from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";

export const WithdrawalMethodsSection = () => {
    const navigate = useNavigate();

    const handleNavigation = (path) => {
        navigate(path);
    };

    const navigationItems = [
        { label: "Home", onClick: () => handleNavigation("./dashboard") },
    ];

    return (
        <header className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:0ms] flex items-center justify-between px-10 py-3 border-b border-[#e5e8ea] w-full">
            <div className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms] inline-flex items-center gap-4">
                <img
                    className="flex-shrink-0"
                    alt="SmartBet Logo"
                    src="https://c.animaapp.com/mfcey0msCkDIKF/img/depth-4--frame-0.svg"
                />
                <div className="flex flex-col">
                    <h1 className="[font-family:'Inter',Helvetica] font-bold text-[#e8edf5] text-lg tracking-[0] leading-[23px] whitespace-nowrap">
                        SmartBet
                    </h1>
                </div>
            </div>

            <div className="flex items-center justify-end gap-8 flex-1">
                <nav className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms] flex items-center gap-9">
                    {navigationItems.map((item, index) => (
                        <button
                            key={item.label}
                            onClick={item.onClick}
                            className="[font-family:'Lexend',Helvetica] font-medium text-[#e8edf5] text-sm tracking-[0] leading-[21px] whitespace-nowrap hover:text-white transition-colors bg-transparent border-none cursor-pointer"
                        >
                            {item.label}
                        </button>
                    ))}
                </nav>

                <Button
                    onClick={() => handleNavigation("/deposit")}
                    className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms] min-w-[84px] max-w-[480px] h-10 px-4 py-0 bg-[#e8edf4] hover:bg-[#d4dae3] rounded-lg [font-family:'Lexend',Helvetica] font-bold text-[#1e1e2f] text-sm tracking-[0] leading-[21px] transition-colors"
                >
                    Deposit
                </Button>

                <Avatar className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:800ms] w-10 h-10">
                    <AvatarImage
                        src="https://via.placeholder.com/40x40/666/fff?text=U"
                        alt="User Avatar"
                    />
                    <AvatarFallback>U</AvatarFallback>
                </Avatar>
            </div>
        </header>
    );
};