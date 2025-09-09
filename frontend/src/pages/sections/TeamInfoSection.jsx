import React from "react";
import { Card, CardContent } from "@/components/ui/card";


const teamData = {
    name: "Mamelodi Sundowns",
    founded: "1970",
    stadium: "Loftus Versfeld Stadium",
    league: "Premier Soccer League",
    logoUrl: "https://c.animaapp.com/mfbgt44woiUDGL/img/sundwns-logo-1.png",
    backgroundUrl:
        "https://c.animaapp.com/mfbgt44woiUDGL/img/depth-6--frame-1.png",
};

export const TeamInfoSection = () => {
    return (
        <section className="flex flex-col items-start p-4 self-stretch w-full relative flex-[0_0_auto]">
            <Card className="justify-between flex-[0_0_auto] rounded-lg flex items-start relative self-stretch w-full border-0 bg-transparent translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
                <CardContent className="flex justify-between items-start w-full p-0">
                    <div className="flex-col w-[608px] h-[171px] gap-1 flex items-start relative">
                        <header className="flex flex-col items-start relative self-stretch w-full flex-[0_0_auto]">
                            <h1 className="relative self-stretch mt-[-1.00px] [font-family:'Public_Sans',Helvetica] font-bold text-[#1e1e2f] text-base tracking-[0] leading-5">
                                {teamData.name}
                            </h1>
                        </header>

                        <div className="flex flex-col items-start relative self-stretch w-full flex-[0_0_auto]">
                            <p className="relative self-stretch mt-[-1.00px] [font-family:'Public_Sans',Helvetica] font-normal text-[#e4e4e4] text-sm tracking-[0] leading-[21px]">
                                Founded: {teamData.founded} | Stadium: {teamData.stadium} |
                                League: {teamData.league}
                            </p>
                        </div>
                    </div>

                    <div
                        className="h-[171px] rounded-lg overflow-hidden relative flex-1 grow"
                        style={{
                            background: `url(${teamData.backgroundUrl}) 50% 50% / cover`,
                        }}
                    >
                        <img
                            className="absolute w-[292px] h-[171px] top-0 left-7 object-cover"
                            alt="Sundowns logo"
                            src={teamData.logoUrl}
                        />
                    </div>
                </CardContent>
            </Card>
        </section>
    );
};
