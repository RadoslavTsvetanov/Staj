"use client";

import React, { useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import WaveBackground from '@/components/ui/WaveBackground';
import FormField from '@/components/ui/SignUp/info/FormField';
import InterestList from '@/components/ui/interests/InterestList';
import SubInterestList from '@/components/ui/interests/SubInterestList';

const interestsList = [
    "Art", "Sports", "Books", "Education", "Entertainment", "Hiking", "History",
    "Movies", "Theater", "Animals", "Shopping", "Relax", "Religion", "Flora", "Food"
];

const foodSubInterests = [
    "Cafe", "Vegan", "Fast food", "Bar", "Bakery", "Desserts"
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

    const handleOtherInterestChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setOtherInterest(event.currentTarget.value);
    }

    const addOtherInterest = async () => {
        if(otherInterest) {
            setSelectedInterests([...selectedInterests, otherInterest]);
            try {
                const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/interests/process`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        customInterest: otherInterest
                    })
                });
                if (!response.ok) {
                    throw new Error('Failed to match interest');
                }
                const matchedInterests = await response.json();
                setSelectedInterests([...selectedInterests, ...matchedInterests]);
            } catch (err) {
                console.error('Error matching interest:', err);
                setError('Failed to add interest');
            }
            setShowOtherInput(false);
            setOtherInterest('');
        }
    };

    const calculateAge = (dob: string) => {
        const birthDate = new Date(dob);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDifference = today.getMonth() - birthDate.getMonth();

        if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        return age;
    };

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
debugger
        if (name.length < 2 || name.length > 40) {
            setError('Name should be between 2 and 40 characters long');
            return;
        }
        const age = calculateAge(dateOfBirth);
        if (age < 13 && age >= 0) {
            setError('Womp womp too young');
            return;
        }
        if (age < 0) {
            setError("You aren't born yet?");
            return;
        }
        if (age > 100) {
            setError('Womp womp too old');
            return;
        }
        if (selectedInterests.length === 0) {
            setError('Please select at least one interest');
            return;
        }
        setError('');

        try {
            const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/auth/register/complete`, {
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
            console.error('Error:', err);
            setError('Failed to complete registration');
        }
    };

    return (
        <div className="relative flex flex-col justify-center py-12 sm:px-6 lg:px-8 bg-blue-100 w-full h-screen">
            <WaveBackground />
            <div className="mt-4 sm:mx-auto sm:w-full sm:max-w-md z-10">
                <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
                    <form onSubmit={handleSubmit}>
                        <h1 className="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
                            Let's get to know you!
                        </h1>

                        <FormField
                            label="Name"
                            type="text"
                            name="name"
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />

                        <FormField
                            label="Date of Birth"
                            type="date"
                            name="dob"
                            id="dob"
                            value={dateOfBirth}
                            onChange={(e) => setDateOfBirth(e.target.value)}
                            required
                        />

                        <InterestList
                            label="Interests"
                            interests={interestsList}
                            selectedInterests={selectedInterests}
                            toggleInterest={toggleInterest}
                            onShowOtherInput={() => setShowOtherInput(true)}
                            showOtherInput={showOtherInput}
                            otherInterest={otherInterest}
                            onOtherInterestChange={handleOtherInterestChange}
                            onAddOtherInterest={addOtherInterest}
                        />

                        {showFoodSubInterests && (
                            <SubInterestList
                                label='Food Sub-Interests'
                                interests={foodSubInterests}
                                selectedInterests={selectedInterests}
                                toggleInterest={toggleInterest}
                            />
                        )}

                        {error && (
                            <p className={`mt-2 text-sm ${error === 'Womp womp too old' ? "text-xl text-red-600" : "text-red-500"}`}>
                                {error}
                            </p>
                        )}

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
        </div>
    );
};

export default InfoRoute;
