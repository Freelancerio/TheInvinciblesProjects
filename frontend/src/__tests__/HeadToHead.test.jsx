import { render, screen, waitFor } from "@testing-library/react";
import HeadToHead from "../components/upcomingMatch/HeadToHead";
import { MemoryRouter } from "react-router-dom";

// Mock useLocation
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => ({
    state: { match: { homeTeam: "Liverpool", awayTeam: "Spurs" } },
  }),
}));

describe("HeadToHead", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test("renders loading then match data on success", async () => {
    const mockData = [
      {
        homeTeam: "Liverpool",
        homeLogo: "l.png",
        awayTeam: "Spurs",
        awayLogo: "s.png",
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

    expect(screen.getByText(/loading head-to-head/i)).toBeInTheDocument();

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

  test("renders no matches when API returns empty array", async () => {
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
      expect(
        screen.getByText(/no head-to-head matches found/i)
      ).toBeInTheDocument();
    });
  });
});
