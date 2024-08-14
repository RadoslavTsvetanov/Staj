"use client";
import MarkerPin from "./marker";
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "90vh",
};

const center = {
  lat: 43.642693,
  lng: -79.3871189,
};

const mapOptions = {
  center: center,
  zoom: 17,
  mapId: "MY_NEXTJS_MAP_ID",
  mapTypeControl: false,
  zoomControl: false,
  streetViewControl: false,
  scrollwheel: true,
};

function Map() {
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
      <MarkerPin />

    </GoogleMap>
  ) : (
    <></>
  );
}

export default Map;
