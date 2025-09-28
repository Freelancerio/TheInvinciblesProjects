import React, { useState, useEffect } from "react";
import Dashboard from "../components/DashBoard";

export default function Home() {
  const [username, setUsername] = useState("");

  useEffect(() => {
    const storedUsername = localStorage.getItem("user-name") || "User";
    setUsername(storedUsername);
  }, []);

  return (
    <div>
      <Dashboard/>
    </div>
  );
}
