import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";   // <-- keep this path

export default function Login() {
  const navigate = useNavigate();

  const handleGoogleLogin = () => {
    // TODO: replace with real auth later
    navigate("/dashboard");
  };

  const handleFacebookLogin = () => {
    // TODO: replace with real auth later
    navigate("/dashboard");
  };

  return (
    <main className="login-wrap">
      {/* Top nav */}
      <header className="login-nav">
        <div className="brand">
          <span className="brand-mark" aria-hidden="true">▦</span>
          <span className="brand-name">SmartBet</span>
        </div>

        <nav className="nav-links" aria-label="Primary">
          <button className="btn btn-ghost">Join Now</button>
        </nav>
      </header>

      {/* Card */}
      <section className="card">
        <h1 className="title">Welcome to SmartBet</h1>
        <p className="subtitle">
          SmartBet is your premier destination for online sports betting, offering a wide range of
          sports and events to bet on. Join us today and experience the thrill of winning!
        </p>

        <form className="form" onSubmit={(e) => e.preventDefault()}>
          <label htmlFor="email">Email</label>
          <input id="email" name="email" type="email" placeholder="Enter your email" required />

          <label htmlFor="password">Password</label>
          <input id="password" name="password" type="password" placeholder="Enter your password" required />

          <div className="row">
            <label className="remember">
              <input type="checkbox" name="remember" /> Remember Me
            </label>
            <a className="muted" href="#forgot">Forgot Password?</a>
          </div>

          {/* transparent primary button to match socials */}
          <button type="submit" className="btn btn-primary">Log In</button>

          <div className="divider"><span>Or log in with</span></div>

          <div className="social-row">
            <button type="button" className="btn btn-soft" onClick={handleFacebookLogin}>
              Continue with Facebook
            </button>
            <button type="button" className="btn btn-soft" onClick={handleGoogleLogin}>
              {/* If you use an icon, add it here; otherwise remove this img */}
              {/* <img src="/images/google-logo.png" alt="Google logo" className="btn-icon" /> */}
              Continue with Google
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}
