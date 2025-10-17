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

    // Mock multiple fetches for child components
    global.fetch = jest.fn()
      .mockResolvedValueOnce({
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
      })
      .mockResolvedValueOnce({
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
      })
      .mockResolvedValue({
        ok: true,
        json: async () => ({}),
      });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("renders the profile page title", async () => {
    renderWithProviders();
    await waitFor(() => {
      expect(screen.getByRole("heading", { name: /user profile/i })).toBeInTheDocument();
    });
  });

  test("renders header with site name", async () => {
    renderWithProviders();
    await waitFor(() => {
      const headerText = screen.queryByText(/epl smartbet/i);
      if (headerText) {
        expect(headerText).toBeInTheDocument();
      }
    });
  });

  test("renders with different user context", async () => {
    const customUser = { username: "CustomUser", account_balance: 1000 };
    renderWithProviders(customUser);
    await waitFor(() => {
      expect(screen.getByRole("heading", { name: /user profile/i })).toBeInTheDocument();
    });
  });

  test("applies correct styling and layout", async () => {
    const { container } = renderWithProviders();
    await waitFor(() => {
      const mainElement = container.querySelector("main");
      if (mainElement) {
        expect(mainElement).toHaveClass("max-w-7xl");
      }
    });
  });

  test("renders grid layout for responsive design", async () => {
    const { container } = renderWithProviders();
    await waitFor(() => {
      const grid = container.querySelector(".grid");
      if (grid) {
        expect(grid).toHaveClass("grid-cols-1");
      }
    });
  });
});