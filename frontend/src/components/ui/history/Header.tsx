import React, {useState} from "react";
import { useSearchParams } from "next/navigation";
import UserList from "../General/UserList";
import DateLabel from "../DateLabel";

const Header: React.FC = () => {
  const searchParams = useSearchParams();
  const [name, setName]= useState(searchParams.get('name') ?? 'Current trip' );

  return (
    <div className="p-4 bg-white flex flex-col items-center">
      <h1 className="text-xl font-bold">{name}</h1>
      <DateLabel startDate="2024-08-28" endDate="2024-09-29" />
      <div className="flex mt-2">
      <UserList />
        </div>
      </div>
  );
};

export default Header;
