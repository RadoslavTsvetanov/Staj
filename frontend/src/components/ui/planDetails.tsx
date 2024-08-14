import React from 'react';

const PlanDetails: React.FC = () => {
  return (
    <div className="bg-blue-200 rounded-xl p-4 w-full max-w-sm overflow-y-auto max-h-64">
      <h2 className="text-lg font-bold mb-2">My plan</h2>
      <div className="mb-2">
        <p className="font-semibold">Day 1</p>
        <ul className="list-disc list-inside">
          <li>🏊 Aqua park</li>
          <li>🍣 Sushi place</li>
        </ul>
      </div>
      <div className="mb-2">
        <p className="font-semibold">Day 2</p>
        <ul className="list-disc list-inside">
          <li>🏛️ Museum</li>
        </ul>
      </div>
    </div>
  );
};

export default PlanDetails;
