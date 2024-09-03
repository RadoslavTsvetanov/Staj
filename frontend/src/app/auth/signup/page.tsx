"use client";

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import WaveBackground from '@/components/ui/WaveBackground';
import CanvasWrapper from '@/components/ui/CanvasWrapper';
import FormField from '@/components/ui/SignUp/FormField';
import PasswordField from '@/components/ui/SignUp/PasswordField';

export default function SignUpRoute() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const router = useRouter();

  // useEffect(() => {
  //   document.body.style.overflow = 'hidden';
  //   document.documentElement.style.overflow = 'hidden';

  //   return () => {
  //     document.body.style.overflow = '';
  //     document.documentElement.style.overflow = '';
  //   };
  // }, []);

  const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(event.target.value);
  };

  const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(event.target.value);
  };

  const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(event.target.value);
  };

  const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setConfirmPassword(event.target.value);
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    
    if (username.length < 5 || username.length > 20) {
      setError("Username should be between 5 and 20 characters long");
      return;
    }
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    if (password.length < 8) {
      setError("Password should be at least 8 characters long");
      return;
    }
    setError('');

    console.log(username, " ", email, " ", password);

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/auth/register/basic`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          username,
          email,
          password,
        }),
      });

      console.log(response);

      if (response.ok) {
        router.push(`/auth/signup/info?username=${encodeURIComponent(username)}`);
      } else {
        const data = await response.text();
        setError(data); // Display the error message from the backend
        console.log(data);
        console.error('Error:', data);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-blue-100 w-full h-full">
      <WaveBackground />
      <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form onSubmit={handleSubmit}>
            <CanvasWrapper />

            <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
              Sign up
            </h2>

            <FormField
              label="Username"
              type="text"
              name="username"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required={true}
              autoComplete="username"
            />

            <FormField
              label="Email address"
              type="email"
              name="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required={true}
              autoComplete="email"
            />

            <PasswordField
              label="Password"
              name="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <PasswordField
              label="Confirm Password"
              name="confirm-password"
              id="confirm-password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />

            {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

            <div className="mt-4 flex items-center justify-between">
              <div className="flex items-center">
                <input
                  className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  required
                  type="checkbox"
                  name="terms-and-condition"
                  id="terms-and-condition"
                />
                <label className="ml-2 block text-sm text-gray-600">
                  I agree to the {''}
                  <a
                    href="./signup/tandc"
                    className="font-medium text-indigo-600 hover:text-indigo-500"
                  >
                   terms and conditions
                  </a>
                </label>
              </div>
            </div>

            <div className="mt-4">
              <button
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                type="submit"
              >
                Next
              </button>
            </div>

            <div className="mt-4 flex items-center justify-between">
              <div className="flex items-center">
                <label className="ml-2 block text-sm text-gray-600">
                  Already have an account? {''}
                  <a
                    href="./signin"
                    className="font-medium text-indigo-600 hover:text-indigo-500"
                  >
                   Log in!
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
