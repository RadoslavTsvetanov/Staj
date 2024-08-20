"use client";

import { useState } from "react";
import CurrentTripComp from "@/components/ui/currentTrip";
import BackButton from "@/components/ui/BackButton";
import PlanningPageMap from "@/components/ui/PlanningPageMap";

export default function PlanningPage() {
  const [address, setAddress] = useState<string>("");

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
