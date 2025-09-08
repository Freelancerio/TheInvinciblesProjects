// This is the entry point of the React app.
// It mounts the <App /> component into the "root" div inside public/index.html.
// React.StrictMode is used to highlight potential issues during development.
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './styles/global.css';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);
