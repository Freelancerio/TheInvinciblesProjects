import Navbar from "../components/navbar";
import ProfileCard from "../components/profile-Card";
import StatsCard from "../components/statsCard";

export default function Profile() {
  
  return (
    <main className="profile-wrap">
      <Navbar/>
      <ProfileCard/>
      <StatsCard/>
    </main>
  );
}
