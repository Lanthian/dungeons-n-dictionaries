// features/language/pages/LanguagePage.tsx
import { useEffect, useState } from 'react';
import LanguageAPI from '../../../api/language'
import type { Language } from '../types/Language';
import LanguageItem from '../components/LanguageItem';

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
  }, [languages])

  return (
    <div>
      {/* Header */}
      <h1>Languages</h1>

      {/* Languages */}
      {Array.isArray(languages) && languages.length !== 0 ? (
        languages?.map(it => (
          <>
            <LanguageItem
              key={it.id}
              {...it}
            />
            {/* Temporary line break to separate LanguageItems */}
            <br></br>
          </>
        ))
      ) : (
        // Alternative text output if no languages returned
        <p>No languages found</p>
      )}
    </div>
  );
}
