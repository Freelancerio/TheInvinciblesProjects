import { useEffect } from "react";
import "../styles/LandingPage.css";
import { useNavigate } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();

  useEffect(() => {
    const loginBtn = document.querySelector(".btn-login");
    const primaryCta = document.querySelector(".primary-cta");
    const secondaryCta = document.querySelector(".secondary-cta");

    loginBtn?.addEventListener("click", () => {
      navigate("/login");
    });

    primaryCta?.addEventListener("click", () => {
      navigate("/login");
    });

    secondaryCta?.addEventListener("click", () => {
      navigate("/login"); // replaced alert
    });
  }, [navigate]);

  return (
    <>
      {/* Header */}
      <header>
        <div className="container">
          <nav>
            <div className="logo">
              <i className="fas fa-futbol"></i>
              <span>EPL Betting & Stats</span>
            </div>
            <button className="btn-login">
              <i className="fas fa-sign-in-alt"></i>
              Login
            </button>
          </nav>
        </div>
      </header>

      {/* Hero */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1>The Ultimate Premier League Betting Experience</h1>
            <p>
              Get real-time statistics, live odds, and expert predictions for all English Premier League matches. Make informed bets and track your performance.
            </p>
            <div className="cta-buttons">
              <button className="cta-btn primary-cta">Get Started</button>
              <button className="cta-btn secondary-cta">Learn More</button>
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="features">
        <div className="container">
          <h2 className="section-title">Why Choose Our Platform?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-chart-line"></i>
              </div>
              <h3>Advanced Statistics</h3>
              <p>
                Access in-depth team and player statistics with interactive visualizations and historical data.
              </p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-bell"></i>
              </div>
              <h3>Live Updates</h3>
              <p>
                Receive real-time match updates, goal alerts, and odds changes directly on your device.
              </p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-calculator"></i>
              </div>
              <h3>Bet Calculator</h3>
              <p>
                Calculate potential winnings with our built-in betting calculator for various bet types.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Stats */}
      <section className="stats-preview">
        <div className="container">
          <h2 className="section-title" style={{ color: "white" }}>
            Premier League Insights
          </h2>
          <div className="stats-container">
            <div className="stat-box">
              <div className="stat-number">380</div>
              <div className="stat-desc">Matches Played Each Season</div>
            </div>
            <div className="stat-box">
              <div className="stat-number">20</div>
              <div className="stat-desc">Teams Competing</div>
            </div>
            <div className="stat-box">
              <div className="stat-number">1000+</div>
              <div className="stat-desc">Statistical Metrics Tracked</div>
            </div>
            <div className="stat-box">
              <div className="stat-number">24/7</div>
              <div className="stat-desc">Odds Updates</div>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer>
        <div className="container">
          <div className="footer-content">
            <div className="footer-section">
              <h3>About Us</h3>
              <p>
                Providing football fans with the most comprehensive Premier League betting statistics and insights since 2023.
              </p>
            </div>
            <div className="footer-section">
              <h3>Quick Links</h3>
              <p>Fixtures</p>
              <p>League Table</p>
              <p>Player Stats</p>
              <p>Betting Tips</p>
            </div>
            <div className="footer-section">
              <h3>Connect With Us</h3>
              <p>Follow us on social media for updates and promotions.</p>
              <div className="social-icons">
                <a href="#"><i className="fab fa-facebook"></i></a>
                <a href="#"><i className="fab fa-twitter"></i></a>
                <a href="#"><i className="fab fa-instagram"></i></a>
                <a href="#"><i className="fab fa-youtube"></i></a>
              </div>
            </div>
          </div>
          <div className="copyright">
            <p>&copy; 2023 EPL Betting & Stats. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </>
  );
}
