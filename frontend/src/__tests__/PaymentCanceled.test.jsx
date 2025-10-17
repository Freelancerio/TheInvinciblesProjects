// frontend/src/__tests__/PaymentCanceled.test.jsx
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import PaymentCanceled from "../pages/PaymentCanceled.jsx";

// Mock window.location
const mockLocationAssign = jest.fn();
const originalLocation = window.location;

describe("PaymentCanceled page", () => {
  beforeAll(() => {
    jest.useFakeTimers();
    
    // Mock window.location
    delete window.location;
    window.location = {
      ...originalLocation,
      href: "http://localhost/",
      assign: mockLocationAssign,
    };
    
    // Mock window.location.href setter
    Object.defineProperty(window.location, 'href', {
      set: function(value) {
        this._href = value;
        mockLocationAssign(value);
      },
      get: function() {
        return this._href || "http://localhost/";
      }
    });
  });

  afterAll(() => {
    jest.useRealTimers();
    window.location = originalLocation;
  });

  beforeEach(() => {
    // Reset mocks and URL
    mockLocationAssign.mockClear();
    window.location._href = "http://localhost/";
    global.fetch = jest.fn();
  });

  test("renders the canceled message and button", () => {
    render(<PaymentCanceled />);
    expect(screen.getByText(/Payment Canceled/i)).toBeInTheDocument();
    expect(
      screen.getByText(/Your payment was canceled/i)
    ).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /Try Again/i })
    ).toBeInTheDocument();
  });

  test("button click redirects immediately to /profile", () => {
    render(<PaymentCanceled />);
    fireEvent.click(screen.getByRole("button", { name: /Try Again/i }));
    
    // Check that window.location.href was set to "/profile"
    expect(window.location.href).toBe("/profile");
  });

  test("auto-redirects to /profile after 5 seconds", () => {
    render(<PaymentCanceled />);
    
    // Verify initial state
    expect(window.location.href).toBe("http://localhost/");
    
    // Advance timers by 5 seconds
    jest.advanceTimersByTime(5000);
    
    // Check that auto-redirect happened
    expect(window.location.href).toBe("/profile");
  });

  test("does not call any API", () => {
    render(<PaymentCanceled />);
    expect(global.fetch).not.toHaveBeenCalled();
  });
});