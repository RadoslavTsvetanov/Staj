"use client";
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";

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
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
    version: "weekly",
  });

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      options={mapOptions}
      center={center}
      zoom={10}
    >
    {children}

    </GoogleMap>
  ) : (
    <></>
  );
}

export default Map;
