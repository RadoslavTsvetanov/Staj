"use client";

import React, {useState } from "react";
import { MarkerF } from "@react-google-maps/api";

import NewTripButton from "./NewTripButton";

const pinIcon = {
    url: "/images/pin.png",
    scaledSize: { width: 50, height: 40 },
};

const positionMarker = {
    lat: 43.642693,
    lng: -79.3871189,
};

function MarkerClicked(event)
{
  console.log("you clicked on the marker");
  console.log(event.latLng.lat());
  console.log(event.latLng.lng());
}



function MarkerPin()
{
    const [isSelectedLocation, setLocation] = useState(false);

    function MarkerFinishDrag(event)
    {
    console.log("The final lat",event.latLng.lat());
    console.log("The final lng",event.latLng.lng());
    setLocation(true);
    }

    return (
        <div className="relative">
            <MarkerF
                position={positionMarker}
                icon={pinIcon}
                draggable
                onClick={MarkerClicked}
                onDragEnd={MarkerFinishDrag}
            />
            {isSelectedLocation && (
                <div className="absolute left-1/2 transform -translate-x-1/2 mt-2">
                    <NewTripButton />
                </div>
            )}
        </div>
    )
}

export default MarkerPin;