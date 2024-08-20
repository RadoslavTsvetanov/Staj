import React from 'react';
import Image from 'next/image';

type ActivityCardProps = {
  name: string;
  imageUrl: string;
};

const ActivityCard: React.FC<ActivityCardProps> = ({ name, imageUrl }) => {
  return (
    <div className="bg-teal-300 rounded-lg p-4 mb-4 shadow-md">
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <Image src={imageUrl} alt={name} width={24} height={24} />
          <span className="ml-2 text-lg font-semibold">{name}</span>
        </div>
        <div className="flex items-center space-x-2">
          <div className="bg-gray-200 w-24 h-16 rounded"></div>
          <div className="bg-gray-200 w-24 h-16 rounded"></div>
          <div className="bg-gray-200 w-24 h-16 rounded"></div>
          <div className="bg-gray-400 w-16 h-16 rounded flex items-center justify-center text-xl">+</div>
        </div>
      </div>
    </div>
  );
};

export default ActivityCard;
