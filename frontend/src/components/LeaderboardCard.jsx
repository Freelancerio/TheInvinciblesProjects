import React, { useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import { UserContext } from "../UserContext";

export default function LeaderboardCard() {
  const { user } = useContext(UserContext);
  const [position, setPosition] = useState(null);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("authToken");
  const currentUser = user?.firebaseId;

  useEffect(() => {
    const fetchUserPosition = async () => {
      if (!currentUser) return;
      try {
        const res = await fetch(
          `http://localhost:8080/api/leaderboard/alltime/${currentUser}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!res.ok) {
          throw new Error("Failed to fetch position");
        }

        const data = await res.json(); // backend returns int or DTO
        setPosition(data);
      } catch (err) {
        console.error("Error fetching user position:", err);
        setPosition(null);
      } finally {
        setLoading(false);
      }
    };

    fetchUserPosition();
  }, [currentUser, token]);

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 text-center">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">
          Predictions Leaderboard
        </h2>
      </div>
      <div className="py-8">
        <div className="text-5xl font-extrabold text-[#00ff85] mb-2">
          {loading
            ? "..."
            : position !== null
            ? position
            : "N/A"}
        </div>
        <div className="text-lg mb-4">Your Current Position</div>

        <NavLink
          to="/leaderboards"
          className="text-[#00ff85] font-semibold inline-flex items-center gap-2 hover:underline"
        >
          View Full Leaderboard <i className="fas fa-arrow-right"></i>
        </NavLink>
      </div>
    </div>
  );
}
