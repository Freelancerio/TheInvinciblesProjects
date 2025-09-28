import { render, screen, fireEvent } from "@testing-library/react";
import LeaderboardCard from "../components/LeaderboardCard.jsx";

describe("LeaderboardCard", () => {
  test("renders the card title", () => {
    render(<LeaderboardCard />);
    expect(screen.getByText(/predictions leaderboard/i)).toBeInTheDocument();
  });

  test("shows current position number and label", () => {
    render(<LeaderboardCard />);
    expect(screen.getByText("27")).toBeInTheDocument();
    expect(screen.getByText(/your current position/i)).toBeInTheDocument();
  });

  test("renders leaderboard link and handles click", () => {
    // mock alert
    const alertMock = jest.spyOn(window, "alert").mockImplementation(() => {});
    render(<LeaderboardCard />);

    const link = screen.getByRole("link", { name: /view full leaderboard/i });
    expect(link).toBeInTheDocument();

    fireEvent.click(link);
    expect(alertMock).toHaveBeenCalledWith("Redirecting to leaderboard page");

    alertMock.mockRestore();
  });
});
