import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth, googleProvider } from "../firebase";
import { createUserWithEmailAndPassword, signInWithPopup } from "firebase/auth";
import "../styles/signup.css"; 

export default function Signup() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [terms, setTerms] = useState(false);
  const [error, setError] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();

    // simple validations
  
    if (password !== confirm) {
      return setError("Passwords do not match");
    }
    if (password.length < 8) {
      return setError("Password must be at least 8 characters");
    }
    if (!terms) {
      return setError("You must agree to the terms");
    }

    try {
      // create firebase user
      await createUserWithEmailAndPassword(auth, email, password);
      alert("Account created successfully!");
      navigate("/login");
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

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
  
    const handleGoogleSignup = async () => {
      try {
        const result = await signInWithPopup(auth, googleProvider);
        const idToken = await result.user.getIdToken();
  
        localStorage.setItem("email", result.user.email);
  
        const backendResponse = await sendTokenToBackend(idToken);
        if (backendResponse) {
          localStorage.setItem("authToken", idToken);
          localStorage.setItem("user-name", backendResponse.username);
          navigate("/profile");
        }
      } catch (err) {
        console.error(err);
        setError("Google login failed. Try again.");
      }
    };

  return (
    <div className="signup-page">
    <div className="signup-container">
      <div className="logo">
        <i className="fas fa-futbol"></i>
        <h1>
          EPL <span>SmartBet</span>
        </h1>
      </div>

      <form onSubmit={handleSignup}>
        {error && <p className="error-message">{error}</p>}

        <div className="form-group">
          <label>Email</label>
          <div className="input-with-icon">
            <i className="fas fa-envelope"></i>
            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label>Password</label>
          <div className="input-with-icon">
            <i className="fas fa-lock"></i>
            <input
              type="password"
              placeholder="Create a password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label>Confirm Password</label>
          <div className="input-with-icon">
            <i className="fas fa-lock"></i>
            <input
              type="password"
              placeholder="Confirm your password"
              value={confirm}
              onChange={(e) => setConfirm(e.target.value)}
              required
            />
          </div>
        </div>

        <div className="terms">
          <input
            type="checkbox"
            checked={terms}
            onChange={(e) => setTerms(e.target.checked)}
          />
          <label>
            I agree to the <a href="#">Terms of Service</a> and{" "}
            <a href="#">Privacy Policy</a>
          </label>
        </div>

        <button type="submit" className="signup-btn">
          Create Account
        </button>
      </form>

      <div className="divider">
        <span>Or sign up with</span>
      </div>

      <button className="google-btn" onClick={handleGoogleSignup}>
        <i className="fab fa-google"></i> Sign up with Google
      </button>

      <div className="login-section">
        <p>
          Already have an account?
          <span
            className="login-link"
            onClick={() => navigate("/login")}
            style={{ cursor: "pointer" }}
          >
            Log in
          </span>
        </p>
      </div>
    </div>
    </div>
  );
}
