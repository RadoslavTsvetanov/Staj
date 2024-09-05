"use client";

import React from 'react';
import Header from '@/components/ui/history/Header';
import DateLabel from '@/components/ui/history/DateLabel';
import ActivityCard from '@/components/ui/history/ActivityCard';
import BackButton from '@/components/ui/General/BackButton';

const HistoryPage: React.FC = () => {
  return (
    <div className="bg-gray-100 min-h-screen flex flex-col items-center">
      <BackButton/>
      <Header />
      <DateLabel startDate=''  endDate=''/>
      <div className="w-full mt-4">
        <ActivityCard name="Aqua park" imageUrl="/images/aqua-park.png" />
        <ActivityCard name="Sushi place" imageUrl="/images/sushi-place.png" />
      </div>
    </div>
  );
};

export default HistoryPage;
