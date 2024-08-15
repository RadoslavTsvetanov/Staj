"use client"

import Map from "@/components/ui/map";
import CurrentTripComp from "@/components/ui/currentTrip";
import MarkerPin from "@/components/ui/marker";
import { useSearchParams } from 'next/navigation'; 

export default function PlanningPage() {
    const searchParams = useSearchParams();

    let lat = parseFloat(searchParams.get('lat') ?? '0');
    let lng = parseFloat(searchParams.get('lng') ?? '0');
    
    const positionMarker = { lat, lng };
    //lat=lat-20;
    const center = { lat, lng };

    return (
        <div className="relative h-[100vh] w-full">
            <Map className="h-full w-full" center={center}> 
                <MarkerPin positionMarker={positionMarker} />
            </Map>
            <CurrentTripComp />
        </div>
    );
}

