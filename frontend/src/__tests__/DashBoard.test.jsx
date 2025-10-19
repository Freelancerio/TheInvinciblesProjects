import { render, screen, waitFor  } from "@testing-library/react";
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

describe("Other Sports section UI", () => {
  test("renders the Other Sports section heading", async () => {
    renderWithProviders();
    const heading = await screen.findByText(/other sports/i);
    expect(heading).toBeInTheDocument();
  });

  test("renders a grid layout for other sports cards", async () => {
    const { container } = renderWithProviders();

    // Wait for heading (ensures section is loaded)
    await screen.findByText(/other sports/i);

    // Look for the grid element directly by class
    const grid = container.querySelector(".other-sports .grid");

    expect(grid).toBeInTheDocument();
    expect(grid).toHaveClass("grid");
  });
});



describe("Dashboard data loading", () => {
  beforeEach(() => {
    global.fetch = jest.fn((url) => {
      if (url.includes("/matches/upcoming")) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ content: [] }),
        });
      }
      if (url.includes("/matches/recent")) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve({ content: [] }),
        });
      }
      if (url.includes("/standings/top5")) {
        return Promise.resolve({
          ok: true,
          json: () => Promise.resolve([]),
        });
      }
      if (url.includes("viewMatches")) {
        // triggers the otherSports branch
        return Promise.resolve({
          ok: true,
          json: () =>
            Promise.resolve([
              {
                id: 1,
                sportType: "Rugby",
                homeTeam: "Sharks",
                awayTeam: "Stormers",
                startTime: "2025-10-20T15:00:00Z",
                venue: "Kings Park",
                status: "Upcoming",
              },
            ]),
        });
      }
      return Promise.reject(new Error("Unknown URL"));
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test("loads and renders other sports data inside the Other Sports section", async () => {
    renderWithProviders();

    // Wait for the 'Other Sports' heading to appear
    const heading = await screen.findByText(/other sports/i);
    expect(heading).toBeInTheDocument();

    // Scope queries to the Other Sports section
    const otherSportsSection = heading.closest(".other-sports");
    expect(otherSportsSection).toBeInTheDocument();

    // Verify that fetched data is rendered in the section
    expect(otherSportsSection).toHaveTextContent(/rugby/i);
    expect(otherSportsSection).toHaveTextContent(/Sharks/i);
    expect(otherSportsSection).toHaveTextContent(/Stormers/i);
    expect(otherSportsSection).toHaveTextContent(/Kings Park/i);
    expect(otherSportsSection).toHaveTextContent(/Upcoming/i);

    // Optional: ensure fetch for viewMatches was called
    expect(global.fetch).toHaveBeenCalledWith(
      expect.stringContaining("viewMatches")
    );
  });
});