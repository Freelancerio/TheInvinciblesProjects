import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Login from "../pages/Login";
import { MemoryRouter } from "react-router-dom";
import { UserContext } from "../UserContext";

// Mock navigate
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

// Mock Firebase
const mockSignInWithPopup = jest.fn();
const mockSignInWithEmailAndPassword = jest.fn();
jest.mock("../firebase", () => ({
  auth: {},
  googleProvider: {},
  signInWithPopup: (...args) => mockSignInWithPopup(...args),
  signInWithEmailAndPassword: (...args) =>
    mockSignInWithEmailAndPassword(...args),
}));

describe("Login Page", () => {
  let loginUser;

  beforeEach(() => {
    loginUser = jest.fn();
    mockNavigate.mockReset();
    mockSignInWithPopup.mockReset();
    mockSignInWithEmailAndPassword.mockReset();
    localStorage.clear();
  });

  test("renders form inputs and buttons", () => {
    render(
      <MemoryRouter>
        <UserContext.Provider value={{ loginUser }}>
          <Login />
        </UserContext.Provider>
      </MemoryRouter>
    );

    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /login/i })).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /sign in with google/i })
    ).toBeInTheDocument();
  });

  test("handles email login success", async () => {
    const mockUser = {
      email: "test@example.com",
      getIdToken: jest.fn(() => Promise.resolve("mock-token")),
    };
    mockSignInWithEmailAndPassword.mockResolvedValueOnce({
      user: mockUser,
    });

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ role: "user" }),
      })
    );

    render(
      <MemoryRouter>
        <UserContext.Provider value={{ loginUser }}>
          <Login />
        </UserContext.Provider>
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByLabelText(/password/i), {
      target: { value: "password123" },
    });
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    await waitFor(() => {
      expect(loginUser).toHaveBeenCalledWith({
        email: "test@example.com",
        role: "user",
      });
      expect(localStorage.getItem("authToken")).toBe("mock-token");
      expect(mockNavigate).toHaveBeenCalledWith("/home");
    });
  });

  test("handles email login failure", async () => {
    mockSignInWithEmailAndPassword.mockRejectedValueOnce(
      new Error("Invalid credentials")
    );

    render(
      <MemoryRouter>
        <UserContext.Provider value={{ loginUser }}>
          <Login />
        </UserContext.Provider>
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/email/i), {
      target: { value: "wrong@example.com" },
    });
    fireEvent.change(screen.getByLabelText(/password/i), {
      target: { value: "badpass" },
    });
    fireEvent.click(screen.getByRole("button", { name: /login/i }));

    await waitFor(() => {
      expect(
        screen.getByText(/email\/password login failed/i)
      ).toBeInTheDocument();
    });
  });

  test("handles google login success", async () => {
    const mockUser = {
      email: "google@example.com",
      getIdToken: jest.fn(() => Promise.resolve("google-token")),
    };
    mockSignInWithPopup.mockResolvedValueOnce({ user: mockUser });

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ role: "google-user" }),
      })
    );

    render(
      <MemoryRouter>
        <UserContext.Provider value={{ loginUser }}>
          <Login />
        </UserContext.Provider>
      </MemoryRouter>
    );

    fireEvent.click(
      screen.getByRole("button", { name: /sign in with google/i })
    );

    await waitFor(() => {
      expect(loginUser).toHaveBeenCalledWith({
        email: "google@example.com",
        role: "google-user",
      });
      expect(localStorage.getItem("authToken")).toBe("google-token");
      expect(mockNavigate).toHaveBeenCalledWith("/home");
    });
  });

  test("handles google login failure", async () => {
    mockSignInWithPopup.mockRejectedValueOnce(new Error("Google failed"));

    render(
      <MemoryRouter>
        <UserContext.Provider value={{ loginUser }}>
          <Login />
        </UserContext.Provider>
      </MemoryRouter>
    );

    fireEvent.click(
      screen.getByRole("button", { name: /sign in with google/i })
    );

    await waitFor(() => {
      expect(
        screen.getByText(/google login failed\. try again\./i)
      ).toBeInTheDocument();
    });
  });
});
