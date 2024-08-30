"use client";

import { useState, useLayoutEffect } from "react";
import CurrentTripComp from "@/components/ui/currentTrip";
import BackButton from "@/components/ui/BackButton";
import PlanningPageMap from "@/components/ui/PlanningPageMap";
import { useRouter } from "next/navigation";
import { isAuthenticated } from "@/lib/utils";

export default function PlanningPage() {
  const [address, setAddress] = useState<string>("");
  const router = useRouter();

  useLayoutEffect(() => {
    const isAuth = isAuthenticated();
    if(!isAuth){
      router.push('/auth/signin');
    }
  }, [router]);

  const handleAddressChange = (newAddress: string) => {
    setAddress(newAddress);
  };

  return (
    <div className="relative h-[100vh] w-full">
      <PlanningPageMap onAddressChange={handleAddressChange} />
      <BackButton />
      <CurrentTripComp location={address} />
    </div>
  );
}
