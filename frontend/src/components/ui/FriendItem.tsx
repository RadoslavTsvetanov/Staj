import { FC } from "react";
import Image from 'next/image'

interface FriendItemProps {
  name: string;
  img: string; 
}

const FriendItem: FC<FriendItemProps> = ({ name, img }) => {
  return (
    <div className="flex items-center p-3 bg-gray-50 rounded-md border border-gray-200 shadow-sm">
      <Image className='w-10 h-10 rounded-full mr-3 object-cover' src={img} alt="" width={40} height={40} /> 
      <span className="text-gray-800 font-medium">{name}</span>
    </div>
  );
};

export default FriendItem;