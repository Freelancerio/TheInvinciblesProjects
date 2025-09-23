import React, { useContext } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { auth } from "../firebase";
import { UserContext } from "../UserContext";

export default function Header() {
  const navigate = useNavigate();
  const { user, logoutUser } = useContext(UserContext);

  const handleLogout = async () => {
    try {
      await auth.signOut(); // Firebase logout
      logoutUser(); // clear context + localStorage
      navigate("/"); // redirect to landing/login
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  return (
    <header
      className="sticky top-0 z-50 shadow-md"
      style={{ backgroundColor: "#38003c" }}
    >
      <div className="max-w-7xl mx-auto px-5">
        <nav className="flex justify-between items-center py-4">
          {/* Logo */}
          <div className="flex items-center font-bold text-xl text-white">
            <i
              className="fas fa-futbol mr-3 text-2xl"
              style={{ color: "#00ff85" }}
            ></i>
            <span>EPL SmartBet</span>
          </div>

          {/* Nav links */}
          {user && (
            <div className="hidden md:flex gap-6">
              <NavLink
                to="/home"
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                Home
              </NavLink>
              <NavLink
                to="/profile"
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                Profile
              </NavLink>
              <NavLink
                to="/predictions"
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                Predictions
              </NavLink>
              <NavLink
                to="/bets"
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                Bets
              </NavLink>
              <NavLink
                to="/leaderboards"
                className={({ isActive }) =>
                  `px-3 py-2 rounded font-medium transition ${
                    isActive
                      ? "text-[#00ff85] bg-white/10"
                      : "text-white hover:text-[#00ff85] hover:bg-white/10"
                  }`
                }
              >
                Leaderboards
              </NavLink>
              <button
                onClick={handleLogout}
                className="px-3 py-2 rounded font-medium text-white hover:text-[#00ff85] hover:bg-white/10 transition"
              >
                Logout
              </button>
            </div>
          )}

          {/* User profile */}
          {user && (
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2 cursor-pointer">
                <div className="w-10 h-10 rounded-full bg-[#00ff85] text-[#38003c] flex items-center justify-center font-bold">
                  {user.username?.[0] || "?"}
                </div>
                <span>{user.username || user.email}</span>
              </div>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
}
