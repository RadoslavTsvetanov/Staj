import Image from 'next/image';

interface PlaceProps{
    text: string,
}

function Place({ text}: PlaceProps){
    return (
        <li className="flex items-center space-x-2">    
        <Image src="/images/gps.png" alt="pin" width={25} height={25} />
            <p>{text}</p>
        </li>

    );
}

export default Place;