// App.tsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import HomePage from './features/root/pages/HomePage';
import AsmPage from './features/asm/pages/AsmPage';
import FeatPage from './features/feat/pages/FeatPage';
import LanguagePage from './features/language/pages/LanguagePage';
import ProficiencyPage from './features/proficiency/pages/ProficiencyPage';

function App() {
  return (
    <>
      <Toaster />
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/asm" element={<AsmPage />} />
          <Route path="/feat" element={<FeatPage />} />
          <Route path="/language" element={<LanguagePage />} />
          <Route path="/proficiency" element={<ProficiencyPage />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
