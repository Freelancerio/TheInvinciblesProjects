import { render, screen } from "@testing-library/react";
import BetHistoryCard from "../components/BetHistoryCard.jsx";

describe("BetHistoryCard", () => {
  test("renders the section title", () => {
    render(<BetHistoryCard />);
    expect(screen.getByText(/recent betting history/i)).toBeInTheDocument();
  });

  test("renders all table headers", () => {
    render(<BetHistoryCard />);
    const headers = ["Date", "Match", "Bet", "Stake", "Odds", "Potential Win", "Status"];
    headers.forEach(h => {
      expect(screen.getByRole("columnheader", { name: h })).toBeInTheDocument();
    });
  });

  test("renders bets with expected content", () => {
    render(<BetHistoryCard />);
    expect(screen.getByText("Arsenal vs Brentford")).toBeInTheDocument();
    expect(screen.getByText("Chelsea vs Fulham")).toBeInTheDocument();
    expect(screen.getByText("Liverpool vs Wolves")).toBeInTheDocument();
    expect(screen.getByText("Man City vs Everton")).toBeInTheDocument();
  });

  test("applies correct status colors", () => {
    render(<BetHistoryCard />);
    const wonCells = screen.getAllByText("Won"); // two "Won" statuses
    const lostCell = screen.getByText("Lost");
    const pendingCell = screen.getByText("Pending");

    wonCells.forEach(cell => {
      expect(cell).toHaveStyle("color: #00ff85");
    });
    expect(lostCell).toHaveStyle("color: #e90052");
    expect(pendingCell).toHaveStyle("color: yellow");
  });
});
