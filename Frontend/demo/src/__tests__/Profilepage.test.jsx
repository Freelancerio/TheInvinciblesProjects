import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
import ProfilePage from "../Components/Profilepage.jsx";
import axios from "axios";

/**
 * Explicit axios mock so Jest 27 never tries to load the real ESM axios.
 * This avoids "Cannot use import statement outside a module".
 */
jest.mock("axios", () => ({
  __esModule: true,
  default: { get: jest.fn() }, // supports `import axios from 'axios'`
  get: jest.fn(),              // (covers rare named import usage)
}));

describe("ProfilePage", () => {
  beforeEach(() => {
    // Clear previous call history between tests
    axios.get.mockReset();
  });

  test("loads user info on mount and renders it", async () => {
    axios.get.mockResolvedValueOnce({
      data: { name: "Alice Tester", picture: "https://example.com/pic.png" },
    });

    render(<ProfilePage />);

    // initial state
    expect(screen.getByText(/loading user data/i)).toBeInTheDocument();

    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledWith("http://localhost:8080/user-info", {
        withCredentials: true,
      });
      // name rendered
      expect(screen.getByText("Alice Tester")).toBeInTheDocument();
      // profile image rendered (ensure component uses alt="User Profile")
      const img = screen.getByRole("img", { name: /user profile/i });
      expect(img).toHaveAttribute("src", "https://example.com/pic.png");
    });
  });

  test("on error, stays in loading state (no user shown)", async () => {
    axios.get.mockRejectedValueOnce(new Error("boom"));

    render(<ProfilePage />);

    expect(screen.getByText(/loading user data/i)).toBeInTheDocument();

    await waitFor(() => expect(axios.get).toHaveBeenCalled());

    // No user-specific UI should appear
    expect(screen.queryByText(/Verified • Level 2/i)).not.toBeInTheDocument();
    expect(screen.queryByRole("img", { name: /user profile/i })).not.toBeInTheDocument();
  });
});
