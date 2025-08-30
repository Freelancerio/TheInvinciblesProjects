import React, { useState, useEffect } from "react";
import {
  auth,
  googleProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
} from "../firebase";
import { useNavigate } from "react-router-dom";
import "./loginpage.css"; // Ensure this file exists
import { baseUrl } from "../baseURL.js";

function Loginfunction() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showError, setShowError] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Check for error parameters in URL
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    
    if (error) {
      setShowError(true);
      
      // Set appropriate error message based on error type
      switch (error) {
        case 'auth_failed':
          setErrorMessage('Authentication failed. Please try again.');
          break;
        case 'database_error':
          setErrorMessage('There was an issue setting up your account. You can still continue, but some features may not work properly.');
          break;
        case 'session_expired':
          setErrorMessage('Your session has expired. Please log in again.');
          break;
        default:
          setErrorMessage('An unexpected error occurred. Please try again.');
      }
      
      // Clean up URL by removing error parameter
      const newUrl = window.location.pathname;
      window.history.replaceState({}, document.title, newUrl);
    }
  }, []);

  const handleCloseError = () => {
    setShowError(false);
  };

  const handleGoogleLogin = () => {
    window.location = `${baseUrl}/oauth2/authorization/google`;
  };

  const handleGithubLogin = () => {
    window.location = `${baseUrl}/oauth2/authorization/github`;
  };

  const handleEmailLogin = async (e) => {
    e.preventDefault();
    try {
      await signInWithEmailAndPassword(auth, email, password);
      navigate("/userDashboard");
    } catch (error) {
      setShowError(true);
      setErrorMessage("Email login failed: " + error.message);
    }
  };

  return (
    <>
      {/* Error Popup */}
      {showError && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md mx-4 shadow-xl">
            <div className="flex items-center mb-4">
              <div className="w-6 h-6 text-red-500 mr-3">
                <svg fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                </svg>
              </div>
              <h3 className="text-lg font-medium text-gray-900">Authentication Error</h3>
            </div>
            <p className="text-gray-600 mb-6">{errorMessage}</p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={handleCloseError}
                className="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition-colors"
              >
                Continue
              </button>
              <button
                onClick={() => {
                  handleCloseError();
                  // Retry the last attempted action
                }}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                Try Again
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Navbar */}
      <nav className="navbar">
        <div className="navbar-left">
          <img
            src="https://img.icons8.com/ios-filled/50/000000/chips.png"
            className="logo"
            alt="SmartBet Logo"
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
          <button className="github-button" onClick={handleGithubLogin}>
            <img
              src="https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png"
              alt="Github logo"
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

        <div className="terms-section">
          <p className="terms-text">
            By signing in, you agree to our Terms of Service and Privacy Policy
          </p>
        </div>
      </div>
    </>
  );
}

export default Loginfunction;