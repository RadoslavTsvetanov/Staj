import React from 'react';

const PlanDetails: React.FC = () => {
  return (
    <div className="h-1/2 bg-blue-100 rounded-xl p-4 m-2 mr-4  w-full border border-gray-200 max-h-[55vh] z-50">
          <h1 className="text-black text-xl font-bold mb-2 text-center">My plan</h1>
          <div className="max-h-[40vh] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
            <div className="mb-2">
              <p className="font-semibold">Day 1</p>
              <ul className="list-none list-inside">
                <li>📍 Aqua park</li>
                <li>📍 Sushi place</li>
              </ul>
            </div>
            <div className="mb-2">
              <p className="font-semibold">Day 2</p>
              <ul className="list-none list-inside">
                <li>📍 Museum</li>
              </ul>
              <p className="font-semibold">Day 1</p>
              <ul className="list-none list-inside">
                <li>📍 Aqua park</li>
                <li>📍 Sushi place</li>
              </ul>
            </div>
            <div className="mb-2">
              <p className="font-semibold">Day 2</p>
              <ul className="list-none list-inside">
                <li>📍 Museum</li>
              </ul>
              <p className="font-semibold">Day 1</p>
              <ul className="list-none list-inside">
                <li>📍 Aqua park</li>
                <li>📍 Sushi place</li>
              </ul>
            </div>
            <div className="mb-2">
              <p className="font-semibold">Day 2</p>
              <ul className="list-none list-inside">
                <li>📍 Museum</li>
              </ul>
              <p className="font-semibold">Day 1</p>
              <ul className="list-none list-inside">
                <li>📍 Aqua park</li>
                <li>📍 Sushi place</li>
              </ul>
            </div>
            <div className="mb-2">
              <p className="font-semibold">Day 2</p>
              <ul className="list-none list-inside">
                <li>📍 Museum</li>
              </ul>
              </div>
            </div>
          </div>

  );
};

export default PlanDetails;
