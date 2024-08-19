import React from "react";
import BudgetSelector from "./Budget";
import DateRangeSelector from "./Dates";
import Location from "./Location";

const TripInfo: React.FC = () => {
  return (
    <div className=" h-1/2 w-full bg-blue-100 rounded-xl m-2 mt-4 mr-4 p-4 z-50">
      <h1 className="text-black text-xl font-bold mb-4 text-center">
        Current trip
      </h1>

      <DateRangeSelector />

      <Location location={"Burgas"} />

      <BudgetSelector />
    </div>
  );
};

export default TripInfo;
