import { Loader2 } from 'lucide-react';

export default function LoadingPage() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-[#1a0b2e] via-[#2d1b4e] to-[#1a0b2e] flex items-center justify-center px-4">
            <div className="max-w-md w-full">
                <div className="bg-[#2d1b4e]/50 backdrop-blur-sm rounded-xl p-12 border border-[#4a2f6e] shadow-2xl">
                    <div className="flex flex-col items-center text-center space-y-8">
                        {/* Loading Spinner */}
                        <div className="relative">
                            {/* Glow Effect */}
                            <div className="absolute inset-0 bg-[#00ff88] blur-3xl opacity-30 rounded-full animate-pulse"></div>

                            {/* Outer Ring */}
                            <div className="relative">
                                <div className="w-24 h-24 rounded-full border-4 border-[#3d2463] border-t-[#00ff88] animate-spin"></div>

                                {/* Inner Circle */}
                                <div className="absolute inset-0 flex items-center justify-center">
                                    <Loader2 className="w-12 h-12 text-[#00ff88] animate-spin" style={{ animationDirection: 'reverse' }} />
                                </div>
                            </div>
                        </div>

                        {/* Loading Text */}
                        <div className="space-y-3">
                            <h2 className="text-[#00ff88]">Loading</h2>
                            <p className="text-white">
                                Please wait while we prepare your content...
                            </p>

                        </div>

                        {/* Loading Progress Indicator */}
                        <div className="w-full space-y-3">
                            <div className="w-full h-2 bg-[#1a0b2e]/60 rounded-full overflow-hidden">
                                <div className="h-full bg-gradient-to-r from-[#00ff88] to-[#00cc66] rounded-full animate-pulse"></div>
                            </div>
                            <div className="flex justify-center gap-2">
                                <div className="w-2 h-2 bg-[#00ff88] rounded-full animate-bounce" style={{ animationDelay: '0ms' }}></div>
                                <div className="w-2 h-2 bg-[#00ff88] rounded-full animate-bounce" style={{ animationDelay: '150ms' }}></div>
                                <div className="w-2 h-2 bg-[#00ff88] rounded-full animate-bounce" style={{ animationDelay: '300ms' }}></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
