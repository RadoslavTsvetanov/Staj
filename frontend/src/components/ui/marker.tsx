"use client";

import React, {useState } from "react";
import { MarkerF } from "@react-google-maps/api";

import NewTripButton from "./NewTripButton";

const pinIcon = {
    url: "/images/pin.png",
    scaledSize: { width: 50, height: 40 },
};


function MarkerClicked(event)
{
  console.log("you clicked on the marker");
  console.log(event.latLng.lat());
  console.log(event.latLng.lng());
}

type MarkerPinProps = {
    positionMarker: {
        lat: number,
        lng: number,
      },
      draggable?: boolean;
}

const MarkerPin: React.FC<MarkerPinProps> =({positionMarker, draggable = false})=>
{
    const [isSelectedLocation, setSelected] = useState(false);
    const [Location, setLocation] = useState(positionMarker);

    function MarkerFinishDrag(event: google.maps.MapMouseEvent) {
        if (event.latLng) {
            let lat = event.latLng.lat();
            let lng = event.latLng.lng();
            console.log("The final lat", lat);
            console.log("The final lng", lng);
            setSelected(true);
            setLocation({ lat, lng });
        }
    }

    return (
        <div className="relative">
            <MarkerF
                position={positionMarker}
                icon={pinIcon}
                draggable={draggable}
                onClick={MarkerClicked}
                onDragEnd={MarkerFinishDrag}
            />
            {isSelectedLocation && (
                <div className="absolute left-1/2 transform -translate-x-1/2 mt-2">
                    <NewTripButton position={Location} />
                </div>
            )}
        </div>
    );
}

export default MarkerPin;