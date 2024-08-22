"use client";
import React, {useState } from "react";
import MarkerPin from "./marker";
import Map from "./map";
import NewTripButton from "./NewTripButton";

const center = {
    lat: 43.642693,
    lng: -79.3871189,
  };

function HomePageMap() {

  const [isSelectedLocation, setSelected] = useState(false);
  const [Location, setLocation] = useState({lat:43.553185075739556,lng:-79.42145117539064});

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

  // function handleOnClick(event)
  // {
  //   if (event.latLng) {
  //     let lat = event.latLng.lat();
  //     let lng = event.latLng.lng();
  //     console.log("The final lat", lat);
  //     console.log("The final lng", lng);
  //     setSelected(true);
  //     console.log(isSelectedLocation);
  //     setLocation({ lat, lng });
  // }

  // }
  
  return (
    <div className="relative w-full h-full">
                <Map center={center} onClick={MarkerFinish}>
                    <MarkerPin
                        positionMarker={Location}
                        draggable={true}
                        onDragEnd={MarkerFinish}
                    />
                </Map>
                {isSelectedLocation && (
                    <div className="absolute bottom-4 right-4">
                        <NewTripButton position={Location} />
                    </div>
                )}
        </div>
  )
}

export default HomePageMap;