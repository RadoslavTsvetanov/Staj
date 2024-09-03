// components/ApiComponent.tsx
import React, { useState, useEffect } from 'react';

const ApiComponent: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<any>(null);
  const [error, setError] = useState<Error | null>(null);

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await fetch('/interests/process');
      const result = await response.json();
      setData(result);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading) return <div className="loader">Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {data ? <div>{JSON.stringify(data)}</div> : <div>No data available</div>}
    </div>
  );
};

export default ApiComponent;
