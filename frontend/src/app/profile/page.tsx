"use client";

import { NextPage } from 'next';
import Head from 'next/head';
import Image from 'next/image';
import { useState, useEffect } from 'react';
import  Charles  from "./charles-leclerc-ferrari.jpg";
import LArrow from "./left.png";
import { Popup } from "../../components/ui/Popup";
import axios from "axios";

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
  const [name, setName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [dob, setDob] = useState('');
  
  const [isPopupVisible, setIsPopupVisible] = useState(false);

  useEffect(() => {
    const getName = () => {
        // const data = axios.get()
    }
  }, [])

  const handleDelete = () => {
    setIsPopupVisible(true);
  };

  const handleSave = () => {
    //handle save
  };

  const togglePopup = () => {
    setIsPopupVisible(!isPopupVisible);
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
          <div className="flex items-center justify-center mb-4 relative">
            <div className="absolute left-0 max-w-7">
              <Image
                src={LArrow}
                alt="Left arrow"
                onClick={() => history.back()}
                className='cursor-pointer'
              />
            </div>
            <h1 className="text-2xl font-semibold text-gray-700">
              My profile
            </h1>
            <div className='absolute right-0'>
              <button
                className="text-white-500 bg-green-300 rounded-md px-3 py-1 hover:text-white-700 hover:bg-green-500"
                onClick={handleSave}
                type="submit"
              >
                Save
              </button>
            </div>
          </div>

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
              onClick={handleDelete}
              className="w-full bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
            >
              Delete profile
            </button>
          </form>
        </div>
      </div>
      {isPopupVisible && <Popup togglePopup={togglePopup} />}
    </>
  );
};

export default AccountPage;
