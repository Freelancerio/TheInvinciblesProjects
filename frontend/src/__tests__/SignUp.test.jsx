import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import SignUp from "../pages/SignUp";
import { MemoryRouter } from "react-router-dom";
import { UserContext } from "../UserContext";

const mockNavigate = jest.fn();
const mockCreateUser = jest.fn();
const mockSignInWithPopup = jest.fn();
const mockLoginUser = jest.fn();

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

global.fetch = jest.fn();

const mockContextValue = {
  loginUser: mockLoginUser,
};

const renderWithContext = (component) => {
  return render(
    <MemoryRouter>
      <UserContext.Provider value={mockContextValue}>
        {component}
      </UserContext.Provider>
    </MemoryRouter>
  );
};

describe("SignUp Page", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
    window.alert = jest.fn();
    global.fetch.mockClear();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("renders signup form elements", () => {
    renderWithContext(<SignUp />);

    expect(screen.getByText(/create account/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/enter your email/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/create a password/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/confirm your password/i)).toBeInTheDocument();
    expect(screen.getByText(/sign up with google/i)).toBeInTheDocument();
  });

  test("shows error when passwords do not match", async () => {
    renderWithContext(<SignUp />);

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
    renderWithContext(<SignUp />);

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
    renderWithContext(<SignUp />);

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

    renderWithContext(<SignUp />);

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "password123" },
    });

    const checkbox = screen.getByRole("checkbox");
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

    renderWithContext(<SignUp />);

    fireEvent.change(screen.getByPlaceholderText(/enter your email/i), {
      target: { value: "test@example.com" },
    });
    fireEvent.change(screen.getByPlaceholderText(/create a password/i), {
      target: { value: "password123" },
    });
    fireEvent.change(screen.getByPlaceholderText(/confirm your password/i), {
      target: { value: "password123" },
    });

    const checkbox = screen.getByRole("checkbox");
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

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ username: "GoogleUser" }),
    });

    renderWithContext(<SignUp />);

    fireEvent.click(screen.getByText(/sign up with google/i));

    await waitFor(() => {
      expect(localStorage.getItem("authToken")).toBe("fake-token");
      expect(mockLoginUser).toHaveBeenCalledWith(
        expect.objectContaining({
          email: "google@example.com",
          username: "GoogleUser",
        })
      );
      expect(mockNavigate).toHaveBeenCalledWith("/home");
    });
  });

  test("google signup fails gracefully", async () => {
    mockSignInWithPopup.mockRejectedValueOnce(new Error("Google login failed"));

    renderWithContext(<SignUp />);

    fireEvent.click(screen.getByText(/sign up with google/i));

    await waitFor(() => {
      expect(
        screen.getByText(/google login failed\. try again\./i)
      ).toBeInTheDocument();
    });
  });

  test("clicking login link navigates to login page", () => {
    renderWithContext(<SignUp />);

    fireEvent.click(screen.getByText(/log in/i));
    expect(mockNavigate).toHaveBeenCalledWith("/login");
  });
});