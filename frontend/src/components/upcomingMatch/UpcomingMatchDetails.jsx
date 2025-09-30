// UpcomingMatchDetails.jsx
import Header from '../Header';
import FixtureInfo from './FixtureInfo';
import HeadToHead from './HeadToHead';
import RecentForm from './RecentForm';
import TeamStatistics from './TeamStatistics';
import MatchPrediction from './MatchPrediction';

const UpcomingMatchDetails = () => {
  return (
    <div className="min-h-screen text-white" style={{
      background: "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95)), url('https://static.standard.co.uk/2025/07/03/9/15/Diogo-Jota.jpeg?trim=1885,0,2892,0&quality=75&auto=webp&width=960') center/cover no-repeat fixed"
    }}>
    <Header/>
    <div className="fixture-content py-8">
      <div className="container mx-auto px-5">
        <h1 className="page-title text-4xl text-secondary mb-8 text-center">Fixture Details</h1>
        
        <div className="fixture-grid grid grid-cols-1 lg:grid-cols-2 gap-6">
          <FixtureInfo />
          <HeadToHead />
          <RecentForm />
          <TeamStatistics />
          <MatchPrediction />
        </div>
      </div>
    </div>
    </div>
  );
};

export default UpcomingMatchDetails;