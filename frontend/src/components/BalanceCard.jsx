import React, { useContext, useState } from "react";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api.js";
import toast, { Toaster } from "react-hot-toast";

const baseUrl = getBaseUrl();

export default function BalanceCard() {
  const { user } = useContext(UserContext);
  const [showModal, setShowModal] = useState(false);
  const [amount, setAmount] = useState("");

  if (!user) return null;

  const handleDeposit = async () => {
    if (!amount || isNaN(amount) || Number(amount) <= 0) {
      toast.error("Please enter a valid amount");
      return;
    }

    try {
      const token = localStorage.getItem("authToken");
      const response = await fetch(`${baseUrl}/api/payment/create-checkout-session`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: user.firebaseId,
          amount: Number(amount) * 100,
        }),
      });

      if (!response.ok) throw new Error("Failed to create checkout session");

      const data = await response.json();
      if (data.url) {
        toast.success("Redirecting to secure payment...");
        window.location.href = data.url;
      } else {
        toast.error("Something went wrong. Please try again.");
      }
    } catch (err) {
      console.error(err);
      toast.error("Network error. Please try again later.");
    }
  };

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 text-center">
      <Toaster position="top-right" reverseOrder={false} />
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">Account Balance</h2>
      </div>
      <div className="text-4xl font-bold text-[#00ff85] mb-2">
        R {user.account_balance}
      </div>
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
          onClick={() => toast("Withdrawal modal would open here")}
        >
          Withdraw Money
        </button>
      </div>

      {showModal && (
        <div
          className="fixed inset-0 flex items-center justify-center z-50 p-4 bg-black/60 backdrop-blur-sm transition-opacity duration-300"
          onClick={() => setShowModal(false)}
        >
          <div
            className="bg-[#1b0026] border border-white/10 rounded-2xl p-8 w-full max-w-md relative transform scale-90 opacity-0 animate-modalIn"
            onClick={(e) => e.stopPropagation()}
          >
            {/* Close Button */}
            <button
              onClick={() => setShowModal(false)}
              className="absolute top-4 right-4 text-white/70 hover:text-white transition-colors"
            >
              <i className="fas fa-times text-xl"></i>
            </button>

            {/* Modal Content */}
            <div className="text-center">
              <div className="w-16 h-16 bg-secondary rounded-full flex items-center justify-center mx-auto mb-4">
                <i className="fas fa-wallet text-primary text-xl"></i>
              </div>

              <h3 className="text-secondary text-2xl font-semibold mb-2">
                Deposit Funds
              </h3>
              <p className="text-white/80 mb-6">
                Enter the amount you want to deposit
              </p>

              {/* Amount Input */}
              <div className="relative mb-6">
                <div className="absolute left-4 top-1/2 transform -translate-y-1/2 text-white/60">
                  <span className="font-semibold">R</span>
                </div>
                <input
                  type="number"
                  placeholder="0.00"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  className="w-full p-4 pl-12 rounded-xl border border-white/20 bg-white/5 text-white text-xl font-semibold focus:outline-none focus:border-secondary focus:ring-2 focus:ring-secondary/20 transition-all"
                  min="1"
                  step="0.01"
                />
              </div>

              {/* Quick Amount Buttons */}
              <div className="grid grid-cols-3 gap-3 mb-6">
                {[100, 500, 1000].map((quickAmount) => (
                  <button
                    key={quickAmount}
                    onClick={() => setAmount(quickAmount.toString())}
                    className="py-2 rounded-lg border border-white/20 bg-white/5 text-white hover:bg-white/10 hover:border-secondary transition-all duration-200"
                  >
                    R {quickAmount}
                  </button>
                ))}
              </div>

              {/* Action Buttons */}
              <div className="flex gap-4">
                <button
                  className="flex-1 py-3 rounded-xl bg-white/10 hover:bg-white/20 text-white font-semibold border border-white/20 transition-all duration-200"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button
                  className="flex-1 py-3 rounded-xl bg-secondary hover:bg-[#00d46e] text-primary font-semibold transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-50"
                  onClick={handleDeposit}
                  disabled={!amount || Number(amount) <= 0}
                >
                  <i className="fas fa-lock"></i>
                  Deposit
                </button>
              </div>

              {/* Security Note */}
              <div className="mt-6 pt-4 border-t border-white/10">
                <div className="flex items-center justify-center gap-2 text-white/60 text-sm">
                  <i className="fas fa-shield-alt"></i>
                  <span>Secure payment powered by Stripe</span>
                </div>
              </div>
            </div>
          </div>

          {/* Animation CSS */}
          <style>
            {`
              @keyframes modalIn {
                0% { opacity: 0; transform: scale(0.9); }
                100% { opacity: 1; transform: scale(1); }
              }
              .animate-modalIn {
                animation: modalIn 0.25s ease-out forwards;
              }
            `}
          </style>
        </div>
      )}
    </div>
  );
}
