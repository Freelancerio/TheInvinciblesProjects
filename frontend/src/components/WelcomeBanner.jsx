// components/WelcomeBanner.js
import React, { useContext } from "react";
import { UserContext } from "../UserContext";

const WelcomeBanner = () => {
  const { user } = useContext(UserContext);

  if (!user) return null; // Optional: don't render until user is loaded

  return (
    <div className="card-bg backdrop-blur-md rounded-[10px] p-5 mb-[30px] flex justify-between items-center border border-[rgba(255,255,255,0.1)]">
      <div className="welcome-text">
        <h2 className="text-secondary mb-1 text-[1.8rem] font-semibold">
          Welcome back, {user.username}!
        </h2>
        <p>Check out the latest matches and place your bets.</p>
      </div>
      <div className="balance-card bg-gradient-to-br from-primary to-[#4a0050] py-[15px] px-[25px] rounded-[10px] text-right shadow-[0_5px_15px_rgba(0,0,0,0.2)]">
        <p className="mb-1 opacity-80">Your Balance</p>
        <div className="balance-amount text-[1.8rem] font-bold text-secondary">
          R{user.account_balance}
        </div>
      </div>
    </div>
  );
};

export default WelcomeBanner;
