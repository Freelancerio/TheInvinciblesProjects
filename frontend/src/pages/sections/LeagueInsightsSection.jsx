import React from "react";
import { useNavigate } from "react-router-dom";
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";

export const LeagueInsightsSection = () => {
  const navigate = useNavigate();

  const handleNavigation = (path) => {
    navigate(path);
  };

  const navigationItems = [
    { label: "Home", onClick: () => handleNavigation("/dashboard") },
    { label: "Live", onClick: null },
    { label: "Fixtures", onClick: () => handleNavigation("/fixtures") },
    { label: "Results", onClick: null },
    { label: "Promotions", onClick: null },
  ];

  return (
    <header className="flex items-center justify-between px-10 py-3 border-b border-[#e5e8ea] w-full translate-y-[-1rem] animate-fade-in opacity-0">
      <div className="flex items-center gap-4">
        <img
          className="flex-shrink-0"
          alt="Depth frame"
          src="https://c.animaapp.com/mfbg0pmw3MAIHg/img/depth-4--frame-0.svg"
        />
        <div className="flex flex-col items-start">
          <h1 className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-lg tracking-[0] leading-[23px] whitespace-nowrap">
            Smartbet
          </h1>
        </div>
      </div>

      <div className="flex items-center justify-end gap-8 flex-1">
        <nav className="flex items-center gap-9 h-10">
          {navigationItems.map((item, index) => (
            <button
              key={item.label}
              onClick={item.onClick}
              className="flex flex-col items-start transition-colors hover:text-white cursor-pointer"
            >
              <span className="[font-family:'Inter',Helvetica] font-medium text-[#b0bec5] text-sm tracking-[0] leading-[21px] whitespace-nowrap">
                {item.label}
              </span>
            </button>
          ))}
        </nav>

        <Button className="h-10 w-[84px] max-w-[480px] px-4 py-0 bg-[#723ae8] hover:bg-[#5f2bc4] rounded-lg transition-colors">
          <span className="[font-family:'Inter',Helvetica] font-bold text-[#b0bec5] text-sm text-center tracking-[0] leading-[21px] whitespace-nowrap">
            Help
          </span>
        </Button>

        <Avatar
          className="w-10 h-10 cursor-pointer hover:opacity-80 transition-opacity"
          onClick={() => handleNavigation("/profile")}
        >
          <AvatarImage
            src="data:image/png;base64,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"
            alt="User avatar"
            className="object-cover"
          />
        </Avatar>
      </div>
    </header>
  );
};