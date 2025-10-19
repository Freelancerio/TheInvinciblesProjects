import React, { useState, useContext } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { auth } from "../firebase";
import { UserContext } from "../UserContext";

export default function Header() {
  const navigate = useNavigate();
  const { user, logoutUser } = useContext(UserContext);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = async () => {
    try {
      await auth.signOut();
      logoutUser();
      navigate("/");
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  const navLinks = [
    { name: "Home", to: "/home" },
    { name: "Profile", to: "/profile" },
    { name: "Predictions", to: "/predictions" },
    { name: "Bets", to: "/betting" },
    { name: "Leaderboards", to: "/leaderboards" },
  ];

  return (
    <header className="sticky top-0 z-50 bg-[#38003c] shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 md:px-8">
        <nav className="flex justify-between items-center py-4">
          {/* Logo */}
          <div className="flex items-center font-bold text-xl text-white">
            <i className="fas fa-futbol mr-3 text-2xl text-[#00ff85]"></i>
            <span>EPL SmartBet</span>
          </div>

          {/* Desktop nav links */}
          {user && (
            <div className="hidden md:flex gap-6">
              {navLinks.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  className={({ isActive }) =>
                    `px-3 py-2 rounded font-medium transition ${
                      isActive
                        ? "text-[#00ff85] bg-white/10"
                        : "text-white hover:text-[#00ff85] hover:bg-white/10"
                    }`
                  }
                >
                  {link.name}
                </NavLink>
              ))}
              <button
                onClick={handleLogout}
                className="px-3 py-2 rounded font-medium text-white hover:text-[#00ff85] hover:bg-white/10 transition"
              >
                Logout
              </button>
            </div>
          )}

          {/* Mobile menu button */}
          {user && (
            <div className="md:hidden flex items-center">
              <button
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                className="text-white focus:outline-none"
              >
                <i className={`fas ${mobileMenuOpen ? "fa-times" : "fa-bars"} text-2xl`}></i>
              </button>
            </div>
          )}

          {/* User avatar */}
          {user && (
            <div className="hidden md:flex items-center gap-4 ml-4">
              <div className="flex items-center gap-2 cursor-pointer">
                <div className="w-10 h-10 rounded-full bg-[#00ff85] text-[#38003c] flex items-center justify-center font-bold">
                  {user.username?.[0] || "?"}
                </div>
                <span className="truncate max-w-[100px]">{user.username || user.email}</span>
              </div>
            </div>
          )}
        </nav>

        {/* Mobile nav menu */}
        {mobileMenuOpen && (
          <div className="md:hidden bg-[#38003c] w-full py-2 px-4 flex flex-col gap-2">
            {navLinks.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={() => setMobileMenuOpen(false)}
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                {link.name}
              </NavLink>
            ))}
            <button
              onClick={handleLogout}
              className="px-3 py-2 rounded font-medium text-white hover:text-[#00ff85] hover:bg-white/10 transition text-left"
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </header>
  );
}
