"use client";

import { useState, useCallback, useEffect } from "react";
import Map from "@/components/ui/map";
import MarkerPin from "@/components/ui/marker";
import Region from "@/components/ui/Region";
import Slider from "@/components/ui/Slider";
import { useSearchParams } from "next/navigation";

interface PlanningPageMapProps {
    onAddressChange?: (address: string) => void;
  }
  
  function PlanningPageMap({ onAddressChange }: PlanningPageMapProps) {
  const searchParams = useSearchParams();
  const [radius, setRadius] = useState(0);
  const [mapLoaded, setMapLoaded] = useState(false);

  let lat = parseFloat(searchParams.get("lat") ?? "0");
  let lng = parseFloat(searchParams.get("lng") ?? "0");

  const positionMarker = { lat, lng };
  const center = { lat, lng };

  const handleMapLoad = useCallback((loaded: boolean) => {
    setMapLoaded(loaded);
  }, []);

  useEffect(() => {
    if (mapLoaded && lat && lng) {
      const geocoder = new google.maps.Geocoder();
      let address = '';

      geocoder
        .geocode({ location: { lat: center.lat, lng: center.lng } })
        .then((response) => {
          if (response.results[0]) {
            address = response.results[0].formatted_address;
            console.log(response);
            if (onAddressChange) {
              onAddressChange(address);
            }
          } else {
            alert("No results found");
          }
        })
        .catch((e) => alert("Geocoder failed due to: " + e));
    }
  }, [mapLoaded, lat, lng, center, onAddressChange]);

  return (
    <div className="relative h-screen w-full">
      <Map className="h-full w-full" center={center} onMapLoad={handleMapLoad}>
        <Region center={positionMarker} radius={radius} />
        <MarkerPin positionMarker={positionMarker} />
      </Map>
      <div className="absolute bottom-10 left-1/4 transform -translate-x-1/2">
        <Slider radius={radius} setRadius={setRadius} />
      </div>
      {mapLoaded ? null : <div>Map is still loading...</div>}
    </div>
  );
}

export default PlanningPageMap;
