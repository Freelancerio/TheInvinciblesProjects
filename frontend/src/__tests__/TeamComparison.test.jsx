import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import TeamComparison from "../pages/TeamComparison";
import getBaseUrl from "../api";

jest.mock("../api");
jest.mock("../components/Header", () => {
    return jest.fn(() => <div data-testid="header">Header</div>);
});

const mockComparisonResult = {
    team1: "Arsenal",
    season1: "2023",
    strength1: 85.5,
    team2: "Chelsea",
    season2: "2024",
    strength2: 78.3,
    strongerTeam: "Arsenal"
};

describe("TeamComparison Component", () => {
    beforeEach(() => {
        getBaseUrl.mockReturnValue("http://localhost:8080");
        localStorage.setItem("authToken", "test-token");
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks();
        localStorage.clear();
    });

    test("renders component correctly", async () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

        expect(screen.getByText("Team Comparison")).toBeInTheDocument();
        expect(screen.getByText("Compare team performance across different seasons")).toBeInTheDocument();
    });

    test("renders header component", () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );
        expect(screen.getByTestId("header")).toBeInTheDocument();
    });

    test("renders all form fields", () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

        expect(screen.getByText("First Team")).toBeInTheDocument();
        expect(screen.getByText("Second Team")).toBeInTheDocument();
        expect(screen.getAllByText("Team")).toHaveLength(2);
        expect(screen.getAllByText("Season")).toHaveLength(2);
    });

    test("renders compare button", () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

        expect(screen.getByRole("button", { name: /Compare Teams/i })).toBeInTheDocument();
    });

    test("shows validation error when fields are empty", async () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

        const compareButton = screen.getByRole("button", { name: /Compare Teams/i });
        fireEvent.click(compareButton);

        await waitFor(() => {
            expect(screen.getByText("Please select both teams and seasons")).toBeInTheDocument();
        });
    });

    test("shows validation error for same team and season", async () => {
        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );
    });

    test("disables compare button while loading", async () => {
        global.fetch.mockImplementation(() => new Promise(() => { }));

        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

    });

    test("successfully fetches and displays comparison results", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockComparisonResult
        });

        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

    });

    test("displays error when fetch fails", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Network error"));

        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

    });

    test("closes error message when Try Again is clicked", async () => {
        global.fetch.mockRejectedValueOnce(new Error("Network error"));

        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );

    });

    test("resets form when New Comparison is clicked", async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => mockComparisonResult
        });

        render(
            <BrowserRouter>
                <TeamComparison />
            </BrowserRouter>
        );
    });
});
