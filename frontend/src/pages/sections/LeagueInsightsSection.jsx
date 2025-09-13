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
    { label: "Live", onClick: null },
    { label: "Fixtures", onClick: () => handleNavigation("/fixtures") },
    { label: "Results", onClick: null },
    { label: "Deposit", onClick: ()=> handleNavigation("/deposit") },
    { label: "Withdrawal", onClick: ()=> handleNavigation("/Withdrawal")},
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
            src="https://www.google.com/imgres?q=profile%20ico&imgurl=https%3A%2F%2Fa0.anyrgb.com%2Fpngimg%2F1912%2F680%2Ficon-user-profile-avatar-ico-facebook-user-head-black-icons-circle.png&imgrefurl=https%3A%2F%2Fwww.anyrgb.com%2Fen-clipart-yuiiv&docid=N-AOg-NTImD5UM&tbnid=NDxWb31inrqAiM&vet=12ahUKEwjW55bm19WPAxWMk_0HHZZpG2AQM3oECBcQAA..i&w=512&h=512&hcb=2&ved=2ahUKEwjW55bm19WPAxWMk_0HHZZpG2AQM3oECBcQAA"
            alt="User avatar"
            className="object-cover"
          />
        </Avatar>
      </div>
    </header>
  );
};