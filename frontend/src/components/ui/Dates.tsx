import React, { useState } from 'react';

const DateRangeSelector: React.FC = () => {
  const [startDate, setStartDate] = useState<string | null>(null);
  const [endDate, setEndDate] = useState<string | null>(null);
  const [days, setDays] = useState<number | string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const currentDate = new Date().toISOString().split('T')[0]; 

  const validateDates = (start: string, end: string) => {
    const today = new Date();
    const startD = new Date(start);
    const endD = new Date(end);

    const threeYearsLater = new Date(today);
    threeYearsLater.setFullYear(today.getFullYear() + 3);

    const maxDuration = 28; 

    if (startD < today) {
      setError('The start date cannot be before today.');
      return false;
    }

    if (startD > endD) {
      setError('The start date cannot be after the end date.');
      return false;
    }

    if (endD > threeYearsLater) {
      setError('The trip cannot be planned more than 3 years ahead.');
      return false;
    }

    const timeDiff = Math.abs(endD.getTime() - startD.getTime());
    const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));

    if (dayDiff > maxDuration) {
      setError('The trip cannot be longer than 4 weeks.');
      return false;
    }

    setError(null);
    setDays(dayDiff + 1); 
    return true;
  };

  const handleStartDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newStartDate = e.target.value;
    setStartDate(newStartDate);
    if (endDate && newStartDate) {
      validateDates(newStartDate, endDate);
    }
  };

  const handleEndDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newEndDate = e.target.value;
    setEndDate(newEndDate);
    if (startDate && newEndDate) {
      validateDates(startDate, newEndDate);
    }
  };

  return (
    <div className="p-4 bg-blue-100 rounded-lg shadow-md flex flex-col space-y-2">
      <div className="flex items-center space-x-4">
        <div className="flex items-center space-x-2">
          <label className="text-lg">From:</label>
          <input
            type="date"
            value={startDate ?? ''}
            onChange={handleStartDateChange}
            className="border rounded p-1 text-gray-700"
            min={currentDate} 
          />
        </div>
        <div className="flex items-center space-x-2">
          <label className="text-lg">To:</label>
          <input
            type="date"
            value={endDate ?? ''}
            onChange={handleEndDateChange}
            className="border rounded p-1 text-gray-700"
            min={startDate ?? currentDate} 
          />
        </div>
        <span className="text-lg">{days !== null ? `${days} days` : '__ days'}</span>
      </div>
      {error && <span className="text-red-600">{error}</span>}
    </div>
  );
};

export default DateRangeSelector;
