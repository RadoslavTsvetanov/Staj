import Map from "@/components/ui/map";
import TripInfo from "@/components/ui/tripInfo";
import PlanDetails from "@/components/ui/planDetails";

export default function PlanningPage() {
    return (
        <div className="h-[100vh] w-full">
            {/* <Map/> */}
            <div className="flex justify-center items-start space-x-4 p-4">
      <TripInfo />
      <PlanDetails />
    </div>
        </div>
    );
}
