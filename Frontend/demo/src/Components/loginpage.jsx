import React, { useState } from "react";
import {
  auth,
  googleProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
} from "../firebase";
import { useNavigate } from "react-router-dom";
import "./loginpage.css"; // Ensure this file exists
import {baseUrl} from "../baseURL.js";

function Loginfunction() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate(); // allows to go toanother page like /home

  // const handleGoogleLogin = async () => {  // runs when :Continue with Google" is clicked
  //   try {
  //     await signInWithPopup(auth, googleProvider); // opens pop-up to sign in with google
  //     navigate("/profile"); //redirects to home is succesful
  //   } catch (error) {
  //     alert("Google login failed: " + error.message);
  //   }
  // };

  const handleGoogleLogin = () => {
    window.location = `${baseUrl}/oauth2/authorization/google`;
  }

  const handleGithubLogin = () => {
    window.location = `${baseUrl}/oauth2/authorization/github`;
  };

  const handleEmailLogin = async (e) => { // runs when user entes email/password
    e.preventDefault();
    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate("/profile");
    } catch (error) {
      alert("Email login failed: " + error.message);
    }
  };

  return (
      <>
        {/* Navbar */}
        <nav className="navbar">
          <div className="navbar-left">
            <img
                src="https://img.icons8.com/ios-filled/50/000000/chips.png"
                className="logo"
            />
            <span className="brand">SmartBet</span>
          </div>
          <div className="navbar-right">
            <a href="#">Fixtures</a>
            <a href="#">Esports</a>
            <button className="join-button">Join Now</button>
          </div>
        </nav>

        {/* Main Login UI */}
        <div className="login-container">
          <h1 className="title">Welcome to SmartBet</h1>
          <p className="subtitle">
            SmartBet is your premier destination for online sports betting, offering a wide range of sports and events to bet on.
            <br />
            Join us today and experience the thrill of winning!
          </p>

          <form onSubmit={handleEmailLogin} className="login-form">
            <label>Email</label>
            <input
                type="email"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
            />

            <label>Password</label>
            <input
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />

            <div className="options">
              <label>
                <input type="checkbox" /> Remember Me
              </label>
              <a href="#" className="forgot-link">
                Forgot Password?
              </a>
            </div>

            <button type="submit" className="login-button">
              Log In
            </button>
          </form>

          <p className="or-divider">Or log in with</p>

          <div className="social-buttons">
            <button className="github-button" onClick = {handleGithubLogin}>
              <img
                  src = "https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png"
                  alt = "Github logo"
                  className="github-logo"
              />
              Continue with Github
            </button>


            <button className="google-button" onClick={handleGoogleLogin}>
              <img
                  src="https://developers.google.com/identity/images/g-logo.png"
                  alt="Google logo"
                  className="google-logo"
              />
              Continue with Google
            </button>
          </div>
        </div>
      </>
  );
}

export default Loginfunction;  //makes component usable in other files