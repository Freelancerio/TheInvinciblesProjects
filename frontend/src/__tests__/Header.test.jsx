import { render, screen, fireEvent, waitFor } from "@testing-library/react";
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

describe("Header", () => {
  test("renders logo text EPL SmartBet", () => {
    renderWithUser({ username: "TestUser", email: "test@example.com" });
    expect(screen.getByText(/EPL SmartBet/i)).toBeInTheDocument();
  });

  test("renders navigation links when user exists", () => {
    renderWithUser({ username: "TestUser", email: "test@example.com" });
    expect(screen.getByRole("link", { name: /home/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /profile/i })).toBeInTheDocument();
  });

  test("calls logoutUser and navigate on logout click", async () => {
    const logoutUser = jest.fn();
    renderWithUser({ username: "TestUser" }, logoutUser);

    fireEvent.click(screen.getByRole("button", { name: /logout/i }));

    await waitFor(() => {
      expect(auth.signOut).toHaveBeenCalled();
      expect(logoutUser).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith("/");
    });
  });
});
