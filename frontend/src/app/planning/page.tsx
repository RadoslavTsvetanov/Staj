"use client";

import { useState } from 'react';
import Map from "@/components/ui/map";
import CurrentTripComp from "@/components/ui/currentTrip";
import MarkerPin from "@/components/ui/marker";
import Region from "@/components/ui/Region";
import BackButton from "@/components/ui/BackButton";
import Slider from "@/components/ui/Slider";
import { useSearchParams } from 'next/navigation'; 

export default function PlanningPage() {
    const searchParams = useSearchParams();
    const [radius, setRadius] = useState(0);  

    let lat = parseFloat(searchParams.get('lat') ?? '0');
    let lng = parseFloat(searchParams.get('lng') ?? '0');
    
    const positionMarker = { lat, lng };
    const center = { lat, lng };

    return (
        <div className="relative h-[100vh] w-full">
            <Map className="h-full w-full" center={center}> 
                <Region center={positionMarker} radius={radius}/> 
                <MarkerPin positionMarker={positionMarker} />
            </Map>
            <BackButton />
            <CurrentTripComp />
            <div className="absolute bottom-10 left-1/4 transform -translate-x-1/2">
                <Slider radius={radius} setRadius={setRadius} />
            </div>
        </div>
    );
}
