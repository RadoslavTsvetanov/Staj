import React from 'react';

const TripInfo: React.FC = () => {
  return (
    <div className="bg-blue-200 rounded-xl p-4 w-full max-w-sm">
      <h2 className="text-lg font-bold mb-2">Current trip</h2>
      <p>From: ___ To: ___ ___ days</p>
      <p>🏙️ Burgas</p>
      <p>Budget</p>
      <div className="flex space-x-1">
        <span>⭐</span>
        <span>⭐</span>
        <span>⭐</span>
        <span>⭐</span>
        <span>⭐</span>
      </div>
    </div>
  );
};

export default TripInfo;
