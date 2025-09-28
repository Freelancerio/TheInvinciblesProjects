import { render, cleanup } from "@testing-library/react";
import App from "../App";

afterEach(cleanup);

test("App renders without crashing", () => {
  expect(() => {
    render(<App />);
  }).not.toThrow();
});

test("App mounts some content to the DOM", () => {
  const { container } = render(<App />);
  expect(container.firstChild).toBeTruthy();
});