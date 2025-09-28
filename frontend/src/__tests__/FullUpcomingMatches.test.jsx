import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

// Mock the Header component to avoid context issues
jest.mock("../components/Header", () => () => (
  <div data-testid="header-stub">Header Stub</div>
));

// Mock the UpcomingMatches component
jest.mock("../components/UpcomingMatches", () => () => (
  <div data-testid="upcoming-matches-stub">UpcomingMatches Stub</div>
));

import FullUpcomingMatches from "../pages/FullUpcomingMatches";

describe("FullUpcomingMatches page", () => {
  it("renders the UpcomingMatches wrapper", () => {
    render(
      <MemoryRouter>
        <FullUpcomingMatches />
      </MemoryRouter>
    );
    
    expect(screen.getByTestId("header-stub")).toBeInTheDocument();
    expect(screen.getByTestId("upcoming-matches-stub")).toBeInTheDocument();
    expect(screen.getByText(/UpcomingMatches Stub/i)).toBeInTheDocument();
  });

  it("renders season selector", () => {
    render(
      <MemoryRouter>
        <FullUpcomingMatches />
      </MemoryRouter>
    );

    // Check that the season selector is present
    expect(screen.getByLabelText(/season:/i)).toBeInTheDocument();
    
    // Check that the select element exists
    const select = screen.getByRole('combobox');
    expect(select).toBeInTheDocument();
    
    // Check that all season options are present
    expect(screen.getByText("2023/24")).toBeInTheDocument();
    expect(screen.getByText("2024/25")).toBeInTheDocument();
    expect(screen.getByText("2025/26")).toBeInTheDocument();
    
    // Verify the select has the correct default value
    expect(select).toHaveValue('2025');
  });
});