// src/__tests__/RecentForm.test.jsx
import { render, screen, waitFor } from "@testing-library/react";
import RecentForm from "../components/upcomingMatch/RecentForm";
import { MemoryRouter } from "react-router-dom";

// Mock useLocation so RecentForm receives a match
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => ({
    state: { match: { homeTeam: "Liverpool", awayTeam: "Spurs", homeLogo: "l.png", awayLogo: "s.png" } },
  }),
}));

// Mock fetch globally
global.fetch = jest.fn();

describe("RecentForm", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");
  });

  afterEach(() => {
    localStorage.clear();
  });

  test("shows loading state initially", () => {
    // Mock a pending fetch
    global.fetch.mockImplementation(() => new Promise(() => {})); // Never resolves

    render(
      <MemoryRouter>
        <RecentForm />
      </MemoryRouter>
    );

    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });

  test("renders error state when fetch fails", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: false,
      status: 500,
    });

    render(
      <MemoryRouter>
        <RecentForm />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/no data available/i)).toBeInTheDocument();
    });
  });

  test("renders teams even when API returns empty forms", async () => {
    const emptyResponse = {
      teamA: { teamName: "Liverpool", form: "", last5Matches: [] },
      teamB: { teamName: "Spurs", form: "", last5Matches: [] },
    };

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => emptyResponse,
    });

    render(
      <MemoryRouter>
        <RecentForm />
      </MemoryRouter>
    );

    await waitFor(() => {
      // The component should still render the teams even with empty data
      expect(screen.getByText("Liverpool")).toBeInTheDocument();
      expect(screen.getByText("Spurs")).toBeInTheDocument();
    });
  });

  test("renders recent form and last 5 matches on success", async () => {
    const mockResponse = {
      teamA: {
        teamName: "Liverpool",
        form: "WWDL",
        last5Matches: [
          { homeTeam: "Liverpool", homeScore: 2, awayScore: 1, awayTeam: "Chelsea" },
        ],
      },
      teamB: {
        teamName: "Spurs",
        form: "LDW",
        last5Matches: [
          { homeTeam: "Spurs", homeScore: 3, awayScore: 2, awayTeam: "Arsenal" },
        ],
      },
    };

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    render(
      <MemoryRouter>
        <RecentForm />
      </MemoryRouter>
    );

    await waitFor(() => {
      // Team names - use getAllByText since there are multiple instances
      expect(screen.getAllByText("Liverpool").length).toBeGreaterThan(0);
      expect(screen.getAllByText("Spurs").length).toBeGreaterThan(0);

      // Form indicators
      expect(screen.getAllByText("W").length).toBeGreaterThan(0);
      expect(screen.getAllByText("D").length).toBeGreaterThan(0);
      expect(screen.getAllByText("L").length).toBeGreaterThan(0);

      // Match scores
      expect(screen.getByText("2 - 1")).toBeInTheDocument();
      expect(screen.getByText("3 - 2")).toBeInTheDocument();

      // Opponent teams
      expect(screen.getByText("Chelsea")).toBeInTheDocument();
      expect(screen.getByText("Arsenal")).toBeInTheDocument();
    });
  });

  test("handles fetch rejection", async () => {
    global.fetch.mockRejectedValueOnce(new Error("Network error"));

    render(
      <MemoryRouter>
        <RecentForm />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/no data available/i)).toBeInTheDocument();
    });
  });
});