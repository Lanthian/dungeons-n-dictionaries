// features/root/pages/HomePage.tsx
import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function HomePage() {
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

  const [data, setData] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Links to navigate to
  const links = [
      { path: '/asm', label: 'Ability Score Modifiers' },
      { path: '/feat', label: 'Feats' },
      { path: '/language', label: 'Languages' },
      { path: '/proficiency', label: 'Proficiencies' },
  ]

  // Test API Connection
  const fetchData = async () => {
    setLoading(true);
    setData(null);
    try {
      const res = await fetch(`${API_BASE_URL}/test`);
      if (res.status > 299) {
        setData(`Bad response status ${res.status} from API`);
        return;
      }
      setData(await res.text());
    } catch (e) {
      setData(`Error occurred: ${e}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Header */}
      <h1>Home Page</h1>

      {/* API connection test */}
      <div>
        {data && <p>{data}</p>}
        {loading && "loading..."}
        <button
          disabled={loading}
          type="button"
          onClick={fetchData}
        >
          Ping Server
        </button>
      </div>

      <p></p>

      {/* Available navigation menu */}
      {links.map((link) => (
        <>
          <Link
            key={link.path}
            to={link.path}
          >
            {link.label}
          </Link>
          <br />
        </>
      ))}
    </div>
  );
}
