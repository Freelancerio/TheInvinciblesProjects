
// The root component of the application.
// It imports and renders Header and Footer, and provides a basic structure for the page.
// All other pages/components will be added into this component later.

import React from 'react';
import Header from './components/Header';
import Footer from './components/Footer';

function App() {
  return (
    <div>
      <Header />
      <h1>Hello from React App </h1>
      <Footer />
    </div>
  );
}

export default App;
