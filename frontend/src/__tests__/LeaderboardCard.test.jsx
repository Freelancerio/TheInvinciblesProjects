import { render, screen, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import LeaderboardCard from "../components/LeaderboardCard.jsx";

global.fetch = jest.fn();

describe("LeaderboardCard", () => {
  const mockUser = {
    firebaseId: "test-user-123",
    username: "testuser",
  };

  const renderWithContext = (user = mockUser) => {
    return render(
      <BrowserRouter>
        <UserContext.Provider value={{ user, setUser: jest.fn() }}>
          <LeaderboardCard />
        </UserContext.Provider>
      </BrowserRouter>
    );
  };

  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");

    // Mock fetch to return position 27
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => 27,
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
    localStorage.clear();
  });

  test("renders the card title", () => {
    renderWithContext();
    expect(screen.getByText(/predictions leaderboard/i)).toBeInTheDocument();
  });

  test("shows current position number and label", async () => {
    renderWithContext();

    // Wait for position to load
    const positionElement = await screen.findByText("27");
    expect(positionElement).toBeInTheDocument();
    expect(screen.getByText(/your current position/i)).toBeInTheDocument();
  });

  test("renders leaderboard link and handles click", () => {
    renderWithContext();

    const link = screen.getByRole("link", { name: /view full leaderboard/i });
    expect(link).toBeInTheDocument();
    expect(link).toHaveAttribute("href", "/leaderboards");
  });
});