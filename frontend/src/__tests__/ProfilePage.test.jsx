import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import { UserContext } from "../UserContext";
import ProfilePage from "../components/ProfilePage.jsx";

// --- child stubs so we don't pull in their JSX ---
jest.mock("../components/Header", () => () => <div data-testid="header" />);
jest.mock("../components/UserDetailsCard", () => () => <div data-testid="user-details" />);
jest.mock("../components/BalanceCard", () => () => <div data-testid="balance" />);
jest.mock("../components/BettingStatsCard", () => () => <div data-testid="stats" />);
jest.mock("../components/LeaderboardCard", () => () => <div data-testid="leaderboard" />);
jest.mock("../components/BetHistoryCard", () => () => <div data-testid="history" />);
jest.mock("../components/LoadingPage", () => () => <div data-testid="loading" />);

// base-URL util
jest.mock("../api.js", () => ({ __esModule: true, default: () => "http://test-base-url" }));

const renderWithUser = (user = { firebaseId: "uid-123" }) =>
  render(
    <UserContext.Provider value={{ user, setUser: jest.fn() }}>
      <ProfilePage />
    </UserContext.Provider>
  );

beforeEach(() => {
  // Clear localStorage and set up auth token for tests
  localStorage.clear();
  localStorage.setItem("authToken", "test-token-123");
  
  global.fetch = jest.fn();
});

afterEach(() => {
  jest.clearAllMocks();
});

// ────────────────────────────────────────────────
test("renders LoadingPage while fetching", async () => {
  // fetch promises never resolve yet
  global.fetch.mockReturnValue(new Promise(() => {}));
  
  renderWithUser();
  
  // Should show loading initially
  expect(screen.getByTestId("loading")).toBeInTheDocument();
});

test("loads data successfully and displays all cards", async () => {
  const mockData = {
    bets: [],
    stats: { wins: 5, losses: 3 },
    leaderboard: { position: 10 }
  };

  global.fetch
    .mockResolvedValueOnce({ 
      ok: true, 
      json: async () => mockData.bets 
    })
    .mockResolvedValueOnce({ 
      ok: true, 
      json: async () => mockData.stats 
    })
    .mockResolvedValueOnce({ 
      ok: true, 
      json: async () => mockData.leaderboard 
    });

  renderWithUser();

  // All 3 fetch calls made
  await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(3));

  // Eventually loading disappears and cards appear
  await waitFor(() => expect(screen.queryByTestId("loading")).not.toBeInTheDocument());
  
  expect(screen.getByTestId("header")).toBeInTheDocument();
  expect(screen.getByTestId("user-details")).toBeInTheDocument();
  expect(screen.getByTestId("balance")).toBeInTheDocument();
  expect(screen.getByTestId("stats")).toBeInTheDocument();
  expect(screen.getByTestId("leaderboard")).toBeInTheDocument();
  expect(screen.getByTestId("history")).toBeInTheDocument();

  // Verify URLs and headers
  const calls = global.fetch.mock.calls;
  expect(calls).toHaveLength(3);
  
  const urls = calls.map(([url]) => url);
  expect(urls).toEqual([
    "http://test-base-url/api/bets/user/uid-123",
    "http://test-base-url/api/bets/stats/uid-123", 
    "http://test-base-url/api/leaderboard/alltime/uid-123"
  ]);

  // Verify auth headers
  calls.forEach(([, options]) => {
    expect(options.headers.Authorization).toBe("Bearer test-token-123");
  });
});

test("handles missing user without crashing", async () => {
  renderWithUser(null);
  
  await waitFor(() => {
    expect(screen.queryByTestId("loading")).not.toBeInTheDocument();
  });
  
  // Should still render the page frame even if no fetch occurs
  expect(global.fetch).not.toHaveBeenCalled();
  expect(screen.getByText(/User Profile/i)).toBeInTheDocument();
});

test("handles failed fetch gracefully (error path)", async () => {
  // One of the fetches fails
  global.fetch
    .mockResolvedValueOnce({ 
      ok: true, 
      json: async () => [] 
    })
    .mockResolvedValueOnce({ 
      ok: false, 
      status: 500 
    })
    .mockResolvedValueOnce({ 
      ok: true, 
      json: async () => [] 
    });

  renderWithUser();

  // Wait for all fetch calls to complete
  await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(3));
  
  // After error it should recover and render page, not stay stuck
  await waitFor(() => expect(screen.queryByTestId("loading")).not.toBeInTheDocument());
  
  expect(screen.getByTestId("header")).toBeInTheDocument();
  expect(screen.getByTestId("user-details")).toBeInTheDocument();
  expect(screen.getByTestId("balance")).toBeInTheDocument();
  expect(screen.getByTestId("stats")).toBeInTheDocument();
  expect(screen.getByTestId("leaderboard")).toBeInTheDocument();
  expect(screen.getByTestId("history")).toBeInTheDocument();
});

test("handles missing auth token gracefully", async () => {
  // Remove auth token for this test
  localStorage.removeItem("authToken");
  
  renderWithUser();

  // Should handle the error and stop loading
  await waitFor(() => expect(screen.queryByTestId("loading")).not.toBeInTheDocument());
  
  // Page should still render
  expect(screen.getByTestId("header")).toBeInTheDocument();
});