import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";

const playerData = [
    {
        player: "Thabo Cele",
        team: "Orlando Pirates",
        position: "Forward",
        games: 25,
        goals: 12,
        assists: 8,
        shots: 45,
        shotsOnTarget: 20,
        passes: 300,
        yellowCards: 3,
        redCards: 0,
        rating: 7.5,
    },
    {
        player: "Sipho Dlamini",
        team: "Kaizer Chiefs",
        position: "Midfielder",
        games: 28,
        goals: 5,
        assists: 15,
        shots: 30,
        shotsOnTarget: 15,
        passes: 450,
        yellowCards: 2,
        redCards: 0,
        rating: 7.2,
    },
    {
        player: "Bongani Khumalo",
        team: "Mamelodi Sundowns",
        position: "Defender",
        games: 22,
        goals: 2,
        assists: 3,
        shots: 10,
        shotsOnTarget: 5,
        passes: 200,
        yellowCards: 1,
        redCards: 1,
        rating: 6.8,
    },
    {
        player: "Musa Nkosi",
        team: "SuperSport United",
        position: "Forward",
        games: 26,
        goals: 10,
        assists: 5,
        shots: 40,
        shotsOnTarget: 25,
        passes: 250,
        yellowCards: 4,
        redCards: 0,
        rating: 7.3,
    },
    {
        player: "Siyabonga Zulu",
        team: "Cape Town City",
        position: "Midfielder",
        games: 24,
        goals: 4,
        assists: 10,
        shots: 25,
        shotsOnTarget: 10,
        passes: 350,
        yellowCards: 3,
        redCards: 0,
        rating: 7.0,
    },
    {
        player: "Thando Mthembu",
        team: "AmaZulu",
        position: "Defender",
        games: 27,
        goals: 1,
        assists: 2,
        shots: 5,
        shotsOnTarget: 2,
        passes: 150,
        yellowCards: 2,
        redCards: 1,
        rating: 6.5,
    },
    {
        player: "Lwazi Mabaso",
        team: "Stellenbosch FC",
        position: "Forward",
        games: 23,
        goals: 8,
        assists: 7,
        shots: 35,
        shotsOnTarget: 18,
        passes: 200,
        yellowCards: 1,
        redCards: 0,
        rating: 7.1,
    },
    {
        player: "Sanele Ndlovu",
        team: "Maritzburg United",
        position: "Midfielder",
        games: 25,
        goals: 3,
        assists: 8,
        shots: 20,
        shotsOnTarget: 8,
        passes: 300,
        yellowCards: 2,
        redCards: 0,
        rating: 6.9,
    },
    {
        player: "Mlungisi Ngubane",
        team: "Chippa United",
        position: "Defender",
        games: 26,
        goals: 0,
        assists: 1,
        shots: 2,
        shotsOnTarget: 1,
        passes: 100,
        yellowCards: 3,
        redCards: 2,
        rating: 6.2,
    },
    {
        player: "Sibusiso Mkhize",
        team: "Baroka FC",
        position: "Forward",
        games: 22,
        goals: 6,
        assists: 4,
        shots: 30,
        shotsOnTarget: 15,
        passes: 180,
        yellowCards: 1,
        redCards: 0,
        rating: 7.0,
    },
];

export const PlayerStatsContainerSection = () => {
    return (
        <section className="flex flex-col items-start px-4 py-3 relative self-stretch w-full flex-[0_0_auto] translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
            <div className="flex items-start relative self-stretch w-full flex-[0_0_auto] bg-[#1e1e2f] rounded-lg overflow-hidden border border-solid border-[#dbdbdb]">
                <div className="flex flex-col items-start relative flex-1 grow">
                    <Table>
                        <TableHeader>
                            <TableRow className="bg-[#1e1e2f] border-none">
                                <TableHead className="w-[101px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Player
                                </TableHead>
                                <TableHead className="w-[117px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Team
                                </TableHead>
                                <TableHead className="w-[99px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Position
                                </TableHead>
                                <TableHead className="w-[78px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Games
                                </TableHead>
                                <TableHead className="w-[69px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    goals
                                </TableHead>
                                <TableHead className="w-[79px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Assists
                                </TableHead>
                                <TableHead className="w-[70px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Shots
                                </TableHead>
                                <TableHead className="w-[75px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Shots on Target
                                </TableHead>
                                <TableHead className="w-[78px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Passes
                                </TableHead>
                                <TableHead className="w-[75px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Yellow Cards
                                </TableHead>
                                <TableHead className="w-[70px] px-4 py-3 [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Red Cards
                                </TableHead>
                                <TableHead className="w-[75px] px-4 py-3 mr-[-60.00px] [font-family:'Public_Sans',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                    Rating
                                </TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {playerData.map((player, index) => (
                                <TableRow
                                    key={`player-${index}`}
                                    className="h-[72px] border-t border-[#e5e8ea]"
                                >
                                    <TableCell className="w-[101px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="relative self-stretch [font-family:'Public_Sans',Helvetica] font-normal text-[#b0bec5] text-sm tracking-[0] leading-[21px]">
                                            {player.player}
                                        </div>
                                    </TableCell>
                                    <TableCell className="h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col w-[117px] relative">
                                        <div className="relative self-stretch [font-family:'Public_Sans',Helvetica] font-normal text-[#727272] text-sm tracking-[0] leading-[21px]">
                                            {player.team}
                                        </div>
                                    </TableCell>
                                    <TableCell className="h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col w-[99px] relative">
                                        <div className="relative self-stretch [font-family:'Public_Sans',Helvetica] font-normal text-[#727272] text-sm tracking-[0] leading-[21px]">
                                            {player.position}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[78px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.games}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[69px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.goals}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[79px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.assists}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[70px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.shots}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[75px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.shotsOnTarget}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[78px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.passes}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[75px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.yellowCards}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[70px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] flex flex-col relative">
                                        <div className="self-stretch font-normal text-[#727272] relative [font-family:'Public_Sans',Helvetica] text-sm tracking-[0] leading-[21px]">
                                            {player.redCards}
                                        </div>
                                    </TableCell>
                                    <TableCell className="w-[75px] h-[72px] items-center justify-center px-4 py-2 mb-[-1.00px] mr-[-60.00px] flex flex-col relative">
                                        <div className="relative self-stretch [font-family:'Public_Sans',Helvetica] font-normal text-[#727272] text-sm tracking-[0] leading-[21px]">
                                            {player.rating}
                                        </div>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </div>
        </section>
    );
};
