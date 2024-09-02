import React from 'react';

interface DateLabelProps {
  startDate: string;
  endDate: string;
}

const formatDate = (dateString: string) => {
  const options: Intl.DateTimeFormatOptions = { day: 'numeric', month: 'long', year: 'numeric' };
  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', options);
};

const DateLabel: React.FC<DateLabelProps> = ({ startDate, endDate }) => {
  return (
    <div className="px-4 py-2 text-lg font-medium text-gray-700">
      {formatDate(startDate)} - {formatDate(endDate)}
    </div>
  );
};

export default DateLabel;
