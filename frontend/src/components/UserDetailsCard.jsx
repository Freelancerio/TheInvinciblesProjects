import React, { useState, useContext, useEffect } from "react";
import { UserContext } from "../UserContext"; // your context file



export default function UserDetailsCard() {
  const { user, setUser } = useContext(UserContext); // get user & setter
  const [username, setUsername] = useState("");

  useEffect(() => {
    if (user) {
      setUsername(user.username); // initialize with context data
    }
  }, [user]);

  const handleSave = async () => {
    try {
      const token = localStorage.getItem("authToken");
      const response = await fetch("http://localhost:8080/api/me", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ username }),
      });

      if (!response.ok) throw new Error("Failed to update username");

      const updatedUser = await response.json();
      console.log("Updated user:", updatedUser);

      // Correctly update context + localStorage
      setUser(prevUser => {
        const newUser = { ...prevUser, ...updatedUser };
        localStorage.setItem("user-data", JSON.stringify(newUser));
        return newUser;
      });
    } catch (err) {
      console.error("Error updating username:", err);
    }
  };


  if (!user) return null; // optionally render nothing if user is not loaded

  const joinedDate = new Date(user.joined).toLocaleDateString("en-US", {
    day: "2-digit",
    month: "long",
    year: "numeric",
  });

  return (
    <div className="bg-white/5 backdrop-blur-md rounded-xl p-6 border border-white/10 text-center">
      <div className="flex justify-between items-center border-b border-white/10 pb-4 mb-4">
        <h2 className="text-[#00ff85] text-xl font-semibold">User Details</h2>
      </div>
      <div className="relative w-24 h-24 mx-auto mb-4 rounded-full bg-[#00ff85] text-[#38003c] flex items-center justify-center text-4xl">
        <i className="fas fa-user"></i>
        <div
          className="absolute bottom-0 right-0 w-7 h-7 rounded-full bg-[#38003c] border-2 border-[#00ff85] flex items-center justify-center cursor-pointer"
          onClick={() => alert("Avatar editing modal would open here")}
        >
          <i className="fas fa-pencil-alt text-sm"></i>
        </div>
      </div>
      <div className="mb-4">
        <div className="text-xl opacity-90">{user.email}</div>
        <div className="text-sm opacity-70">Member since: {joinedDate}</div>
      </div>
      <form className="flex flex-col gap-4" onSubmit={(e) => e.preventDefault()}>
        <div className="text-left">
          <label htmlFor="username" className="block mb-2 font-medium">
            Username
          </label>
          <input
            type="text"
            id="username"
            className="w-full px-4 py-3 rounded-lg bg-white/10 border border-white/20 focus:outline-none focus:ring-2 focus:ring-[#00ff85] text-white"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <button
          type="button"
          className="bg-[#00ff85] text-[#38003c] font-semibold py-3 rounded-lg hover:bg-[#00d46e] transition"
          onClick={handleSave}
        >
          Save Changes
        </button>
      </form>
    </div>
  );
}
