"use client";
// Note: Due to way its handling displaying the interests when a custom interst is removed it does not reflect that it has existed since its not present in the "interestLists" 
import { NextPage } from 'next';
import Head from 'next/head';
import Image from 'next/image';
import { useState, useEffect, useRef, useLayoutEffect } from 'react';
import axios from 'axios';
import DefaultPfp from "./buffpfp.webp";
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
    const [password, setPassword] = useState<string>('');
    const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
    const [showFoodSubInterests, setShowFoodSubInterests] = useState<boolean>(false);
    const [profilePicture, setProfilePicture] = useState<File | null>(null);
    const [profilePictureUrl, setProfilePictureUrl] = useState<string>(DefaultPfp.src);
    const [isPopupVisible, setIsPopupVisible] = useState<boolean>(false);
    const [initialState, setInitialState] = useState<any>(null);
    const [isDirty, setIsDirty] = useState<boolean>(false);
    const router = useRouter();
    const fileInputRef = useRef<HTMLInputElement | null>(null);

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
                userData.preferences?.interests.forEach((pref: string) => {
                    interestsList.push(pref)

                })
                const userInitialState = {
                    name: userData.name || '',
                    username: userData.username || '',
                    email: userData.credentials?.email || '',
                    dob: userData.dateOfBirth || '',
                    password: userData.credentials?.password || '',
                    profilePictureUrl: userData.profilePictureUrl || DefaultPfp,
                    selectedInterests: userData.preferences?.interests || []
                };

                setName(userInitialState.name);
                setUsername(userInitialState.username);
                setEmail(userInitialState.email);
                setDob(userInitialState.dob);
                setPassword(userInitialState.password);
                setProfilePictureUrl(userInitialState.profilePictureUrl);
                setSelectedInterests(userInitialState.selectedInterests);
                setShowFoodSubInterests(userInitialState.selectedInterests.includes('Food'));
                setInitialState(userInitialState);
            } catch (error) {
                console.error('Error fetching user data:', error);
                alert('Failed to fetch user data. Please try again.');
            }
        };

        fetchUserData();
    }, []);

    useEffect(() => {
        if (initialState) {
            if (
                name !== initialState.name ||
                username !== initialState.username ||
                email !== initialState.email ||
                dob !== initialState.dob ||
                password !== initialState.password ||
                profilePictureUrl !== initialState.profilePictureUrl ||
                JSON.stringify(selectedInterests) !== JSON.stringify(initialState.selectedInterests)
            ) {
                setIsDirty(true);
            } else {
                setIsDirty(false);
            }
        }
    }, [name, username, email, dob, password, profilePictureUrl, selectedInterests, initialState]);

    const handleProfilePictureChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const newProfilePicture = e.target.files[0];
            setProfilePicture(newProfilePicture);
            setProfilePictureUrl(URL.createObjectURL(newProfilePicture));
            setIsDirty(true);
        }
    }

    const handleSave = async () => {
        try {
            const token = cookies.authToken.get();
            if (!token) {
                throw new Error("No auth token found.");
            }
    
            let profilePictureUploadUrl = profilePictureUrl;

            if (profilePicture) {
                const formData = new FormData();
                formData.append('file', profilePicture);

                const uploadResponse = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/user-access/profile/upload`, formData, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                });

                profilePictureUploadUrl = uploadResponse.data;  
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
                },
                profilePictureUrl: profilePictureUploadUrl,
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
            setInitialState(updatedUserData);
            setIsDirty(false);
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

    const handleProfilePictureClick = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    return (
        <>
            <Head>
                <title>Your profile</title>
                <meta name="description" content="Your account page" />
            </Head>

            <div className="min-h-screen bg-blue-100 flex items-center justify-center w-full h-screen">
                <div className='box'>
                    <div className='wave -one'></div>
                    <div className='wave -two'></div>
                    <div className='wave -three'></div>
                </div>
                <div className="w-full max-w-md bg-white shadow-md rounded-lg p-6 z-10">
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
                                className={`text-white-500 rounded-md px-3 py-1 ${isDirty ? 'font-bold' : 'bg-transparent'}`}
                                onClick={handleSave}
                                type="button"
                                disabled={!isDirty}
                            >
                                Save
                            </button>
                        </div>
                    </div>

                    <div className="flex items-center float-left mb-30">
                        <div
                            onClick={handleProfilePictureClick}
                            className="cursor-pointer"
                        >
                            <Image
                                src={profilePictureUrl}
                                alt="Profile Picture"
                                width={70}
                                height={70}
                                className="rounded-full"
                            />
                        </div>
                        <input
                            type='file'
                            accept="image/*"
                            className='absolute flex left-0 top-0 opacity-0'
                            onChange={handleProfilePictureChange}
                            ref={fileInputRef}
                        />
                    </div>
                    <form className="space-y-4">
                        <div className='ml-20'>
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
                                    readOnly
                                />
                            </div>
                        </div>
                        <div className='flex'>
                            <div className='w-full'>
                                <label className='block text-sm font-medium text-gray-700'>Email</label>
                                <input
                                    className="mt-1 block w-full px-3 py-2 border border-gray-300 text-gray-500 rounded-md shadow-sm focus:outline-none focus:ring-1 focus:ring-indigo-500"
                                    type="email"
                                    name='email'
                                    id="email"
                                    value={email}
                                    readOnly
                                />
                            </div>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-600">Interests</label>
                            <div className="flex flex-wrap gap-2">
                                { Array.from(new Set([...interestsList,...selectedInterests])).map((interest) => (
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

                    <div className='flex float-right'>
                        <button
                            onClick={handleDelete}
                            className="mt-4 bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600"
                        >
                            Delete Account
                        </button>
                    </div>

                    <Popup
                        visible={isPopupVisible}
                        togglePopup={togglePopup}
                        onConfirm={handleConfirmDelete}
                        message="Are you sure you want to delete your profile? This action cannot be undone."
                    />
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
                        to { transform: rotate(360deg); }
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
        </>
    );
};

export default AccountPage;
