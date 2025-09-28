import { render, screen } from "@testing-library/react";
import { UserContext } from "../UserContext";
import WelcomeBanner from "../components/WelcomeBanner.jsx";

function renderWithUser(user) {
  return render(
    <UserContext.Provider value={{ user }}>
      <WelcomeBanner />
    </UserContext.Provider>
  );
}

describe("WelcomeBanner", () => {
  test("renders greeting and balance when user is provided", () => {
    const fakeUser = { username: "Steph", account_balance: 2500 };
    renderWithUser(fakeUser);

    expect(screen.getByText(/welcome back, steph!/i)).toBeInTheDocument();
    expect(
      screen.getByText(/check out the latest matches and place your bets./i)
    ).toBeInTheDocument();
    expect(screen.getByText(/R2500/i)).toBeInTheDocument();
  });

  test("renders nothing if no user is in context", () => {
    const { container } = renderWithUser(null);
    expect(container.firstChild).toBeNull();
  });
});
