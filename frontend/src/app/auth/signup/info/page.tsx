"use client";

import React, { useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

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
];

const foodSubInterests = [
    "Cafe",
    "Vegan",
    "Fast food",
    "Bar",
    "Bakery",
    "Desserts"
];

const InfoRoute: React.FC = () => {
    const [selectedInterests, setSelectedInterests] = useState<string[]>([]);
    const [showFoodSubInterests, setShowFoodSubInterests] = useState(false);
    const [showOtherInput, setShowOtherInput] = useState(false);
    const [otherInterest, setOtherInterest] = useState('');
    const [name, setName] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [error, setError] = useState('');
    const router = useRouter();
    const searchParams = useSearchParams();
    const username = searchParams.get('username') || '';

    const toggleInterest = (event: React.MouseEvent<HTMLButtonElement>, interest: string) => {
        event.preventDefault(); // Prevent form submission

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

    const addOtherInterest = async () => {
        if(otherInterest) {
            try {
                const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/match-interest`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        customInterest: otherInterest
                    })
                });
        
                const matchedInterests = await response.json();
        
                setSelectedInterests([...selectedInterests, ...matchedInterests]);
            } catch (err) {
                console.error('Error matching interest:', err);
            }
            setShowOtherInput(false);
            setOtherInterest('');
        }
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/auth/register/complete`, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              },
              body: new URLSearchParams({
                username,
                name,
                dateOfBirth,
                interests: selectedInterests.join(','),
              }),
            });

            if (response.ok) {
              alert('Registration complete');
              router.push('../signin');
            } else {
              const data = await response.text();
              setError(data);
            }
        } catch (err) {
            setError('An error occurred while completing registration. Please try again.');
        }
    };

    return (
        <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-[#0e6cc4] w-full h-screen">
            {/* Wave animation */}
            <div className='box'>
                <div className='wave -one'></div>
                <div className='wave -two'></div>
                <div className='wave -three'></div>
            </div>

            <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
                <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                    <form onSubmit={handleSubmit}>
                        <h1 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                            Let's get to know you!
                        </h1>

                        <div className="mt-4">
                            <label className="mt-6">Name</label>
                            <div className="block text-sm font-medium text-gray-700">
                                <input
                                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    required
                                    type="text"
                                    name="name"
                                    id="name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="mt-4">
                            <label className="block text-sm font-medium text-gray-700" htmlFor="dob">
                                Date of Birth
                            </label>
                            <div className="mt-1">
                                <input
                                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                    required
                                    type="date"
                                    name="dob"
                                    id="dob"
                                    value={dateOfBirth}
                                    onChange={(e) => setDateOfBirth(e.target.value)}
                                />
                            </div>
                        </div>

                        <div className="mt-4">
                            <label className='block text-sm font-medium text-gray-700'>
                                Interests
                            </label>
                            <div className="appearance-none w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm flex flex-wrap gap-2">
                                {interestsList.map((interest) => (
                                    <button
                                        key={interest}
                                        onClick={(event) => toggleInterest(event, interest)}
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
                                        onClick={(event) => toggleInterest(event, subInterest)}
                                        className={`px-3 py-1 rounded-lg border 
                                    ${selectedInterests.includes(subInterest) ? "bg-blue-300 text-white" : "bg-gray-200 text-gray-700"}
                                    `}
                                    >
                                        {subInterest}
                                    </button>
                                ))}
                                {showOtherInput ? (
                                    <input
                                        type="text"
                                        value={otherInterest}
                                        onChange={handleOtherInterest}
                                        onBlur={addOtherInterest}
                                        className='px-3 py-1 rounded-lg border bg-gray-200 text-gray-700'
                                        placeholder="Your interest"
                                        autoFocus
                                    />
                                ) : (
                                <button
                                    onClick={() => setShowOtherInput(true)}
                                    className='px-3 py-1 rounded-lg border bg-gray-200 text-gray-700'>
                                    Other
                                </button>
                                )}
                            </div>
                        </div>

                        <div className="mt-4">
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
    );
};

export default InfoRoute;
