import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import Dashboard from "../components/DashBoard.jsx";

function renderWithProviders(user = { username: "Steph", account_balance: 500 }) {
  return render(
    <BrowserRouter>
      <UserContext.Provider value={{ user, logoutUser: jest.fn(), setUser: jest.fn() }}>
        <Dashboard />
      </UserContext.Provider>
    </BrowserRouter>
  );
}

describe("Dashboard", () => {
  test("renders without crashing and shows header", () => {
    renderWithProviders();
    expect(screen.getByText(/epl smartbet/i)).toBeInTheDocument();
  });

  test("renders welcome banner", () => {
    renderWithProviders();
    expect(screen.getByText(/welcome back/i)).toBeInTheDocument();
  });

  test("renders quick actions section", () => {
    renderWithProviders();
    expect(screen.getByText(/quick actions/i)).toBeInTheDocument();
  });

  test("applies background style", () => {
    const { container } = renderWithProviders();
    const rootDiv = container.firstChild;
    expect(rootDiv).toHaveStyle("background: linear-gradient");
  });
});
