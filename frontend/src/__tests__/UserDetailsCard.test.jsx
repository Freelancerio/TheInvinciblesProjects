import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { UserContext } from "../UserContext";
import UserDetailsCard from "../components/UserDetailsCard.jsx";

// Mock fetch and alert
global.fetch = jest.fn();
const alertMock = jest.spyOn(window, "alert").mockImplementation(() => {});

function renderWithUser(user, setUser) {
  return render(
    <UserContext.Provider value={{ user, setUser }}>
      <UserDetailsCard />
    </UserContext.Provider>
  );
}

describe("UserDetailsCard", () => {
  const fakeUser = {
    username: "Steph",
    email: "steph@example.com",
    joined: "2023-01-01T00:00:00Z",
  };

  beforeEach(() => {
    fetch.mockReset();
    localStorage.clear();
  });

  test("renders user details when user is provided", () => {
    renderWithUser(fakeUser, jest.fn());
    expect(screen.getByText(/user details/i)).toBeInTheDocument();
    expect(screen.getByText(fakeUser.email)).toBeInTheDocument();
    expect(screen.getByLabelText(/username/i)).toHaveValue("Steph");
  });

  test("renders nothing if no user", () => {
    const { container } = renderWithUser(null, jest.fn());
    expect(container.firstChild).toBeNull();
  });

  test("updates username input on change", () => {
    renderWithUser(fakeUser, jest.fn());
    const input = screen.getByLabelText(/username/i);
    fireEvent.change(input, { target: { value: "NewName" } });
    expect(input).toHaveValue("NewName");
  });

  test("calls fetch and setUser on save", async () => {
    const setUser = jest.fn((updater) =>
      updater(fakeUser) // simulate context update
    );
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({ username: "UpdatedName" }),
    });

    renderWithUser(fakeUser, setUser);
    const input = screen.getByLabelText(/username/i);
    fireEvent.change(input, { target: { value: "UpdatedName" } });

    fireEvent.click(screen.getByRole("button", { name: /save changes/i }));

    await waitFor(() => {
      expect(fetch).toHaveBeenCalled();
      expect(setUser).toHaveBeenCalled();
    });
  });

  test("clicking avatar edit triggers alert", () => {
    renderWithUser(fakeUser, jest.fn());

    // Find the pencil icon's container div and click it
    const pencilIcon = screen.getByText((_, el) =>
      el.classList.contains("fa-pencil-alt")
    );
    fireEvent.click(pencilIcon.parentElement);

    expect(alertMock).toHaveBeenCalledWith("Avatar editing modal would open here");
  });
});
