import React from "react";
import { render, screen, waitFor, fireEvent, act } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import UpcomingMatches from "../components/UpcomingMatches";

global.fetch = jest.fn();

beforeEach(() => {
  localStorage.setItem("authToken", "fake-token");
  jest.clearAllMocks();
});

afterEach(() => {
  jest.clearAllMocks();
  localStorage.clear();
});

describe("UpcomingMatches", () => {
  test("shows loading state initially", async () => {
    fetch.mockImplementationOnce(
      () =>
        new Promise((resolve) =>
          setTimeout(
            () =>
              resolve({
                ok: true,
                json: async () => ({ content: [], totalPages: 0 }),
              }),
            100
          )
        )
    );

    await act(async () => {
      render(
        <MemoryRouter>
          <UpcomingMatches />
        </MemoryRouter>
      );
    });

    expect(screen.getByText(/loading matches/i)).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.queryByText(/loading matches/i)).not.toBeInTheDocument();
    });
  });

  test("shows pagination controls when paginated=true and totalPages>1", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        content: [
          {
            matchId: 1,
            homeTeam: "Team A",
            awayTeam: "Team B",
            homeLogo: "a.png",
            awayLogo: "b.png",
            dateTime: "2030-12-25T20:00:00Z",
          },
        ],
        totalPages: 4,
      }),
    });

    await act(async () => {
      render(
        <MemoryRouter>
          <UpcomingMatches paginated={true} />
        </MemoryRouter>
      );
    });

    await waitFor(() => {
      expect(screen.getByText(/page 1 of 4/i)).toBeInTheDocument();
    });

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        content: [
          {
            matchId: 2,
            homeTeam: "Team C",
            awayTeam: "Team D",
            homeLogo: "c.png",
            awayLogo: "d.png",
            dateTime: "2030-12-26T20:00:00Z",
          },
        ],
        totalPages: 4,
      }),
    });

    await act(async () => {
      fireEvent.click(screen.getByRole("button", { name: /next/i }));
    });

    await waitFor(() => {
      expect(screen.getByText(/page 2 of 4/i)).toBeInTheDocument();
    });
  });

  test("handles fetch error gracefully", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    await act(async () => {
      render(
        <MemoryRouter>
          <UpcomingMatches />
        </MemoryRouter>
      );
    });

    await waitFor(() => {
      expect(screen.queryByText(/loading matches/i)).not.toBeInTheDocument();
    });

    expect(screen.getByText("Upcoming Fixtures")).toBeInTheDocument();
  });

  test("handles HTTP error response gracefully", async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 500,
    });

    await act(async () => {
      render(
        <MemoryRouter>
          <UpcomingMatches />
        </MemoryRouter>
      );
    });

    await waitFor(() => {
      expect(screen.queryByText(/loading matches/i)).not.toBeInTheDocument();
    });

    expect(screen.getByText("Upcoming Fixtures")).toBeInTheDocument();
  });
});