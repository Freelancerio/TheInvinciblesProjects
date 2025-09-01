import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import StatCard from "../Components/StatsCard.jsx"; // path matches your structure

describe("StatCard", () => {
  test("renders title and value", () => {
    render(<StatCard title="Total Bets" value="123" />);
    expect(screen.getByText("Total Bets")).toBeInTheDocument();
    expect(screen.getByText("123")).toBeInTheDocument();
  });

  test("has base card styles applied inline (outer card div)", () => {
    render(<StatCard title="Users" value="42" />);

    // Structure in component:
    // OUTER <div style=card>
    //   INNER <div style={{ display: 'flex', flexDirection: 'column' }}>
    //     <h3>{title}</h3>
    //     <div>{value}</div>
    //   </div>
    // </div>
    const titleEl = screen.getByText("Users");       // <h3>
    const inner = titleEl.closest("div");            // inner wrapper
    const outerCard = inner && inner.parentElement;  // outer card

    expect(outerCard).toBeInTheDocument();
    expect(outerCard).toHaveStyle({
      backgroundColor: "white",
      borderRadius: "8px",
      border: "1px solid #ebe7f3",
      padding: "24px",
      minWidth: "200px",
    });
  });

  test("hover adds elevation and translateY; mouse out resets", () => {
    render(<StatCard title="Sessions" value="99" />);
    const titleEl = screen.getByText("Sessions");
    const inner = titleEl.closest("div");
    const outerCard = inner && inner.parentElement;

    expect(outerCard).toBeInTheDocument();

    // Initial state: component sets an initial boxShadow, but NOT transform.
    expect(outerCard).toHaveStyle({
      boxShadow: "0 1px 3px rgba(0, 0, 0, 0.1)",
    });

    // Hover
    fireEvent.mouseOver(outerCard);
    expect(outerCard).toHaveStyle({
      boxShadow: "0 4px 12px rgba(101, 78, 151, 0.15)",
      transform: "translateY(-2px)",
    });

    // Mouse out -> component explicitly resets both shadow and transform
    fireEvent.mouseOut(outerCard);
    expect(outerCard).toHaveStyle({
      boxShadow: "0 1px 3px rgba(0, 0, 0, 0.1)",
      transform: "translateY(0)",
    });
  });

  test("supports flexible width for layout rows (flex contains '1')", () => {
    render(<StatCard title="Flex" value="yes" />);
    const titleEl = screen.getByText("Flex");
    const inner = titleEl.closest("div");
    const outerCard = inner && inner.parentElement;

    expect(outerCard).toBeInTheDocument();

    // JSDOM can normalize `flex: 1` as `1 1 0%`, so just assert it contains "1"
    expect(outerCard.style.flex).toContain("1");
  });
});
