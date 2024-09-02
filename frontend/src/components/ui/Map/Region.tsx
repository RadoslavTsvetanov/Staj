"use client";
import { Circle } from "@react-google-maps/api";

interface CircleProps {
    children?: React.ReactNode;
    className?: string;
    center:  {
      lat: number,
      lng: number,
    },
    radius: number,
  }

  const circleOptions = {
      fillColor: 'green',
      fillOpacity: .2,
      strokeColor:'green',
      strokeOpacity: .3,
      strokeWeight: 2,

  };

const Region: React.FC<CircleProps> = ({center,radius}) =>
{

    return(
       
        <Circle options={circleOptions} center={center} radius={radius*1000}>
          
        </Circle>

    );
} 

export default Region;