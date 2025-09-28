import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import userEvent from "@testing-library/user-event";
import MatchPrediction from "../components/upcomingMatch/MatchPrediction";

// Mock useLocation to provide the teams the component expects
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useLocation: () => ({
    state: {
      match: {
        homeTeam: "Liverpool",
        awayTeam: "Spurs",
      },
    },
  }),
}));

describe("MatchPrediction", () => {
  let user;
  let alertSpy;

  beforeEach(() => {
    jest.clearAllMocks();
    user = userEvent.setup();
    alertSpy = jest.spyOn(window, "alert").mockImplementation(() => {});
    // Mock fetch to avoid API errors in tests
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({}),
      })
    );
  });

  afterEach(() => {
    alertSpy.mockRestore();
  });

  test("renders header and toggles the prediction form", async () => {
    render(
      <MemoryRouter>
        <MatchPrediction />
      </MemoryRouter>
    );

    // Basic header present
    expect(screen.getByText(/match prediction/i)).toBeInTheDocument();

    // Toggle button starts closed
    const toggleBtn = screen.getByRole("button", { name: /show predicted outcome/i });
    expect(toggleBtn).toBeInTheDocument();

    // Click to reveal form
    await user.click(toggleBtn);

    // Form elements become visible
    expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
    const inputs = screen.getAllByPlaceholderText("0");
    expect(inputs).toHaveLength(2);
    expect(
      screen.getByRole("button", { name: /submit prediction/i })
    ).toBeInTheDocument();

    // And the button text should flip to "Hide Prediction"
    expect(
      screen.getByRole("button", { name: /hide prediction/i })
    ).toBeInTheDocument();
  });

  test("submits a valid user prediction", async () => {
    render(
      <MemoryRouter>
        <MatchPrediction />
      </MemoryRouter>
    );

    // Open the form
    await user.click(screen.getByRole("button", { name: /show predicted outcome/i }));

    const [homeInput, awayInput] = screen.getAllByPlaceholderText("0");

    // Enter valid scores
    await user.type(homeInput, "2");
    await user.type(awayInput, "1");

    // Submit
    await user.click(screen.getByRole("button", { name: /submit prediction/i }));

    // Expect alert to include the prediction
    expect(alertSpy).toHaveBeenCalledWith("Prediction submitted: Liverpool 2 - 1 Spurs");
  });

  test("shows error for invalid prediction", async () => {
    render(
      <MemoryRouter>
        <MatchPrediction />
      </MemoryRouter>
    );

    // Open the form
    await user.click(screen.getByRole("button", { name: /show predicted outcome/i }));

    // Submit without entering scores (both will be empty/0)
    await user.click(screen.getByRole("button", { name: /submit prediction/i }));

    // Should show validation error
    expect(alertSpy).toHaveBeenCalledWith("Please enter a valid score prediction");
  });

  test("handles fetch error path gracefully", async () => {
    // Mock a failed fetch
    global.fetch = jest.fn(() => Promise.resolve({ ok: false }));

    render(
      <MemoryRouter>
        <MatchPrediction />
      </MemoryRouter>
    );

    // Even if the fetch fails, the toggle should still work and the form should render
    await user.click(screen.getByRole("button", { name: /show predicted outcome/i }));
    expect(screen.getByText(/make your prediction/i)).toBeInTheDocument();
  });
});