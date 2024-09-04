import React from 'react';

interface InterestButtonProps {
  interest: string;
  isSelected: boolean;
  onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

const InterestButton: React.FC<InterestButtonProps> = ({ interest, isSelected, onClick }) => (
  <button
    onClick={onClick}
    className={`px-3 py-1 rounded-lg border ${isSelected ? "bg-blue-300 text-white" : "bg-gray-200 text-gray-700"}`}
  >
    {interest}
  </button>
);

export default InterestButton;
