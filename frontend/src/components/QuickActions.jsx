// components/QuickActions.js
import React from 'react';

const QuickActions = () => {
  return (
    <div className="card-bg backdrop-blur-md rounded-[10px] p-5 mb-[25px] border border-[rgba(255,255,255,0.1)]">
      <div className="card-header flex justify-between items-center mb-5 pb-[10px] border-b border-[rgba(255,255,255,0.1)]">
        <h3 className="card-title text-[1.3rem] font-semibold text-secondary">Quick Actions</h3>
      </div>
      <div className="quick-actions grid grid-cols-2 gap-[15px]">
        <button className="action-btn bg-[rgba(255,255,255,0.1)] border border-[rgba(255,255,255,0.1)] text-white p-[15px] rounded-[8px] cursor-pointer transition-all duration-300 ease-in-out flex flex-col items-center gap-[8px] hover:bg-[rgba(255,255,255,0.15)] hover:-translate-y-[3px]">
          <i className="fas fa-money-bill-wave action-icon text-[1.5rem] text-secondary"></i>
          <span>Deposit Funds</span>
        </button>
        <button className="action-btn bg-[rgba(255,255,255,0.1)] border border-[rgba(255,255,255,0.1)] text-white p-[15px] rounded-[8px] cursor-pointer transition-all duration-300 ease-in-out flex flex-col items-center gap-[8px] hover:bg-[rgba(255,255,255,0.15)] hover:-translate-y-[3px]">
          <i className="fas fa-chart-line action-icon text-[1.5rem] text-secondary"></i>
          <span>Stats Analysis</span>
        </button>
        <button className="action-btn bg-[rgba(255,255,255,0.1)] border border-[rgba(255,255,255,0.1)] text-white p-[15px] rounded-[8px] cursor-pointer transition-all duration-300 ease-in-out flex flex-col items-center gap-[8px] hover:bg-[rgba(255,255,255,0.15)] hover:-translate-y-[3px]">
          <i className="fas fa-trophy action-icon text-[1.5rem] text-secondary"></i>
          <span>Leaderboard</span>
        </button>
        <button className="action-btn bg-[rgba(255,255,255,0.1)] border border-[rgba(255,255,255,0.1)] text-white p-[15px] rounded-[8px] cursor-pointer transition-all duration-300 ease-in-out flex flex-col items-center gap-[8px] hover:bg-[rgba(255,255,255,0.15)] hover:-translate-y-[3px]">
          <i className="fas fa-history action-icon text-[1.5rem] text-secondary"></i>
          <span>Bet History</span>
        </button>
      </div>
    </div>
  );
};

export default QuickActions;