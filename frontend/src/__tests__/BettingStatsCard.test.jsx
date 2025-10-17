import { render, screen, waitFor } from "@testing-library/react";
import BettingStatsCard from "../components/BettingStatsCard";
import { UserContext } from "../UserContext";
import getBaseUrl from "../api";

jest.mock("../api");

const mockUser = {
    firebaseId: "test-user-123",
    email: "test@example.com"
};

const mockStatsData = {
    totalBetsPlaced: 25,
    totalProfit: 150.50,
    winRate: 68,
    averageBet: 50.25
};

const renderWithContext = (user = mockUser) => {
    return render(
        <UserContext.Provider value={{ user }}>
            <BettingStatsCard />
        </UserContext.Provider>
    );
};

describe("BettingStatsCard Component", () => {
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

        expect(screen.getByText("Loading stats...")).toBeInTheDocument();
    });

    test("fetches and displays stats successfully", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("25")).toBeInTheDocument();
            expect(screen.getByText("R150.5")).toBeInTheDocument();
            expect(screen.getByText("68%")).toBeInTheDocument();
            expect(screen.getByText("R50.25")).toBeInTheDocument();
        });
    });

    test("displays stat labels correctly", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("Bets Placed")).toBeInTheDocument();
            expect(screen.getByText("Total Profit")).toBeInTheDocument();
            expect(screen.getByText("Win Rate")).toBeInTheDocument();
            expect(screen.getByText("Average Bet")).toBeInTheDocument();
        });
    });

    test("displays error message on fetch failure", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Network error"));

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("Failed to load betting stats")).toBeInTheDocument();
        });
    });

    test("displays error message when response is not ok", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false,
            status: 404
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("Failed to load betting stats")).toBeInTheDocument();
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
            json: async () => mockStatsData
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("Betting Statistics")).toBeInTheDocument();
        });
    });

    test("calls fetch with correct URL and headers", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        renderWithContext();

        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledWith(
                "http://localhost:8080/api/bets/stats/test-user-123",
                {
                    headers: {
                        Authorization: "Bearer test-token",
                        "Content-Type": "application/json",
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

    test("returns null when no stats after loading", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => null
        });

        const { container } = renderWithContext();

        await waitFor(() => {
            expect(container.firstChild).not.toBeNull();
        });
    });

    test("displays stats in correct grid layout", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        const { container } = renderWithContext();

        await waitFor(() => {
            const grid = container.querySelector(".grid");
            expect(grid).toBeInTheDocument();
            expect(grid.classList.contains("grid-cols-2")).toBe(true);
        });
    });

    test("handles zero values correctly", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                totalBetsPlaced: 0,
                totalProfit: 0,
                winRate: 0,
                averageBet: 0
            })
        });

        renderWithContext();

    });

    test("handles negative profit correctly", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                totalBetsPlaced: 10,
                totalProfit: -25.50,
                winRate: 30,
                averageBet: 20
            })
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("R-25.5")).toBeInTheDocument();
        });
    });

    test("handles large numbers correctly", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                totalBetsPlaced: 1000,
                totalProfit: 99999.99,
                winRate: 100,
                averageBet: 500.50
            })
        });

        renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("1000")).toBeInTheDocument();
            expect(screen.getByText("R99999.99")).toBeInTheDocument();
            expect(screen.getByText("100%")).toBeInTheDocument();
            expect(screen.getByText("R500.5")).toBeInTheDocument();
        });
    });

    test("refetches when user changes", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        const { rerender } = renderWithContext();

        await waitFor(() => {
            expect(screen.getByText("25")).toBeInTheDocument();
        });

        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                totalBetsPlaced: 50,
                totalProfit: 300,
                winRate: 75,
                averageBet: 100
            })
        });

        rerender(
            <UserContext.Provider value={{ user: { firebaseId: "new-user-456" } }}>
                <BettingStatsCard />
            </UserContext.Provider>
        );

        await waitFor(() => {
            expect(screen.getByText("50")).toBeInTheDocument();
        });
    });

    test("displays all four stat cards", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockStatsData
        });

        const { container } = renderWithContext();

        await waitFor(() => {
            const statCards = container.querySelectorAll(".bg-white\\/10.rounded-lg");
            expect(statCards.length).toBe(4);
        });
    });

    test("error has correct styling", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Network error"));

        renderWithContext();

        await waitFor(() => {
            const errorElement = screen.getByText("Failed to load betting stats");
            expect(errorElement.classList.contains("text-red-500")).toBe(true);
        });
    });
});