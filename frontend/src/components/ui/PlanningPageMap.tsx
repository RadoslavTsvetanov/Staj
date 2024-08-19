"use client";

import { useState, useEffect } from 'react';
import Map from "@/components/ui/map";
import MarkerPin from "@/components/ui/marker";
import Region from "@/components/ui/Region";
import Slider from "@/components/ui/Slider";
import { useSearchParams } from 'next/navigation';
import { useJsApiLoader, InfoWindowF } from "@react-google-maps/api";

function PlanningPageMap() {
  const searchParams = useSearchParams();
  const [radius, setRadius] = useState(0);  

  let lat = parseFloat(searchParams.get('lat') ?? '0');
  let lng = parseFloat(searchParams.get('lng') ?? '0');
  
  const positionMarker = { lat, lng };
  const center = { lat, lng };


  



  return (
    <div className="relative h-screen w-full">
      <Map className="h-full w-full" center={center}> 
        <Region center={positionMarker} radius={radius}/> 
        <MarkerPin positionMarker={positionMarker} />
        {/* {infoWindowText && (
          <InfoWindowF position={positionMarker}>
            <div>{infoWindowText}</div>
          </InfoWindowF>
        )} */}
      </Map>
      <div className="absolute bottom-10 left-1/4 transform -translate-x-1/2">
        <Slider radius={radius} setRadius={setRadius} />
      </div>
    </div>
  );
}

export default PlanningPageMap;
