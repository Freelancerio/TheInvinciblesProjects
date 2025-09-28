import React from "react";
import Header from "./Header";
import UserDetailsCard from "./UserDetailsCard";
import BalanceCard from "./BalanceCard";
import BettingStatsCard from "./BettingStatsCard";
import LeaderboardCard from "./LeaderboardCard";
import BetHistoryCard from "./BetHistoryCard";

export default function ProfilePage() {
  return (
    <div style={{ background: "linear-gradient(rgba(56,0,60,0.9), rgba(56,0,60,0.95)), url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3') center/cover no-repeat fixed" }} className="min-h-screen text-white">
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
