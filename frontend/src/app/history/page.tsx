"use client";

import React, { useLayoutEffect } from 'react';
import Header from '@/components/ui/Header';
import DateLabel from '@/components/ui/DateLabel';
import ActivityCard from '@/components/ui/ActivityCard';
import { useRouter } from 'next/navigation';
import { isAuthenticated } from '@/lib/utils';

const HomePage: React.FC = () => {
  const router = useRouter();

  useLayoutEffect(() => {
    const isAuth = isAuthenticated();
    if(!isAuth){
      router.push('/auth/signin');
    }
  }, [router]);

  return (
    <div className="bg-gray-100 min-h-screen flex flex-col items-center">
      <Header />
      <DateLabel />
      <div className="w-full max-w-2xl mt-4">
        <ActivityCard name="Aqua park" imageUrl="/images/aqua-park.png" />
        <ActivityCard name="Sushi place" imageUrl="/images/sushi-place.png" />
      </div>
    </div>
  );
};

export default HomePage;
