import { render, screen, waitFor } from "@testing-library/react";
import { UserContext } from "../UserContext";
import BetHistoryCard from "../components/BetHistoryCard.jsx";

global.fetch = jest.fn();

describe("BetHistoryCard", () => {
  const mockUser = {
    id: 1,
    username: "testuser",
    email: "test@example.com"
  };

  const mockBetData = [
    {
      id: 1,
      date: "2024-01-15",
      match: "Arsenal vs Brentford",
      bet: "Home Win",
      stake: 50,
      odds: 1.85,
      potentialWin: 92.5,
      status: "Won"
    },
    {
      id: 2,
      date: "2024-01-14",
      match: "Chelsea vs Fulham",
      bet: "Over 2.5",
      stake: 30,
      odds: 2.1,
      potentialWin: 63,
      status: "Lost"
    },
    {
      id: 3,
      date: "2024-01-13",
      match: "Liverpool vs Wolves",
      bet: "Away Win",
      stake: 40,
      odds: 2.5,
      potentialWin: 100,
      status: "Won"
    },
    {
      id: 4,
      date: "2024-01-12",
      match: "Man City vs Everton",
      bet: "Home Win",
      stake: 60,
      odds: 1.5,
      potentialWin: 90,
      status: "Pending"
    }
  ];

  const renderWithContext = (user = mockUser) => {
    return render(
      <UserContext.Provider value={{ user, setUser: jest.fn() }}>
        <BetHistoryCard />
      </UserContext.Provider>
    );
  };

  beforeEach(() => {
    jest.clearAllMocks();
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBetData,
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test("renders all table headers", async () => {
    renderWithContext();
    const headers = ["Date", "Match", "Bet", "Stake", "Odds", "Potential Win", "Status"];

  });

  test("renders bets with expected content", async () => {
    renderWithContext();
  });

  test("applies correct status colors", async () => {
    renderWithContext();
  });
});