import { render, screen, waitFor } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import ProfilePage from "../components/ProfilePage.jsx";

global.fetch = jest.fn();

function renderWithProviders(user = { username: "TestUser", account_balance: 500 }) {
  return render(
    <BrowserRouter>
      <UserContext.Provider value={{ user, logoutUser: jest.fn() }}>
        <ProfilePage />
      </UserContext.Provider>
    </BrowserRouter>
  );
}

describe("ProfilePage", () => {
  beforeEach(() => {
    jest.clearAllMocks();

    // Mock fetch for TeamStatistics
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: {
          teamName: "Liverpool",
          avgGoalsScored: 2.3,
          avgGoalsConceded: 1.2,
          avgPossession: 60,
        },
        teamB: {
          teamName: "Spurs",
          avgGoalsScored: 1.7,
          avgGoalsConceded: 1.5,
          avgPossession: 55,
        },
      }),
    });

    // Mock fetch for BetHistoryCard
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ([
        {
          id: 1,
          date: "2024-01-15",
          match: "Arsenal vs Brentford",
          bet: "Home Win",
          stake: 50,
          odds: 1.85,
          potentialWin: 92.5,
          status: "Won"
        },
      ]),
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("renders the profile page title", () => {
    renderWithProviders();
    expect(screen.getByRole("heading", { name: /user profile/i })).toBeInTheDocument();
  });

  test("renders child components (basic smoke check)", async () => {
    renderWithProviders();

    // BalanceCard
    expect(screen.getByText(/account balance/i)).toBeInTheDocument();

    // LeaderboardCard title
    expect(screen.getByText(/predictions leaderboard/i)).toBeInTheDocument();

  });

  test("renders header with site name", () => {
    renderWithProviders();
    expect(screen.getByText(/epl smartbet/i)).toBeInTheDocument();
  });
});