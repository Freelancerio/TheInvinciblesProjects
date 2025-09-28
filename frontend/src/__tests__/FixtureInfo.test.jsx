import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import FixtureInfo from "../components/upcomingMatch/FixtureInfo";

const renderWithRouter = (state) => {
  return render(
    <MemoryRouter initialEntries={[{ pathname: "/fixture", state }]}>
      <Routes>
        <Route path="/fixture" element={<FixtureInfo />} />
      </Routes>
    </MemoryRouter>
  );
};

describe("FixtureInfo", () => {
  test("renders fallback when no match data provided", () => {
    renderWithRouter({});
    expect(screen.getByText(/no match data provided/i)).toBeInTheDocument();
  });

  test("renders fixture info with logos, teams, round, date, time, and venue", () => {
    const match = {
      homeTeam: "Arsenal",
      awayTeam: "Chelsea",
      homeLogo: "home.png",
      awayLogo: "away.png",
      round: "Matchweek 10",
      dateTime: "2025-06-01T15:30:00Z",
      venue: "Emirates Stadium",
    };

    renderWithRouter({ match });

    // League header
    expect(
      screen.getByText(/premier league - matchweek 10/i)
    ).toBeInTheDocument();

    // Teams
    expect(screen.getByText("Arsenal")).toBeInTheDocument();
    expect(screen.getByText("Chelsea")).toBeInTheDocument();
    expect(screen.getByAltText("Arsenal")).toBeInTheDocument();
    expect(screen.getByAltText("Chelsea")).toBeInTheDocument();

    // Round marker (VS)
    expect(screen.getByText("VS")).toBeInTheDocument();

    // Date / Time / Venue labels
    expect(screen.getByText(/date/i)).toBeInTheDocument();
    expect(screen.getByText(/time/i)).toBeInTheDocument();
    expect(screen.getByText(/venue/i)).toBeInTheDocument();

    // Venue value
    expect(screen.getByText("Emirates Stadium")).toBeInTheDocument();
  });

  test("renders fallback text for missing values", () => {
    const match = {
      homeTeam: "Man City",
      awayTeam: "Liverpool",
      dateTime: null,
      venue: "",
    };

    renderWithRouter({ match });

    expect(screen.getByText("Man City")).toBeInTheDocument();
    expect(screen.getByText("Liverpool")).toBeInTheDocument();

    // If no date/time provided
    expect(screen.getAllByText("TBD").length).toBeGreaterThan(0);

    // Venue fallback
    expect(screen.getByText(/stadium tbd/i)).toBeInTheDocument();
  });
});
