import React, { useEffect } from "react";

const PaymentCanceled = () => {
  // Auto-redirect after 5 seconds
  useEffect(() => {
    const timer = setTimeout(() => {
      window.location.href = "/profile";
    }, 5000);

    return () => clearTimeout(timer); // Cleanup timer on unmount
  }, []);

  return (
    <div
      className="text-white min-h-screen flex items-center justify-center p-4"
      style={{
        background:
          "linear-gradient(rgba(56, 0, 60, 0.9), rgba(56, 0, 60, 0.95))",
      }}
    >
      <div className="text-center max-w-md">
        {/* Canceled Icon */}
        <div className="w-20 h-20 bg-accent rounded-full flex items-center justify-center mx-auto mb-6">
          <i className="fas fa-times text-white text-2xl"></i>
        </div>

        {/* Message */}
        <h1 className="text-3xl font-bold text-accent mb-4">Payment Canceled</h1>
        <p className="text-gray-300 mb-8">
          Your payment was canceled. No charges have been made to your account.
        </p>

        {/* Redirect Button */}
        <button
          onClick={() => (window.location.href = "/profile")}
          className="bg-secondary text-primary font-semibold py-3 px-8 rounded-lg hover:bg-[#00d46e] transition-colors w-full"
        >
          Try Again
        </button>
      </div>
    </div>
  );
};

export default PaymentCanceled;
