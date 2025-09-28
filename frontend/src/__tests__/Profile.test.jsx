import { render, screen } from "@testing-library/react";
import Profile from "../pages/Profile";
import ProfilePage from "../components/ProfilePage";

// Mock ProfilePage so we can detect it being rendered
jest.mock("../components/ProfilePage", () => jest.fn(() => <div>Mocked ProfilePage</div>));

describe("Profile Page Wrapper", () => {
  test("renders without crashing", () => {
    render(<Profile />);
    expect(screen.getByText(/Mocked ProfilePage/i)).toBeInTheDocument();
  });

  test("includes the ProfilePage component", () => {
    render(<Profile />);
    expect(ProfilePage).toHaveBeenCalled();
  });
});
