// src/__tests__/MatchStatistics.test.jsx
import { render, screen, waitFor } from "@testing-library/react";
import MatchStatistics from "../pages/MatchStatistics";
import { MemoryRouter, Route, Routes } from "react-router-dom";

// Mock Header to simplify DOM
jest.mock("../components/Header", () => () => <div>Mock Header</div>);

describe("MatchStatistics", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");
  });

  afterEach(() => {
    localStorage.clear();
  });

  const renderWithRouter = (matchId = "123") => {
    render(
      <MemoryRouter initialEntries={[`/stats/${matchId}`]}>
        <Routes>
          <Route path="/stats/:matchId" element={<MatchStatistics />} />
        </Routes>
      </MemoryRouter>
    );
  };

  test("renders loading state", () => {
    global.fetch = jest.fn(() =>
      new Promise(() => {}) // never resolves
    );

    renderWithRouter();

    expect(
      screen.getByText(/loading match statistics/i)
    ).toBeInTheDocument();
  });

  test("renders statistics when fetch succeeds", async () => {
    const mockStats = [
      {
        teamId: 1,
        teamName: "Liverpool",
        teamLogo: "l.png",
        shotsOnGoal: 5,
        shotsOffGoal: 2,
        totalShots: 7,
        blockedShots: 1,
        fouls: 3,
        cornerKicks: 4,
        offsides: 2,
        ballPossession: "60%",
        yellowCards: 1,
        redCards: 0,
        goalkeeperSaves: 2,
        totalPasses: 500,
        passesAccurate: 450,
        passesPercentage: "90%",
        expectedGoals: 1.5,
        goalsPrevented: 0.8,
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockStats),
      })
    );

    renderWithRouter();

    await waitFor(() => {
      expect(screen.getByText("Liverpool")).toBeInTheDocument();
      expect(screen.getByText(/shots on goal/i)).toBeInTheDocument();
      expect(screen.getByText(/500/)).toBeInTheDocument(); // total passes
    });
  });

  test("renders empty state when no stats available", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      })
    );

    renderWithRouter();

    await waitFor(() => {
      expect(
        screen.getByText(/no statistics available/i)
      ).toBeInTheDocument();
    });
  });

  test("handles fetch error gracefully", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({ ok: false })
    );
    jest.spyOn(console, "error").mockImplementation(() => {});

    renderWithRouter();

    await waitFor(() => {
      expect(
        screen.queryByText(/loading match statistics/i)
      ).not.toBeInTheDocument();
    });

    expect(console.error).toHaveBeenCalled();
  });
});
