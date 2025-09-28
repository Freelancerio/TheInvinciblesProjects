import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import ProfilePage from "../components/ProfilePage.jsx";

// helper to wrap with context + router
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
  test("renders the profile page title", () => {
    renderWithProviders();
    expect(screen.getByRole("heading", { name: /user profile/i })).toBeInTheDocument();
  });

  test("renders child components (basic smoke check)", () => {
    renderWithProviders();
    // BalanceCard pulls from user context
    expect(screen.getByText(/account balance/i)).toBeInTheDocument();

    // LeaderboardCard title
    expect(screen.getByText(/predictions leaderboard/i)).toBeInTheDocument();

    // BetHistoryCard title
    expect(screen.getByText(/recent betting history/i)).toBeInTheDocument();
  });

  test("renders header with site name", () => {
    renderWithProviders();
    expect(screen.getByText(/epl smartbet/i)).toBeInTheDocument();
  });
});
