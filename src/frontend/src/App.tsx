import { useState } from 'react';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

function App() {
  const [data, setData] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

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
    <>
      {data && <p>{data}</p>}
      {loading && <p>loading...</p>}
      <button
        disabled={loading}
        type="button"
        onClick={fetchData}
      >
        Ping Server
      </button>
    </>
  );
}

export default App;
