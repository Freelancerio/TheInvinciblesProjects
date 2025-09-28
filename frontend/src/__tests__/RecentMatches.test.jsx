// src/__tests__/RecentMatches.test.jsx
import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import RecentMatches from "../components/RecentMatches";

// Mock fetch globally
beforeEach(() => {
  global.fetch = jest.fn();
  localStorage.setItem("authToken", "fake-token"); // fake token for header
});

afterEach(() => {
  jest.clearAllMocks();
  localStorage.clear();
});

describe("RecentMatches", () => {
  test("shows loading state initially", () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ content: [], totalPages: 0 }),
    });

    render(
      <MemoryRouter>
        <RecentMatches />
      </MemoryRouter>
    );

    expect(screen.getByText(/loading matches/i)).toBeInTheDocument();
  });

  test("renders fetched matches", async () => {
    const fakeMatches = {
      content: [
        {
          matchId: 1,
          homeTeam: "Arsenal",
          awayTeam: "Chelsea",
          homeScore: 2,
          awayScore: 1,
          homeLogo: "home.png",
          awayLogo: "away.png",
          matchDate: "2025-05-01T00:00:00Z",
        },
      ],
      totalPages: 1,
    };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeMatches,
    });

    render(
      <MemoryRouter>
        <RecentMatches />
      </MemoryRouter>
    );

    // Wait for match to appear
    await waitFor(() => {
      expect(screen.getByText("Arsenal")).toBeInTheDocument();
      expect(screen.getByText("Chelsea")).toBeInTheDocument();
      expect(screen.getByText("2 - 1")).toBeInTheDocument();
      expect(screen.getByText(/recent results/i)).toBeInTheDocument();
    });
  });

  test("renders pagination when paginated=true and totalPages > 1", async () => {
    const fakeMatches = {
      content: [],
      totalPages: 3,
    };

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => fakeMatches,
    });

    render(
      <MemoryRouter>
        <RecentMatches paginated={true} />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/page 1 of 3/i)).toBeInTheDocument();
    });

    const nextBtn = screen.getByRole("button", { name: /next/i });
    fireEvent.click(nextBtn);

    // after clicking next, page 2 text should render
    await waitFor(() => {
      expect(screen.getByText(/page 2 of 3/i)).toBeInTheDocument();
    });
  });

  test("handles fetch error gracefully", async () => {
    fetch.mockResolvedValueOnce({ ok: false });

    render(
      <MemoryRouter>
        <RecentMatches />
      </MemoryRouter>
    );

    await waitFor(() => {
      // Even after error, loading should disappear
      expect(
        screen.queryByText(/loading matches/i)
      ).not.toBeInTheDocument();
    });
  });
});
