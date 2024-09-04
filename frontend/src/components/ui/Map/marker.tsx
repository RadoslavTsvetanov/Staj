"use client";
import React from "react";
import { MarkerF } from "@react-google-maps/api";


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
      draggable?: boolean,
      onDragEnd?: (event: google.maps.MapMouseEvent) => void,
}

const MarkerPin: React.FC<MarkerPinProps> =({positionMarker, draggable = false, onDragEnd })=>
{

    return (
            <MarkerF
                position={positionMarker}
                icon={pinIcon}
                draggable={draggable}
                onClick={MarkerClicked}
                // onClick={(event) => {
                //     console.log("you clicked on the marker");
                //     console.log(event.latLng?.lat());
                //     console.log(event.latLng?.lng());
                // }}
                onDragEnd={onDragEnd}
            />
            
    );
}

export default MarkerPin;