import React, { useEffect, useContext } from "react";
import { useSearchParams } from "react-router-dom";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();

const PaymentSuccess = () => {
  const [searchParams] = useSearchParams();
  const sessionId = searchParams.get("session_id");
  const { user, setUser } = useContext(UserContext);

  useEffect(() => {
    if (sessionId && user) {
      verifyPayment(sessionId, user.firebaseId);
    }

    // Auto-redirect after 5 seconds
    const timer = setTimeout(() => {
      window.location.href = "/profile";
    }, 5000);

    return () => clearTimeout(timer);
  }, [sessionId, user]);

  const verifyPayment = async (sessionId, userId) => {
    try {
      const token = localStorage.getItem("authToken");
      const response = await fetch(`${baseUrl}/api/payment/verify-session`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ sessionId, userId }),
      });

      const data = await response.json();

      if (response.ok && data.updatedUser) {
        setUser(data.updatedUser);
        localStorage.setItem("user-data", JSON.stringify(data.updatedUser));
      } else {
        console.error("Payment verification failed:", data.error);
      }
    } catch (err) {
      console.error("Error verifying payment:", err);
    }
  };

  return (
    <div
      className="text-white min-h-screen flex items-center justify-center p-4"
      style={{
        background:
          "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95))",
      }}
    >
      <div className="text-center max-w-md">
        {/* Success Icon */}
        <div className="w-20 h-20 bg-secondary rounded-full flex items-center justify-center mx-auto mb-6">
          <i className="fas fa-check text-primary text-2xl"></i>
        </div>

        {/* Message */}
        <h1 className="text-3xl font-bold text-secondary mb-4">
          Payment Successful
        </h1>
        <p className="text-gray-300 mb-8">
          Your payment has been processed successfully. Your account has been credited.
        </p>

        {/* Redirect Button */}
        <button
          onClick={() => (window.location.href = "/profile")}
          className="bg-secondary text-primary font-semibold py-3 px-8 rounded-lg hover:bg-[#00d46e] transition-colors w-full"
        >
          Return to Profile
        </button>
      </div>
    </div>
  );
};

export default PaymentSuccess;
