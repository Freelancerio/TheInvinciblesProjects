import { render, screen, waitFor } from "@testing-library/react";
import BetHistoryCard from "../components/BetHistoryCard";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api";

jest.mock("../api");

const mockUser = {
  firebaseId: "test-user-123",
  email: "test@example.com"
};

const mockBetsData = [
  {
    date: "2024-01-15",
    match: "Arsenal vs Chelsea",
    bet: "Arsenal Win",
    stake: "R100",
    odds: "2.50",
    potential: "R250",
    status: "won"
  },
  {
    date: "2024-01-16",
    match: "Liverpool vs Man City",
    bet: "Draw",
    stake: "R50",
    odds: "3.00",
    potential: "R150",
    status: "lost"
  },
  {
    date: "2024-01-17",
    match: "Tottenham vs Man Utd",
    bet: "Man Utd Win",
    stake: "R75",
    odds: "2.00",
    potential: "R150",
    status: "pending"
  }
];

const renderWithContext = (user = mockUser) => {
  return render(
    <UserContext.Provider value={{ user }}>
      <BetHistoryCard />
    </UserContext.Provider>
  );
};

describe("BetHistoryCard Component", () => {
  beforeEach(() => {
    getBaseUrl.mockReturnValue("http://localhost:8080");
    localStorage.setItem("authToken", "test-token");
    global.fetch = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  test("renders loading state initially", () => {
    global.fetch.mockImplementation(() => new Promise(() => { }));

    renderWithContext();

    expect(screen.getByText("Loading betting history...")).toBeInTheDocument();
  });

  test("fetches and displays bet history successfully", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Arsenal vs Chelsea")).toBeInTheDocument();
      expect(screen.getByText("Liverpool vs Man City")).toBeInTheDocument();
      expect(screen.getByText("Tottenham vs Man Utd")).toBeInTheDocument();
    });
  });

  test("displays all table headers", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Date")).toBeInTheDocument();
      expect(screen.getByText("Match")).toBeInTheDocument();
      expect(screen.getByText("Bet")).toBeInTheDocument();
      expect(screen.getByText("Stake")).toBeInTheDocument();
      expect(screen.getByText("Odds")).toBeInTheDocument();
      expect(screen.getByText("Potential Win")).toBeInTheDocument();
      expect(screen.getByText("Status")).toBeInTheDocument();
    });
  });

  test("displays bet data in correct columns", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("2024-01-15")).toBeInTheDocument();
      expect(screen.getByText("Arsenal Win")).toBeInTheDocument();
      expect(screen.getByText("R100")).toBeInTheDocument();
      expect(screen.getByText("2.50")).toBeInTheDocument();
      expect(screen.getByText("R250")).toBeInTheDocument();
    });
  });

  test("displays status with correct capitalization", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Won")).toBeInTheDocument();
      expect(screen.getByText("Lost")).toBeInTheDocument();
      expect(screen.getByText("Pending")).toBeInTheDocument();
    });
  });

  test("displays error message on fetch failure", async () => {
    global.fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Network error")).toBeInTheDocument();
    });
  });

  test("displays error when no auth token", async () => {
    localStorage.removeItem("authToken");

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("No auth token found")).toBeInTheDocument();
    });
  });

  test("displays error when response is not ok", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: false,
      status: 404
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("HTTP error 404")).toBeInTheDocument();
    });
  });

  test("does not fetch when user is null", () => {
    renderWithContext(null);

    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("does not fetch when user has no firebaseId", () => {
    renderWithContext({ email: "test@example.com" });

    expect(global.fetch).not.toHaveBeenCalled();
  });

  test("renders component title", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Recent Betting History")).toBeInTheDocument();
    });
  });

  test("calls fetch with correct URL and headers", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        "http://localhost:8080/api/bets/user/test-user-123",
        {
          headers: {
            Authorization: "Bearer test-token",
          },
        }
      );
    });
  });

  test("logs error to console on fetch failure", async () => {
    const consoleErrorSpy = jest.spyOn(console, "error").mockImplementation();
    global.fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    await waitFor(() => {
      expect(consoleErrorSpy).toHaveBeenCalled();
    });

    consoleErrorSpy.mockRestore();
  });

  test("logs data to console on successful fetch", async () => {
    const consoleLogSpy = jest.spyOn(console, "log").mockImplementation();
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    renderWithContext();

    await waitFor(() => {
      expect(consoleLogSpy).toHaveBeenCalledWith(mockBetsData);
    });

    consoleLogSpy.mockRestore();
  });

  test("renders table with correct structure", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    const { container } = renderWithContext();

    await waitFor(() => {
      const table = container.querySelector("table");
      expect(table).toBeInTheDocument();
      expect(table.querySelector("thead")).toBeInTheDocument();
      expect(table.querySelector("tbody")).toBeInTheDocument();
    });
  });

  test("renders correct number of bet rows", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    const { container } = renderWithContext();

    await waitFor(() => {
      const rows = container.querySelectorAll("tbody tr");
      expect(rows).toHaveLength(3);
    });
  });

  test("handles empty bets array", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => []
    });

    const { container } = renderWithContext();

    await waitFor(() => {
      const rows = container.querySelectorAll("tbody tr");
      expect(rows).toHaveLength(0);
    });
  });

  test("error has correct styling", async () => {
    global.fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    await waitFor(() => {
      const errorElement = screen.getByText("Network error");
      expect(errorElement.classList.contains("text-red-500")).toBe(true);
    });
  });

  test("table has minimum width class", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    const { container } = renderWithContext();

    await waitFor(() => {
      const table = container.querySelector("table");
      expect(table.classList.contains("min-w-[700px]")).toBe(true);
    });
  });

  test("refetches when user changes", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    const { rerender } = renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Arsenal vs Chelsea")).toBeInTheDocument();
    });

    const newBets = [
      {
        date: "2024-01-20",
        match: "Newcastle vs Brighton",
        bet: "Newcastle Win",
        stake: "R200",
        odds: "1.80",
        potential: "R360",
        status: "won"
      }
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => newBets
    });

    rerender(
      <UserContext.Provider value={{ user: { firebaseId: "new-user-456" } }}>
        <BetHistoryCard />
      </UserContext.Provider>
    );

    await waitFor(() => {
      expect(screen.getByText("Newcastle vs Brighton")).toBeInTheDocument();
    });
  });

  test("handles bet with unknown status", async () => {
    const betsWithUnknownStatus = [
      {
        date: "2024-01-15",
        match: "Arsenal vs Chelsea",
        bet: "Arsenal Win",
        stake: "R100",
        odds: "2.50",
        potential: "R250",
        status: "cancelled"
      }
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => betsWithUnknownStatus
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText("Cancelled")).toBeInTheDocument();
    });
  });

  test("table rows have hover effect class", async () => {
    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetsData
    });

    const { container } = renderWithContext();

    await waitFor(() => {
      const rows = container.querySelectorAll("tbody tr");
      rows.forEach(row => {
        expect(row.classList.contains("hover:bg-white/10")).toBe(true);
      });
    });
  });

  test("handles multiple bets with same status", async () => {
    const multipleSameStatus = [
      { ...mockBetsData[0], status: "won" },
      { ...mockBetsData[1], status: "won" },
      { ...mockBetsData[2], status: "won" }
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => multipleSameStatus
    });

    renderWithContext();

    await waitFor(() => {
      const wonElements = screen.getAllByText("Won");
      expect(wonElements).toHaveLength(3);
    });
  });
});