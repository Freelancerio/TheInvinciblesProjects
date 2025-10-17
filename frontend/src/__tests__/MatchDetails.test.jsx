import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import MatchDetails from "../pages/MatchDetails";
import UpcomingMatchDetails from "../components/upcomingMatch/UpcomingMatchDetails";

// Mock the UpcomingMatchDetails component
jest.mock("../components/upcomingMatch/UpcomingMatchDetails", () => {
    return jest.fn(() => <div data-testid="upcoming-match-details">Upcoming Match Details</div>);
});

describe("MatchDetails Component", () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test("renders without crashing", () => {
        render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );
    });

    test("renders UpcomingMatchDetails component", () => {
        render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );

        expect(screen.getByTestId("upcoming-match-details")).toBeInTheDocument();
    });

    test("calls UpcomingMatchDetails component once", () => {
        render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );

        expect(UpcomingMatchDetails).toHaveBeenCalledTimes(1);
    });

    test("renders a div wrapper", () => {
        const { container } = render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );

        expect(container.querySelector("div")).toBeInTheDocument();
    });

    test("component structure is correct", () => {
        const { container } = render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );

        const divs = container.querySelectorAll("div");
        expect(divs.length).toBeGreaterThan(0);
    });

    test("UpcomingMatchDetails is mounted correctly", () => {
        render(
            <BrowserRouter>
                <MatchDetails />
            </BrowserRouter>
        );

        expect(UpcomingMatchDetails).toHaveBeenCalled();
    });
});