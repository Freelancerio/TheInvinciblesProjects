import React from "react";
import { render, screen, within } from "@testing-library/react";
import "@testing-library/jest-dom";
import Section from "../Components/Section.jsx"; // adjust the path if needed

describe("Section", () => {
  test("renders the title in an h2", () => {
    render(<Section title="Overview">Body</Section>);
    const heading = screen.getByRole("heading", { level: 2, name: "Overview" });
    expect(heading).toBeInTheDocument();
  });

  test("renders children inside the content area", () => {
    render(
      <Section title="Details">
        <p data-testid="child">Hello child</p>
      </Section>
    );
    expect(screen.getByTestId("child")).toHaveTextContent("Hello child");
  });

  test("applies base container styles", () => {
    render(<Section title="Styled">x</Section>);
    // outermost container is the first wrapping div
    const outer = screen.getByRole("heading", { level: 2, name: "Styled" }).closest("div").parentElement;
    expect(outer).toHaveStyle({
      backgroundColor: "white",
      borderRadius: "8px",
      border: "1px solid #ebe7f3",
      boxShadow: "0 1px 3px rgba(0, 0, 0, 0.1)",
      marginBottom: "24px",
      overflow: "hidden",
    });
  });

  test("header row has divider, padding, and background color", () => {
    render(<Section title="Header Check">content</Section>);
    const heading = screen.getByRole("heading", { level: 2, name: "Header Check" });
    const headerRow = heading.parentElement; // the div wrapping the h2
    expect(headerRow).toHaveStyle({
      borderBottom: "1px solid #ebe7f3",
      padding: "16px 24px",
      backgroundColor: "#faf9fc",
    });
  });

  test("content area wraps children with padding", () => {
    render(
      <Section title="Content Padding">
        <div data-testid="inside">stuff</div>
      </Section>
    );
    // outer -> [header, content]; find the content div by padding style
    const heading = screen.getByRole("heading", { level: 2, name: "Content Padding" });
    const container = heading.closest("div").parentElement; // outer
    const contentArea = Array.from(container.children).find(
      (el) => el !== container.firstChild && el.tagName.toLowerCase() === "div" && el.style.padding === "24px"
    );
    expect(contentArea).toBeTruthy();
    expect(within(contentArea).getByTestId("inside")).toBeInTheDocument();
  });

  test("does not mutate children structure", () => {
    render(
      <Section title="Structure">
        <ul>
          <li>one</li>
          <li>two</li>
        </ul>
      </Section>
    );
    const list = screen.getByRole("list");
    const items = screen.getAllByRole("listitem");
    expect(list).toBeInTheDocument();
    expect(items.map((li) => li.textContent)).toEqual(["one", "two"]);
  });
});
