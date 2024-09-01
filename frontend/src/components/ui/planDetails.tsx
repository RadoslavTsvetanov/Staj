import React from 'react';
import Day from './Day';
import Place from './Place';

const PlanDetails: React.FC = () => {
  return (
    <div className="h-1/2 bg-blue-100 rounded-xl p-4 m-2 mr-4  w-full border border-gray-200 min-h-[50vh] max-h-[55vh] z-50">
          <h1 className="text-black text-xl font-bold mb-2 text-center">My plan</h1>
          <div className="max-h-[40vh] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
            <div className="mb-2 space-y-1">
              <Day dayNumber={1}/>
              <ul className="list-none list-inside">
              <Place text="Aqua park" />
              <Place text="Sushi place" />
              </ul>
            </div>
            <div className="mb-2">
              <Day dayNumber={2}/>
              <ul className="list-none list-inside">
              <Place text="Museum" />
              </ul>
              <Day dayNumber={3}/>
              <ul className="list-none list-inside">
                <Place text={"Park"}/>
              </ul>
            </div>
            </div>
          </div>

  );
};

export default PlanDetails;
