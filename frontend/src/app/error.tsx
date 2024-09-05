'use client'; // Error boundaries must be Client Components

import { useEffect } from 'react';

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    // Log the error to an error reporting service
    console.error(error);
  }, [error]);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-red-100 text-red-700 p-6 text-center">
      <h2 className="text-3xl font-bold mb-4">Oops! Something went wrong.</h2>
      <p className="text-lg mb-6">
        We're sorry for the inconvenience. Please try refreshing the page or contact support if the issue persists. <br/>(we don't have support for you to contact)
      </p>
      <button
        onClick={reset}
        className="bg-red-600 text-white py-2 px-6 rounded hover:bg-red-700 transition duration-300"
      >
        Try Again
      </button>
    </div>
  );
}
