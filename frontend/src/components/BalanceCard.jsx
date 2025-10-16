import React, { useContext, useState } from "react";
import { UserContext } from "../UserContext";

export default function BalanceCard() {
  const { user } = useContext(UserContext);
  const [showModal, setShowModal] = useState(false);
  const [amount, setAmount] = useState("");

  if (!user) return null;

  const handleDeposit = async () => {
    if (!amount || isNaN(amount) || Number(amount) <= 0) {
      alert("Please enter a valid amount");
      return;
    }

    // Call backend to create Stripe checkout session
    const token = localStorage.getItem("authToken");
    const response = await fetch("/create-checkout-session", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ amount: Number(amount) * 100 }), // convert to cents
    });

    const data = await response.json();
    window.location.href = data.url; // redirect to Stripe checkout
  };

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
          onClick={() => setShowModal(true)}
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

     {showModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
          <div className="bg-white/5 backdrop-blur-md border border-white/10 rounded-xl p-6 w-80 md:w-96 text-center">
            <h3 className="text-[#00ff85] text-xl font-semibold mb-4">Enter Deposit Amount</h3>
            <input
              type="number"
              placeholder="Amount in R"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              className="w-full p-3 mb-4 rounded-lg border border-white/10 bg-white/5 text-[#00ff85] font-semibold focus:outline-none focus:ring-2 focus:ring-[#00ff85]"
            />
            <div className="flex justify-between gap-4">
              <button
                className="flex-1 py-3 rounded-lg bg-gray-700 hover:bg-gray-600 text-white font-semibold transition"
                onClick={() => setShowModal(false)}
              >
                Cancel
              </button>
              <button
                className="flex-1 py-3 rounded-lg bg-[#00ff85] hover:bg-[#00d46e] text-[#38003c] font-semibold transition"
                onClick={handleDeposit}
              >
                Deposit
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
