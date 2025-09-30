// components/QuickActions.js
import React from "react";
import { NavLink } from "react-router-dom";

const QuickActions = () => {
  const actionLinks = [
    { to: "/deposit", icon: "fa-money-bill-wave", label: "Deposit Funds" },
    { to: "/comparison", icon: "fa-chart-line", label: "Stats Analysis" },
    { to: "/leaderboards", icon: "fa-trophy", label: "Leaderboard" },
    { to: "/profile", icon: "fa-history", label: "Bet History" },
  ];

  return (
    <div className="card-bg backdrop-blur-md rounded-[10px] p-5 mb-[25px] border border-[rgba(255,255,255,0.1)]">
      <div className="card-header flex justify-between items-center mb-5 pb-[10px] border-b border-[rgba(255,255,255,0.1)]">
        <h3 className="card-title text-[1.3rem] font-semibold text-secondary">
          Quick Actions
        </h3>
      </div>
      <div className="quick-actions grid grid-cols-2 gap-[15px]">
        {actionLinks.map((action) => (
          <NavLink
            key={action.to}
            to={action.to}
            className="bg-[rgba(255,255,255,0.1)] border border-[rgba(255,255,255,0.1)] text-white p-[15px] rounded-[8px] cursor-pointer transition-all duration-300 ease-in-out flex flex-col items-center gap-[8px] hover:bg-[rgba(255,255,255,0.15)] hover:-translate-y-[3px]"
          >
            <i className={`fas ${action.icon} text-[1.5rem] text-secondary`}></i>
            <span>{action.label}</span>
          </NavLink>
        ))}
      </div>
    </div>
  );
};

export default QuickActions;
