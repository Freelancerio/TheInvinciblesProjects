import React, { useState } from "react";
import { Button } from "@/components/ui/button";


export const LiveUpdatesSection = () => {
    const [activeTab, setActiveTab] = useState("Stats");

    const navigationTabs = [
        { id: "Stats", label: "Stats" },
        { id: "Players", label: "Players" },
        { id: "View Predictions", label: "View Predictions" },
        { id: "Place Bet", label: "Place Bet" },
    ];

    return (
        <section className="flex items-start relative w-full bg-[#1e1e2f] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <div className="flex items-center justify-between gap-4 px-4 py-3 w-full max-w-[960px] mx-auto">
                {navigationTabs.map((tab, index) => (
                    <Button
                        key={tab.id}
                        onClick={() => setActiveTab(tab.id)}
                        className={`h-10 px-4 py-0 rounded-lg font-bold text-sm tracking-[0] leading-[21px] transition-colors ${activeTab === tab.id ||
                            tab.id === "Stats" ||
                            tab.id === "Place Bet"
                            ? "bg-[#b0bec5] text-[#1e1e2f] hover:bg-[#9cabb2]"
                            : "bg-transparent text-[#b0bec5] hover:bg-[#2a2a3f] border border-[#b0bec5]"
                            } [font-family:'Space_Grotesk',Helvetica] translate-y-[-1rem] animate-fade-in opacity-0`}
                        style={{ "--animation-delay": `${400 + index * 100}ms` }}
                    >
                        {tab.label}
                    </Button>
                ))}
            </div>
        </section>
    );
};
