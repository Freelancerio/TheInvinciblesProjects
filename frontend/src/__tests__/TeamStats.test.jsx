// src/__tests__/TeamStats.test.jsx
import { render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import TeamStrength from "../pages/TeamStats";

// Mock Header so we don't need to render its internals
jest.mock("../components/Header", () => () => <div>Mock Header</div>);

describe("TeamStrength", () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  test("renders loading state", () => {
    render(
      <MemoryRouter initialEntries={["/team/Liverpool"]}>
        <Routes>
          <Route path="/team/:teamName" element={<TeamStrength />} />
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByText(/loading team strength/i)).toBeInTheDocument();
  });

  test("renders team strength on success", async () => {
    const mockData = {
      teamName: "Liverpool",
      attackStrength: 1.25,
      midfieldStrength: 0.95,
      defenseStrength: 1.10,
      squadStrength: 1.05,
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter initialEntries={["/team/Liverpool"]}>
        <Routes>
          <Route path="/team/:teamName" element={<TeamStrength />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/Liverpool Strength/i)).toBeInTheDocument();
      expect(screen.getByText("1.25")).toBeInTheDocument();
      expect(screen.getByText("0.95")).toBeInTheDocument();
      expect(screen.getByText("1.10")).toBeInTheDocument();
      expect(screen.getByText("1.05")).toBeInTheDocument();
      expect(screen.getByText(/Attack Strength:/i)).toBeInTheDocument();
      expect(screen.getByText(/Midfield Strength:/i)).toBeInTheDocument();
      expect(screen.getByText(/Defense Strength:/i)).toBeInTheDocument();
      expect(screen.getByText(/Squad Strength:/i)).toBeInTheDocument();
    });

    global.fetch.mockClear();
  });

  test("renders error state when fetch fails", async () => {
    global.fetch = jest.fn(() => Promise.resolve({ ok: false }));

    render(
      <MemoryRouter initialEntries={["/team/Spurs"]}>
        <Routes>
          <Route path="/team/:teamName" element={<TeamStrength />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/error:/i)).toBeInTheDocument();
    });

    global.fetch.mockClear();
  });

  test("renders no data when API returns null", async () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(null),
      })
    );

    render(
      <MemoryRouter initialEntries={["/team/Chelsea"]}>
        <Routes>
          <Route path="/team/:teamName" element={<TeamStrength />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/no strength data found/i)).toBeInTheDocument();
    });

    global.fetch.mockClear();
  });

  test("renders team strength with correct formatting", async () => {
    const mockData = {
      teamName: "Liverpool",
      attackStrength: 1.25,
      midfieldStrength: 0.95,
      defenseStrength: 1.10,
      squadStrength: 1.05,
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockData),
      })
    );

    render(
      <MemoryRouter initialEntries={["/team/Liverpool"]}>
        <Routes>
          <Route path="/team/:teamName" element={<TeamStrength />} />
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      const paragraphs = screen.getAllByText((content, element) => {
        return element.tagName.toLowerCase() === "p";
      });

      const attackParagraph = paragraphs.find((p) =>
        p.textContent.includes("Attack Strength:")
      );
      expect(attackParagraph).toHaveTextContent("Attack Strength: 1.25");

      const midfieldParagraph = paragraphs.find((p) =>
        p.textContent.includes("Midfield Strength:")
      );
      expect(midfieldParagraph).toHaveTextContent("Midfield Strength: 0.95");

      const defenseParagraph = paragraphs.find((p) =>
        p.textContent.includes("Defense Strength:")
      );
      expect(defenseParagraph).toHaveTextContent("Defense Strength: 1.10");

      const squadParagraph = paragraphs.find((p) =>
        p.textContent.includes("Squad Strength:")
      );
      expect(squadParagraph).toHaveTextContent("Squad Strength: 1.05");
    });

    global.fetch.mockClear();
  });
});