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
    standings: [],
    otherSports: [] // new state for other sports
  });
  const baseUrl = getBaseUrl();

  useEffect(() => {
    const loadAllDashboardData = async () => {
      try {
        setLoading(true);
        const idToken = localStorage.getItem("authToken");

        const [upcomingData, recentData, standingsData, otherSportsData] = await Promise.all([
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

          // Other sports public data
          fetch(`https://prime-backend.azurewebsites.net/api/users/viewMatches`)
            .then(res => {
              if (!res.ok) throw new Error("Failed to fetch other sports");
              return res.json();
            }),
        ]);

        setDashboardData({
          upcomingMatches: upcomingData.content || [],
          recentMatches: recentData.content || [],
          standings: standingsData || [],
          otherSports: otherSportsData || []
        });

        setLoading(false);
      } catch (error) {
        console.error("Error loading dashboard data:", error);
        setLoading(false);
      }
    };

    loadAllDashboardData();
  }, [baseUrl]);

  if (loading) {
    return <LoadingPage />;
  }

  return (
    <div className="min-h-screen text-white" style={{
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://e0.365dm.com/22/09/768x432/skysports-premier-league-promo_5897092.png?20220913083410') center/cover no-repeat fixed"
    }}>
      <Header />

      <div className="dashboard py-6 md:py-10">
        <div className="container max-w-[1400px] mx-auto px-4 sm:px-6 md:px-8">
          <WelcomeBanner />

          <div className="dashboard-grid grid grid-cols-1 lg:grid-cols-2 gap-6 md:gap-8">
            <div className="left-column space-y-6">
              <UpcomingMatches
                paginated={false}
                size={5}
                preloadedData={dashboardData.upcomingMatches}
              />
              <RecentMatches
                paginated={false}
                season={2025}
                preloadedData={dashboardData.recentMatches}
              />
            </div>

            <div className="right-column space-y-6">
              <LeagueTable
                topN={5}
                season={2025}
                preloadedData={dashboardData.standings}
              />
              <QuickActions />
            </div>
          </div>

          {/* Other Sports Section */}
          <div className="other-sports mt-10">
            <h2 className="text-2xl font-bold mb-4">Other Sports</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {dashboardData.otherSports.map(match => (
                <div key={match.id} className="card-bg p-4">
                  <p><strong>Sport:</strong> {match.sportType}</p>
                  <p><strong>Home Team:</strong> {match.homeTeam}</p>
                  <p><strong>Away Team:</strong> {match.awayTeam}</p>
                  <p><strong>Start Time:</strong> {new Date(match.startTime).toLocaleString()}</p>
                  <p><strong>Venue:</strong> {match.venue}</p>
                  <p><strong>Status:</strong> {match.status}</p>
                </div>
              ))}
            </div>
          </div>

        </div>
      </div>

      <style>{`
        .card-bg {
          background: rgba(255, 255, 255, 0.05);
          border-radius: 12px;
          padding: 16px;
          transition: all 0.3s ease;
        }
        .card-bg:hover {
          background: rgba(255, 255, 255, 0.08);
        }
      `}</style>
    </div>
  );
};

export default Dashboard;
