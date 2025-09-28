import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

// Mock the Header component to avoid context issues
jest.mock("../components/Header", () => () => (
  <div data-testid="header-stub">Header Stub</div>
));

// Mock the RecentMatches component
jest.mock("../components/RecentMatches", () => () => (
  <div data-testid="recent-matches-stub">RecentMatches Stub</div>
));

import FullRecentMatches from "../pages/FullRecentMatches";

describe("FullRecentMatches page", () => {
  it("renders the RecentMatches wrapper", () => {
    render(
      <MemoryRouter>
        <FullRecentMatches />
      </MemoryRouter>
    );
    
    expect(screen.getByTestId("header-stub")).toBeInTheDocument();
    expect(screen.getByTestId("recent-matches-stub")).toBeInTheDocument();
    expect(screen.getByText(/RecentMatches Stub/i)).toBeInTheDocument();
  });

  it("renders season selector", () => {
    render(
      <MemoryRouter>
        <FullRecentMatches />
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