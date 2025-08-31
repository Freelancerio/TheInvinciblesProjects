import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Loginfunction from "../Components/loginpage.jsx";
import { BrowserRouter } from "react-router-dom";

// Mock the router navigate
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => {
  const actual = jest.requireActual("react-router-dom");
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

// Mock firebase module used by the component
jest.mock("../firebase", () => ({
  auth: {},
  googleProvider: {},
  signInWithPopup: jest.fn(),
  signInWithEmailAndPassword: jest.fn(() => Promise.resolve({ user: { uid: "123" } })),
}));

function renderWithRouter(ui) {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
}

describe("Loginfunction", () => {
  beforeEach(() => {
    jest.clearAllMocks();

    // Make window.location writable & predictable for tests
    delete window.location;
    // JSDOM defaults to http://localhost/
    window.location = new URL("http://localhost/");
  });

  test("allows entering email and password", () => {
    renderWithRouter(<Loginfunction />);

    const emailInput = screen.getByPlaceholderText(/enter your email/i);
    const passwordInput = screen.getByPlaceholderText(/enter your password/i);

    fireEvent.change(emailInput, { target: { value: "user@example.com" } });
    fireEvent.change(passwordInput, { target: { value: "secret123" } });

    expect(emailInput).toHaveValue("user@example.com");
    expect(passwordInput).toHaveValue("secret123");
  });

  test("submitting email/password calls Firebase and navigates to /profile", async () => {
    const { signInWithEmailAndPassword } = require("../firebase");
    renderWithRouter(<Loginfunction />);

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "user@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/enter your password/i), {
      target: { value: "secret123" },
    });

    fireEvent.click(screen.getByRole("button", { name: /log in/i }));

    await waitFor(() => {
      expect(signInWithEmailAndPassword).toHaveBeenCalledWith(
        expect.any(Object), // auth
        "user@example.com",
        "secret123"
      );
      expect(mockNavigate).toHaveBeenCalledWith("/profile");
    });
  });

  test("Google button redirects to local OAuth when on localhost", () => {
    renderWithRouter(<Loginfunction />);

    fireEvent.click(screen.getByRole("button", { name: /continue with google/i }));
    expect(String(window.location)).toBe("http://localhost:8080/oauth2/authorization/google");
  });

  test("GitHub button redirects to local OAuth when on localhost", () => {
    renderWithRouter(<Loginfunction />);

    fireEvent.click(screen.getByRole("button", { name: /continue with github/i }));
    expect(String(window.location)).toBe("http://localhost:8080/oauth2/authorization/github");
  });
});
