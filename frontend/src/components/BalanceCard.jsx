import React, { useContext } from "react";
import { UserContext } from "../UserContext";

export default function BalanceCard() {
  const { user } = useContext(UserContext); // Get user from context

  if (!user) return null; // Optionally, render nothing if user isn't loaded yet

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 text-center">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">Account Balance</h2>
      </div>
      <div className="text-4xl font-bold text-[#00ff85] mb-2">R {user.account_balance}</div>
      <p className="opacity-80">Available for betting and withdrawals</p>
      <div className="grid grid-cols-2 gap-4 mt-6 md:grid-cols-1">
        <button
          className="bg-[#00ff85] text-[#38003c] py-3 rounded-lg font-semibold hover:bg-[#00d46e] transition"
          onClick={() => alert("Deposit funds modal would open here")}
        >
          Deposit Funds
        </button>
        <button
          className="border border-[#00ff85] text-[#00ff85] py-3 rounded-lg font-semibold hover:bg-white/10 transition"
          onClick={() => alert("Withdrawal modal would open here")}
        >
          Withdraw Money
        </button>
      </div>
    </div>
  );
}
