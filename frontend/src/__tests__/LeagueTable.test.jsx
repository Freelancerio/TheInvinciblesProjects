import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import LeagueTable from "../components/LeagueTable.jsx";

// Mock fetch
global.fetch = jest.fn();

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => {
  const original = jest.requireActual("react-router-dom");
  return {
    ...original,
    useNavigate: () => mockNavigate,
  };
});

describe("LeagueTable", () => {
  beforeEach(() => {
    fetch.mockReset();
    mockNavigate.mockReset();
    localStorage.setItem("authToken", "fake-token");
  });

  test("renders header and Full Table link", () => {
    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    expect(
      screen.getByText(/Premier League Standings - 2025\/2026/i)
    ).toBeInTheDocument();
    expect(screen.getByText(/Full Table/i)).toHaveAttribute(
      "href",
      "/league-table"
    );
  });

  test("renders rows after successful fetch", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
      },
      {
        rank: 2,
        teamName: "Chelsea",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: -2,
        points: 18,
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeData,
    });

    render(
      <BrowserRouter>
        <LeagueTable topN={2} season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("Arsenal")).toBeInTheDocument();
      expect(screen.getByText("Chelsea")).toBeInTheDocument();
      expect(screen.getByText("+12")).toBeInTheDocument();
      expect(screen.getByText("-2")).toBeInTheDocument();
    });
  });

  test("navigates to teamStats page when row is clicked", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Liverpool",
        teamLogo: "",
        matchesPlayed: 12,
        goalDifference: 5,
        points: 30,
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeData,
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    const row = await screen.findByText("Liverpool");
    fireEvent.click(row);

    expect(mockNavigate).toHaveBeenCalledWith("/teamStats/Liverpool");
  });

  test("handles fetch failure gracefully", async () => {
    const consoleSpy = jest.spyOn(console, "error").mockImplementation(() => {});
    fetch.mockResolvedValueOnce({ ok: false });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(consoleSpy).toHaveBeenCalled();
    });

    consoleSpy.mockRestore();
  });
});
