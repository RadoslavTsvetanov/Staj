import React, {useState} from "react";
import { useSearchParams } from "next/navigation";

const Header: React.FC = () => {
  const searchParams = useSearchParams();
  const [name, setName]= useState(searchParams.get('name') ?? 'Current trip' );

  return (
    <div className="p-4 bg-white flex flex-col items-center">
      <h1 className="text-xl font-bold">{name}</h1>
      <p className="text-sm text-gray-500">dates from - to</p>
      <div className="flex mt-2">
        <div className="w-6 h-6 bg-red-400 rounded-full"></div>
        <div className="w-6 h-6 bg-green-400 rounded-full ml-2"></div>
        <div className="w-6 h-6 bg-purple-400 rounded-full ml-2"></div>
        <div className="w-6 h-6 bg-gray-300 rounded-full ml-2 flex items-center justify-center">
          <span className="text-sm font-bold">+</span>
        </div>
      </div>
    </div>
  );
};

export default Header;
