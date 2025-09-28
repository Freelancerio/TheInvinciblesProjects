import { render, screen } from "@testing-library/react";
import QuickActions from "../components/QuickActions.jsx";

describe("QuickActions", () => {
  test("renders the section title", () => {
    render(<QuickActions />);
    expect(screen.getByText(/quick actions/i)).toBeInTheDocument();
  });

  test("renders all four action buttons with correct labels", () => {
    render(<QuickActions />);
    expect(screen.getByRole("button", { name: /deposit funds/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /stats analysis/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /leaderboard/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /bet history/i })).toBeInTheDocument();
  });

  test("renders matching icons for each button", () => {
    render(<QuickActions />);
    expect(screen.getByText(/deposit funds/i).previousSibling).toHaveClass("fa-money-bill-wave");
    expect(screen.getByText(/stats analysis/i).previousSibling).toHaveClass("fa-chart-line");
    expect(screen.getByText(/leaderboard/i).previousSibling).toHaveClass("fa-trophy");
    expect(screen.getByText(/bet history/i).previousSibling).toHaveClass("fa-history");
  });
});
