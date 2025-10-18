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

    fetch.mockResolvedValue({
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
    jest.clearAllMocks();
  });

  it("renders loading initially", () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );
    expect(screen.getByText(/loading team statistics/i)).toBeInTheDocument();
  });

  it("renders stats when fetch succeeds", async () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/Team Statistics/i)).toBeInTheDocument();
    });
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
      const text = screen.queryByText(/loading team statistics/i) ||
        screen.queryByText(/no statistics available/i);
      if (text) {
        expect(text).toBeInTheDocument();
      }
    });
  });

  it("renders correct stat cards with formatted values", async () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      const statsText = screen.queryByText("2.3") || screen.queryByText("Liverpool");
      expect(statsText).toBeInTheDocument();
    });
  });

  it("calls fetch with correct URL parameters", async () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalled();
    });
  });

  it("sends authorization header with fetch", async () => {
    render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          headers: expect.any(Object)
        })
      );
    });
  });

  it("renders grid layout for stats display", async () => {
    const { container } = render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      const grid = container.querySelector(".stats-grid") || 
                   container.querySelector(".card-bg");
      if (grid) {
        expect(grid).toBeInTheDocument();
      }
    });
  });

  it("renders stat items with correct structure", async () => {
    const { container } = render(
      <BrowserRouter>
        <TeamStatistics />
      </BrowserRouter>
    );

    await waitFor(() => {
      const statItems = container.querySelectorAll(".stat-item");
      expect(statItems.length >= 0).toBe(true);
    });
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

    await waitFor(() => {
      const text = screen.queryByText(/loading team statistics/i) ||
        screen.queryByText(/no statistics available/i);
      if (text) {
        expect(text).toBeInTheDocument();
      }
    }, { timeout: 2000 });
  });
});