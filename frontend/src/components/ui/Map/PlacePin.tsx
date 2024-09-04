"use client";
import React, { useState } from "react";
import Image from 'next/image'
import { MarkerF, InfoWindowF } from "@react-google-maps/api";

const pinIcon = {
    url: "/images/gps3.png",
    scaledSize: { width: 50, height: 40 },
};

function MarkerClicked(event) {
  console.log("You clicked on the marker at:", event.latLng.lat(), event.latLng.lng());
}

type PlacePinProps = {
  positionMarker: {
    lat: number,
    lng: number,
  },
  data: {
    imageUrl: string,
    name: string,
    rating: number,
    type: string,
    estimateCost: string,
    location: string,
    openHours: string,
    description: string,
    isAgeRestricted: boolean,
  }
}

const PlacePin: React.FC<PlacePinProps> = ({ positionMarker, data }) => {
  const [showInfo, setShowInfo] = useState(false);

  return (
    <MarkerF
      position={positionMarker}
      icon={pinIcon}
      onClick={() => setShowInfo(true)}
    >
      {showInfo && (
        <InfoWindowF position={positionMarker} onCloseClick={() => setShowInfo(false)}>
          <div className="w-80 p-2 bg-white rounded-lg shadow-lg">
            <div className="relative w-full h-[10vh]">
              <Image src={data.imageUrl || '/images/default.png'} // Use a default image if none is provided
                alt="Place"
                width={60}
                height={60}
              />
            </div>
            <div className="p-2">
              <div className="flex justify-between items-center mb-2">
                <span className="font-bold text-lg">{data.name || "Unknown Place"}</span>
                <span className="text-yellow-500 text-lg">⭐ {data.rating || 0}</span>
              </div>
              <div className="flex justify-between mb-2">
                <span className="text-gray-700">{data.type || "Type not specified"}</span>
                <span className="text-gray-500">{data.estimateCost || "Cost not available"}</span>
              </div>
              <div className="text-gray-600 mb-2">{data.location || "Location not specified"}</div>
              <div className="text-gray-500">Open Hours: {data.openHours || "Hours not available"}</div>
              <div className="text-gray-600 mt-1">{data.description || "No description available"}</div>
              {data.isAgeRestricted && (
                <div className="text-red-500 mt-1">Age Restricted</div>
              )}
            </div>
          </div>
        </InfoWindowF>
      )}
    </MarkerF>
  );
};

export default PlacePin;
