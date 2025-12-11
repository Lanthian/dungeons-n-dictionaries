// App.tsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './features/root/pages/HomePage';
import LanguagePage from './features/language/pages/LanguagePage';

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/language" element={<LanguagePage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
