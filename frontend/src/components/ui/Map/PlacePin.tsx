"use client";
import React from "react";
import Image from 'next/image'
import { MarkerF, InfoWindowF} from "@react-google-maps/api";


const pinIcon = {
    url: "/images/gps3.png",
    scaledSize: { width: 50, height: 40 },
};

function MarkerClicked(event)
{
  console.log("you clicked on the marker");
  console.log(event.latLng.lat());
  console.log(event.latLng.lng());
}

type PlacePinProps = {
    positionMarker: {
        lat: number,
        lng: number,
      },
}

const PlacePin: React.FC<PlacePinProps> =({positionMarker })=>
{

    const data = {
        imageUrl: '/public/images/memory2.png',
        name: 'Place Name',
        rating: 4.5,
        type: 'Type of Place',
        estimateCost: '$50 - $100',
        location: '123 Example St, City, Country',
        openHours: '9:00 AM - 6:00 PM',
      };

    return (
            <MarkerF
                position={positionMarker}
                icon={pinIcon}
                onClick={MarkerClicked}
            >
                <InfoWindowF position={positionMarker}>
                <div className="w-80 p-2 bg-white rounded-lg shadow-lg">
        <div className="relative w-full h-[10vh]">
          <Image src={data.imageUrl}
            alt="Place" width={60} height={60} />
        </div>
        <div className="p-2">
          <div className="flex justify-between items-center mb-2">
            <span className="font-bold text-lg">{data.name}</span>
            <span className="text-yellow-500 text-lg">⭐ {data.rating}</span>
          </div>
          <div className="flex justify-between mb-2">
            <span className="text-gray-700">{data.type}</span>
            <span className="text-gray-500">{data.estimateCost}</span>
          </div>
          <div className="text-gray-600 mb-2">{data.location}</div>
          <div className="text-gray-500">Open Hours: {data.openHours}</div>
        </div>
      </div>

                </InfoWindowF>
                </MarkerF>
            
    );
}

export default PlacePin;