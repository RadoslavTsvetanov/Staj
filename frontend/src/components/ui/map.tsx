"use client";
import { GoogleMap, useJsApiLoader, InfoWindow} from "@react-google-maps/api";
import { useState, useEffect } from 'react';

const containerStyle = {
  width: "100%",
  height: "100vh",
};

const mapOptions = {
  zoom: 17,
  mapId: "MY_NEXTJS_MAP_ID",
  mapTypeControl: false,
  zoomControl: false,
  streetViewControl: false,
  fullscreenControl: false,
  scrollwheel: true,
};

interface MapProps {
  children?: React.ReactNode;
  className?: string;
  center:  {
    lat: number,
    lng: number,
  };
}

const Map: React.FC<MapProps> = ({ children ,center}) => {
  const [infoWindowText, setInfoWindowText] = useState<string | null>(null);

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
    version: "weekly",
  });

  useEffect(() => {
    if (isLoaded && center.lat && center.lng) {
      const geocoder = new google.maps.Geocoder();

      geocoder
        .geocode({ location: { lat: center.lat, lng: center.lng } })
        .then((response) => {
          if (response.results[0]) {
            setInfoWindowText(response.results[0].formatted_address);
          } else {
            alert("No results found");
          }
        })
        .catch((e) => alert("Geocoder failed due to: " + e));
    }
  }, [isLoaded, center.lat, center.lng]);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      options={mapOptions}
      center={center}
      zoom={10}
    >
    {children}
    {infoWindowText && (
        <InfoWindow position={center}>
          <div>{infoWindowText}</div>
        </InfoWindow>
      )}

    </GoogleMap>
  ) : (
    <></>
  );
}

export default Map;
