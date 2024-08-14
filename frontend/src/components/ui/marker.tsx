"use client";

import React, { useEffect,useState } from "react";
import { GoogleMap, useJsApiLoader, MarkerF } from "@react-google-maps/api";

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

// const [isSelectedLocation, setLocation] = useState(false);

function MarkerFinishDrag(event)
{
  console.log("The final lat",event.latLng.lat());
  console.log("The final lng",event.latLng.lng());
 // setLocation(true);
}

function MarkerPin()
{
    return (
        <MarkerF
        position={positionMarker}
        icon={pinIcon}
        // label={{
        //   text: "Travel location",
        //   className: "pt-4",
        // }}
        draggable
        onClick={ MarkerClicked}
        onDragEnd={MarkerFinishDrag}
      > 
     
      </MarkerF>
    )
}

export default MarkerPin;