import React, {useState} from 'react';
import Image from 'next/image';
import Memory from '@/components/ui/Memory';
import ImageSlider from './ImageSlider';

type ActivityCardProps = {
  name: string;
  imageUrl: string;
};

const ActivityCard: React.FC<ActivityCardProps> = ({ name, imageUrl }) => {

  const [images, setImages] = useState([
    '/images/memory.png',
    '/images/memory.png',
    '/images/memory.pngg',
  ]);

  const addPhoto = () => {
    // Logic to add a new photo
    const newPhoto = prompt('Enter the URL of the new photo:');
    if (newPhoto) {
      setImages([...images, newPhoto]);
    }
  };
  return (
    <div className="h-[40vh] bg-teal-200 rounded-lg p-4 mb-4 mx-8 shadow-md flex flex-col justify-between">
      <div className="flex items-center">
          <Image src="/images/gps.png" alt="LocationIcon" width={24} height={24} />
          <span className="ml-2 text-lg font-semibold">{name}</span> 
      </div>
      
      <div className="flex justify-center items-center h-screen  bg-teal-400">
      <ImageSlider />
    </div>
    </div>
  );
};

export default ActivityCard;
