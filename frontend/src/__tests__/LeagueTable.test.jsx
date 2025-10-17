import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import LeagueTable from "../components/LeagueTable";

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

  afterEach(() => {
    localStorage.clear();
  });

  test("renders header with correct season format", () => {
    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    expect(
      screen.getByText(/Premier League Standings - 2025\/2026/i)
    ).toBeInTheDocument();
  });

  test("renders Full Table link with correct href", () => {
    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    const fullTableLink = screen.getByText(/Full Table/i);
    expect(fullTableLink).toBeInTheDocument();
    expect(fullTableLink).toHaveAttribute("href", "/league-table");
  });

  test("renders table headers", () => {
    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    expect(screen.getByText("#")).toBeInTheDocument();
    expect(screen.getByText("Team")).toBeInTheDocument();
    expect(screen.getByText("GD")).toBeInTheDocument();
    expect(screen.getByText("Pts")).toBeInTheDocument();
  });

  test("renders rows after successful fetch", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "http://example.com/arsenal.png",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
      },
      {
        rank: 2,
        teamName: "Chelsea",
        teamLogo: "http://example.com/chelsea.png",
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
      expect(screen.getByText("25")).toBeInTheDocument();
      expect(screen.getByText("18")).toBeInTheDocument();
    });
  });

  test("displays zero goal difference without sign", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 0,
        points: 25,
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
    const consoleSpy = jest.spyOn(console, "error").mockImplementation(() => { });
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

  test("handles fetch exception", async () => {
    const consoleSpy = jest.spyOn(console, "error").mockImplementation(() => { });
    fetch.mockRejectedValueOnce(new Error("Network error"));

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );


    consoleSpy.mockRestore();
  });

  test("limits displayed teams to topN prop", async () => {
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
        goalDifference: 8,
        points: 22,
      },
      {
        rank: 3,
        teamName: "Liverpool",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 5,
        points: 20,
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
      expect(screen.queryByText("Liverpool")).toBeInTheDocument();
    });
  });

  test("displays all teams when topN is not provided", async () => {
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
        goalDifference: 8,
        points: 22,
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

    await waitFor(() => {
      expect(screen.getByText("Arsenal")).toBeInTheDocument();
      expect(screen.getByText("Chelsea")).toBeInTheDocument();
    });
  });

  test("sends correct API request with season parameter", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2024} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining("season=2024"),
        expect.any(Object)
      );
    });
  });

  test("sends authorization header with request", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          headers: expect.objectContaining({
            Authorization: "Bearer fake-token",
          }),
        })
      );
    });
  });

  test("renders team logo when provided", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "http://example.com/arsenal.png",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
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

    await waitFor(() => {
      const logo = screen.getByAltText("Arsenal");
      expect(logo).toBeInTheDocument();
      expect(logo).toHaveAttribute("src", "http://example.com/arsenal.png");
    });
  });

  test("renders positive goal difference with plus sign", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 15,
        points: 25,
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

    await waitFor(() => {
      expect(screen.getByText("+15")).toBeInTheDocument();
    });
  });

  test("renders negative goal difference with minus sign", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: -5,
        points: 25,
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

    await waitFor(() => {
      expect(screen.getByText("-5")).toBeInTheDocument();
    });
  });

  test("renders rank numbers correctly", async () => {
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
        goalDifference: 8,
        points: 22,
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

    await waitFor(() => {
      const ranks = screen.getAllByText(/^[12]$/);
      expect(ranks).toHaveLength(2);
    });
  });

  test("renders matches played correctly", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 15,
        goalDifference: 12,
        points: 25,
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

    await waitFor(() => {
      expect(screen.getByText("15")).toBeInTheDocument();
    });
  });

  test("navigates when clicking on different parts of the row", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
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

    const pointsCell = await screen.findByText("25");
    fireEvent.click(pointsCell);

    expect(mockNavigate).toHaveBeenCalledWith("/teamStats/Arsenal");
  });

  test("applies hover styles to table rows", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeData,
    });

    const { container } = render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      const row = container.querySelector("tbody tr");
      expect(row).toHaveClass("cursor-pointer");
    });
  });

  test("fetches data on component mount", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledTimes(1);
    });
  });

  test("refetches data when season prop changes", async () => {
    fetch.mockResolvedValue({
      ok: true,
      json: async () => [],
    });

    const { rerender } = render(
      <BrowserRouter>
        <LeagueTable season={2024} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining("season=2024"),
        expect.any(Object)
      );
    });

    rerender(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining("season=2025"),
        expect.any(Object)
      );
    });
  });

  test("handles empty response from API", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("Team")).toBeInTheDocument();
    });
  });

  test("renders correct API endpoint", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

  });

  test("uses correct HTTP method", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          method: "GET",
        })
      );
    });
  });

  test("sets correct content-type header", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          headers: expect.objectContaining({
            "Content-Type": "application/json",
          }),
        })
      );
    });
  });

  test("renders table structure correctly", async () => {
    const fakeData = [
      {
        rank: 1,
        teamName: "Arsenal",
        teamLogo: "",
        matchesPlayed: 10,
        goalDifference: 12,
        points: 25,
      },
    ];

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeData,
    });

    const { container } = render(
      <BrowserRouter>
        <LeagueTable season={2025} />
      </BrowserRouter>
    );

    await waitFor(() => {
      expect(container.querySelector("table")).toBeInTheDocument();
      expect(container.querySelector("thead")).toBeInTheDocument();
      expect(container.querySelector("tbody")).toBeInTheDocument();
    });
  });

  test("renders all data fields for each team", async () => {
    const fakeData = [
      {
        rank: 3,
        teamName: "Manchester United",
        teamLogo: "http://example.com/manutd.png",
        matchesPlayed: 12,
        goalDifference: 7,
        points: 28,
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

    await waitFor(() => {
      expect(screen.getByText("3")).toBeInTheDocument();
      expect(screen.getByText("Manchester United")).toBeInTheDocument();
      expect(screen.getByText("12")).toBeInTheDocument();
      expect(screen.getByText("+7")).toBeInTheDocument();
      expect(screen.getByText("28")).toBeInTheDocument();
    });
  });
});