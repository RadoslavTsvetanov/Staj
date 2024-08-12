"use client"

import React, {useEffect} from "react";
import { Loader } from "@googlemaps/js-api-loader";

import Plan from "@/components/ui/plan";
import Navbar from '@/components/ui/navbar';

export default function Map()
{
    const mapRef = React.useRef<HTMLDivElement>(null);

    useEffect(() =>
    {

        const initMap = async () => {
            const loader = new Loader(
                {apiKey: process.env.NEXT_PUBLIC_MAPS_API_KEY as string,
                    version:'weekly'
                });

            const { Map } = await loader.importLibrary('maps');

            const { Marker} = await loader.importLibrary('marker') as google.maps.MarkerLibrary;

            const position = 
            {
                lat: 43.642693, 
                lng:-79.3871189
            }

            const mapOptions: google.maps.MapOptions = {
                center: position,
                zoom: 17,
                mapId:"MY_NEXTJS_MAP_ID"
            }  

            const map = new Map(mapRef.current as HTMLDivElement ,mapOptions);

            const marker = new Marker({
                map: map,
                position: position
            });

        }

         initMap()
        },[]);

  

    return (

        <div>
        <Navbar/>
        
        <div style={{ display: 'flex', height: '90vh' }}>
            <div style={{ width: '50vw', borderRadius: '5%' }} ref={mapRef} />
            <div style={{ width: '50vw',backgroundColor:'blue'}}>
                <div style={{ height: '50%', backgroundColor: 'pink',borderRadius: '10%', margin:"1%" }}>
                    <h1>Current trips</h1>
                    <Plan name="Mountains" />
                    <Plan name="Rivers" />
                    <Plan name="Forests" />
                </div>
                <div style={{ height: '50%', backgroundColor: 'purple',borderRadius: '5%', margin:"1%" }}>pink</div>
            </div>
        </div>
    </div>
    



    )
}