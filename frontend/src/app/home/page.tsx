"use client";

import React, { useEffect } from "react";
import { Loader } from "@googlemaps/js-api-loader";

import Plan from "@/components/ui/plan";
import Navbar from "@/components/ui/navbar";

export default function Map() {
  const mapRef = React.useRef<HTMLDivElement>(null);

  useEffect(() => {
    const initMap = async () => {
      const loader = new Loader({
        apiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
        version: "weekly",
      });

      const { Map } = await loader.importLibrary("maps");

      const { Marker } = (await loader.importLibrary(
        "marker"
      )) as google.maps.MarkerLibrary;

      const position = {
        lat: 43.642693,
        lng: -79.3871189,
      };

      const mapOptions: google.maps.MapOptions = {
        center: position,
        zoom: 17,
        mapId: "MY_NEXTJS_MAP_ID",
      };

      const map = new Map(mapRef.current as HTMLDivElement, mapOptions);

      const marker = new Marker({
        map: map,
        position: position,
      });
    };

    initMap();
  }, []);

  return (
    <div>
      <Navbar  />

      <div className="flex h-[90vh]">
        {/* Map Section */}
        <div className="w-1/2 rounded-lg m-2 relative overflow-hidden">
          <div ref={mapRef} className="w-full h-full rounded-lg">
            {/* Your map implementation here */}
          </div>
          <button className="absolute bottom-5 left-5 bg-blue-600 text-black py-2 px-4 rounded-lg">
            New trip plan
          </button>
        </div>

        {/* Planning and Previous Trips Section */}
        <div className="w-1/2 flex flex-col justify-between">
          {/* Currently Planning */}
          <div className="h-1/2 bg-customBlue rounded-xl m-2 p-4">
            <h1 className="text-black text-xl font-bold mb-4 text-center">
              Currently planning
            </h1>
            <div className="space-y-2">
              <Plan name="Italy" isEditable={true} />
              <Plan name="Italy" isEditable={true} />
            </div>
          </div>

          <div className="relative h-1/2 bg-red-200 rounded-xl m-2 p-4 overflow-hidden">
  <h1 className="absolute top-0 left-0 right-0 text-black text-xl font-bold mb-4 text-center bg-red-200 p-4 z-10">
    Previous trips
  </h1>
  <div className="pt-8 h-[calc(5*50px)] overflow-y-auto scrollbar-thin scrollbar-thumb-gray-400 scrollbar-track-transparent">
    <div className="space-y-2">
      <Plan name="Mountains" isEditable={false} />
      <Plan name="Our trip to Greece" isEditable={false} />
      <Plan name="Christmas holiday" isEditable={false} />
      <Plan name="Paris" isEditable={false} />
      <Plan name="New York" isEditable={false} />
    </div>
  </div>
</div>




         
        </div>
      </div>
    </div>
  );
}
