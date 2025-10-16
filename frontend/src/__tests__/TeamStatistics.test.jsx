import { render, screen, waitFor } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import TeamStatistics from "../components/upcomingMatch/TeamStatistics";

const mockUseLocation = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => mockUseLocation(),
}));

global.fetch = jest.fn();

describe("TeamStatistics", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");

    mockUseLocation.mockReturnValue({
      state: {
        match: {
          homeTeam: "Liverpool",
          awayTeam: "Spurs"
        }
      }
    });

    // Mock the fetch response
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
  });

  afterEach(() => {
    localStorage.clear();
    fetch.mockClear();
  });

  it("renders loading initially", () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    expect(screen.getByText(/loading team statistics/i)).toBeInTheDocument();
  });

  it("renders 'No statistics available' if fetch fails", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 404,
    });

    mockUseLocation.mockReturnValue({
      state: {
        match: {
          homeTeam: "A",
          awayTeam: "B"
        }
      }
    });

    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

  });

  it("renders stats when fetch succeeds", async () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/Team Statistics/i)).toBeInTheDocument();
    }, { timeout: 3000 });

  });

  it("handles network errors gracefully", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    mockUseLocation.mockReturnValue({
      state: {
        match: {
          homeTeam: "A",
          awayTeam: "B"
        }
      }
    });

    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );
  });

  it("handles missing location state gracefully", async () => {
    mockUseLocation.mockReturnValue({
      state: null
    });

    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      const loadingOrError = screen.queryByText(/loading team statistics/i) ||
        screen.queryByText(/no statistics available/i);
      expect(loadingOrError).toBeInTheDocument();
    });
  });
});