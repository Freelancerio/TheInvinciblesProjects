import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import MatchPrediction from "../components/upcomingMatch/MatchPrediction";

const mockUseLocation = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => mockUseLocation(),
}));

global.fetch = jest.fn();

describe("MatchPrediction", () => {
  const mockUser = {
    firebaseId: "test-user-123",
  };

  const renderWithContext = (user = mockUser) => {
    return render(
      <BrowserRouter>
        <UserContext.Provider value={{ user, setUser: jest.fn() }}>
          <MatchPrediction />
        </UserContext.Provider>
      </BrowserRouter>
    );
  };

  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.setItem("authToken", "fake-token");
    window.alert = jest.fn();

    mockUseLocation.mockReturnValue({
      state: {
        match: {
          homeTeam: "Liverpool",
          awayTeam: "Arsenal",
          matchId: "match-123",
        },
      },
    });
  });

  afterEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  test("renders header and toggles the prediction form", () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
      text: async () => JSON.stringify({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();
    expect(screen.getByText(/match prediction/i)).toBeInTheDocument();
    expect(screen.getByText(/show predicted outcome/i)).toBeInTheDocument();
  });

  test("submits a valid user prediction", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
      text: async () => JSON.stringify({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    // Click to show prediction
    fireEvent.click(screen.getByText(/show predicted outcome/i));

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    // Fill in score inputs
    const inputs = screen.getAllByPlaceholderText("0");
    fireEvent.change(inputs[0], { target: { value: "2" } });
    fireEvent.change(inputs[1], { target: { value: "1" } });

    // Mock the POST request for submission
    fetch.mockResolvedValueOnce({
      ok: true,
    });

    fireEvent.click(screen.getByText(/submit prediction/i));

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith("Prediction submitted successfully!");
    });
  });

  test("shows error for invalid prediction", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
      text: async () => JSON.stringify({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    fireEvent.click(screen.getByText(/show predicted outcome/i));

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    // Don't fill in any scores (both will be 0)
    fireEvent.click(screen.getByText(/submit prediction/i));

    expect(window.alert).toHaveBeenCalledWith("Please enter a valid score prediction");
  });

  test("handles fetch error path gracefully", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    fireEvent.click(screen.getByText(/show predicted outcome/i));

  });
});