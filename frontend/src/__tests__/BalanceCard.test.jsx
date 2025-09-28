// src/__tests__/BalanceCard.test.jsx
import { render, screen } from "@testing-library/react";
import BalanceCard from "../components/BalanceCard.jsx";
import { UserContext } from "../UserContext";

function renderWithUser(user) {
  return render(
    <UserContext.Provider value={{ user }}>
      <BalanceCard />
    </UserContext.Provider>
  );
}

describe("BalanceCard", () => {
  test("renders account balance when user is provided", () => {
    const fakeUser = { account_balance: 5000 };
    renderWithUser(fakeUser);

    expect(screen.getByText(/account balance/i)).toBeInTheDocument();
    expect(screen.getByText(/R 5000/i)).toBeInTheDocument();
    expect(
      screen.getByText(/available for betting and withdrawals/i)
    ).toBeInTheDocument();
  });

  test("renders nothing if no user in context", () => {
    const { container } = renderWithUser(null);
    expect(container.firstChild).toBeNull();
  });
});
