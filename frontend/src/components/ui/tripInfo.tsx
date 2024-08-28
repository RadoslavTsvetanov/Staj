import React from "react";
import BudgetSelector from "./Budget";
import DateRangeSelector from "./Dates";
import Location from "./Location";

type TripInfoProps = {
  location: string,
};

const TripInfo: React.FC<TripInfoProps> = ({location}) => {
  return (
    <div className=" h-1/2 max-h-[45vh] w-full bg-blue-100 rounded-xl m-2 mt-4 mr-4 p-4 z-50 space-y-1 overflow-hidden">
      <h1 className="text-black text-xl font-bold mb-2 text-center">
        Current trip
      </h1>

      <DateRangeSelector />

      <Location location={location} />

      <BudgetSelector />
    </div>
  );
};

export default TripInfo;
