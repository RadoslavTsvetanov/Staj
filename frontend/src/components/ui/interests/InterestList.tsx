import React from 'react';
import InterestButton from './InterestButton';

interface InterestListProps {
  label: string;
  interests: string[];
  selectedInterests: string[];
  toggleInterest: (event: React.MouseEvent<HTMLButtonElement>, interest: string) => void;
  showOtherInput: boolean;
  otherInterest: string;
  onOtherInterestChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  onAddOtherInterest: () => void;
  onShowOtherInput: () => void;
}

const InterestList: React.FC<InterestListProps> = ({
  label,
  interests,
  selectedInterests,
  toggleInterest,
  showOtherInput,
  otherInterest,
  onOtherInterestChange,
  onAddOtherInterest,
  onShowOtherInput
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
      {showOtherInput ? (
        <input
          type="text"
          value={otherInterest}
          onChange={onOtherInterestChange}
          onBlur={onAddOtherInterest}
          className='px-3 py-1 rounded-lg border bg-gray-200 text-gray-700'
          placeholder="Your interest"
          autoFocus
        />
      ) : (
        <button
          onClick={onShowOtherInput}
          className='px-3 py-1 rounded-lg border bg-gray-200 text-gray-700'>
          Other
        </button>
      )}
    </div>
  </div>
);

export default InterestList;
