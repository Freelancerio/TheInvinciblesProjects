import { AlertCircle } from 'lucide-react';
import { Button } from './ui/button';

export default function ErrorPage() {
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
              <h1 className="text-[#00ff88]">Oops! Something went wrong</h1>
              <p className="text-gray-300">
                We encountered an unexpected error. Don't worry, we're on it!
              </p>
            </div>

            {/* Error Details (Optional) */}
            <div className="w-full bg-[#1a0b2e]/60 rounded-lg p-4 border border-[#4a2f6e]">
              <p className="text-sm text-gray-400">
                Error Code: <span className="text-[#00ff88]">500</span>
              </p>
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-3 w-full pt-2">
              <Button
                onClick={() => window.location.reload()}
                className="flex-1 bg-[#00ff88] hover:bg-[#00dd77] text-[#1a0b2e] transition-all duration-200"
              >
                Try Again
              </Button>
              <Button
                onClick={() => window.location.href = '/'}
                variant="outline"
                className="flex-1 border-[#00ff88] text-[#00ff88] hover:bg-[#00ff88]/10 transition-all duration-200"
              >
                Go Home
              </Button>
            </div>
          </div>
        </div>

        {/* Footer Text */}
        <p className="text-center text-gray-500 mt-6">
          If this problem persists, please contact support
        </p>
      </div>
    </div>
  );
}
