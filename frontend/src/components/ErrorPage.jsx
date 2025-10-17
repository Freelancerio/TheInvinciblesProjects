import React from 'react';
import { AlertCircle, Home, RefreshCw } from 'lucide-react';

export default function ErrorPage() {
  const handleGoHome = () => {
    window.location.href = '/';
  };

  const handleTryAgain = () => {
    window.location.reload();
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#1a0b2e] via-[#2d1b4e] to-[#1a0b2e] flex items-center justify-center px-4">
      <div className="max-w-md w-full">
        <div className="bg-[#2d1b4e]/50 backdrop-blur-sm rounded-xl p-8 border border-[#4a2f6e] shadow-2xl">
          <div className="flex flex-col items-center text-center space-y-6">
            {/* Error Icon */}
            <div className="relative">
              <div className="absolute inset-0 bg-[#00ff88] blur-2xl opacity-20 rounded-full"></div>
              <div className="relative bg-[#3d2463] p-6 rounded-full border-2 border-[#00ff88]">
                <AlertCircle className="w-16 h-16 text-[#00ff88]" />
              </div>
            </div>

            {/* Error Title */}
            <div className="space-y-2">
              <h1 className="text-3xl font-bold text-[#00ff88]">404 - Page Not Found</h1>
              <p className="text-gray-300">
                Oops! The page you're looking for doesn't exist.
              </p>
            </div>

            {/* Error Details */}
            <div className="w-full bg-[#1a0b2e]/60 rounded-lg p-4 border border-[#4a2f6e]">
              <p className="text-sm text-gray-400">
                Error Code: <span className="text-[#00ff88] font-semibold">404</span>
              </p>
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-3 w-full pt-2">
              <button
                onClick={handleTryAgain}
                className="flex-1 flex items-center justify-center gap-2 bg-[#00ff88] hover:bg-[#00dd77] text-[#1a0b2e] font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
              >
                <RefreshCw className="w-5 h-5" />
                Try Again
              </button>
              <button
                onClick={handleGoHome}
                className="flex-1 flex items-center justify-center gap-2 border-2 border-[#00ff88] text-[#00ff88] hover:bg-[#00ff88]/10 font-semibold py-3 px-6 rounded-lg transition-all duration-200 transform hover:scale-105"
              >
                <Home className="w-5 h-5" />
                Go Home
              </button>
            </div>
          </div>
        </div>

        {/* Footer Text */}
        <p className="text-center text-gray-500 mt-6 text-sm">
          If this problem persists, please contact support
        </p>
      </div>
    </div>
  );
}