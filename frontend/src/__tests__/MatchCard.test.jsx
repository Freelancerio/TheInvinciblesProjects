import React from "react";
import { render, screen, fireEvent, waitFor, act } from "@testing-library/react";
import MatchCard from "../components/MatchCard.jsx";
import { UserContext } from "../UserContext";

jest.mock("../api.js", () => ({ __esModule: true, default: () => "http://test-base-url" }));

const sampleMatch = {
  matchId: 1,
  homeTeam: "Team A",
  awayTeam: "Team B",
  homeLogo: "home.png",
  awayLogo: "away.png",
  status: "NS",
  matchDate: "2025-10-17T00:00:00Z",
  homeWinProbability: 0.4,
  drawProbability: 0.3,
  awayWinProbability: 0.3,
};

const mockUser = { firebaseId: "uid-1", account_balance: 100 };

const renderWithContext = (ui, user = mockUser) => {
  const setUser = jest.fn();
  return render(
    <UserContext.Provider value={{ user, setUser }}>{ui}</UserContext.Provider>
  );
};

beforeEach(() => {
  jest.useFakeTimers();
  global.fetch = jest.fn();
  localStorage.setItem("authToken", "fake-token");
  localStorage.setItem("user-data", JSON.stringify(mockUser));
});

afterEach(() => {
  jest.runOnlyPendingTimers();
  jest.useRealTimers();
  localStorage.clear();
});

test("renders match card with correct teams and probabilities", () => {
  renderWithContext(<MatchCard match={sampleMatch} />);
  
  // Check team names appear (multiple times is expected)
  const teamAElements = screen.getAllByText("Team A");
  expect(teamAElements.length).toBe(2); // Once in team name, once in probability section
  
  const teamBElements = screen.getAllByText("Team B");
  expect(teamBElements.length).toBe(2); // Once in team name, once in probability section
  
  expect(screen.getByText(/Upcoming/i)).toBeInTheDocument();
  expect(screen.getByText(/Win Probabilities/i)).toBeInTheDocument();
  
  // Check probabilities - use getAllByText since 30.0% appears twice
  expect(screen.getByText("40.0%")).toBeInTheDocument();
  
  const thirtyPercentElements = screen.getAllByText("30.0%");
  expect(thirtyPercentElements.length).toBe(2); // Draw and away win both have 30.0%
  
  // Check decimal odds
  expect(screen.getByText("2.50")).toBeInTheDocument();
  expect(screen.getAllByText("3.33").length).toBe(2); // Draw and away win both have 3.33
});

test("opens and closes the betting modal", () => {
  renderWithContext(<MatchCard match={sampleMatch} />);
  
  // Click on the card to open modal
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);
  
  expect(screen.getByText(/Place Your Bet/i)).toBeInTheDocument();
  
  // Close modal
  fireEvent.click(screen.getByText("✕"));
  expect(screen.queryByText(/Place Your Bet/i)).not.toBeInTheDocument();
});

test("shows error when placing invalid bet amount", async () => {
  renderWithContext(<MatchCard match={sampleMatch} />);
  
  // Click on the card to open modal
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);

  // select outcome but no valid amount
  fireEvent.change(screen.getByRole("combobox"), {
    target: { value: "homewin" },
  });
  fireEvent.change(screen.getByRole("spinbutton"), {
    target: { value: "-5" },
  });
  
  await act(async () => {
    fireEvent.click(screen.getByText(/Place Bet/i));
  });

  expect(await screen.findByText(/Enter a valid bet amount/i)).toBeInTheDocument();
});

test("shows error when balance is insufficient", async () => {
  const lowBalanceUser = { ...mockUser, account_balance: 5 };
  renderWithContext(<MatchCard match={sampleMatch} />, lowBalanceUser);
  
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);

  fireEvent.change(screen.getByRole("combobox"), {
    target: { value: "homewin" },
  });
  fireEvent.change(screen.getByRole("spinbutton"), {
    target: { value: "50" },
  });
  
  await act(async () => {
    fireEvent.click(screen.getByText(/Place Bet/i));
  });

  expect(await screen.findByText(/Insufficient balance/i)).toBeInTheDocument();
});

test("successfully places a bet and updates balance", async () => {
  const setUser = jest.fn();
  const user = { ...mockUser };
  
  global.fetch.mockResolvedValueOnce({ 
    ok: true, 
    json: async () => ({}) 
  });

  render(
    <UserContext.Provider value={{ user, setUser }}>
      <MatchCard match={sampleMatch} />
    </UserContext.Provider>
  );

  // Click on the card to open modal
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);
  
  fireEvent.change(screen.getByRole("combobox"), { 
    target: { value: "homewin" } 
  });
  fireEvent.change(screen.getByRole("spinbutton"), { 
    target: { value: "10" } 
  });
  
  await act(async () => {
    fireEvent.click(screen.getByText(/Place Bet/i));
  });

  await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(1));
  
  expect(global.fetch).toHaveBeenCalledWith(
    "http://test-base-url/api/bets/place",
    expect.objectContaining({
      method: "POST",
      headers: expect.objectContaining({ 
        Authorization: "Bearer fake-token",
        "Content-Type": "application/json" 
      }),
      body: JSON.stringify({
        userId: "uid-1",
        matchId: 1,
        outcome: "homewin",
        betAmount: 10,
        expectedWinAmount: 25 // 10 / 0.4 = 25
      })
    })
  );

  // Check that setUser was called with updated balance
  await waitFor(() => expect(setUser).toHaveBeenCalled());
  expect(setUser).toHaveBeenCalledWith({
    ...user,
    account_balance: 90 // 100 - 10 = 90
  });
});

test("displays locked state after placing bet", async () => {
  global.fetch.mockResolvedValueOnce({ 
    ok: true, 
    json: async () => ({}) 
  });
  
  renderWithContext(<MatchCard match={sampleMatch} />);

  // Click on the card to open modal
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);
  
  fireEvent.change(screen.getByRole("combobox"), { 
    target: { value: "draw" } 
  });
  fireEvent.change(screen.getByRole("spinbutton"), { 
    target: { value: "5" } 
  });
  
  await act(async () => {
    fireEvent.click(screen.getByText(/Place Bet/i));
  });

  await waitFor(() => {
    expect(screen.queryByText(/Place Your Bet/i)).not.toBeInTheDocument();
  });

  // Check that locked state is displayed
  expect(screen.getByText(/Betting locked/i)).toBeInTheDocument();
  
  // Advance timers and check countdown updates
  act(() => {
    jest.advanceTimersByTime(1000);
  });
  
  expect(screen.getByText(/Betting locked for 6s/i)).toBeInTheDocument();
});

// Additional test for payout calculation
test("calculates potential payout correctly", async () => {
  renderWithContext(<MatchCard match={sampleMatch} />);

  // Click on the card to open modal
  const card = screen.getByText(/VS/i).closest('.bg-white\\/5');
  fireEvent.click(card);

  // Select outcome and enter amount
  fireEvent.change(screen.getByRole("combobox"), { 
    target: { value: "homewin" } 
  });
  fireEvent.change(screen.getByRole("spinbutton"), { 
    target: { value: "10" } 
  });

  // Check that payout is calculated correctly
  expect(screen.getByText(/Potential Payout: 25.00/i)).toBeInTheDocument();
});