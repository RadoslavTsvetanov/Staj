"use client";

import CurrentTripComp from "@/components/ui/currentTrip";
import BackButton from "@/components/ui/BackButton"; 
import PlanningPageMap from '@/components/ui/PlanningPageMap';

export default function PlanningPage() {

    return (
        <div className="relative h-[100vh] w-full">
            <PlanningPageMap/>
            <BackButton />
            <CurrentTripComp />
        </div>
    );
}
