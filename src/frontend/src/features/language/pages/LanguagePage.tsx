// features/language/pages/LanguagePage.tsx
import { useEffect, useState } from 'react';
import LanguageAPI from '../../../api/language'
import type { Language } from '../types/Language';

export default function LanguagePage() {
  const [languages, setLanguages] = useState<Language[] | null>([]);

  // Fetch languages upon navigating to this page
  useEffect(() => {
    async function load() {
      // Fetch current languages
      const data = await LanguageAPI.fetchLanguages();
      setLanguages(data);
    }

    load();
    console.log(languages);
  }, [languages])

  return (
    <div>
      <h1>Languages</h1>
      <p>Work in progress...</p>
    </div>
  );
}
