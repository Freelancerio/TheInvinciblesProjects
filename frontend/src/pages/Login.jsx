import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import {
  auth,
  googleProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
} from "../firebase";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api.js";

const baseUrl = getBaseUrl();

export default function Login() {
  const navigate = useNavigate();
  const { loginUser } = useContext(UserContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

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

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, googleProvider);
      const idToken = await result.user.getIdToken();

      const backendResponse = await sendTokenToBackend(idToken);
      if (backendResponse) {
        localStorage.setItem("authToken", idToken);
        loginUser({
          email: result.user.email,
          ...backendResponse,
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
            {error && (
                <div className="bg-red-500/20 border border-red-500 text-white px-4 py-3 rounded mb-4">
                  {error}
                </div>
            )}

            <form onSubmit={handleEmailLogin} className="space-y-6">
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-white">
                  Email
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i className="fas fa-envelope text-gray-400"></i>
                  </div>
                  <input
                      id="email"
                      type="email"
                      required
                      className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-secondary focus:border-secondary sm:text-sm"
                      placeholder="Enter your email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                  />
                </div>
              </div>

              <div>
                <label htmlFor="password" className="block text-sm font-medium text-white">
                  Password
                </label>
                <div className="mt-1 relative rounded-md shadow-sm">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                    <i className="fas fa-lock text-gray-400"></i>
                  </div>
                  <input
                      id="password"
                      type="password"
                      required
                      className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md bg-white/10 text-white placeholder-gray-400 focus:outline-none focus:ring-secondary focus:border-secondary sm:text-sm"
                      placeholder="Enter your password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                  />
                </div>
              </div>

              <button
                  type="submit"
                  className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-secondary hover:bg-secondary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary"
              >
                Login
              </button>
            </form>

            <div className="mt-6">
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-gray-300"></div>
                </div>
                <div className="relative flex justify-center text-sm">
                  <span className="px-2 bg-transparent text-white">Or continue with</span>
                </div>
              </div>

              <div className="mt-6">
                <button
                    onClick={handleGoogleLogin}
                    className="w-full flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-secondary"
                >
                  <i className="fab fa-google text-red-500 mr-2"></i>
                  Sign in with Google
                </button>
              </div>
            </div>

            <div className="mt-6 text-center">
              <p className="text-sm text-white">
                Don't have an account?{' '}
                <a href="/signup" className="font-medium text-secondary hover:text-secondary/90">
                  Sign up
                </a>
              </p>
            </div>
          </div>
        </div>
      </div>
  );
}