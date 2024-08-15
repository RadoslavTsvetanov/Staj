import React from "react";
import TripInfo from "./TripInfo";
import PlanDetails from "./PlanDetails";

function CurrentTripComp() {
  return (
    <div className="absolute top-0 right-7 w-2/5 flex flex-col justify-between ">
      <TripInfo />
      <PlanDetails />
    </div>
  );
}

export default CurrentTripComp;
