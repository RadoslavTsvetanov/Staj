import React, {useState} from 'react';
import Image from 'next/image';
import ImageSlider from './ImageSlider';

const ActivityCard = ({ name, imageUrl }: {
  name: string;
  imageUrl: string;
}) => {

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
      <div className="flex items-center m-auto ">
          <Image src="/images/gps.png" alt="LocationIcon" width={30} height={30} />
          <span className="ml-2 text-lg font-semibold ">{name}</span> 
      </div>
      
      <div className="flex justify-center items-center h-screen  bg-teal-200">
      <ImageSlider />
    </div>
    </div>
  );
};

export default ActivityCard;
