import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth, googleProvider } from "../firebase";
import { createUserWithEmailAndPassword, signInWithPopup } from "firebase/auth";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();

export default function Signup() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [terms, setTerms] = useState(false);
  const [error, setError] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();

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
      const response = await fetch(`${baseUrl}/api/me`, {
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
      <div className="min-h-screen bg-gradient-to-br from-purple-900 to-pink-800 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-md">
          {/* Logo */}
          <div className="flex flex-col items-center justify-center">
            <i className="fas fa-futbol text-5xl text-white mb-4"></i>
            <h1 className="text-3xl font-bold text-white">
              EPL <span className="text-secondary">SmartBet</span>
            </h1>
          </div>

          <div className="mt-8 bg-white/10 backdrop-blur-md py-8 px-4 shadow sm:rounded-lg sm:px-10 mx-4 sm:mx-0">
            <form onSubmit={handleSignup}>
              {error && (
                  <div className="bg-red-500/20 border border-red-500 text-white px-4 py-3 rounded mb-4">
                    {error}
                  </div>
              )}

              <div className="space-y-6">
                <div>
                  <label className="block text-sm font-medium text-white">Email</label>
                  <div className="mt-1 relative rounded-md shadow-sm">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <i className="fas fa-envelope text-gray-400"></i>
                    </div>
                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-secondary focus:border-secondary sm:text-sm"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-white">Password</label>
                  <div className="mt-1 relative rounded-md shadow-sm">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <i className="fas fa-lock text-gray-400"></i>
                    </div>
                    <input
                        type="password"
                        placeholder="Create a password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-secondary focus:border-secondary sm:text-sm"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-white">Confirm Password</label>
                  <div className="mt-1 relative rounded-md shadow-sm">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <i className="fas fa-lock text-gray-400"></i>
                    </div>
                    <input
                        type="password"
                        placeholder="Confirm your password"
                        value={confirm}
                        onChange={(e) => setConfirm(e.target.value)}
                        required
                        className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-secondary focus:border-secondary sm:text-sm"
                    />
                  </div>
                </div>

                <div className="flex items-center">
                  <input
                      type="checkbox"
                      checked={terms}
                      onChange={(e) => setTerms(e.target.checked)}
                      className="h-4 w-4 text-secondary focus:ring-secondary border-gray-300 rounded"
                  />
                  <label className="ml-2 block text-sm text-white">
                    I agree to the <a href="#" className="text-secondary hover:text-secondary/90">Terms of Service</a> and{" "}
                    <a href="#" className="text-secondary hover:text-secondary/90">Privacy Policy</a>
                  </label>
                </div>

                <button type="submit" className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-secondary hover:bg-secondary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary">
                  Create Account
                </button>
              </div>
            </form>

            <div className="mt-6">
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-gray-300"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                  <span className="px-2 bg-transparent text-white">Or sign up with</span>
                </div>
              </div>

              <div className="mt-6">
                <button
                    onClick={handleGoogleSignup}
                    className="w-full flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary"
                >
                  <i className="fab fa-google text-red-500 mr-2"></i>
                  Sign up with Google
                </button>
              </div>
            </div>

            <div className="mt-6 text-center">
              <p className="text-sm text-white">
                Already have an account?{' '}
                <button
                    onClick={() => navigate("/login")}
                    className="font-medium text-secondary hover:text-secondary/90"
                >
                  Log in
                </button>
              </p>
            </div>
          </div>
        </div>
      </div>
  );
}