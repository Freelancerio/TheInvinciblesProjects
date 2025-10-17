import React, { useState, useEffect, useContext } from "react";
import { UserContext } from "../UserContext";
import Header from "./Header";
import UserDetailsCard from "./UserDetailsCard";
import BalanceCard from "./BalanceCard";
import BettingStatsCard from "./BettingStatsCard";
import LeaderboardCard from "./LeaderboardCard";
import BetHistoryCard from "./BetHistoryCard";
import LoadingPage from "./LoadingPage";
import getBaseUrl from "../api.js";

export default function ProfilePage() {
  const { user } = useContext(UserContext);
  const [loading, setLoading] = useState(true);
  const baseUrl = getBaseUrl();

  useEffect(() => {
    const loadAllData = async () => {
      if (!user?.firebaseId) {
        setLoading(false);
        return;
      }

      try {
        setLoading(true);

        const token = localStorage.getItem("authToken");
        if (!token) throw new Error("No auth token found");

        // Fetch all data in parallel
        await Promise.all([
          // Bet History
          fetch(`${baseUrl}/api/bets/user/${user.firebaseId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }).then(res => {
            if (!res.ok) throw new Error(`Bet history error: ${res.status}`);
            return res.json();
          }),

          // Betting Stats
          fetch(`${baseUrl}/api/bets/stats/${user.firebaseId}`, {
            headers: { Authorization: `Bearer ${token}`, "Content-Type": "application/json" },
          }).then(res => {
            if (!res.ok) throw new Error(`Stats error: ${res.status}`);
            return res.json();
          }),

          // Leaderboard Position
          fetch(`${baseUrl}/api/leaderboard/alltime/${user.firebaseId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }).then(res => {
            if (!res.ok) throw new Error(`Leaderboard error: ${res.status}`);
            return res.json();
          }),
        ]);

        setLoading(false);
      } catch (err) {
        console.error("Error loading profile data:", err);
        setLoading(false);
      }
    };

    loadAllData();
  }, [user, baseUrl]);

  // Show LoadingPage while all data is loading
  if (loading) {
    return <LoadingPage />;
  }

  return (
    <div style={{ background: "linear-gradient(rgba(56,0,60,0.9), rgba(56,0,60,0.95)), url('https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/08/09/15/thierry-henry.jpg') center/cover no-repeat fixed" }} className="min-h-screen text-white">
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