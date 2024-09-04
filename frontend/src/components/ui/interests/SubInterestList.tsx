import React from 'react';
import InterestButton from './InterestButton';

interface SubInterestListProps {
  label: string;
  interests: string[];
  selectedInterests: string[];
  toggleInterest: (event: React.MouseEvent<HTMLButtonElement>, interest: string) => void;
}

const SubInterestList: React.FC<SubInterestListProps> = ({
  label,
  interests,
  selectedInterests,
  toggleInterest
}) => (
  <div className="mt-4">
    <label className="block text-sm font-medium text-gray-700">
      {label}
    </label>
    <div className="appearance-none w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm flex flex-wrap gap-2">
      {interests.map((interest) => (
        <InterestButton
          key={interest}
          interest={interest}
          isSelected={selectedInterests.includes(interest)}
          onClick={(event) => toggleInterest(event, interest)}
        />
      ))}
    </div>
  </div>
);

export default SubInterestList;
