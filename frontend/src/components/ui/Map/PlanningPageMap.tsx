"use client";

import { useState, useCallback, useEffect } from "react";
import Map from "@/components/ui/Map/map";
import MarkerPin from "@/components/ui/Map/marker";
import PlacePin from "./PlacePin";
import Region from "@/components/ui/Map/Region";
import Slider from "@/components/ui/Map/Slider";
import { useSearchParams } from "next/navigation";
import { fetchNearbyPlaces } from '@/utils/nearby';

interface PlanningPageMapProps {
  onAddressChange?: (address: string) => void;
}

function PlanningPageMap({ onAddressChange }: PlanningPageMapProps) {
  const searchParams = useSearchParams();
  const [radius, setRadius] = useState(0); 
  const [mapLoaded, setMapLoaded] = useState(false);
  const [nearbyPlaces, setNearbyPlaces] = useState([]); // State to hold nearby places

  let lat = parseFloat(searchParams.get("lat") ?? "0");
  let lng = parseFloat(searchParams.get("lng") ?? "0");

  const [positionMarker, setPositionMarker] = useState({ lat, lng });
  const center = { lat, lng };

  const handleMapLoad = useCallback((loaded: boolean) => {
    setMapLoaded(loaded);
  }, []);

  const MarkerChange = (event: google.maps.MapMouseEvent) => {
    if (event.latLng) {
      let lat = event.latLng.lat();
      let lng = event.latLng.lng();
      setPositionMarker({ lat, lng });
      if (onAddressChange) {
        onAddressChange("");
      }
    }
  };

  useEffect(() => {
    if (mapLoaded && lat && lng) {
      const geocoder = new google.maps.Geocoder();
      let address = '';

      geocoder
        .geocode({ location: { lat: positionMarker.lat, lng: positionMarker.lng } })
        .then((response) => {
          if (response.results[0]) {
            address = response.results[0].formatted_address;
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

  // Fetch nearby places when the positionMarker or radius changes
 
  useEffect(() => {
    const payload = {
      location: positionMarker,
      radius: radius,
      type: 'restaurant',
    };

    fetchNearbyPlaces(payload)
      .then(data => {
        console.log('Nearby Places:', data);
        setNearbyPlaces(data); // Set the nearby places data
      })
      .catch(error => console.error('Error:', error));
  }, [positionMarker, radius]);

  return (
    <div className="relative h-screen w-full">
      <Map
        className="h-full w-full"
        center={{ lat: center.lat, lng: center.lng }}
        onMapLoad={handleMapLoad}
        onClick={MarkerChange}
      >
        <Region center={positionMarker} radius={radius} />
        <MarkerPin positionMarker={positionMarker} draggable={true} onDragEnd={MarkerChange} />

        {nearbyPlaces.map((place, index) => (
          <PlacePin
            key={index}
            positionMarker={{ lat: place.coordinates.lat, lng: place.coordinates.lon }}
            data={{
              imageUrl: place.iconUrl,
              name: place.description || "No Name Available",
              rating: place.rating,
              type: 'Restaurant', // Assuming type as 'Restaurant', adjust based on actual data if available
              estimateCost: place.cost === 0 ? "Free" : `$${place.cost * 10} - $${place.cost * 20}`, // Example cost estimate
              location: `${place.coordinates.lat.toFixed(4)}, ${place.coordinates.lon.toFixed(4)}`, // Placeholder location
              openHours: 'Open Hours Unavailable', // Update this if open hours data is available
              description: place.description || "Description not available",
              isAgeRestricted: place.isAgeRestricted,
            }}
          />
        ))}
      </Map>

      <div className="absolute bottom-10 left-1/4 transform -translate-x-1/2">
        <Slider radius={radius} setRadius={setRadius} />
      </div>

      {mapLoaded ? null : <div>Map is still loading...</div>}
    </div>
  );
}

export default PlanningPageMap;
