import React, {useState} from "react";
import { useSearchParams } from "next/navigation";
import BudgetSelector from "./Budget";
import DateRangeSelector from "./Dates";
import Location from "./Location";

type TripInfoProps = {
  location: string,
};

const TripInfo: React.FC<TripInfoProps> = ({location}) => {
  const searchParams = useSearchParams();
  const [name, setName]= useState(searchParams.get('name') ?? 'Current trip' );

  return (
    <div className=" h-1/2 max-h-[45vh] w-full bg-blue-100 rounded-xl m-2 mt-4 mr-4 p-4 z-50 space-y-1 overflow-hidden">
      <h1 className="text-black text-xl font-bold  text-center">
        {name}
      </h1>

      <DateRangeSelector />

      <Location location={location} />

      <BudgetSelector />
    </div>
  );
};

export default TripInfo;
