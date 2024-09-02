import React, { useState, useEffect } from "react";

// Define types for location and error objects
interface Coordinates {
  latitude: number;
  longitude: number;
}

interface LocationState {
  loaded: boolean;
  coordinates: { lat: number; lng: number } | null;
  error?: {
    code: number;
    message: string;
  };
}

// Define GeolocationPosition and GeolocationPositionError types for the parameters
const useGeoLocation = () => {
  const [location, setLocation] = useState<LocationState>({
    loaded: false,
    coordinates: null,
  });

  // Function to handle successful geolocation
  const onSuccess = (position: GeolocationPosition) => {
    setLocation({
      loaded: true,
      coordinates: {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      },
    });
  };

  // Function to handle geolocation error
  const onError = (error: GeolocationPositionError) => {
    setLocation({
      loaded: true,
      coordinates: null,
      error: {
        code: error.code,
        message: error.message,
      },
    });
  };

  useEffect(() => {
    // Check if geolocation is supported
    if (!("geolocation" in navigator)) {
      onError({
        code: 0,
        message: "Geolocation not supported",
      } as GeolocationPositionError); // Type assertion for custom error
      return;
    }

    // Request the current position
    navigator.geolocation.getCurrentPosition(onSuccess, onError);
  }, []);

  return location;
};

export default useGeoLocation;
