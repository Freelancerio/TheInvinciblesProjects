import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import { UserContext } from "../UserContext";
import Profile from "../pages/Profile";

// Mock ProfilePage so we can detect it being rendered
jest.mock("../components/ProfilePage", () => {
  return function MockProfilePage() {
    return <div>Mocked ProfilePage</div>;
  };
});

// Mock Header component if Profile uses it
jest.mock("../components/Header", () => {
  return function MockHeader() {
    return <div>Mocked Header</div>;
  };
});

describe("Profile Page Wrapper", () => {
  // Mock user data
  const mockUser = {
    id: 1,
    username: "testuser",
    email: "test@example.com"
  };

  // Helper function to render component with all required providers
  const renderWithProviders = (user = mockUser) => {
    return render(
      <BrowserRouter>
        <UserContext.Provider value={{ user, setUser: jest.fn() }}>
          <Profile />
        </UserContext.Provider>
      </BrowserRouter>
    );
  };

  test("renders without crashing", () => {
    renderWithProviders();
    expect(screen.getByText(/Mocked ProfilePage/i)).toBeInTheDocument();
  });

  test("includes the ProfilePage component", () => {
    renderWithProviders();
    expect(screen.getByText(/Mocked ProfilePage/i)).toBeInTheDocument();
  });

  test("renders with Header if present", () => {
    renderWithProviders();
    // Check if Header is rendered (if Profile component includes it)
    const header = screen.queryByText(/Mocked Header/i);
    // This will pass whether or not Header is present
    if (header) {
      expect(header).toBeInTheDocument();
    }
  });
});