export default function StatsCard(){
    return(
          <div className="stats">
            <div className="stat">
                <div className="stat-label">Total Bets Placed</div>
                <div className="stat-value">125</div>
            </div>
            <div className="stat">
                <div className="stat-label">Win Rate</div>
                <div className="stat-value">68%</div>
            </div>
            <div className="stat">
                <div className="stat-label">Total Winnings</div>
                <div className="stat-value">R 15,250</div>
            </div>
            <div className="stat">
                <div className="stat-label">Available Balance</div>
                <div className="stat-value">R 1,300</div>
            </div>
        </div>
    );
}