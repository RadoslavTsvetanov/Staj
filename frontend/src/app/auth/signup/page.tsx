"use client";

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import Earth from '../../../../public/Earth';

export default function SignUpRoute() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  const router = useRouter();

  useEffect(() => {
    document.body.style.overflow = 'hidden';
    document.documentElement.style.overflow = 'hidden';

    return () => {
      document.body.style.overflow = '';
      document.documentElement.style.overflow = '';
    };
  }, []);

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
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    setError('');

    console.log(username, " ", email, " ", password);

    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/register/basic`, {
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
      console.log()

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
    <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-[#0e6cc4] w-full h-full">
      <div className='box'>
        <div className='wave -one'></div>
        <div className='wave -two'></div>
        <div className='wave -three'></div>
      </div>
      
      <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form onSubmit={handleSubmit}>
            <div>
              <Canvas style={{ height: '150px'}}>
                <ambientLight intensity={1.5} />
                <directionalLight position={[10, 10, 5]} intensity={2} />
                <Earth />
                <OrbitControls />
              </Canvas>
              <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                Sign up
              </h2>
            </div>
            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700" htmlFor="username">
                Username
              </label>
              <div className="mt-1">
                <input
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                  autoComplete="username"
                  type="text"
                  name="username"
                  id="username"
                  value={username}
                  onChange={handleUsernameChange}
                />
              </div>
            </div>

            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700" htmlFor="email">
                Email address
              </label>
              <div className="mt-1">
                <input
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                  autoComplete="email"
                  type="email"
                  name="email"
                  id="email"
                  value={email}
                  onChange={handleEmailChange}
                />
              </div>
            </div>

            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700" htmlFor="password">
                Password
              </label>
              <div className="mt-1">
                <input
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                  autoComplete="current-password"
                  type="password"
                  name="password"
                  id="password"
                  value={password}
                  onChange={handlePasswordChange}
                />
              </div>
            </div>

            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700" htmlFor="confirm-password">
                Confirm Password
              </label>
              <div className="mt-1">
                <input
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  required
                  autoComplete="current-password"
                  type="password"
                  name="confirm-password"
                  id="confirm-password"
                  value={confirmPassword}
                  onChange={handleConfirmPasswordChange}
                />
              </div>
            </div>

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

      <style jsx>{`
        .bg-main-bg {
          background-color: #0e6cc4;
        }

        .box {
          position: fixed;
          top: 0;
          transform: rotate(80deg);
          left: 0;
        }

        .wave {
          position: fixed;
          top: 0;
          left: 0;
          opacity: .4;
          position: absolute;
          top: 3%;
          left: 10%;
          background: #0af;
          width: 1500px;
          height: 1300px;
          margin-left: -150px;
          margin-top: -250px;
          transform-origin: 50% 48%;
          border-radius: 43%;
          animation: drift 7000ms infinite linear;
        }

        .wave.-three {
          animation: drift 7500ms infinite linear;
          position: fixed;
          background-color: #77daff;
        }

        .wave.-two {
          animation: drift 3000ms infinite linear;
          opacity: .1;
          background: black;
          position: fixed;
        }

        .box:after {
          content: '';
          display: block;
          left: 0;
          top: 0;
          width: 100%;
          height: 100%;
          z-index: 11;
          transform: translate3d(0, 0, 0);
        }

        @keyframes drift {
          from { transform: rotate(0deg); }
          from { transform: rotate(360deg); }
        }

        .contain {
          animation-delay: 4s;
          z-index: 1000;
          position: fixed;
          top: 0;
          left: 0;
          bottom: 0;
          right: 0;
          display: flex;
          justify-content: center;
          align-items: center;
          background: #25a7d7;
          background: linear-gradient(#25a7d7, #25a7d7);
        }

        .icon {
          width: 100px;
          height: 100px;
          margin: 0 5px;
        }

        .icon:nth-child(2) img { animation-delay: 0.2s; }
        .icon:nth-child(3) img { animation-delay: 0.3s; }
        .icon:nth-child(4) img { animation-delay: 0.4s; }

        .icon img {
          animation: anim 2s ease infinite;
          transform: scale(0,0) rotateZ(180deg);
        }

        @keyframes anim {
          0% {
            transform: scale(0,0) rotateZ(-90deg); opacity:0;
          }
          30% {
            transform: scale(1,1) rotateZ(0deg); opacity:1;
          }
          50% {
            transform: scale(1,1) rotateZ(0deg); opacity:1;
          }
          80% {
            transform: scale(0,0) rotateZ(90deg); opacity:0;
          }
        }
      `}</style>
    </div>
  );
}
