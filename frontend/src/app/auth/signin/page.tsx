"use client";

import React, { useState } from 'react';
import { cookies } from '@/lib/utils';
import { useRouter } from 'next/navigation';
import WaveBackground from '@/components/ui/WaveBackground';
import CanvasWrapper from '@/components/ui/CanvasWrapper';
import FormField from '@/components/ui/signIn/FormField';

export default function SignUpRoute() {
  const [error, setError] = useState('');
  const router = useRouter(); // Use the hook inside the component

  const handleSignIn = async (event) => {
    event.preventDefault();

    const email = event.target.email.value;
    const password = event.target.password.value;

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/auth/signin`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        console.error('Sign-in failed:', errorMessage);
        //alert('Failed to sign in. Please check your credentials.');
        setError(errorMessage);
        return;
      }

      const token = await response.text();
      cookies.authToken.set(token);
      console.log('Sign-in successful:', token);

      router.push('../../home');
    } catch (err) {
      console.error('Network error:', err);
      alert('Network error. Please try again later.');
    }
  };

  return (
    <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-blue-100 w-full h-screen">
      <WaveBackground />
      <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form method="POST" action="#" onSubmit={handleSignIn}>
            <div>
              <CanvasWrapper />
              <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                Sign in
              </h2>
            </div>

            <FormField
              label="Email address"
              type="email"
              name="email"
              id="email"
              required={true}
              autoComplete="email"
            />

            <FormField
              label="Password"
              type="password"
              name="password"
              id="password"
              required={true}
              autoComplete="current-password"
            />

            <div className="mt-4 flex items-center justify-between">
              <div className="flex items-center">
                <input
                  className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  type="checkbox"
                  name="remember-me"
                  id="remember-me"
                />
                <label className="ml-2 block text-sm text-gray-600">
                  Remember me
                </label>
              </div>
            </div>

            {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

            <div className="mt-4">
              <button
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                type="submit"
              >
                Sign in
              </button>
            </div>

            <div className="mt-4 flex items-center justify-between">
              <div className="flex items-center">
                <label className="ml-2 block text-sm text-gray-600">
                  Don't have an account? {''}
                  <a
                  href="./signup"
                  className="font-medium text-indigo-600 hover:text-indigo-500"
                  >
                   Create one!
                  </a>
                </label>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}