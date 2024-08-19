import React, { useState } from 'react';

const DateRangeSelector: React.FC = () => {
  const [startDate, setStartDate] = useState<string | null>(null);
  const [endDate, setEndDate] = useState<string | null>(null);
  const [days, setDays] = useState<number | string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleStartDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStartDate(e.target.value);
    if (endDate) {
      calculateDays(e.target.value, endDate);
    }
  };

  const handleEndDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEndDate(e.target.value);
    if (startDate) {
      calculateDays(startDate, e.target.value);
    }
  };

  const calculateDays = (start: string, end: string) => {
    const startD = new Date(start);
    const endD = new Date(end);

    if (startD > endD) {
      setDays(null);
      setError('The start date cannot be after the end date.');
      return;
    }

    setError(null);

    const timeDiff = Math.abs(endD.getTime() - startD.getTime());
    const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1;
    setDays(dayDiff);
  };

  return (
    <div className="p-4 bg-blue-100 rounded-lg shadow-md flex flex-col space-y-2">
      <div className="flex items-center space-x-2">
        <label className="text-lg">From:</label>
        <input
          type="date"
          value={startDate ?? ''}
          onChange={handleStartDateChange}
          className="border rounded p-1 text-gray-700"
        />
        <label className="text-lg">To:</label>
        <input
          type="date"
          value={endDate ?? ''}
          onChange={handleEndDateChange}
          className="border rounded p-1 text-gray-700"
        />
        <span className="text-lg">{days !== null ? `${days} days` : '__ days'}</span>
      </div>
      {error && <span className="text-red-600">{error}</span>}
    </div>
  );
};

export default DateRangeSelector;
