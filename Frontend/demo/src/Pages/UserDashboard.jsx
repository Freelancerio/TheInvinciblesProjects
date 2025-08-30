import React, { useEffect, useState } from "react";
import StatCard from "../Components/StatsCard";
import Section from "../Components/Section"

function UserDashboard() {
    const [user, setUser] = useState(null);
    const [stats, setStats] = useState(null);
    const [matches, setMatches] = useState([]);
    const [standings, setStandings] = useState([]);

    useEffect(() => {
        fetch("http://localhost:3000/user-info")
            .then(res => res.json())
            .then(setUser);

        fetch("http://localhost:3000/api/psl/stats")
            .then(res => res.json())
            .then(setStats);

        fetch("http://localhost:3000/api/psl/matches")
            .then(res => res.json())
            .then(setMatches);

        fetch("http://localhost:3000/api/psl/standings")
            .then(res => res.json())
            .then(setStandings);
    }, []);

    if (!user || !stats) {
        return <div className="flex items-center justify-center h-screen">Loading...</div>;
    }

    return (
        <div
            className="relative flex size-full min-h-screen flex-col bg-[#f9f8fc] overflow-x-hidden"
            style={{ fontFamily: "Inter, Noto Sans, sans-serif" }}
        >
            {/* HEADER */}
            <header className="flex items-center justify-between border-b border-[#ebe7f3] px-10 py-3">
                <div className="flex items-center gap-4 text-[#120e1b]">
                    <div className="size-4">
                        <svg viewBox="0 0 48 48" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                            <path d="M44 11.27C44 14 39.8 16.4 33.7 17.6 39.8 18.9 44 21.3 44 24s-4.2 5.1-10.3 6.4C39.8 31.6 44 34 44 36.7 44 40.7 35 44 24 44S4 40.7 4 36.7c0-2.7 4.2-5.1 10.3-6.4C8.2 29.1 4 26.7 4 24s4.2-5.1 10.3-6.4C8.2 16.4 4 14 4 11.3 4 7.3 13 4 24 4s20 3.3 20 7.3z" />
                        </svg>
                    </div>
                    <h2 className="text-lg font-bold">SmartBet</h2>
                </div>
                <div className="flex flex-1 justify-end gap-8">
                    <nav className="flex items-center gap-9">
                        {["Home", "Live", "Fixtures", "Results", "Promotions"].map((link, i) => (
                            <a key={i} href="#" className="text-sm font-medium text-[#120e1b]">
                                {link}
                            </a>
                        ))}
                    </nav>
                    <button className="h-10 px-4 rounded-lg bg-[#ebe7f3] text-sm font-bold">Help</button>
                    <div
                        className="bg-center bg-cover rounded-full size-10"
                        style={{ backgroundImage: `url(${user.avatarUrl})` }}
                    ></div>
                </div>
            </header>

            {/* CONTENT */}
            <div className="px-40 py-5 flex justify-center">
                <div className="flex flex-col max-w-[960px] flex-1">
                    <h1 className="text-[32px] font-bold px-4">Welcome back, {user.name}</h1>

                    {/* STATS */}
                    <div className="flex flex-wrap gap-4 p-4">
                        <StatCard title="Total Bets Placed" value={stats.totalBets} />
                        <StatCard title="Win Rate" value={`${stats.winRate}%`} />
                        <StatCard title="Total Winnings" value={`$${stats.winnings}`} />
                        <StatCard title="Available Balance" value={`$${stats.balance}`} />
                    </div>

                    {/* MATCHES */}
                    <Section title="Favorite Teams - Upcoming Matches">
                        <table className="flex-1">
                            <thead>
                            <tr>
                                <th className="px-4 py-3 text-left">Match</th>
                                <th className="px-4 py-3 text-left">Date/Time</th>
                                <th className="px-4 py-3 text-left">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            {matches.map((m, i) => (
                                <tr key={i} className="border-t border-[#d7d0e7]">
                                    <td className="px-4 py-2">{m.match}</td>
                                    <td className="px-4 py-2 text-[#654e97]">{m.dateTime}</td>
                                    <td className="px-4 py-2 text-[#654e97] font-bold">Bet Now</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Section>

                    {/* STANDINGS */}
                    <Section title="Followed Leagues - Standings">
                        <table className="flex-1">
                            <thead>
                            <tr>
                                <th className="px-4 py-3 text-left">Rank</th>
                                <th className="px-4 py-3 text-left">Team</th>
                                <th className="px-4 py-3 text-left">Points</th>
                                <th className="px-4 py-3 text-left">Goal Difference</th>
                            </tr>
                            </thead>
                            <tbody>
                            {standings.map((s, i) => (
                                <tr key={i} className="border-t border-[#d7d0e7]">
                                    <td className="px-4 py-2 text-[#654e97]">{s.rank}</td>
                                    <td className="px-4 py-2">{s.team}</td>
                                    <td className="px-4 py-2 text-[#654e97]">{s.points}</td>
                                    <td className="px-4 py-2 text-[#654e97]">{s.goalDiff}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Section>
                </div>
            </div>
        </div>
    );
}

export default UserDashboard;