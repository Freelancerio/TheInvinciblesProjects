import React from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";

export const UpcomingMatchesSection = () => {
    const navigate = useNavigate();

    const handleLoginNavigation = () => {
        navigate("/login");
    };

    const navigationItems = [
        { label: "Home", href: "#", onClick: handleLoginNavigation },
        { label: "Live", href: "#" },
        { label: "Upcoming", href: "#" },
        { label: "Promotions", href: "#" },
        { label: "Help", href: "#" },
    ];

    return (
        <header className="items-center justify-between px-10 py-3 flex-[0_0_auto] border-b [border-bottom-style:solid] border-[#e5e8ea] flex relative self-stretch w-full translate-y-[-1rem] animate-fade-in opacity-0">
            <div className="inline-flex items-center gap-4 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                <img
                    className="relative flex-[0_0_auto]"
                    alt="SmartBet logo"
                    src="https://c.animaapp.com/mfb6odeh0eWapb/img/depth-4--frame-0.svg"
                />

                <div className="items-start inline-flex flex-col relative flex-[0_0_auto]">
                    <div className="relative self-stretch mt-[-1.00px] [font-family:'Space_Grotesk',Helvetica] font-bold text-white text-lg tracking-[0] leading-[23px] whitespace-nowrap">
                        SmartBet
                    </div>
                </div>
            </div>

            <div className="flex items-start justify-end gap-8 relative flex-1 grow">
                <nav className="inline-flex h-10 items-center gap-9 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
                    {navigationItems.map((item, index) => (
                        <a
                            key={item.label}
                            href={item.href}
                            onClick={(e) => {
                                if (item.onClick) {
                                    e.preventDefault();
                                    item.onClick();
                                }
                            }}
                            className="items-start inline-flex flex-col relative flex-[0_0_auto] transition-colors hover:text-[#8a2be2] cursor-pointer"
                        >
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Space_Grotesk',Helvetica] font-medium text-white text-sm tracking-[0] leading-[21px] whitespace-nowrap">
                                {item.label}
                            </div>
                        </a>
                    ))}
                </nav>

                <div className="inline-flex items-start gap-2 relative flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
                    <Button
                        variant="secondary"
                        className="inline-flex min-w-[84px] max-w-[480px] h-10 items-center justify-center px-4 py-0 relative flex-[0_0_auto] bg-[#b0bec5] rounded-lg overflow-hidden hover:bg-[#9cabb2] transition-colors"
                        onClick={handleLoginNavigation}
                    >
                        <div className="items-center inline-flex flex-col relative flex-[0_0_auto]">
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Space_Grotesk',Helvetica] font-bold text-[#1e1e2f] text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap">
                                Join Now
                            </div>
                        </div>
                    </Button>

                    <Button
                        variant="default"
                        className="flex max-w-[480px] w-[84px] h-10 items-center justify-center px-4 py-0 bg-[#8a2be2] relative rounded-lg overflow-hidden hover:bg-[#7a24d1] transition-colors"
                        onClick={handleLoginNavigation}
                    >
                        <div className="items-center inline-flex flex-col relative flex-[0_0_auto]">
                            <div className="relative self-stretch mt-[-1.00px] [font-family:'Space_Grotesk',Helvetica] font-bold text-[#1e1e2f] text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap">
                                Log In
                            </div>
                        </div>
                    </Button>
                </div>
            </div>
        </header>
    );
};