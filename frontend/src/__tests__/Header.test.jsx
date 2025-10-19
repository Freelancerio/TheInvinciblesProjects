import { render, screen, fireEvent, waitFor, within } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import Header from "../components/Header.jsx";
import { UserContext } from "../UserContext";

// Mock react-router's useNavigate
const mockNavigate = jest.fn();
jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

// Mock firebase auth
jest.mock("../firebase", () => ({
  auth: { signOut: jest.fn(() => Promise.resolve()) }
}));
import { auth } from "../firebase";

function renderWithUser(user, logoutUser = jest.fn()) {
  return render(
    <BrowserRouter>
      <UserContext.Provider value={{ user, logoutUser }}>
        <Header />
      </UserContext.Provider>
    </BrowserRouter>
  );
}

describe("Header component", () => {
  const user = { username: "TestUser", email: "test@example.com" };

  test("renders logo text EPL SmartBet", () => {
    renderWithUser(user);
    expect(screen.getByText(/EPL SmartBet/i)).toBeInTheDocument();
  });

  test("renders desktop navigation links when user exists", () => {
    renderWithUser(user);
    const desktopMenu = screen.getByTestId("desktop-menu");

    expect(within(desktopMenu).getByText(/home/i)).toBeVisible();
    expect(within(desktopMenu).getByText(/profile/i)).toBeVisible();
    expect(within(desktopMenu).getByText(/logout/i)).toBeVisible();
  });

  test("renders mobile menu button and toggles menu on click", () => {
    renderWithUser(user);

    const mobileButton = screen.getByTestId("mobile-menu-button");
    fireEvent.click(mobileButton);

    const mobileMenu = screen.getByTestId("mobile-menu");
    expect(within(mobileMenu).getByText(/home/i)).toBeVisible();
    expect(within(mobileMenu).getByText(/profile/i)).toBeVisible();
    expect(within(mobileMenu).getByText(/logout/i)).toBeVisible();

    // Close menu
    fireEvent.click(mobileButton);
    const desktopMenu = screen.getByTestId("desktop-menu");
    expect(within(desktopMenu).getByText(/home/i)).toBeVisible(); // desktop remains visible
  });

  test("clicking mobile nav link closes the menu", () => {
    renderWithUser(user);

    const mobileButton = screen.getByTestId("mobile-menu-button");
    fireEvent.click(mobileButton);

    const mobileMenu = screen.getByTestId("mobile-menu");
    const profileLink = within(mobileMenu).getByText(/profile/i);
    fireEvent.click(profileLink);

    const desktopMenu = screen.getByTestId("desktop-menu");
    expect(within(desktopMenu).getByText(/profile/i)).toBeVisible();
  });

  test("calls logoutUser and navigate on logout click (desktop)", async () => {
    const logoutUser = jest.fn();
    renderWithUser(user, logoutUser);

    const desktopMenu = screen.getByTestId("desktop-menu");
    const logoutBtn = within(desktopMenu).getByText(/logout/i);
    fireEvent.click(logoutBtn);

    await waitFor(() => {
      expect(auth.signOut).toHaveBeenCalled();
      expect(logoutUser).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith("/");
    });
  });

  test("calls logoutUser and navigate on logout click (mobile)", async () => {
    const logoutUser = jest.fn();
    renderWithUser(user, logoutUser);

    const mobileButton = screen.getByTestId("mobile-menu-button");
    fireEvent.click(mobileButton);

    const mobileMenu = screen.getByTestId("mobile-menu");
    const logoutBtn = within(mobileMenu).getByText(/logout/i);
    fireEvent.click(logoutBtn);

    await waitFor(() => {
      expect(auth.signOut).toHaveBeenCalled();
      expect(logoutUser).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith("/");
    });
  });
});
