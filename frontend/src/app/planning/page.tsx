"use client";

import { useState } from "react";
import CurrentTripComp from "@/components/ui/currentTrip";
import BackButton from "@/components/ui/BackButton";
import PlanningPageMap from "@/components/ui/PlanningPageMap";

export default function PlanningPage() {
  const [address, setAddress] = useState<string>("");

    const lat = parseFloat(searchParams.get('lat') ?? '0');
    const lng = parseFloat(searchParams.get('lng') ?? '0');
    
    const positionMarker = { lat, lng };
    
    return (
        <div className="relative h-[100vh] w-full">
            <Map className="h-full w-full"> 
                <MarkerPin positionMarker={positionMarker} />
            </Map>
            <CurrentTripComp />
        </div>
    );
}
