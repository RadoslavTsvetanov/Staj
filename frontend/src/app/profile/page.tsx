"use client";

import { NextPage } from 'next';
import Head from 'next/head';
import Image from 'next/image';
import { useState, useEffect } from 'react';
import axios from 'axios';
import Charles from "./charles-leclerc-ferrari.jpg";
import LArrow from "./left.png";
import { Popup } from "../../components/ui/Popup";
import { useRouter } from 'next/navigation';
import { cookies } from '../../lib/utils';

const interestsList = [
    "Art", "Sports", "Books", "Education", "Entertainment", "Hiking",
    "History", "Movies", "Theater", "Animals", "Shopping", "Relax",
    "Religion", "Flora", "Food"
];

const foodSubInterests = [
    "Cafe", "Vegan", "Fast food", "Bar", "Bakery", "Desserts"
];

const AccountPage: NextPage = () => {
    const [name, setName] = useState<string>('');
    const [username, setUsername] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [dob, setDob] = useState<string>('');
    const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
    const [showFoodSubInterests, setShowFoodSubInterests] = useState<boolean>(false);
    const [isPopupVisible, setIsPopupVisible] = useState<boolean>(false);
    const [password, setPassword] = useState<string>('');
    const router = useRouter();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const token = cookies.authToken.get();
                const profileResponse = await axios.get(`${process.env.NEXT_PUBLIC_API_BASE_URL}/user-access/profile`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const userData = profileResponse.data;

                setName(userData.name || '');
                setUsername(userData.username || '');
                setEmail(userData.credentials?.email || '');
                setDob(userData.dateOfBirth || '');
                setPassword(userData.credentials?.password || '');

                if (userData.preferences && userData.preferences.interests) {
                    setSelectedInterests(userData.preferences.interests);
                    setShowFoodSubInterests(userData.preferences.interests.includes('Food'));
                } else {
                    setSelectedInterests([]);
                    setShowFoodSubInterests(false);
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
                alert('Failed to fetch user data. Please try again.');
            }
        };

        fetchUserData();
    }, []);

    const handleSave = async () => {
        try {
            const token = cookies.authToken.get();
            if (!token) {
                throw new Error("No auth token found.");
            }
    
            const updatedUser = {
                name,
                username,
                dateOfBirth: dob,
                preferences: {
                    interests: selectedInterests
                },
                credentials: {
                    email,
                    password
                }
            };
    
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_API_BASE_URL}/user-access/profile/update`,
                {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updatedUser)
                }
            );
    
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to update profile: ${errorText}`);
            }
    
            const updatedUserData = await response.json();
            console.log('Updated user data:', updatedUserData);
    
            // Update auth token in cookies if provided
            const newToken = response.headers.get('Authorization')?.replace('Bearer ', '');
            if (newToken) {
                cookies.authToken.set(newToken);
                console.log('New token:', newToken);
            }
    
            alert('Profile updated successfully!');
        } catch (error) {
            console.error('Error updating user profile:', error);
            alert('Failed to update profile. Please try again.');
        }
    };
      
    
    const handleDelete = () => {
        setIsPopupVisible(true);
    };

    const handleConfirmDelete = async () => {
        try {
            const token = cookies.authToken.get();
            if (!token) {
                throw new Error("No auth token found.");
            }
    
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/users/profile/delete`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
    
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to delete profile: ${errorText}`);
            }
    
            alert('Profile deleted successfully!');
            cookies.authToken.delete();
            router.push('../auth/signup'); 
        } catch (error) {
            console.error('Error deleting profile:', error);
            alert('Failed to delete profile. Please try again.');
        } finally {
            setIsPopupVisible(false);
        }
    };
    

    const togglePopup = () => {
        setIsPopupVisible(!isPopupVisible);
    };

    const toggleInterest = (event: React.MouseEvent<HTMLButtonElement>, interest: string) => {
        event.preventDefault();

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
                                onClick={() => router.back()}
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
                                type="button"
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
                                    value={dob}
                                    onChange={(e) => setDob(e.target.value)}
                                />
                            </div>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-600">Interests</label>
                            <div className="flex flex-wrap gap-2">
                                {interestsList.map((interest) => (
                                    <button
                                        key={interest}
                                        onClick={(e) => toggleInterest(e, interest)}
                                        className={`px-4 py-2 rounded-md ${
                                            selectedInterests.includes(interest) ? 'bg-blue-500 text-white' : 'bg-gray-200'
                                        }`}
                                    >
                                        {interest}
                                    </button>
                                ))}
                            </div>
                            {showFoodSubInterests && (
                                <div className="mt-4">
                                    <label className="block text-sm font-medium text-gray-600">Food Sub-Interests</label>
                                    <div className="flex flex-wrap gap-2">
                                        {foodSubInterests.map((subInterest) => (
                                            <button
                                                key={subInterest}
                                                onClick={(e) => toggleInterest(e, subInterest)}
                                                className={`px-4 py-2 rounded-md ${
                                                    selectedInterests.includes(subInterest) ? 'bg-blue-500 text-white' : 'bg-gray-200'
                                                }`}
                                            >
                                                {subInterest}
                                            </button>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    </form>

                    <button
                        onClick={handleDelete}
                        className="mt-4 bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600"
                    >
                        Delete Account
                    </button>

                    <Popup
                        visible={isPopupVisible}
                        togglePopup={togglePopup}
                        onConfirm={handleConfirmDelete}
                        message="Are you sure you want to delete your profile? This action cannot be undone."
                    />
                </div>
            </div>
        </>
    );
};

export default AccountPage;