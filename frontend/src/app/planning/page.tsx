"use client"

import Map from "@/components/ui/map";
import CurrentTripComp from "@/components/ui/currentTrip";
import MarkerPin from "@/components/ui/marker";
import Region from "@/components/ui/Region";
import BackButton from "@/components/ui/BackButton";
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
                
                <Region center={positionMarker} radius={10}/>
                <MarkerPin positionMarker={positionMarker} />
            </Map>
            <BackButton/>
            <CurrentTripComp />
        </div>
    );
}

