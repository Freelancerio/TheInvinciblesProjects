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
      alert("Learn more about the app features!");
    });
  }, [navigate]);

  return (
      <div className="min-h-screen bg-gradient-to-br from-purple-900 to-pink-800">
        {/* Header */}
        <header className="bg-white/10 backdrop-blur-md sticky top-0 z-50">
          <div className="container mx-auto px-4 sm:px-6">
            <nav className="flex justify-between items-center py-4">
              <div className="logo flex items-center gap-2 text-white">
                <i className="fas fa-futbol text-xl"></i>
                <span className="font-bold text-lg sm:text-xl">EPL Betting & Stats</span>
              </div>
              <button className="btn-login bg-secondary text-primary px-4 py-2 rounded-lg font-semibold hover:bg-opacity-90 transition-colors text-sm sm:text-base">
                <i className="fas fa-sign-in-alt mr-2"></i>
                Login
              </button>
            </nav>
          </div>
        </header>

        {/* Hero */}
        <section className="hero py-12 sm:py-20">
          <div className="container mx-auto px-4 sm:px-6">
            <div className="hero-content text-center max-w-4xl mx-auto">
              <h1 className="text-3xl sm:text-5xl md:text-6xl font-bold text-white mb-4 sm:mb-6 leading-tight">
                The Ultimate Premier League Betting Experience
              </h1>
              <p className="text-lg sm:text-xl text-gray-200 mb-8 sm:mb-10 leading-relaxed">
                Get real-time statistics, live odds, and expert predictions for all English Premier League matches. Make informed bets and track your performance.
              </p>
              <div className="cta-buttons flex flex-col sm:flex-row gap-4 justify-center">
                <button className="cta-btn primary-cta bg-secondary text-primary px-8 py-3 rounded-lg font-bold hover:bg-opacity-90 transition-colors text-lg">
                  Get Started
                </button>
                <button className="cta-btn secondary-cta bg-transparent border-2 border-white text-white px-8 py-3 rounded-lg font-bold hover:bg-white hover:text-purple-900 transition-colors text-lg">
                  Learn More
                </button>
              </div>
            </div>
          </div>
        </section>

        {/* Features */}
        <section className="features py-12 sm:py-20 bg-white/5">
          <div className="container mx-auto px-4 sm:px-6">
            <h2 className="section-title text-3xl sm:text-4xl font-bold text-center text-white mb-12 sm:mb-16">
              Why Choose Our Platform?
            </h2>
            <div className="features-grid grid grid-cols-1 md:grid-cols-3 gap-6 sm:gap-8">
              {[
                { icon: "chart-line", title: "Advanced Statistics", desc: "Access in-depth team and player statistics with interactive visualizations and historical data." },
                { icon: "bell", title: "Live Updates", desc: "Receive real-time match updates, goal alerts, and odds changes directly on your device." },
                { icon: "calculator", title: "Bet Calculator", desc: "Calculate potential winnings with our built-in betting calculator for various bet types." }
              ].map((feature, index) => (
                  <div key={index} className="feature-card bg-white/10 backdrop-blur-md rounded-xl p-6 text-center border border-white/10 hover:border-white/20 transition-all">
                    <div className="feature-icon text-4xl text-secondary mb-4">
                      <i className={`fas fa-${feature.icon}`}></i>
                    </div>
                    <h3 className="text-xl font-bold text-white mb-3">{feature.title}</h3>
                    <p className="text-gray-200 leading-relaxed">{feature.desc}</p>
                  </div>
              ))}
            </div>
          </div>
        </section>

        {/* Stats */}
        <section className="stats-preview py-12 sm:py-20">
          <div className="container mx-auto px-4 sm:px-6">
            <h2 className="section-title text-3xl sm:text-4xl font-bold text-center text-white mb-12 sm:mb-16">
              Premier League Insights
            </h2>
            <div className="stats-container grid grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6">
              {[
                { number: "380", desc: "Matches Played Each Season" },
                { number: "20", desc: "Teams Competing" },
                { number: "1000+", desc: "Statistical Metrics Tracked" },
                { number: "24/7", desc: "Odds Updates" }
              ].map((stat, index) => (
                  <div key={index} className="stat-box bg-white/10 backdrop-blur-md rounded-lg p-4 sm:p-6 text-center border border-white/10">
                    <div className="stat-number text-2xl sm:text-3xl font-bold text-secondary mb-2">{stat.number}</div>
                    <div className="stat-desc text-sm sm:text-base text-gray-200">{stat.desc}</div>
                  </div>
              ))}
            </div>
          </div>
        </section>

        {/* Footer */}
        <footer className="bg-black/30 py-12">
          <div className="container mx-auto px-4 sm:px-6">
            <div className="footer-content grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">
              <div className="footer-section">
                <h3 className="text-xl font-bold text-white mb-4">About Us</h3>
                <p className="text-gray-300 leading-relaxed">
                  Providing football fans with the most comprehensive Premier League betting statistics and insights since 2023.
                </p>
              </div>
              <div className="footer-section">
                <h3 className="text-xl font-bold text-white mb-4">Quick Links</h3>
                <div className="space-y-2">
                  {["Fixtures", "League Table", "Player Stats", "Betting Tips"].map((link, index) => (
                      <p key={index} className="text-gray-300 hover:text-white cursor-pointer transition-colors">{link}</p>
                  ))}
                </div>
              </div>
              <div className="footer-section">
                <h3 className="text-xl font-bold text-white mb-4">Connect With Us</h3>
                <p className="text-gray-300 mb-4">Follow us on social media for updates and promotions.</p>
                <div className="social-icons flex gap-4">
                  {["facebook", "twitter", "instagram", "youtube"].map((platform) => (
                      <a key={platform} href="#" className="text-gray-300 hover:text-white text-xl transition-colors">
                        <i className={`fab fa-${platform}`}></i>
                      </a>
                  ))}
                </div>
              </div>
            </div>
            <div className="copyright text-center pt-8 border-t border-white/20">
              <p className="text-gray-400">&copy; 2023 EPL Betting & Stats. All rights reserved.</p>
            </div>
          </div>
        </footer>
      </div>
  );
}