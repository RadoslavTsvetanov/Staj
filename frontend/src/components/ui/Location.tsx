import Image from 'next/image';
import React from 'react';

type LocationProps={
    location: string,
}

const Location: React.FC <LocationProps> =({location}) =>
{
    return(
        <div className="p-4 bg-blue-100 rounded-lg shadow-md flex space-y-2">
             <Image src="/images/map.png" alt="mapIcon" width={20} height={20} />
             <p>{location}</p>
        </div>
    );

}

export default Location;