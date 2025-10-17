import React, { useState, useEffect } from 'react';
import Header from './Header';
import WelcomeBanner from './WelcomeBanner';
import UpcomingMatches from './UpcomingMatches';
import RecentMatches from './RecentMatches';
import LeagueTable from './LeagueTable';
import QuickActions from './QuickActions';
import LoadingPage from './LoadingPage';
import getBaseUrl from '../api.js';

const Dashboard = () => {
  const [loading, setLoading] = useState(true);
  const [dashboardData, setDashboardData] = useState({
    upcomingMatches: [],
    recentMatches: [],
    standings: []
  });
  const baseUrl = getBaseUrl();

  useEffect(() => {
    const loadAllDashboardData = async () => {
      try {
        setLoading(true);
        const idToken = localStorage.getItem("authToken");

        // Fetch all dashboard data in parallel and wait for ALL to complete
        const [upcomingData, recentData, standingsData] = await Promise.all([
          // Upcoming matches
          fetch(`${baseUrl}/api/matches/upcoming?page=0&size=5&season=2025`, {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${idToken}`,
            },
          }).then(res => {
            if (!res.ok) throw new Error("Failed to fetch upcoming matches");
            return res.json();
          }),

          // Recent matches
          fetch(`${baseUrl}/api/matches/recent?page=0&size=5&season=2025`, {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${idToken}`,
            },
          }).then(res => {
            if (!res.ok) throw new Error("Failed to fetch recent matches");
            return res.json();
          }),

          // League standings
          fetch(`${baseUrl}/api/standings/top5?season=2025`, {
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${idToken}`,
            },
          }).then(res => {
            if (!res.ok) throw new Error("Failed to fetch standings");
            return res.json();
          }),
        ]);

        // Store all data
        setDashboardData({
          upcomingMatches: upcomingData.content || [],
          recentMatches: recentData.content || [],
          standings: standingsData || []
        });

        // Only set loading to false after ALL data is received
        setLoading(false);
      } catch (error) {
        console.error("Error loading dashboard data:", error);
        // Even on error, stop loading so user can see the page
        setLoading(false);
      }
    };

    loadAllDashboardData();
  }, [baseUrl]);

  // Keep showing LoadingPage until ALL data is loaded
  if (loading) {
    return <LoadingPage />;
  }

  return (
    <div className="min-h-screen text-white" style={{
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://e0.365dm.com/22/09/768x432/skysports-premier-league-promo_5897092.png?20220913083410') center/cover no-repeat fixed"
    }}>
      <Header />

      <div className="dashboard py-[30px]">
        <div className="container max-w-[1400px] mx-auto px-5">
          <WelcomeBanner />

          <div className="dashboard-grid grid grid-cols-1 md:grid-cols-2 gap-[25px]">
            <div className="left-column">
              <UpcomingMatches paginated={false} size={5} preloadedData={dashboardData.upcomingMatches} />
              <RecentMatches paginated={false} season={2025} preloadedData={dashboardData.recentMatches} />
            </div>

            <div className="right-column">
              <LeagueTable topN={5} season={2025} preloadedData={dashboardData.standings} />
              <QuickActions />
            </div>
          </div>
        </div>
      </div>

      <style>{`
        .card-bg {
          background: rgba(255, 255, 255, 0.05);
        }
        .team-logo {
          width: 25px;
          height: 25px;
          font-size: 0.7rem;
        }
        @media (max-width: 768px) {
          .match-teams-mobile {
            flex-direction: column;
            gap: 5px;
          }
          .team-mobile {
            width: 100%;
            justify-content: center;
          }
        }
      `}</style>
    </div>
  );
};

export default Dashboard;