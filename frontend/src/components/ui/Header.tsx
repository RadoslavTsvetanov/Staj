import React, {useState} from "react";
import { useSearchParams } from "next/navigation";
import UserList from "./UserList";

const Header: React.FC = () => {
  const searchParams = useSearchParams();
  const [name, setName]= useState(searchParams.get('name') ?? 'Current trip' );

  return (
    <div className="p-4 bg-white flex flex-col items-center">
      <h1 className="text-xl font-bold">{name}</h1>
      <p className="text-sm text-gray-500">dates from - to</p>
      <div className="flex mt-2">
      <UserList />
        </div>
      </div>
  );
};

export default Header;
