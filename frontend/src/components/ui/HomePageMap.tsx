"use client";
import MarkerPin from "./marker";
import Map from "./map";

function HomePageMap() {
  
  return (
    <Map>
      <MarkerPin positionMarker={{lat:43.553185075739556,lng:-79.42145117539064}}/>
    </Map>
  )
}

export default HomePageMap;