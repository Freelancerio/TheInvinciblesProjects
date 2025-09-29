// components/Dashboard.js
import React from 'react';
import Header from './Header';
import WelcomeBanner from './WelcomeBanner';
import UpcomingMatches from './UpcomingMatches';
import RecentMatches from './RecentMatches';
import LeagueTable from './LeagueTable';
import QuickActions from './QuickActions';

const Dashboard = () => {
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
              <UpcomingMatches paginated={false} size={5} />
              <RecentMatches paginated={false} season={2025} />

            </div>
            
            <div className="right-column">
              <LeagueTable topN={5} season={2025} />
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