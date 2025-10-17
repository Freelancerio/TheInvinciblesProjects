import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import HeadToHead from "../components/upcomingMatch/HeadToHead";

// Mock getBaseUrl

// Mock useLocation
const mockLocation = {
  state: { match: { homeTeam: "Liverpool", awayTeam: "Spurs" } },
};

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => mockLocation,
}));

describe("HeadToHead", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");
    global.fetch = jest.fn();
  });

  afterEach(() => {
    localStorage.clear();
    jest.restoreAllMocks();
  });

  test("renders loading state initially", () => {
    global.fetch = jest.fn(() => new Promise(() => { })); // Never resolves

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    expect(screen.getByText(/loading head-to-head matches/i)).toBeInTheDocument();
  });

  test("renders match data after successful fetch", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("Liverpool")).toBeInTheDocument();
      expect(screen.getByText("Spurs")).toBeInTheDocument();
      expect(screen.getByText("2 - 1")).toBeInTheDocument();
    });
  });

  test("renders error state when fetch fails", async () => {
    global.fetch = jest.fn(() => Promise.resolve({ ok: false }));

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/error/i)).toBeInTheDocument();
    });
  });

  test("renders no matches message when API returns empty array", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/no head-to-head matches found/i)).toBeInTheDocument();
    });
  });

  test("handles multiple matches correctly", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
      {
        homeTeam: "Spurs",
        homeLogo: "http://example.com/spurs.png",
        awayTeam: "Liverpool",
        awayLogo: "http://example.com/liverpool.png",
        homeScore: 0,
        awayScore: 3,
        dateTime: "2024-03-15T20:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("2 - 1")).toBeInTheDocument();
      expect(screen.getByText("0 - 3")).toBeInTheDocument();
    });
  });

  test("formats date correctly", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/20 May 2024/i)).toBeInTheDocument();
    });
  });

  test("uses fallback logo when homeLogo is missing", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: null,
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      const homeLogoImg = screen.getAllByAltText("Liverpool")[0];
      expect(homeLogoImg).toHaveAttribute("src", "LIV");
    });
  });

  test("uses fallback logo when awayLogo is missing", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: null,
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      const awayLogoImg = screen.getAllByAltText("Spurs")[0];
      expect(awayLogoImg).toHaveAttribute("src", "SPU");
    });
  });

  test("handles fetch rejection with error message", async () => {
    global.fetch = jest.fn(() => Promise.reject(new Error("Network error")));

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/error: network error/i)).toBeInTheDocument();
    });
  });

  test("sends correct API request with query parameters", async () => {
    const mockData = [];
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        expect.stringContaining("teamA=Liverpool&teamB=Spurs"),
        expect.objectContaining({
          method: "GET",
          headers: expect.objectContaining({
            "Content-Type": "application/json",
            Authorization: "Bearer fake-token",
          }),
        })
      );
    });
  });

  test("does not fetch when match state is missing", () => {
    jest.spyOn(require("react-router-dom"), "useLocation").mockReturnValue({
      state: null,
    });

    global.fetch = jest.fn();

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("does not fetch when homeTeam is missing", () => {
    jest.spyOn(require("react-router-dom"), "useLocation").mockReturnValue({
      state: { match: { awayTeam: "Spurs" } },
    });

    global.fetch = jest.fn();

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("does not fetch when awayTeam is missing", () => {
    jest.spyOn(require("react-router-dom"), "useLocation").mockReturnValue({
      state: { match: { homeTeam: "Liverpool" } },
    });

    global.fetch = jest.fn();

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("renders Head to Head title", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText("Head to Head")).toBeInTheDocument();
    });
  });

  test("renders team logos with correct alt text", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "http://example.com/liverpool.png",
        awayTeam: "Spurs",
        awayLogo: "http://example.com/spurs.png",
        homeScore: 2,
        awayScore: 1,
        dateTime: "2024-05-20T15:00:00Z",
      },
    ];

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByAltText("Liverpool")).toBeInTheDocument();
      expect(screen.getByAltText("Spurs")).toBeInTheDocument();
    });
  });

  test("uses authorization token from localStorage", async () => {
    const mockData = [];
    localStorage.setItem("authToken", "my-custom-token");

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter>
        <HeadToHead />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        expect.any(String),
        expect.objectContaining({
          headers: expect.objectContaining({
            Authorization: "Bearer my-custom-token",
          }),
        })
      );
    });
  });
});