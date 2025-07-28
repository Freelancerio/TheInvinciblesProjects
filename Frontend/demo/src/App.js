import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Loginfunction from "./Components/loginpage";
import Home from "./Pages/Home";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Loginfunction />} />
        <Route path="/home" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
