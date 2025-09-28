
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#38003c',
        secondary: '#00ff85',
        accent: '#e90052',
        light: '#f5f5f5',
        dark: '#1a1a1a',
        gray: '#717171',
      },
    },
  },
  plugins: [],
}
