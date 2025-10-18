// frontend/src/__tests__/PaymentSuccess.test.jsx
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import { UserContext } from "../UserContext";

// --- Mock base URL used inside PaymentSuccess.jsx ---
jest.mock("../api.js", () => {
  return { __esModule: true, default: () => "http://test-base-url" };
});

// Create a mock for URLSearchParams that we can control
let mockSearchParams = new URLSearchParams("?session_id=test-session");

// Mock react-router-dom with controllable search params
jest.mock("react-router-dom", () => {
  const actual = jest.requireActual("react-router-dom");
  return {
    ...actual,
    useSearchParams: () => [mockSearchParams],
  };
});

// IMPORTANT: import component after mocks
import PaymentSuccess from "../pages/PaymentSuccess.jsx";

describe("PaymentSuccess page", () => {
  const originalLocation = window.location;

  beforeAll(() => {
    jest.useFakeTimers();
    
    // Mock window.location
    delete window.location;
    window.location = {
      ...originalLocation,
      href: "http://localhost/?session_id=test-session",
    };
  });

  afterAll(() => {
    jest.useRealTimers();
    window.location = originalLocation;
  });

  beforeEach(() => {
    // Reset to default test URL with session id
    mockSearchParams = new URLSearchParams("?session_id=test-session");
    window.location.href = "http://localhost/?session_id=test-session";
    
    global.fetch = jest.fn();
    localStorage.clear();
    localStorage.setItem("authToken", "fake-token");
  });

  const renderWithProviders = (ui, { user = { firebaseId: "uid-123" }, setUser = jest.fn() } = {}) =>
    render(
      <MemoryRouter>
        <UserContext.Provider value={{ user, setUser }}>{ui}</UserContext.Provider>
      </MemoryRouter>
    );

  test("renders success UI", () => {
    renderWithProviders(<PaymentSuccess />);
    expect(screen.getByText(/Payment Successful/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Return to Profile/i })).toBeInTheDocument();
  });

  test("verifies payment when session_id and user exist, then stores updated user", async () => {
    const setUser = jest.fn();
    const updatedUser = { firebaseId: "uid-123", walletBalance: 500 };

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ updatedUser }),
    });

    renderWithProviders(<PaymentSuccess />, { setUser });

    await waitFor(() => expect(global.fetch).toHaveBeenCalledTimes(1));
    expect(global.fetch).toHaveBeenCalledWith(
      "http://test-base-url/api/payment/verify-session",
      expect.objectContaining({
        method: "POST",
        headers: expect.objectContaining({
          "Content-Type": "application/json",
          Authorization: "Bearer fake-token",
        }),
        body: JSON.stringify({ sessionId: "test-session", userId: "uid-123" }),
      })
    );

    await waitFor(() => expect(setUser).toHaveBeenCalledWith(updatedUser));
    expect(localStorage.getItem("user-data")).toBe(JSON.stringify(updatedUser));
  });

  test("auto-redirects to /profile after 5 seconds", () => {
    renderWithProviders(<PaymentSuccess />);
    jest.advanceTimersByTime(5000);
    expect(window.location.href.endsWith("/profile")).toBe(true);
  });

  test("button click redirects immediately to /profile", () => {
    renderWithProviders(<PaymentSuccess />);
    fireEvent.click(screen.getByRole("button", { name: /Return to Profile/i }));
    expect(window.location.href.endsWith("/profile")).toBe(true);
  });

  test("does not call verify when session_id is missing", async () => {
    // Set empty search params to simulate no session_id
    mockSearchParams = new URLSearchParams("");
    
    renderWithProviders(<PaymentSuccess />);

    // Wait for effects to run
    await waitFor(() => {});
    
    expect(global.fetch).not.toHaveBeenCalled();
  });
});