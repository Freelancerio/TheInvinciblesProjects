// src/__tests__/LandingPage.test.jsx
import { render, screen, fireEvent } from "@testing-library/react";
import LandingPage from "../pages/LandingPage";
import { MemoryRouter } from "react-router-dom";

const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

describe("LandingPage", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test("renders hero section with headline and buttons", () => {
    render(
      <MemoryRouter>
        <LandingPage />
      </MemoryRouter>
    );

    expect(
      screen.getByText(/the ultimate premier league betting experience/i)
    ).toBeInTheDocument();

    expect(screen.getByRole("button", { name: /login/i })).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /get started/i })
    ).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: /learn more/i })
    ).toBeInTheDocument();
  });

  test("navigates to /login when Login button is clicked", () => {
    render(
      <MemoryRouter>
        <LandingPage />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByRole("button", { name: /login/i }));
    expect(mockNavigate).toHaveBeenCalledWith("/login");
  });

  test("navigates to /login when Get Started button is clicked", () => {
    render(
      <MemoryRouter>
        <LandingPage />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByRole("button", { name: /get started/i }));
    expect(mockNavigate).toHaveBeenCalledWith("/login");
  });

  test("Learn More button is clickable", () => {
    render(
      <MemoryRouter>
        <LandingPage />
      </MemoryRouter>
    );

    const learnMoreBtn = screen.getByRole("button", { name: /learn more/i });
    expect(learnMoreBtn).toBeInTheDocument();
    fireEvent.click(learnMoreBtn);
    // Button click succeeds without error
  });

  test("renders features and footer sections", () => {
    render(
      <MemoryRouter>
        <LandingPage />
      </MemoryRouter>
    );

    expect(
      screen.getByText(/why choose our platform/i)
    ).toBeInTheDocument();
    expect(screen.getByText(/advanced statistics/i)).toBeInTheDocument();
    expect(screen.getByText(/live updates/i)).toBeInTheDocument();
    expect(screen.getByText(/bet calculator/i)).toBeInTheDocument();

    expect(screen.getByText(/about us/i)).toBeInTheDocument();
    expect(screen.getByText(/quick links/i)).toBeInTheDocument();
    expect(screen.getByText(/connect with us/i)).toBeInTheDocument();
  });
});