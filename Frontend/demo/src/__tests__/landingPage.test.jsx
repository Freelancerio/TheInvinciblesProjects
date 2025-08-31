import React from "react";
import { render, screen } from "@testing-library/react";
import Greeting from "../Components/landingPage.jsx";

test("renders greeting text", () => {
  render(<Greeting />);
  expect(screen.getByRole("heading", { name: /hello, welcome to smartbet!/i })).toBeInTheDocument();
  expect(screen.getByText(/this is a stat betting app/i)).toBeInTheDocument();
});
