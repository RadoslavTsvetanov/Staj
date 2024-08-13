"use client";

import React, { useEffect } from "react";
//import { Loader} from "@googlemaps/js-api-loader";
import { GoogleMap, useJsApiLoader, MarkerF } from "@react-google-maps/api";

// function Map() {
//     const mapRef = React.useRef<HTMLDivElement>(null);

//     useEffect(() => {
//       const initMap = async () => {
//         const loader = new Loader({
//           apiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
//           version: "weekly",
//         });

//         const { Map } = await loader.importLibrary("maps");

//         const { AdvancedMarkerElement } = (await loader.importLibrary(
//           "marker"
//         )) as google.maps.MarkerLibrary;

//         const position = {
//           lat: 43.642693,
//           lng: -79.3871189,
//         };

//         const mapOptions: google.maps.MapOptions = {
//           center: position,
//           zoom: 17,
//           mapId: "MY_NEXTJS_MAP_ID",
//           mapTypeControl: false,
//           zoomControl: false,
//           streetViewControl: false,
//           scrollwheel: true,

//         };

//         const map = new Map(mapRef.current as HTMLDivElement, mapOptions);

//         const pinIcon =
//         {
//             url: '/images/pin.png',
//             scaledSize: {width:50, height:40},
//         }

//         function MarkerClicked()
//         {
//             console.log("you clicked on the marker");
//         }

//         const marker = new AdvancedMarkerElement({
//           map: map,
//           position: position,
//           icon: pinIcon,
//           onClick: MarkerClicked,
//         //   label: {
//         //     text: 'Travel location',
//         //     className:'pt-4'
//         //   }

//         });
//       };

//       initMap();
//     }, []);

//     return (
//         <div ref={mapRef} className="w-full h-full rounded-lg"></div>
//     );
// }

// export default Map;

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

const positionMarker = {
              lat: 43.642693,
              lng: -79.3871189,
            };

function Map() {
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
    version: "weekly",
  });

  const [map, setMap] = React.useState(null);

  const onLoad = React.useCallback(function callback(map) {
    // This is just an example of getting and using the map instance!!! don't just blindly copy!
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map) {
    setMap(null);
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      options={mapOptions}
      center={center}
      zoom={10}
      onLoad={onLoad}
      onUnmount={onUnmount}
    >
        <MarkerF position={positionMarker}/>
      <></>
    </GoogleMap>
  ) : (
    <></>
  );
}

export default Map;
