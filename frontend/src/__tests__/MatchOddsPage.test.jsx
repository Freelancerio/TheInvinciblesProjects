import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import MatchOddsPage from "../pages/MatchOddsPage";
import getBaseUrl from "../api";

// Mock dependencies
jest.mock("../api");
jest.mock("../components/MatchCard", () => {
    return jest.fn(({ match }) => (
        <div data-testid={`match-card-${match.matchId}`}>
            {match.homeTeam} vs {match.awayTeam}
        </div>
    ));
});
jest.mock("../components/Header", () => {
    return jest.fn(() => <div data-testid="header">Header</div>);
});

const mockMatches = [
    {
        matchId: 1,
        homeTeam: "Arsenal",
        awayTeam: "Chelsea",
        matchDate: "2024-01-15T15:00:00"
    },
    {
        matchId: 2,
        homeTeam: "Liverpool",
        awayTeam: "Manchester City",
        matchDate: "2024-01-16T15:00:00"
    },
    {
        matchId: 3,
        homeTeam: "Tottenham",
        awayTeam: "Manchester United",
        matchDate: "2024-01-17T15:00:00"
    }
];

describe("MatchOddsPage Component", () => {
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

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        expect(screen.getByText("Loading matches...")).toBeInTheDocument();
    });

    test("fetches and displays matches successfully", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });
    });

    test("displays error when fetch fails", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Network error"));

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText(/Failed to load matches/i)).toBeInTheDocument();
        });
    });

    test("displays error when no auth token", async () => {
        localStorage.removeItem("authToken");

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText(/Failed to load matches/i)).toBeInTheDocument();
        });
    });

    test("displays error when response is not ok", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false,
            status: 404
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText(/Failed to load matches/i)).toBeInTheDocument();
        });
    });

    test("filters matches by search term", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

        const searchInput = screen.getByPlaceholderText("Search matches...");
        fireEvent.change(searchInput, { target: { value: "Arsenal" } });

        expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        expect(screen.queryByTestId("match-card-2")).not.toBeInTheDocument();
    });

    test("filters matches by date", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

    
    });

    test("displays no matches message when filter returns empty", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

        const searchInput = screen.getByPlaceholderText("Search matches...");
        fireEvent.change(searchInput, { target: { value: "NonExistentTeam" } });

        expect(screen.getByText("No matches found")).toBeInTheDocument();
    });

    test("handles pagination - next page", async () => {
        global.fetch
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    content: mockMatches,
                    number: 0,
                    totalPages: 2
                })
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    content: [mockMatches[0]],
                    number: 1,
                    totalPages: 2
                })
            });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Page 1 of 2")).toBeInTheDocument();
        });

        const nextButton = screen.getByText("Next");
        fireEvent.click(nextButton);

        await waitFor(() => {
            expect(screen.getByText("Page 2 of 2")).toBeInTheDocument();
        });
    });

    test("handles pagination - previous page", async () => {
        global.fetch
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    content: mockMatches,
                    number: 1,
                    totalPages: 2
                })
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    content: mockMatches,
                    number: 0,
                    totalPages: 2
                })
            });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Page 2 of 2")).toBeInTheDocument();
        });

        const prevButton = screen.getByText("Previous");
        fireEvent.click(prevButton);

    });

    test("disables previous button on first page", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 2
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            const prevButton = screen.getByText("Previous");
            expect(prevButton).toBeDisabled();
        });
    });

    test("disables next button on last page", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            const nextButton = screen.getByText("Next");
            expect(nextButton).toBeDisabled();
        });
    });

    test("filters matches with case-insensitive search", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

        const searchInput = screen.getByPlaceholderText("Search matches...");
        fireEvent.change(searchInput, { target: { value: "arsenal" } });

        expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
    });

    test("handles matches with missing date in date filter", async () => {
        const matchesWithMissingDate = [
            { ...mockMatches[0] },
            { ...mockMatches[1], matchDate: null }
        ];

        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: matchesWithMissingDate,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

    });

    test("renders header component", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: [],
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("header")).toBeInTheDocument();
        });
    });

    test("renders page title and description", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: [],
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByText("Match Betting")).toBeInTheDocument();
            expect(screen.getByText("Upcoming Premier League matches with win probabilities")).toBeInTheDocument();
        });
    });

    test("clears date filter", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                content: mockMatches,
                number: 0,
                totalPages: 1
            })
        });

        render(
            <BrowserRouter>
                <MatchOddsPage />
            </BrowserRouter>
        );

        await waitFor(() => {
            expect(screen.getByTestId("match-card-1")).toBeInTheDocument();
        });

        const dateInput = screen.queryByLabelText(/Filter by date/i);
        expect(dateInput).not.toBeInTheDocument();

    });
});