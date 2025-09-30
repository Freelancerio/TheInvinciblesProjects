import React from "react";
import Header from "./Header";
import UserDetailsCard from "./UserDetailsCard";
import BalanceCard from "./BalanceCard";
import BettingStatsCard from "./BettingStatsCard";
import LeaderboardCard from "./LeaderboardCard";
import BetHistoryCard from "./BetHistoryCard";

export default function ProfilePage() {
  return (
    <div style={{ background: "linear-gradient(rgba(56,0,60,0.9), rgba(56,0,60,0.95)), url('https://i.redd.it/93-20-wasnt-just-a-goal-it-was-a-moment-that-defined-an-era-v0-lf3hl9zxrige1.jpg?width=1944&format=pjpg&auto=webp&s=2b4092abd7c43685b90d4399f05d5845684c5a1b') center/cover no-repeat fixed" }} className="min-h-screen text-white">
      <Header />
      <main className="py-10 max-w-7xl mx-auto px-5">
        <h1 className="text-center text-3xl text-[#00ff85] font-semibold mb-10">User Profile</h1>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <UserDetailsCard />
          <BalanceCard />
          <BettingStatsCard />
          <LeaderboardCard />
          <BetHistoryCard />
        </div>
      </main>
    </div>
  );
}
