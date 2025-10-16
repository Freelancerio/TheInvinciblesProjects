import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import QuickActions from "../components/QuickActions.jsx";

describe("QuickActions", () => {
  test("renders the section title", () => {
    render(
      <BrowserRouter>
        <QuickActions />
      </BrowserRouter>
    );
    expect(screen.getByText(/quick actions/i)).toBeInTheDocument();
  });

  test("renders all four action links with correct labels", () => {
    render(
      <BrowserRouter>
        <QuickActions />
      </BrowserRouter>
    );
    expect(screen.getByRole("link", { name: /deposit funds/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /stats analysis/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /leaderboard/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /bet history/i })).toBeInTheDocument();
  });

  test("renders matching icons for each link", () => {
    render(
      <BrowserRouter>
        <QuickActions />
      </BrowserRouter>
    );
    expect(screen.getByText(/deposit funds/i).previousSibling).toHaveClass("fa-money-bill-wave");
    expect(screen.getByText(/stats analysis/i).previousSibling).toHaveClass("fa-chart-line");
    expect(screen.getByText(/leaderboard/i).previousSibling).toHaveClass("fa-trophy");
    expect(screen.getByText(/bet history/i).previousSibling).toHaveClass("fa-history");
  });

  test("has correct href attributes", () => {
    render(
      <BrowserRouter>
        <QuickActions />
      </BrowserRouter>
    );
    expect(screen.getByRole("link", { name: /deposit funds/i })).toHaveAttribute("href", "/deposit");
    expect(screen.getByRole("link", { name: /stats analysis/i })).toHaveAttribute("href", "/comparison");
    expect(screen.getByRole("link", { name: /leaderboard/i })).toHaveAttribute("href", "/leaderboards");
    expect(screen.getByRole("link", { name: /bet history/i })).toHaveAttribute("href", "/profile");
  });
});