import { SearchX, Home, ArrowLeft } from 'lucide-react';
import { Button } from './ui/button';

export default function NotFoundPage() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-[#1a0b2e] via-[#2d1b4e] to-[#1a0b2e] flex items-center justify-center px-4">
            <div className="max-w-md w-full">
                <div className="bg-[#2d1b4e]/50 backdrop-blur-sm rounded-xl p-8 border border-[#4a2f6e] shadow-2xl">
                    <div className="flex flex-col items-center text-center space-y-6">
                        {/* 404 Icon */}
                        <div className="relative">
                            <div className="absolute inset-0 bg-[#00ff88] blur-2xl opacity-20 rounded-full"></div>
                            <div className="relative bg-[#3d2463] p-6 rounded-full border-2 border-[#00ff88]">
                                <SearchX className="w-16 h-16 text-[#00ff88]" />
                            </div>
                        </div>

                        {/* 404 Number */}
                        <div className="space-y-2">
                            <div className="flex items-center justify-center gap-2">
                                <span className="text-6xl text-[#00ff88]">404</span>
                            </div>
                            <h1 className="text-[#00ff88]">Page Not Found</h1>
                            <p className="text-gray-300">
                                The page you're looking for doesn't exist or has been moved.
                            </p>
                        </div>

                        {/* Suggested Actions */}
                        <div className="w-full bg-[#1a0b2e]/60 rounded-lg p-4 border border-[#4a2f6e]">
                            <p className="text-sm text-gray-400">
                                Here are some helpful links instead:
                            </p>
                        </div>

                        {/* Action Buttons */}
                        <div className="flex flex-col sm:flex-row gap-3 w-full pt-2">
                            <Button
                                onClick={() => window.history.back()}
                                variant="outline"
                                className="flex-1 border-[#00ff88] text-[#00ff88] hover:bg-[#00ff88]/10 transition-all duration-200 gap-2"
                            >
                                <ArrowLeft className="w-4 h-4" />
                                Go Back
                            </Button>
                            <Button
                                onClick={() => window.location.href = '/'}
                                className="flex-1 bg-[#00ff88] hover:bg-[#00dd77] text-[#1a0b2e] transition-all duration-200 gap-2"
                            >
                                <Home className="w-4 h-4" />
                                Home
                            </Button>
                        </div>
                    </div>
                </div>

                {/* Footer Text */}
                <p className="text-center text-gray-500 mt-6">
                    Lost? Try searching or return to the homepage
                </p>
            </div>
        </div>
    );
}
