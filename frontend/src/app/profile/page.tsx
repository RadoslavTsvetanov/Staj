"use client";

import { NextPage } from 'next';
import Head from 'next/head';
import Image from 'next/image';
import { useState } from 'react';
import  Charles  from "./charles-leclerc-ferrari.jpg"

const interestsList = [
    "Art",
    "Sports",
    "Books",
    "Education",
    "Entertainment",
    "Hiking",
    "History",
    "Movies",
    "Theater",
    "Animals",
    "Shopping",
    "Relax",
    "Religion",
    "Flora",
    "Food"
]

const foodSubInterests = [
    "Cafe",
    "Vegan",
    "Fast food",
    "Bar",
    "Bakery",
    "Deserts"
]

const AccountPage: NextPage = () => {
  const [name, setName] = useState('Charles Leclerc');
  const [username, setUsername] = useState('charles_leclerc');
  const [email, setEmail] = useState('charles@example.com');
  const [dob, setDob] = useState('');
  
  const handleSave = () => {
    // Handle save logic here
    alert('Profile updated!');
  };

  const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
    const [showFoodSubInterests, setShowFoodSubInterests] = useState(false);

    const toggleInterest = (interest: string) => {
        if (selectedInterests.includes(interest)) {
          setSelectedInterests(selectedInterests.filter((item) => item !== interest));
          if (interest === "Food") {
            setShowFoodSubInterests(false);
        }
        } else {
          setSelectedInterests([...selectedInterests, interest]);
          if (interest === "Food") {
            setShowFoodSubInterests(true);
        }
        }
      };

  return (
    <>
      <Head>
        <title>Your profile</title>
        <meta name="description" content="Your account page" />
      </Head>

      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <div className="w-full max-w-md bg-white shadow-md rounded-lg p-6">
          <h1 className="text-2xl font-semibold text-gray-700 flex justify-center mb-4">My profile</h1>
          <div className="flex items-center float-left mb-4">
            <Image
              src={Charles}
              alt="Profile Picture"
              width={90}
              height={90}
              className="rounded-full"
            />
          </div>
          <form className="space-y-4">
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-600">Name</label>
              <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="mt-1 block px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
              />
            </div>
            <div className='flex space-x-4'>
              <div className='w-1/2'>
              <label htmlFor="username" className="block text-sm font-medium text-gray-600">Username</label>
              <input
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
                type="text"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
              </div>
              <div className='w-1/2'>
                <label className="block text-sm font-medium text-gray-700" htmlFor="dob">Date of Birth</label>
                <input
                  className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
                  type="date"
                  name="dob"
                  id="dob"
                  onChange={(e) => setDob(e.target.value)}
                />
              </div>
            </div>
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-600">Email</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
              />
            </div>
            <div className="mt-4">
                <label className='block text-sm font-medium text-gray-700'>
                    Interests
                </label>
                <div className="appearance-none w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm flex flex-wrap gap-2">
                    {interestsList.map((interest) => (
                        <button
                            key={interest}
                            onClick={() => toggleInterest(interest)}
                            className={`px-3 py-1 rounded-lg border 
                            ${selectedInterests.includes(interest) ? "bg-blue-300 text-white" : "bg-gray-200 text-gray-700"}
                            `}
                        >
                            {interest}
                        </button>
                    ))}
                    {showFoodSubInterests && foodSubInterests.map((subInterest) => (
                        <button
                            key={subInterest}
                            onClick={() => toggleInterest(subInterest)}
                            className={`px-3 py-1 rounded-lg border 
                        ${selectedInterests.includes(subInterest) ? "bg-blue-300 text-white" : "bg-gray-200 text-gray-700"}
                        `}
                        >
                            {subInterest}
                        </button>
                    ))}
                </div>
            </div>
            <button
              type="button"
              onClick={handleSave}
              className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              Save Changes
            </button>
          </form>
        </div>
      </div>
    </>
  );
};

export default AccountPage;
