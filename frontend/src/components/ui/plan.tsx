import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';

interface PlanProps {
    name: string;
    isEditable: boolean;
};

const Plan: React.FC<PlanProps> = ({ name, isEditable }) => {
    const [isHovered, setIsHovered] = useState(false);
    const router = useRouter();
    
    const handleClickHistory = () => {
        const query = new URLSearchParams({ name }).toString();
        router.push(`/history?${query}`);
    };

    const handleClickPlanning = () => {
        const query = new URLSearchParams({ name }).toString();
        router.push(`/planning?${query}`);
    };

    const handleClick = () => {
        if (isEditable) {
            handleClickPlanning();
        } else {
            handleClickHistory();
        }
    };

    return (
        <button
            className={`w-full flex justify-between items-center p-2.5 rounded-lg border border-black cursor-pointer shadow-md 
                ${isEditable ? 'bg-[#C2E6F4]' : 'bg-[#F58F92]'} 
                ${isHovered ? 'text-black' : 'text-black'}`}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            onClick={handleClick}
        >
            <span>{name}</span>
            {isHovered && (
                isEditable ? (
                    <Image className="ml-2" src="/images/edit.png" alt="edit" width={20} height={20} />
                ) : (
                    <span className="ml-2">&#x25B6;</span>
                )
            )}
        </button>
    );
};

export default Plan;
