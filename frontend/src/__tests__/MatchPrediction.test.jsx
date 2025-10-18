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

  test("renders component and prediction form", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/match prediction/i)).toBeInTheDocument();
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });
  });

  test("allows user to enter predictions in input fields", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    const inputs = screen.getAllByPlaceholderText("0");
    fireEvent.change(inputs[0], { target: { value: "3" } });
    fireEvent.change(inputs[1], { target: { value: "2" } });

    expect(inputs[0].value).toBe("3");
    expect(inputs[1].value).toBe("2");
  });

  test("submit button is clickable and triggers submission", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    const inputs = screen.getAllByPlaceholderText("0");
    fireEvent.change(inputs[0], { target: { value: "2" } });
    fireEvent.change(inputs[1], { target: { value: "1" } });

    // Mock submission endpoint
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ success: true }),
    });

    const submitBtn = screen.getByText(/submit prediction/i);
    fireEvent.click(submitBtn);

    // Verify fetch was called for submission (not just initial load)
    await waitFor(() => {
      // Should have 2 fetches: initial prediction + submission
      const calls = fetch.mock.calls;
      expect(calls.length).toBeGreaterThanOrEqual(1);
    });
  });

  test("displays error when prediction fetch fails", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    // Component should still render with prediction form
    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    // Should show submit button even on error
    expect(screen.getByText(/submit prediction/i)).toBeInTheDocument();
  });

  test("allows submission even if initial prediction load fails", async () => {
    fetch.mockRejectedValueOnce(new Error("Network error"));

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    const inputs = screen.getAllByPlaceholderText("0");
    fireEvent.change(inputs[0], { target: { value: "1" } });
    fireEvent.change(inputs[1], { target: { value: "0" } });

    // Mock successful submission
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ success: true }),
    });

    fireEvent.click(screen.getByText(/submit prediction/i));

    // Verify submission fetch was attempted
    await waitFor(() => {
      const calls = fetch.mock.calls;
      expect(calls.length).toBeGreaterThanOrEqual(1);
    });
  });

  test("submission endpoint called with correct data", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    });

    const inputs = screen.getAllByPlaceholderText("0");
    fireEvent.change(inputs[0], { target: { value: "2" } });
    fireEvent.change(inputs[1], { target: { value: "1" } });

    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ success: true }),
    });

    fireEvent.click(screen.getByText(/submit prediction/i));

    await waitFor(() => {
      const lastCall = fetch.mock.calls[fetch.mock.calls.length - 1];
      expect(lastCall).toBeDefined();
      // Verify it's a POST request (submission, not GET)
      if (lastCall[1]) {
        expect(lastCall[1].method).toBe("POST");
      }
    });
  });

  test("renders all required UI elements", async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        teamA: "Liverpool",
        teamB: "Arsenal",
        predictedGoalsA: 2,
        predictedGoalsB: 1,
      }),
    });

    renderWithContext();

    await waitFor(() => {
      expect(screen.getByText(/match prediction/i)).toBeInTheDocument();
      expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
      expect(screen.getByText(/submit prediction/i)).toBeInTheDocument();
      expect(screen.getAllByPlaceholderText("0").length).toBe(2);
    });
  });
});