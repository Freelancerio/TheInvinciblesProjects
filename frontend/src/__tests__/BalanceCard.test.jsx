// src/__tests__/BalanceCard.test.jsx
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { UserContext } from "../UserContext";

jest.mock("../api.js", () => ({
  __esModule: true,
  default: () => "http://test-base-url"
}));

import BalanceCard from "../components/BalanceCard.jsx";

const renderWithUser = (user) =>
  render(
    <UserContext.Provider value={{ user, setUser: jest.fn() }}>
      <BalanceCard />
    </UserContext.Provider>
  );

// Mock window.location.href
const mockLocationAssign = jest.fn();
Object.defineProperty(window, 'location', {
  value: {
    assign: mockLocationAssign,
    href: 'http://localhost/'
  },
  writable: true,
  configurable: true
});

describe("BalanceCard", () => {
  beforeEach(() => {
    global.fetch = jest.fn();
    localStorage.clear();
    localStorage.setItem("authToken", "fake-token");
    mockLocationAssign.mockClear();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("renders account balance when user is provided", () => {
    renderWithUser({ firebaseId: "u1", account_balance: 5000 });
    expect(screen.getByText(/Account Balance/i)).toBeInTheDocument();
    expect(screen.getByText(/R 5000\b/)).toBeInTheDocument();
    expect(screen.getByText(/Available for betting and withdrawals/i)).toBeInTheDocument();
  });

  test("renders nothing if no user in context", () => {
    const { container } = renderWithUser(null);
    expect(container.firstChild).toBeNull();
  });

  test("opens modal and shows disabled Deposit button until amount > 0", () => {
    renderWithUser({ firebaseId: "u1", account_balance: 100 });

    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));

    expect(screen.getByRole("heading", { name: /Deposit Funds/i })).toBeInTheDocument();

    const depositBtn = screen.getByRole("button", { name: /^Deposit$/i });
    expect(depositBtn).toBeDisabled();

    fireEvent.change(screen.getByPlaceholderText("0.00"), { target: { value: "0" } });
    expect(depositBtn).toBeDisabled();

    fireEvent.change(screen.getByPlaceholderText("0.00"), { target: { value: "10" } });
    expect(depositBtn).not.toBeDisabled();
  });

  test("quick amount buttons fill the input", () => {
    renderWithUser({ firebaseId: "u1", account_balance: 1000 });
    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));

    const input = screen.getByPlaceholderText("0.00");
    fireEvent.click(screen.getByRole("button", { name: "R 500" }));
    expect(input).toHaveValue(500);

    fireEvent.click(screen.getByRole("button", { name: "R 1000" }));
    expect(input).toHaveValue(1000);
  });

  test("success path: POSTs to create session and redirects to returned URL", async () => {
    renderWithUser({ firebaseId: "u1", account_balance: 1000 });

    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));
    fireEvent.change(screen.getByPlaceholderText("0.00"), { target: { value: "25" } });

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ url: "https://checkout.example/session/abc" }),
    });

    fireEvent.click(screen.getByRole("button", { name: /^Deposit$/i }));

    await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(1));

    expect(global.fetch).toHaveBeenCalledWith(
      "http://test-base-url/api/payment/create-checkout-session",
      expect.objectContaining({
        method: "POST",
        headers: expect.objectContaining({
          Authorization: "Bearer fake-token",
          "Content-Type": "application/json",
        }),
        body: JSON.stringify({
          userId: "u1",
          amount: 2500
        }),
      })
    );

    await waitFor(() => {
      expect(window.location.href).toBe("https://checkout.example/session/abc");
    });
  });

  test("API ok but missing url triggers error handling", async () => {
    renderWithUser({ firebaseId: "u1", account_balance: 1000 });

    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));
    fireEvent.change(screen.getByPlaceholderText("0.00"), { target: { value: "50" } });

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({})
    });

    fireEvent.click(screen.getByRole("button", { name: /^Deposit$/i }));

    await waitFor(() => expect(global.fetch).toHaveBeenCalled());
  });

  test("API failure triggers error handling", async () => {
    renderWithUser({ firebaseId: "u1", account_balance: 1000 });

    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));
    fireEvent.change(screen.getByPlaceholderText("0.00"), { target: { value: "10" } });

    global.fetch.mockResolvedValueOnce({
      ok: false,
      status: 500
    });

    fireEvent.click(screen.getByRole("button", { name: /^Deposit$/i }));

    await waitFor(() => expect(global.fetch).toHaveBeenCalled());
  });

  test("withdrawal button is clickable", () => {
    renderWithUser({ firebaseId: "u1", account_balance: 1000 });

    const withdrawBtn = screen.getByRole("button", { name: /Withdraw Money/i });
    expect(withdrawBtn).toBeInTheDocument();
    fireEvent.click(withdrawBtn);
  });

  test("modal closes via Cancel button", () => {
    renderWithUser({ firebaseId: "u1", account_balance: 100 });

    fireEvent.click(screen.getByRole("button", { name: /Deposit Funds/i }));
    expect(screen.getByRole("heading", { name: /Deposit Funds/i })).toBeInTheDocument();

    fireEvent.click(screen.getByRole("button", { name: /Cancel/i }));
    expect(screen.queryByRole("heading", { name: /Deposit Funds/i })).not.toBeInTheDocument();
  });
});