"use client";
import React, { useState, useEffect } from "react";
import MarkerPin from "./marker";
import Map from "./map";
import NewTripButton from "./NewTripButton";
import useGeoLocation from "./useGeo";

const center = { lat: 42.136097, lng: 24.742168 };

function HomePageMap() {
  const [isSelectedLocation, setSelected] = useState(false);
  const [location, setLocation] = useState(center);
  const userLocation = useGeoLocation();

  function MarkerFinish(event: google.maps.MapMouseEvent) {
    if (event.latLng) {
      let lat = event.latLng.lat();
      let lng = event.latLng.lng();
      console.log("The final lat", lat);
      console.log("The final lng", lng);
      setSelected(true);
      console.log(isSelectedLocation);
      setLocation({ lat, lng });
    }
  }
  useEffect(() => {
    if (userLocation.loaded && userLocation.coordinates) {
      setLocation({
        lat: userLocation.coordinates.lat,
        lng: userLocation.coordinates.lng,
      });
    }
  }, [userLocation]);

  return (
    <div className="relative w-full h-full">
      <Map
        center={userLocation.loaded ? location : center}
        onClick={MarkerFinish}
      >
        <MarkerPin
          positionMarker={location}
          draggable={true}
          onDragEnd={MarkerFinish}
        />
      </Map>
      {isSelectedLocation && (
        <div className="absolute bottom-4 right-4">
          <NewTripButton position={location} />
        </div>
      )}
    </div>
  );
}

export default HomePageMap;
