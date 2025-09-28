import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  auth,
  googleProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
} from "../firebase";
import "../styles/login.css";
import { UserContext } from "../UserContext";

export default function Login() {
  const navigate = useNavigate();
  const { loginUser } = useContext(UserContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const sendTokenToBackend = async (idToken) => {
    try {
      const response = await fetch("http://localhost:8080/api/me", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${idToken}`,
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

      //localStorage.setItem("email", result.user.email);

      const backendResponse = await sendTokenToBackend(idToken);
      if (backendResponse) {
        localStorage.setItem("authToken", idToken);
         loginUser({
          email: result.user.email,
          ...backendResponse, // merge backend fields into user object
        });
        navigate("/home");
      }
    } catch (err) {
      console.error(err);
      setError("Google login failed. Try again.");
    }
  };

  const handleEmailLogin = async (e) => {
    e.preventDefault();
    try {
      const userCredential = await signInWithEmailAndPassword(
        auth,
        email,
        password
      );
      const idToken = await userCredential.user.getIdToken();

      //localStorage.setItem("email", userCredential.user.email);

      const backendResponse = await sendTokenToBackend(idToken);
      if (backendResponse) {
        localStorage.setItem("authToken", idToken);
         loginUser({
          email: userCredential.user.email,
          ...backendResponse,
        });

        navigate("/home");
      }
    } catch (err) {
      console.error(err);
      setError("Email/password login failed. Check your credentials.");
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        {/* Logo */}
        <div className="logo">
          <i className="fas fa-futbol"></i>
          <h1>
            EPL <span>SmartBet</span>
          </h1>
        </div>

        {/* Error message */}
        {error && <p className="error-message">{error}</p>}

        {/* Form */}
        <form onSubmit={handleEmailLogin}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <div className="input-with-icon">
              <i className="fas fa-envelope"></i>
              <input
                type="email"
                id="email"
                placeholder="Enter your email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="input-with-icon">
              <i className="fas fa-lock"></i>
              <input
                type="password"
                id="password"
                placeholder="Enter your password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          <button type="submit" className="login-btn">
            Login
          </button>
        </form>

        {/* Divider */}
        <div className="divider">
          <span>Or continue with</span>
        </div>

        {/* Google login */}
        <button className="google-btn" type="button" onClick={handleGoogleLogin}>
          <i className="fab fa-google"></i>
          Sign in with Google
        </button>

        {/* Signup link */}
        <div className="signup-section">
          <p>
            Don&apos;t have an account?
            <a href="/signup" className="signup-link">
              Sign up
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
