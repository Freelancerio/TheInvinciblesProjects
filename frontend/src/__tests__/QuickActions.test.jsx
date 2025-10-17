import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import QuickActions from "../components/QuickActions";

describe("QuickActions", () => {
  const renderComponent = () => {
    return render(
      <BrowserRouter>
        <QuickActions />
      </BrowserRouter>
    );
  };

  test("renders the component without crashing", () => {
    renderComponent();
    expect(screen.getByText(/quick actions/i)).toBeInTheDocument();
  });

  test("renders the section title with correct styling", () => {
    renderComponent();
    const title = screen.getByText(/quick actions/i);
    expect(title).toBeInTheDocument();
    expect(title).toHaveClass("card-title", "text-[1.3rem]", "font-semibold", "text-secondary");
  });

  test("renders all four action links with correct labels", () => {
    renderComponent();
    expect(screen.getByRole("link", { name: /deposit funds/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /stats analysis/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /leaderboard/i })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /bet history/i })).toBeInTheDocument();
  });

  test("renders matching icons for each link", () => {
    renderComponent();
    const depositIcon = screen.getByText(/deposit funds/i).previousSibling;
    const statsIcon = screen.getByText(/stats analysis/i).previousSibling;
    const leaderboardIcon = screen.getByText(/leaderboard/i).previousSibling;
    const historyIcon = screen.getByText(/bet history/i).previousSibling;

    expect(depositIcon).toHaveClass("fa-money-bill-wave");
    expect(statsIcon).toHaveClass("fa-chart-line");
    expect(leaderboardIcon).toHaveClass("fa-trophy");
    expect(historyIcon).toHaveClass("fa-history");
  });

  test("has correct href attributes for all links", () => {
    renderComponent();
    expect(screen.getByRole("link", { name: /deposit funds/i })).toHaveAttribute("href", "/profile");
    expect(screen.getByRole("link", { name: /stats analysis/i })).toHaveAttribute("href", "/comparison");
    expect(screen.getByRole("link", { name: /leaderboard/i })).toHaveAttribute("href", "/leaderboards");
    expect(screen.getByRole("link", { name: /bet history/i })).toHaveAttribute("href", "/profile");
  });

  test("renders correct number of action links", () => {
    renderComponent();
    const links = screen.getAllByRole("link");
    expect(links).toHaveLength(4);
  });

  test("each link has correct CSS classes for styling", () => {
    renderComponent();
    const links = screen.getAllByRole("link");

    links.forEach(link => {
      expect(link).toHaveClass(
        "bg-[rgba(255,255,255,0.1)]",
        "border",
        "border-[rgba(255,255,255,0.1)]",
        "text-white",
        "p-[15px]",
        "rounded-[8px]",
        "cursor-pointer"
      );
    });
  });

  test("each icon has correct base classes", () => {
    renderComponent();
    const icons = screen.getAllByRole("link").map(link => link.querySelector("i"));

    icons.forEach(icon => {
      expect(icon).toHaveClass("fas", "text-[1.5rem]", "text-secondary");
    });
  });

  test("renders card with correct container classes", () => {
    const { container } = renderComponent();
    const card = container.querySelector(".card-bg");

    expect(card).toHaveClass(
      "card-bg",
      "backdrop-blur-md",
      "rounded-[10px]",
      "p-5",
      "mb-[25px]",
      "border",
      "border-[rgba(255,255,255,0.1)]"
    );
  });

  test("renders grid container with correct classes", () => {
    const { container } = renderComponent();
    const gridContainer = container.querySelector(".quick-actions");

    expect(gridContainer).toHaveClass("quick-actions", "grid", "grid-cols-2", "gap-[15px]");
  });

  test("renders card header with correct styling", () => {
    const { container } = renderComponent();
    const header = container.querySelector(".card-header");

    expect(header).toHaveClass(
      "card-header",
      "flex",
      "justify-between",
      "items-center",
      "mb-5",
      "pb-[10px]",
      "border-b",
      "border-[rgba(255,255,255,0.1)]"
    );
  });

  test("each action link has icon and label in correct order", () => {
    renderComponent();
    const actionLinks = [
      { icon: "fa-money-bill-wave", label: "Deposit Funds" },
      { icon: "fa-chart-line", label: "Stats Analysis" },
      { icon: "fa-trophy", label: "Leaderboard" },
      { icon: "fa-history", label: "Bet History" },
    ];

    actionLinks.forEach(action => {
      const link = screen.getByRole("link", { name: new RegExp(action.label, "i") });
      const icon = link.querySelector("i");
      const label = screen.getByText(action.label);

      expect(icon).toHaveClass(action.icon);
      expect(label).toBeInTheDocument();
    });
  });

  test("action links have flex column layout", () => {
    renderComponent();
    const links = screen.getAllByRole("link");

    links.forEach(link => {
      expect(link).toHaveClass("flex", "flex-col", "items-center", "gap-[8px]");
    });
  });

  test("renders with hover effect classes", () => {
    renderComponent();
    const links = screen.getAllByRole("link");

    links.forEach(link => {
      expect(link).toHaveClass(
        "hover:bg-[rgba(255,255,255,0.15)]",
        "hover:-translate-y-[3px]"
      );
    });
  });

  test("renders transition classes on links", () => {
    renderComponent();
    const links = screen.getAllByRole("link");

    links.forEach(link => {
      expect(link).toHaveClass("transition-all", "duration-300", "ease-in-out");
    });
  });
});