"use client";

import React from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import Earth from '../../../../public/Earth';

export default function SignUpRoute() {
  return (
    <div className="flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          <form method="POST" action="#">
            <div>
              <Canvas style={{ height: '200px'}}>
                <ambientLight intensity={0.5} />
                <directionalLight position={[10, 10, 5]} intensity={1} />
                <Earth />
                <OrbitControls />
              </Canvas>
              <h2 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                Sign up
              </h2>
            </div>
            <div>
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
                />
              </div>
            </div>

            <div className="mt-6">
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
                />
              </div>
            </div>

            <div className="mt-6">
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
                />
              </div>
            </div>

            <div className="mt-6">
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
                />
              </div>
            </div>

            <div className="mt-6 flex items-center justify-between">
              <div className="flex items-center">
                <input
                  className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  type="checkbox"
                  name="terms-and-condition"
                  id="terms-and-condition"
                />
                <label className="mt-2 text-center text-sm text-gray-600">
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

            <div className="mt-6">
              <button
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                type="submit"
              >
                Sign up
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
