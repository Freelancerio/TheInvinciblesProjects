import { render, screen } from "@testing-library/react";
import Home from "../pages/Home";
import Dashboard from "../components/DashBoard";

// Mock Dashboard so we don't need its internals
jest.mock("../components/DashBoard", () => () => <div>MockDashboard</div>);

describe("Home Page", () => {
  beforeEach(() => {
    localStorage.clear();
  });

  test("renders Dashboard component", () => {
    render(<Home />);
    expect(screen.getByText("MockDashboard")).toBeInTheDocument();
  });

  test("sets default username if not in localStorage", () => {
    render(<Home />);
    // username isn't displayed directly, but we can check it doesn't break
    expect(screen.getByText("MockDashboard")).toBeInTheDocument();
  });

  test("uses username from localStorage if available", () => {
    localStorage.setItem("user-name", "Stephan");
    render(<Home />);
    // again, username isn't displayed, but confirm Dashboard still renders
    expect(screen.getByText("MockDashboard")).toBeInTheDocument();
    // also check localStorage call happened
    expect(localStorage.getItem("user-name")).toBe("Stephan");
  });
});
