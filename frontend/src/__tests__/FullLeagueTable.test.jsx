import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

// Mock the heavy child components the page renders
jest.mock("../components/LeagueTable", () => () => (
  <div data-testid="league-table-stub">LeagueTable Stub</div>
));

// Mock the Header component to avoid context issues
jest.mock("../components/Header", () => () => (
  <div data-testid="header-stub">Header Stub</div>
));

import FullLeagueTable from "../pages/FullLeagueTable";

describe("FullLeagueTable page", () => {
  it("renders the LeagueTable wrapper page", () => {
    render(
      <MemoryRouter>
        <FullLeagueTable />
      </MemoryRouter>
    );

    // Assert our stubs rendered, proving the page mounted correctly
    expect(screen.getByTestId("header-stub")).toBeInTheDocument();
    expect(screen.getByTestId("league-table-stub")).toBeInTheDocument();
    expect(screen.getByText(/Header Stub/i)).toBeInTheDocument();
    expect(screen.getByText(/LeagueTable Stub/i)).toBeInTheDocument();
  });

  it("renders season selector", () => {
    render(
      <MemoryRouter>
        <FullLeagueTable />
      </MemoryRouter>
    );

    // Check that the season selector is present
    expect(screen.getByLabelText(/season:/i)).toBeInTheDocument();
    
    // Check that the select element exists and has the correct options
    const select = screen.getByRole('combobox');
    expect(select).toBeInTheDocument();
    
    // Check that all season options are present
    expect(screen.getByText("2023/24")).toBeInTheDocument();
    expect(screen.getByText("2024/25")).toBeInTheDocument();
    expect(screen.getByText("2025/26")).toBeInTheDocument();
    
    // Verify the select has the correct value by checking the selected option
    // Since we can't use getByDisplayValue for select elements easily
    expect(select).toHaveValue('2025'); // This should work with the select element
  });

  it("has correct season options with values", () => {
    render(
      <MemoryRouter>
        <FullLeagueTable />
      </MemoryRouter>
    );

    const select = screen.getByRole('combobox');
    
    // Check each option exists and has correct value
    expect(screen.getByRole('option', { name: '2023/24' })).toHaveValue('2023');
    expect(screen.getByRole('option', { name: '2024/25' })).toHaveValue('2024');
    expect(screen.getByRole('option', { name: '2025/26' })).toHaveValue('2025');
  });
});