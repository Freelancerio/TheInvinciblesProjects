import React from "react";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";

export const SearchSection = () => {
  const navigationItems = [
    { label: "Home", href: "#" },
    { label: "Teams", href: "#" },
    { label: "Fixtures", href: "#" },
    { label: "Standings", href: "#" },
    { label: "News", href: "#" },
  ];

  return (
    <header className="flex items-center justify-between px-10 py-3 border-b border-[#e5e8ea] w-full translate-y-[-1rem] animate-fade-in opacity-0">
      <div className="flex items-center gap-8">
        <div className="flex items-center gap-4 translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:200ms]">
          <img
            className="flex-shrink-0"
            alt="SmartBet Logo"
            src="https://c.animaapp.com/mfbgt44woiUDGL/img/depth-4--frame-0.svg"
          />
          <div className="flex flex-col items-start">
            <h1 className="[font-family:'Public_Sans',Helvetica] font-bold text-[#e5e8eb] text-lg tracking-[0] leading-[23px]">
              SmartBet
            </h1>
          </div>
        </div>

        <NavigationMenu className="translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:400ms]">
          <NavigationMenuList className="flex items-center gap-9">
            {navigationItems.map((item, index) => (
              <NavigationMenuItem key={item.label}>
                <NavigationMenuLink
                  href={item.href}
                  className="[font-family:'Public_Sans',Helvetica] font-medium text-[#1e1e2f] text-sm tracking-[0] leading-[21px] hover:text-primary transition-colors"
                >
                  {item.label}
                </NavigationMenuLink>
              </NavigationMenuItem>
            ))}
          </NavigationMenuList>
        </NavigationMenu>
      </div>

      <div className="flex-1 flex justify-end translate-y-[-1rem] animate-fade-in opacity-0 [--animation-delay:600ms]">
        <img
          className="flex-shrink-0"
          alt="User Profile"
          src="https://c.animaapp.com/mfbgt44woiUDGL/img/depth-3--frame-1.svg"
        />
      </div>
    </header>
  );
};
