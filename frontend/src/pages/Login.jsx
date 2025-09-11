import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth, googleProvider, signInWithPopup, signInWithEmailAndPassword } from "../firebase";
import "../styles/login.css";

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

const sendTokenToBackend = async (idToken) => {
  try {
    const response = await fetch("http://localhost:8080/api/me", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${idToken}`,
      },
    });

    if (!response.ok) {
      const data = await response.json();
      throw new Error(data.message || "Backend authentication failed");
    }

    return await response.json();
  } catch (err) {
    console.error(err);
    setError(err.message);
    return null;
  }
};


const handleGoogleLogin = async () => {
  try {
    const result = await signInWithPopup(auth, googleProvider);
    const idToken = await result.user.getIdToken();

    //set the email into the localstorage
    localStorage.setItem("email",  result.user.email);

    const backendResponse = await sendTokenToBackend(idToken);
    if (backendResponse) {
      console.log(backendResponse);
      localStorage.setItem("authToken", idToken); // i will be using this token to authenticate everytime
      localStorage.setItem("user-name", backendResponse.username);
      navigate("/profile"); // only navigate if backend accepted the token
    }
  } catch (err) {
    console.error(err);
    setError("Google login failed. Try again.");
  }
};

const handleEmailLogin = async (e) => {
  e.preventDefault();
  try {
    const userCredential = await signInWithEmailAndPassword(auth, email, password);
    const idToken = await userCredential.user.getIdToken();

    const backendResponse = await sendTokenToBackend(idToken);
    if (backendResponse) {
      navigate("/profile"); // only navigate if backend accepted the token
    }
  } catch (err) {
    console.error(err);
    setError("Email/password login failed. Check your credentials.");
  }
};


  return (
    <main className="login-wrap">
      <header className="login-nav">
        <div className="brand">
          <span className="brand-mark" aria-hidden="true">▦</span>
          <span className="brand-name">SmartBet</span>
        </div>
      </header>

      <section className="card">
        <h1 className="title">Welcome to SmartBet</h1>
        <p className="subtitle">
          SmartBet is your premier destination for online sports betting. Join us today and experience the thrill of winning!
        </p>

        {error && <p className="error">{error}</p>}

        <form className="form" onSubmit={handleEmailLogin}>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            placeholder="Enter your email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            placeholder="Enter your password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button type="submit" className="btn btn-primary">Log In</button>

          <div className="divider"><span>Or log in with</span></div>

          <div className="social-row">
            <button type="button" className="btn btn-soft"  onClick={handleGoogleLogin}>
              Continue with Google
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}
