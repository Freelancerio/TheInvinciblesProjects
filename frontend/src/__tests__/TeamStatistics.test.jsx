// src/__tests__/TeamStatistics.test.jsx
import { render, screen, waitFor, act } from "@testing-library/react";
import TeamStatistics from "../components/upcomingMatch/TeamStatistics";
import { MemoryRouter } from "react-router-dom";

// Mock react-router's useLocation
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: jest.fn(),
}));

const mockUseLocation = require("react-router-dom").useLocation;

// Mock fetch globally
global.fetch = jest.fn();

describe("TeamStatistics", () => {
  beforeEach(() => {
    jest.resetAllMocks();
    // Set up localStorage token
    localStorage.setItem("authToken", "fake-token");
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("renders loading initially", async () => {
    mockUseLocation.mockReturnValue({ state: { match: { homeTeam: "A", awayTeam: "B" } } });
    
    // Mock a delayed fetch response
    fetch.mockImplementation(() => 
      new Promise(resolve => setTimeout(() => 
        resolve({
          ok: true,
          json: async () => ({}),
        }), 100)
      )
    );

    await act(async () => {
      render(
        <MemoryRouter>
          <TeamStatistics />
        </MemoryRouter>
      );
    });

    expect(screen.getByText(/loading team statistics/i)).toBeInTheDocument();
  });

  it("renders 'No statistics available' if fetch fails", async () => {
    mockUseLocation.mockReturnValue({ state: { match: { homeTeam: "A", awayTeam: "B" } } });

    fetch.mockResolvedValueOnce({
      ok: false,
    });

    await act(async () => {
      render(
        <MemoryRouter>
          <TeamStatistics />
        </MemoryRouter>
      );
    });

    await waitFor(() => {
      expect(screen.getByText(/no statistics available/i)).toBeInTheDocument();
    });
  });

  it("renders stats when fetch succeeds", async () => {
    mockUseLocation.mockReturnValue({ state: { match: { homeTeam: "Liverpool", awayTeam: "Spurs" } } });

    const mockData = {
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
    };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockData,
    });

    await act(async () => {
      render(
        <MemoryRouter>
          <TeamStatistics />
        </MemoryRouter>
      );
    });

    // Wait for stats to show up
    await waitFor(() => {
      expect(screen.getByText(/Team Statistics/i)).toBeInTheDocument();
      expect(screen.getByText("2.3")).toBeInTheDocument();
      expect(screen.getByText("Liverpool Avg Goals")).toBeInTheDocument();
      expect(screen.getByText("Spurs Avg Goals")).toBeInTheDocument();
      expect(screen.getByText("1.2")).toBeInTheDocument();
      expect(screen.getByText("1.5")).toBeInTheDocument();
      expect(screen.getByText("60%")).toBeInTheDocument();
      expect(screen.getByText("55%")).toBeInTheDocument();
    });
  });

  it("handles network errors gracefully", async () => {
    mockUseLocation.mockReturnValue({ state: { match: { homeTeam: "A", awayTeam: "B" } } });

    fetch.mockRejectedValueOnce(new Error("Network error"));

    await act(async () => {
      render(
        <MemoryRouter>
          <TeamStatistics />
        </MemoryRouter>
      );
    });

    await waitFor(() => {
      expect(screen.getByText(/no statistics available/i)).toBeInTheDocument();
    });
  });
});