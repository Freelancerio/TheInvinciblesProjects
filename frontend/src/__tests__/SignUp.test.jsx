import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import SignUp from "../pages/SignUp";
import { MemoryRouter } from "react-router-dom";

// Mocks
const mockNavigate = jest.fn();
const mockCreateUser = jest.fn();
const mockSignInWithPopup = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

jest.mock("../firebase", () => ({
  auth: {},
  googleProvider: {},
}));

jest.mock("firebase/auth", () => ({
  createUserWithEmailAndPassword: (...args) => mockCreateUser(...args),
  signInWithPopup: (...args) => mockSignInWithPopup(...args),
}));

describe("SignUp Page", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
    // Mock alert
    window.alert = jest.fn();
  });

  test("renders signup form elements", () => {
    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    expect(screen.getByText(/create account/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/enter your email/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/create a password/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/confirm your password/i)).toBeInTheDocument();
    expect(screen.getByText(/sign up with google/i)).toBeInTheDocument();
  });

  test("shows error when passwords do not match", async () => {
    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "different" },
    });
    fireEvent.click(screen.getByText(/create account/i));

    await waitFor(() => {
      expect(screen.getByText(/passwords do not match/i)).toBeInTheDocument();
    });
  });

  test("shows error for short password", async () => {
    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "short" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "short" },
    });
    fireEvent.click(screen.getByText(/create account/i));

    await waitFor(() => {
      expect(
        screen.getByText(/password must be at least 8 characters/i)
      ).toBeInTheDocument();
    });
  });

  test("shows error when terms are not accepted", async () => {
    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "password123" },
    });
    fireEvent.click(screen.getByText(/create account/i));

    await waitFor(() => {
      expect(
        screen.getByText(/you must agree to the terms/i)
      ).toBeInTheDocument();
    });
  });

  test("successful signup calls Firebase and navigates", async () => {
    mockCreateUser.mockResolvedValueOnce({});

    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "password123" },
    });
    
    // Click the checkbox directly by its role
    const checkbox = screen.getByRole('checkbox');
    fireEvent.click(checkbox);
    
    fireEvent.click(screen.getByText(/create account/i));

    await waitFor(() => {
      expect(mockCreateUser).toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith("Account created successfully!");
      expect(mockNavigate).toHaveBeenCalledWith("/login");
    });
  });

  test("failed signup shows error", async () => {
    mockCreateUser.mockRejectedValueOnce(new Error("Signup failed"));

    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "password123" },
    });
    
    // Click the checkbox directly by its role
    const checkbox = screen.getByRole('checkbox');
    fireEvent.click(checkbox);
    
    fireEvent.click(screen.getByText(/create account/i));

    await waitFor(() => {
      expect(screen.getByText(/signup failed/i)).toBeInTheDocument();
    });
  });

  test("google signup flow success", async () => {
    const fakeUser = {
      email: "google@example.com",
      getIdToken: () => Promise.resolve("fake-token"),
    };
    mockSignInWithPopup.mockResolvedValueOnce({ user: fakeUser });

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ username: "GoogleUser" }),
      })
    );

    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText(/sign up with google/i));

    await waitFor(() => {
      expect(localStorage.getItem("authToken")).toBe("fake-token");
      expect(localStorage.getItem("user-name")).toBe("GoogleUser");
      expect(mockNavigate).toHaveBeenCalledWith("/profile");
    });
  });

  test("google signup fails gracefully", async () => {
    mockSignInWithPopup.mockRejectedValueOnce(new Error("Google login failed"));

    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText(/sign up with google/i));

    await waitFor(() => {
      expect(
        screen.getByText(/google login failed\. try again\./i)
      ).toBeInTheDocument();
    });
  });

  test("clicking login link navigates to login page", () => {
    render(
      <MemoryRouter>
        <SignUp />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText(/log in/i));
    expect(mockNavigate).toHaveBeenCalledWith("/login");
  });
});