import Map from "@/components/ui/map";
import CurrentTripComp from "@/components/ui/currentTrip";


export default function PlanningPage() {
    return (
        <div className="h-[100vh] w-full">
            {/* <Map/> */}
            <div className="flex justify-center items-start space-x-4 p-4">
      <CurrentTripComp/>
    </div>
        </div>
    );
}
