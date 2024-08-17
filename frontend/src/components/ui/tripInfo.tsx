import React from 'react';
import BudgetSelector from './Budget';


const TripInfo: React.FC = () => {
  return (
    <div className=" h-1/2 w-full bg-blue-100 rounded-xl m-2 mt-4 mr-4 p-4 z-50">
            <h1 className="text-black text-xl font-bold mb-4 text-center">
              Current trip
            </h1>
            <p className="mb-2">From: ___ To: ___ ___ days</p>
            <div className="flex items-center mb-2">
              <span role="img" aria-label="city" className="mr-2">
                📍
              </span>
              <p>Burgas</p>
            </div>
        <BudgetSelector/>
          </div>
  );
};


{/* <div className="w-1/2 flex flex-col justify-between">
          <div className="h-1/2 bg-customBlue rounded-xl m-2 p-4">
            <h1 className="text-black text-xl font-bold mb-4 text-center">
              Currently planning
            </h1>
            <div className="h-[calc(4*51.5px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
            <div className="space-y-2">
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
            </div>
            </div>
          </div> */}

export default TripInfo;

