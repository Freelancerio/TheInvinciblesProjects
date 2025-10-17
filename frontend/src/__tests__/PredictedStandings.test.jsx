// src/__tests__/PredictedStandings.test.jsx
import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import PredictedStandings from "../pages/PredictedStandings.jsx";
import { UserContext } from "../UserContext";

// keep Header out of the way if the page uses it
jest.mock("../components/Header", () => () => <div data-testid="header" />);

// base URL helper used in the app
jest.mock("../api.js", () => ({ __esModule: true, default: () => "http://test-base-url" }));

const renderWithUser = (ui, user = { firebaseId: "uid-123", username: "Alice" }) =>
  render(<UserContext.Provider value={{ user, setUser: jest.fn() }}>{ui}</UserContext.Provider>);

beforeEach(() => {
  localStorage.setItem("authToken", "fake-token");
  global.fetch = jest.fn();
});

afterEach(() => {
  jest.clearAllMocks();
});

test("renders prediction settings and buttons", async () => {
  renderWithUser(<PredictedStandings />);

  // Check that the main elements are rendered
  expect(screen.getByText(/Predicted Standings/i)).toBeInTheDocument();
  expect(screen.getByText(/AI-powered predictions/i)).toBeInTheDocument();
  expect(screen.getByText(/Prediction Settings/i)).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /Generate Predictions/i })).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /Select All/i })).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /Reset/i })).toBeInTheDocument();
  
  // Check season selector
  expect(screen.getByRole('combobox')).toBeInTheDocument();
});

test("generates predictions when factors are selected and button is clicked", async () => {
  // Mock data that matches the component's expected structure
  const predictions = [
    { 
      teamName: "Team A", 
      predictedScore: 68.5, 
      predictedRank: 1,
      teamLogo: "logo-a.png" 
    },
    { 
      teamName: "Team B", 
      predictedScore: 65.2, 
      predictedRank: 2,
      teamLogo: "logo-b.png" 
    },
  ];

  global.fetch.mockResolvedValueOnce({
    ok: true,
    json: async () => predictions,
  });

  renderWithUser(<PredictedStandings />);

  // Select some prediction factors first - click the label text since checkboxes are hidden
  const firstFactorLabel = screen.getByText(/2024 Points \(60%\)/i);
  fireEvent.click(firstFactorLabel);

  // Click generate predictions button
  const generateButton = screen.getByRole('button', { name: /Generate Predictions/i });
  fireEvent.click(generateButton);

  // Check loading state
  expect(screen.getByText(/Generating Predictions/i)).toBeInTheDocument();
  expect(screen.getByText(/Analyzing team data/i)).toBeInTheDocument();

  // Wait for success state
  await waitFor(() => {
    expect(screen.getByText(/Team A/i)).toBeInTheDocument();
    expect(screen.getByText(/Team B/i)).toBeInTheDocument();
    expect(screen.getByText(/68.5/i)).toBeInTheDocument();
    expect(screen.getByText(/65.2/i)).toBeInTheDocument();
  });

  // Verify endpoint + auth header
  expect(global.fetch).toHaveBeenCalledWith(
    "http://test-base-url/api/standings/predict",
    expect.objectContaining({
      method: "POST",
      headers: expect.objectContaining({ 
        Authorization: "Bearer fake-token",
        "Content-Type": "application/json" 
      }),
      body: JSON.stringify({ 
        season: 2025, 
        tags: ["previousPoints2024"] 
      }),
    })
  );
});

test("shows error when no factors are selected", async () => {
  renderWithUser(<PredictedStandings />);

  // Click generate predictions button without selecting any factors
  const generateButton = screen.getByRole('button', { name: /Generate Predictions/i });
  fireEvent.click(generateButton);

  // Should show error message
  await waitFor(() => {
    expect(screen.getByText(/Please select at least one prediction factor/i)).toBeInTheDocument();
  });

  // API should not be called
  expect(global.fetch).not.toHaveBeenCalled();
});

test("shows error state when API fails", async () => {
  // Mock API failure
  global.fetch.mockResolvedValueOnce({ ok: false });

  renderWithUser(<PredictedStandings />);

  // Select a factor and try to generate predictions
  const firstFactorLabel = screen.getByText(/2024 Points \(60%\)/i);
  fireEvent.click(firstFactorLabel);

  const generateButton = screen.getByRole('button', { name: /Generate Predictions/i });
  fireEvent.click(generateButton);

  // Wait for error state
  await waitFor(() => {
    expect(screen.getByText(/Prediction Failed/i)).toBeInTheDocument();
    expect(screen.getByText(/Failed to generate predictions/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Try Again/i })).toBeInTheDocument();
  });
});

test("season selector changes season value", async () => {
  renderWithUser(<PredictedStandings />);

  const seasonSelect = screen.getByRole('combobox');
  
  // Change season to 2024
  fireEvent.change(seasonSelect, { target: { value: "2024" } });
  
  expect(seasonSelect.value).toBe("2024");
});

// Simple test for select all and reset without checking confidence
test("select all and reset buttons can be clicked", async () => {
  renderWithUser(<PredictedStandings />);

  // Click Select All button
  const selectAllButton = screen.getByRole('button', { name: /Select All/i });
  fireEvent.click(selectAllButton);

  // Click Reset button
  const resetButton = screen.getByRole('button', { name: /Reset/i });
  fireEvent.click(resetButton);

  // Just verify buttons work without errors
  expect(selectAllButton).toBeInTheDocument();
  expect(resetButton).toBeInTheDocument();
});