import Image from 'next/image';
import React from 'react';

type LocationProps = {
    location: string;
};

const Location: React.FC<LocationProps> = ({ location }) => {
    return (
        <div className="p-4 bg-blue-100 rounded-lg shadow-md flex items-center space-x-3">
            <div className="flex-shrink-0">
                <Image src="/images/map.png" alt="mapIcon" width={35} height={35} />
            </div>
            <p className="text-gray-800 text-lg">{location}</p>
        </div>
    );
}

export default Location;
